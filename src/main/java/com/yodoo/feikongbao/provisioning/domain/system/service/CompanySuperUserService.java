package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.contract.ApolloConstants;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyMapper;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.enums.CompanyStatusEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.Base64Util;
import com.yodoo.feikongbao.provisioning.util.RequestPrecondition;
import com.yodoo.megalodon.datasource.config.ProvisioningDataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.*;

/**
 * 超级用户
 * @Author houzhen
 * @Date 16:53 2019/7/30
 **/
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class CompanySuperUserService {

    private static Logger logger = LoggerFactory.getLogger(CompanySuperUserService.class);

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private ApolloOpenApiClient openApiClient;

    @Autowired
    private ProvisioningDataSourceConfig provisioningDataSourceConfig;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    /**
     * 创建超级用户
     *
     * @Author houzhen
     * @Date 16:59 2019/7/30
     **/
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public void createSuperUser(Integer companyId) {
        logger.info("CompanySuperUserService.createSuperUser companyId:{}", companyId);
        RequestPrecondition.checkArguments(companyId == null);
        // 查询公司信息
        Company company = companyMapper.selectByPrimaryKey(companyId);
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        // 插入超级用户
        this.addSuperUser(company);
        // 插入步骤信息
        companyCreateProcessService.insertCompanyCreateProcess(companyId,
                CompanyCreationStepsEnum.SUPERUSER_STEP.getOrder(), CompanyCreationStepsEnum.SUPERUSER_STEP.getCode());
        // 更新公司为可用
        company.setStatus(CompanyStatusEnum.RUNNING.getCode());
        companyMapper.updateByPrimaryKey(company);
    }

    private void addSuperUser(Company company) {
        // 数据库信息
        String url = "";
        // 用户名
        String user = "";
        // 密码
        String password = "";
        // 查询集群信息
        OpenNamespaceDTO namespace = openApiClient.getNamespace(provisioningDataSourceConfig.provisioningApolloAppid, provisioningDataSourceConfig.provisioningApolloEvn,
                company.getCompanyCode(), ApolloConstants.DB_CONNECTION_NAMESPACE);
        if (namespace == null || CollectionUtils.isEmpty(namespace.getItems())) {
            throw new ProvisioningException(BundleKey.DB_NOT_EXIST, BundleKey.DB_NOT_EXIST_MSG);
        }
        for (OpenItemDTO itemDTO : namespace.getItems()) {
            if (itemDTO.getKey().equalsIgnoreCase(ApolloConstants.COMPANY_DB_CONNECTION_URL)) {
                url = itemDTO.getValue();
                continue;
            }
            if (itemDTO.getKey().equalsIgnoreCase(ApolloConstants.COMPANY_DB_CONNECTION_USER)) {
                user = itemDTO.getValue();
                continue;
            }
            if (itemDTO.getKey().equalsIgnoreCase(ApolloConstants.COMPANY_DB_CONNECTION_PASSWORD)) {
                password = Base64Util.base64Encoder(itemDTO.getValue());
                continue;
            }
        }
        // 连接数据库，插入超级用户
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            //1.注册驱动
            Class.forName("com.mysql.jdbc.ReplicationDriver");
            //2.获取连接
            Connection con = DriverManager.getConnection(url, user, password);
            //3.获得预处理对象
            String sql = "select * from user_object where user_login_id = ?";
            PreparedStatement stat = con.prepareStatement(sql);
            //4.SQL语句占位符设置实际参数
            stat.setString(1, "admin");
            ResultSet resultSet = stat.executeQuery();
            // 结果集处理
            if (resultSet == null || !resultSet.next()) {
                sql = "insert into user_object(user_login_id, user_name) values (?,?,?)";
                stat = con.prepareStatement(sql);
                stat.setString(1, "admin");
                stat.setString(2, company.getCompanyName());
                stat.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CompanySuperUserService.createSuperUser error:{}", e);
            throw new ProvisioningException(BundleKey.UNDEFINED, BundleKey.UNDEFINED_MSG);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
