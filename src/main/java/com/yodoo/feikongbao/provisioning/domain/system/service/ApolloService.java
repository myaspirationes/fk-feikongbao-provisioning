package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenClusterDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.yodoo.feikongbao.provisioning.contract.ApolloConstants;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbSchema;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.RedisInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbInstanceMapper;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbSchemaMapper;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.RedisInstanceMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyMapper;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.RequestPrecondition;
import com.yodoo.feikongbao.provisioning.util.StringUtils;
import com.yodoo.megalodon.datasource.config.ProvisioningDataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date 2019/7/29 15:57
 * @Created by houzhen
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


    /**
     * 创建apollo集群
     *
     * @Author houzhen
     * @Date 16:14 2019/7/29
     **/
    public OpenClusterDTO createCluster(String companyCode) {
        logger.info("ApolloService.createCluster companyCode:{}", companyCode);
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(companyCode));
        OpenClusterDTO clusterDTO = openApiClient.getCluster(provisioningDataSourceConfig.provisioningApolloAppid,
                provisioningDataSourceConfig.provisioningApolloEvn, companyCode);
        if (clusterDTO != null) {
            return clusterDTO;
        }
        OpenClusterDTO toCreate = new OpenClusterDTO();
        toCreate.setName(companyCode);
        toCreate.setDataChangeCreatedBy(ApolloConstants.OPERATE);
        return openApiClient.createCluster(provisioningDataSourceConfig.provisioningApolloAppid,
                provisioningDataSourceConfig.provisioningApolloEvn, toCreate);

    }

    /**
     * apollo数据库信息
     *
     * @Author houzhen
     * @Date 9:38 2019/7/30
     **/
    public void createdbItems(String companyCode) {
        // 检查公司是否存在
        Company company = this.checkCompany(companyCode);
        // 查询数据库信息
        Integer dbGroupId = company.getDbGroupId();
        if (dbGroupId == null) {
            throw new ProvisioningException(BundleKey.DB_NOT_EXIST, BundleKey.DB_NOT_EXIST_MSG);
        }
        DbSchema dbSchemaParams = new DbSchema();
        dbSchemaParams.setDbGroupId(dbGroupId);
        List<DbSchema> dbSchemaList = dbSchemaMapper.select(dbSchemaParams);
        if (CollectionUtils.isEmpty(dbSchemaList)) {
            throw new ProvisioningException(BundleKey.DB_NOT_EXIST, BundleKey.DB_NOT_EXIST_MSG);
        }
        // 查询数据库实例
        List<Integer> dbInstanceIdList = dbSchemaList.stream().map(DbSchema::getDbInstanceId).collect(Collectors.toList());
        Example example = new Example(DbInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", dbInstanceIdList);
        List<DbInstance> dbInstanceList = dbInstanceMapper.selectByExample(example);
        // 组建itemDTOList
        List<OpenItemDTO> openItemDtos = new ArrayList<>();
        // 数据库名字
        String schemaName = "";
        // 用户名
        String dbUser = "";
        // 密码
        String dbPassword = "";
        // 数据库连接地址
        Integer masterDbInstanceId = 0;
        for (DbSchema schema : dbSchemaList) {
            if (schema.getType() == 0) {
                masterDbInstanceId = schema.getDbInstanceId();
                dbUser = schema.getUsername();
                dbPassword = schema.getPassword();
                schemaName = schema.getSchemaName();
                break;
            }
        }
        StringBuffer sbDbUrl = new StringBuffer();
        for (DbInstance dbInstance : dbInstanceList) {
            if (dbInstance.getId().equals(masterDbInstanceId)) {
                Integer offset = 0;
                if (sbDbUrl.length() > 0) {
                    sbDbUrl.insert(offset, ",");
                    offset++;
                }
                sbDbUrl.insert(offset, "jdbc.url=jdbc:mysql:replication://" + dbInstance.getIp() + ":" + dbInstance.getPort());
            } else {
                if (sbDbUrl.length() > 0) {
                    sbDbUrl.append(",");
                }
                sbDbUrl.append(dbInstance.getIp() + ":" + dbInstance.getPort());
            }
        }
        sbDbUrl.append("/" + schemaName + "?characterEncoding=utf8&autoReconnect=true&roundRobinLoadBalance=true");
        // 	数据库连接地址
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_CONNECTION_URL, sbDbUrl.toString()));
        // 用户名
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_CONNECTION_USER, dbUser));
        // 密码
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_CONNECTION_PASSWORD, dbPassword));
        // 最大连接数
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_POOL_SIZE_MAX, ApolloConstants.COMPANY_DB_POOL_SIZE_MAX_VALUE));
        // 最小连接数
        openItemDtos.add(this.buildItem(ApolloConstants.COMPANY_DB_POOL_SIZE_MIN, ApolloConstants.COMPANY_DB_POOL_SIZE_MIN_VALUE));

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
        // 查询redis信息
        Integer redisGroupId = company.getRedisGroupId();
        if (redisGroupId == null) {
            throw new ProvisioningException(BundleKey.REDIS_NOT_EXIST, BundleKey.REDIS_NOT_EXIST_MSG);
        }
        RedisInstance findRedisParams = new RedisInstance();
        findRedisParams.setRedisGroupId(redisGroupId);
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
        itemDtoList.add(this.buildItem(ApolloConstants.REDIS_HOST, hostname));
        // 端口
        itemDtoList.add(this.buildItem(ApolloConstants.REDIS_PORT, port));
        // 密码
        itemDtoList.add(this.buildItem(ApolloConstants.REDIS_PASSWORD, password));

        this.createItems(companyCode, ApolloConstants.REDIS_NAMESPACE, itemDtoList);

    }

    /**
     * 校验公司是否存在
     *
     * @Author houzhen
     * @Date 10:57 2019/7/30
     **/
    private Company checkCompany(String companyCode) {
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(companyCode));
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
    private OpenItemDTO buildItem(String key, String value) {
        OpenItemDTO item = new OpenItemDTO();
        item.setKey(key);
        item.setValue(value);
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
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(companyCode, namespace));

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
