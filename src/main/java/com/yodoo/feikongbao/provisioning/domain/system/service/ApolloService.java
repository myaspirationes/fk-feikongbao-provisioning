package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenClusterDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.yodoo.feikongbao.provisioning.contract.ApolloConstants;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.CreateDataBaseDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.*;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbInstanceMapper;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbSchemaMapper;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.RedisInstanceMapper;
import com.yodoo.feikongbao.provisioning.domain.paas.service.JdbcCreateDataBaseService;
import com.yodoo.feikongbao.provisioning.domain.paas.service.MqVhostService;
import com.yodoo.feikongbao.provisioning.domain.paas.service.SwiftProjectService;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyMapper;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.megalodon.datasource.config.EurekaServiceConfig;
import com.yodoo.megalodon.datasource.config.ProvisioningDataSourceConfig;
import com.yodoo.megalodon.datasource.config.RabbitMqConfig;
import com.yodoo.megalodon.datasource.config.SwiftConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Date 2019/7/29 15:57
 * @Author by houzhen
 */
@Service
public class ApolloService {

    private static Logger logger = LoggerFactory.getLogger(ApolloService.class);

    @Autowired
    private ProvisioningDataSourceConfig provisioningDataSourceConfig;

    @Autowired
    private ApolloOpenApiClient openApiClient;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private DbSchemaMapper dbSchemaMapper;

    @Autowired
    private DbInstanceMapper dbInstanceMapper;

    @Autowired
    private RedisInstanceMapper redisInstanceMapper;

    @Autowired
    private SwiftProjectService swiftProjectService;

    @Autowired
    private MqVhostService mqVhostService;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    @Autowired
    private SwiftConfig swiftConfig;

    @Autowired
    private EurekaServiceConfig eurekaServiceConfig;

    @Autowired
    private JdbcCreateDataBaseService jdbcCreateDataBaseService;

    /**
     * 创建apollo集群
     *
     * @Author houzhen
     * @Date 16:14 2019/7/29
     **/
    public OpenClusterDTO createCluster(String companyCode) {
        logger.info("ApolloService.createCluster companyCode:{}", companyCode);
        if (StringUtils.isBlank(companyCode)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        OpenClusterDTO clusterDTO = openApiClient.getCluster(provisioningDataSourceConfig.provisioningApolloAppid,
                provisioningDataSourceConfig.provisioningApolloEvn, companyCode);
        if (clusterDTO != null) {
            return clusterDTO;
        }
        OpenClusterDTO toCreate = new OpenClusterDTO();
        toCreate.setName(companyCode);
        toCreate.setDataChangeCreatedBy(ApolloConstants.OPERATE);
        OpenClusterDTO cluster = openApiClient.createCluster(provisioningDataSourceConfig.provisioningApolloAppid,
                provisioningDataSourceConfig.provisioningApolloEvn, toCreate);

        // eureka 配置落 apollo 配置中心
        createEurekaItem(companyCode);
        return cluster;
    }

    /**
     * eureka 配置落 apollo 配置
     * @param companyCode
     */
    public void createEurekaItem(String companyCode) {
        List<OpenItemDTO> itemDtoList = new ArrayList<>();
        itemDtoList.add(buildItem(ApolloConstants.EUREKA_SERVER_URL, eurekaServiceConfig.eurekaServerUrl, "eureka 连接地址 "));
        this.createItems(companyCode, ApolloConstants.DB_CONNECTION_NAMESPACE, itemDtoList);
    }

    /**
     * apollo数据库信息
     *
     * @Author houzhen
     * @Date 9:38 2019/7/30
     **/
    public void createDbItems(String companyCode) {
        // 检查公司是否存在
        Company company = this.checkCompany(companyCode);
        if (company.getDbGroupId() == null || company.getDbGroupId() < 0) {
            throw new ProvisioningException(BundleKey.DB_NOT_EXIST, BundleKey.DB_NOT_EXIST_MSG);
        }
        // 查询数据库信息
        Example example = new Example(DbSchema.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dbGroupId",company.getGroupId());
        List<DbSchema> dbSchemaList = dbSchemaMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(dbSchemaList)) {
            throw new ProvisioningException(BundleKey.DB_NOT_EXIST, BundleKey.DB_NOT_EXIST_MSG);
        }
        // 获取主库
        List<DbSchema> masterDbSchemaList = dbSchemaList.stream().filter(Objects::nonNull).filter(dbSchema -> dbSchema.getType() == 0).collect(Collectors.toList());
        // 主库 instance
        List<DbInstance> masterDbInstanceList = null;
        if (!CollectionUtils.isEmpty(masterDbSchemaList)){
            masterDbInstanceList = getDbInstanceList(masterDbSchemaList);
        }
        // 获取从库
        List<DbSchema> slaveDbSchemaList = dbSchemaList.stream().filter(Objects::nonNull).filter(dbSchema -> dbSchema.getType() == 1).collect(Collectors.toList());
        // 从库 instance
        List<DbInstance> slaveDbInstanceList = null;
        if (!CollectionUtils.isEmpty(slaveDbSchemaList)){
            slaveDbInstanceList = getDbInstanceList(slaveDbSchemaList);
        }

        // 获取一个主库，创建数据库，其它从库会同步
        DbInstance getDbInstanceById = masterDbInstanceList.get(0);
        // 创建数据库
        CreateDataBaseDto dataBase = jdbcCreateDataBaseService.createDataBase(companyCode, getDbInstanceById.getIp(), getDbInstanceById.getPort(), getDbInstanceById.getUsername(), getDbInstanceById.getPassword());

        // 拼主从数据库连接地址
        StringBuffer sqlUrl = new StringBuffer();
        String masterAndSlaveString = buildSqlBuffer(masterDbInstanceList, slaveDbInstanceList);
        sqlUrl.append(masterAndSlaveString.substring(0, masterAndSlaveString.length() - 1));
        sqlUrl.append("/" + dataBase.getSchemaName() + "?characterEncoding=utf8&autoReconnect=true&roundRobinLoadBalance=true&readFromMasterWhenNoSlaves=true");

        // 组建itemDTOList
        List<OpenItemDTO> openItemDtos = new ArrayList<>();
        // 	数据库连接地址
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_CONNECTION_URL, sqlUrl.toString(), "数据库连接地址"));
        // 用户名
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_CONNECTION_USER, dataBase.getUsername(), "数据库用户名"));
        // 密码
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_CONNECTION_PASSWORD, dataBase.getPassword(), "数据库密码"));
        // 最大连接数
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_POOL_SIZE_MAX, ApolloConstants.COMPANY_DB_POOL_SIZE_MAX_VALUE, "数据库最大连接数"));
        // 最小连接数
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_POOL_SIZE_MIN, ApolloConstants.COMPANY_DB_POOL_SIZE_MIN_VALUE, "数据库最小连接数"));

        this.createItems(companyCode, ApolloConstants.DB_CONNECTION_NAMESPACE, openItemDtos);
    }

    /**
     * 创建redis
     *
     * @Author houzhen
     * @Date 11:23 2019/7/30
     **/
    public void createRedisItems(String companyCode) {
        // 检查公司是否存在
        Company company = this.checkCompany(companyCode);
        if (company.getRedisGroupId() == null || company.getRedisGroupId() < 0) {
            throw new ProvisioningException(BundleKey.REDIS_NOT_EXIST, BundleKey.REDIS_NOT_EXIST_MSG);
        }
        // 查询redis信息
        RedisInstance findRedisParams = new RedisInstance();
        findRedisParams.setRedisGroupId(company.getRedisGroupId());
        List<RedisInstance> redisInstanceList = redisInstanceMapper.select(findRedisParams);
        if (CollectionUtils.isEmpty(redisInstanceList)) {
            throw new ProvisioningException(BundleKey.REDIS_NOT_EXIST, BundleKey.REDIS_NOT_EXIST_MSG);
        }
        // 组建itemDTOList
        List<OpenItemDTO> itemDtoList = new ArrayList<>();
        // 地址
        String hostname = "";
        // 用户名
        String port = "";
        // 密码
        String password = "";

        for (RedisInstance instance : redisInstanceList) {
            hostname = instance.getIp();
            port = String.valueOf(instance.getPort());
            password = instance.getPassword();
            break;
        }
        // 连接地址
        itemDtoList.add(this.buildItem(ApolloConstants.REDIS_HOST, hostname, "redis 连接地址"));
        // 端口
        itemDtoList.add(this.buildItem(ApolloConstants.REDIS_PORT, port, " redis 端口"));
        // 密码
        itemDtoList.add(this.buildItem(ApolloConstants.REDIS_PASSWORD, password, "redis 密码（base64加密）"));

        this.createItems(companyCode, ApolloConstants.DB_CONNECTION_NAMESPACE, itemDtoList);
    }

    /**
     * 创建 neo4j 到apollo
     * @param neo4jInstance
     */
    public void createNeo4j(Neo4jInstance neo4jInstance) {
        // 检查公司是否存在
        Company company = this.checkCompany(neo4jInstance.getNeo4jName());
        if (company.getNeo4jInstanceId() == null || company.getNeo4jInstanceId() < 0){
            throw new ProvisioningException(BundleKey.NEO4J_INSTANCE_NOT_EXIST, BundleKey.NEO4J_INSTANCE_NOT_EXIST_MSG);
        }
        // 封装参数
        List<OpenItemDTO> itemDtoList = new ArrayList<>();
        itemDtoList.add(buildItem(ApolloConstants.NEO4J_URL, neo4jInstance.getUrl(), "neo4j 连接地址"));
        itemDtoList.add(buildItem(ApolloConstants.NEO4J_USERNAME, neo4jInstance.getUsername(), "neo4j 用户名"));
        itemDtoList.add(buildItem(ApolloConstants.NEO4J_PASSWORD, neo4jInstance.getPassword(), "neo4j 密码"));

        // 创建
        this.createItems(neo4jInstance.getNeo4jName(), ApolloConstants.DB_CONNECTION_NAMESPACE, itemDtoList);
    }

    /**
     * 对象存储 落 apollo
     * @param companyCode
     */
    public void createSwiftProjectItem(String companyCode) {
        // 检查公司是否存在
        Company company = this.checkCompany(companyCode);
        if (company.getNeo4jInstanceId() == null || company.getNeo4jInstanceId() < 0){
            throw new ProvisioningException(BundleKey.SWIFT_PROJECT_NOT_EXIST, BundleKey.SWIFT_PROJECT_NOT_EXIST_MSG);
        }
        SwiftProject swiftProject = swiftProjectService.selectByPrimaryKey(company.getSwiftProjectId());
        if (swiftProject == null){
            throw new ProvisioningException(BundleKey.SWIFT_PROJECT_NOT_EXIST, BundleKey.SWIFT_PROJECT_NOT_EXIST_MSG);
        }
        // TODO 其它参数是否需要
        List<OpenItemDTO> itemDtoList = new ArrayList<>();
        itemDtoList.add(buildItem(ApolloConstants.OPENSTACK_USER_NAME, swiftConfig.userName, "openstack-用户名"));
        itemDtoList.add(buildItem(ApolloConstants.OPENSTACK_USER_PASSWORD, swiftConfig.userPassword, "openstack-密码"));
        itemDtoList.add(buildItem(ApolloConstants.OPENSTACK_ENDPOINT_URL, swiftConfig.endpointUrl, "openstack-地址"));
        itemDtoList.add(buildItem(ApolloConstants.OPENSTACK_DOMAIN_ID, swiftConfig.domainId, "openstack-默认域id"));
        itemDtoList.add(buildItem(ApolloConstants.OPENSTACK_PROJECT_NAME, swiftConfig.projectName, "openstack-默认租户名称"));
        itemDtoList.add(buildItem(ApolloConstants.OPENSTACK_ROLE_NAME, swiftConfig.roleName, "openstack-默认角色"));
        itemDtoList.add(buildItem(ApolloConstants.OPENSTACK_SWIFT_URL, swiftConfig.swiftUrl, "openstack-对象存储地址"));
        itemDtoList.add(buildItem(ApolloConstants.OPENSTACK_DEFAULT_STORAGE_MAXSIZE, swiftConfig.defaultStorageMaxSize.toString(), "openstack-最大使用范围（单位M，1M=1024B"));
        itemDtoList.add(buildItem(ApolloConstants.OPENSTACK_CONSUMER_PROJECT_NAME, companyCode, "openstack-用户租户名称"));

        this.createItems(companyCode, ApolloConstants.DB_CONNECTION_NAMESPACE, itemDtoList);
    }

    /**
     * rabbitmq vHostName 落 apollo
     * @param companyCode
     */
    public void createVirtualHostItem(String companyCode) {
        // 检查公司是否存在
        Company company = this.checkCompany(companyCode);
        if (company.getMqVhostId() == null || company.getMqVhostId() < 0){
            throw new ProvisioningException(BundleKey.RABBITMQ_VHOST_NAME_NOT_EXIST_ERROR, BundleKey.RABBITMQ_VHOST_NAME_NOT_EXIST_ERROR_MSG);
        }
        MqVhost mqVhost = mqVhostService.selectByPrimaryKey(company.getMqVhostId());
        if (mqVhost == null){
            throw new ProvisioningException(BundleKey.RABBITMQ_VHOST_NAME_NOT_EXIST_ERROR, BundleKey.RABBITMQ_VHOST_NAME_NOT_EXIST_ERROR_MSG);
        }
        List<OpenItemDTO> itemDtoList = new ArrayList<>();
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_GENERAL_USERNAME, rabbitMqConfig.rabbitmqGeneralUsername, "rabbitmq 普通用户名"));
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_GENERAL_PASSWORD, rabbitMqConfig.rabbitmqGeneralPassword, "rabbitmq普通用户密码"));
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_URL_HOST, rabbitMqConfig.rabbitmqUrlHost, "rabbitmq 连接地址"));
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_URL_SERVICE_PORT, rabbitMqConfig.rabbitmqUrlServicePort.toString(), "rabbitmq 服务端口号 5672"));
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_PUBLISHER_CONFIRMS, rabbitMqConfig.rabbitmqPublisherConfirms.toString(), "rabbitmq 消息发送到交换机确认机制，是否确认回调: true 回调，false不回调"));
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_PUBLISHER_RETURNS, rabbitMqConfig.rabbitmqPublisherReturns.toString(), "rabbitmq 消息发送到队列确认机制，是否确认返回回调"));
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_TEMPLATE_MANDATORY, rabbitMqConfig.rabbitmqTemplateMandatory.toString(), "rabbitmq发送消息时设置强制标志,设置为true时return callback才生效 "));
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_REPLY_TIMEOUT, rabbitMqConfig.rabbitmqReplyTimeout.toString(), "等待答复时间"));
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_CONFIRM_RETRY_COUNT, rabbitMqConfig.rabbitmqConfirmRetryCount.toString(), "发送失败重发次数"));
        itemDtoList.add(buildItem(ApolloConstants.RABBITMQ_VHOST, companyCode, "fei kong bao vhost name"));
        this.createItems(companyCode,ApolloConstants.DB_CONNECTION_NAMESPACE, itemDtoList);
    }

    /**
     * 获取db instance list
     * @param dbSchemaList
     * @return
     */
    private List<DbInstance> getDbInstanceList(List<DbSchema> dbSchemaList) {
        return dbSchemaList.stream()
                .filter(Objects::nonNull)
                .map(dbSchema -> {
                    return dbInstanceMapper.selectByPrimaryKey(dbSchema.getDbInstanceId());
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 构建sql
     * @param masterDbInstanceList
     * @param slaveDbInstanceList
     * @return
     */
    private String  buildSqlBuffer(List<DbInstance> masterDbInstanceList, List<DbInstance> slaveDbInstanceList) {
        StringBuffer masterAndSlaveString = new StringBuffer();
        masterAndSlaveString.append("jdbc:mysql:replication://");
        // 先把主的拼接
        if (!CollectionUtils.isEmpty(masterDbInstanceList)){
            buildSql(masterAndSlaveString,masterDbInstanceList);
        }
        // 后拼接从的
        if (!CollectionUtils.isEmpty(slaveDbInstanceList)){
            buildSql(masterAndSlaveString,slaveDbInstanceList);
        }
        return masterAndSlaveString.toString();
    }

    /**
     * 构建sql
     * @param masterAndSlaveString
     * @param masterDbInstanceList
     */
    private void buildSql(StringBuffer masterAndSlaveString, List<DbInstance> masterDbInstanceList) {
        masterDbInstanceList.stream()
                .filter(Objects::nonNull)
                .forEach(dbInstance -> {
                    masterAndSlaveString.append(dbInstance.getIp());
                    masterAndSlaveString.append(":");
                    masterAndSlaveString.append(dbInstance.getPort());
                    masterAndSlaveString.append(",");
                });
    }

    /**
     * 校验公司是否存在
     *
     * @Author houzhen
     * @Date 10:57 2019/7/30
     **/
    private Company checkCompany(String companyCode) {
        if (StringUtils.isBlank(companyCode)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询公司信息
        Company findCompanyParams = new Company();
        findCompanyParams.setCompanyCode(companyCode);
        Company company = companyMapper.selectOne(findCompanyParams);
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        return company;
    }

    /**
     * 组建item
     *
     * @Author houzhen
     * @Date 10:57 2019/7/30
     **/
    private OpenItemDTO buildItem(String key, String value, String comment) {
        OpenItemDTO item = new OpenItemDTO();
        item.setKey(key);
        item.setValue(value);
        if (StringUtils.isNotBlank(comment)){
            item.setComment(comment);
        }
        item.setDataChangeCreatedBy(ApolloConstants.OPERATE);
        item.setDataChangeLastModifiedBy(ApolloConstants.OPERATE);
        return item;
    }

    /**
     * 创建items
     *
     * @Author houzhen
     * @Date 16:45 2019/7/29
     **/
    private void createItems(String companyCode, String namespace, List<OpenItemDTO> itemDtoList) {
        if (StringUtils.isBlank(companyCode) || StringUtils.isBlank(namespace)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        String appId = provisioningDataSourceConfig.provisioningApolloAppid;
        String env = provisioningDataSourceConfig.provisioningApolloEvn;
        // 查询集群是否存在
        OpenClusterDTO clusterDTO = openApiClient.getCluster(appId, env, companyCode);
        if (clusterDTO == null) {
            throw new ProvisioningException(BundleKey.CLUSTER_NOT_EXIST, BundleKey.CLUSTER_NOT_EXIST_MSG);
        }
        // 查询namespace是否存在
        OpenNamespaceDTO namespaceDTO = openApiClient.getNamespace(appId, env, companyCode, namespace);
        if (namespaceDTO == null) {
            // 创建namespace
            OpenNamespaceDTO toCreate = new OpenNamespaceDTO();
            toCreate.setNamespaceName(namespace);
            toCreate.setPublic(true);
            toCreate.setDataChangeCreatedBy(ApolloConstants.OPERATE);
            namespaceDTO = openApiClient.createNamespace(appId, env, companyCode, toCreate);
        }
        // 创建items
        for (OpenItemDTO itemDTO : itemDtoList) {
            openApiClient.createOrUpdateItem(appId, env, companyCode, namespace, itemDTO);
        }
        // 发布
        this.publishNamespace(appId, env, companyCode, namespace, ApolloConstants.OPERATE);
    }

    /**
     * 发布项目
     *
     * @Author houzhen
     * @Date 17:08 2019/7/29
     **/
    private void publishNamespace(String appId, String evn, String clusterName, String namespaceName, String operate) {
        NamespaceReleaseDTO namespaceReleaseDTO = new NamespaceReleaseDTO();
        namespaceReleaseDTO.setReleaseTitle("namespace:" + namespaceName + " publish for " + clusterName);
        namespaceReleaseDTO.setReleasedBy(operate);
        openApiClient.publishNamespace(appId, evn, clusterName, namespaceName, namespaceReleaseDTO);
    }
}
