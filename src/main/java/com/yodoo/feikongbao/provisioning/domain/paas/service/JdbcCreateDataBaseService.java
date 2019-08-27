package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.yodoo.feikongbao.provisioning.domain.paas.dto.CreateDataBaseDto;
import com.yodoo.feikongbao.provisioning.enums.DefaultDataBaseEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.Base64Util;
import com.yodoo.feikongbao.provisioning.util.JsonUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Description ：创建数据库
 * @Author ：jinjun_luo
 * @Date ： 2019/8/27 0027
 */
@Service
public class JdbcCreateDataBaseService {

    private static Logger logger = LoggerFactory.getLogger(JdbcCreateDataBaseService.class);

    /**
     * jdbc 创建数据库
     **/
    public CreateDataBaseDto createDataBase(String companyCode, String ip, Integer port, String username, String password) {
        if (StringUtils.isBlank(companyCode) || StringUtils.isBlank(ip) || port == null || port < 0 || StringUtils.isBlank(username)
                || StringUtils.isBlank(password)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Connection conn = null;
        Statement stat = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 一开始必须填一个已经存在的数据库
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + DefaultDataBaseEnum.DATA_BASE.getCode() + "?useUnicode=true&characterEncoding=utf-8";
            conn = DriverManager.getConnection(url, username, password);
            stat = conn.createStatement();
            // 创建数据库 companyCode
            stat.executeUpdate("CREATE DATABASE IF NOT EXISTS " + companyCode + " CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI");

            // 创建用户
            String newUsername = companyCode;
            String newPassword = RandomStringUtils.random(12);

            // 刷新权限
            stat.execute("FLUSH PRIVILEGES");
            stat.executeUpdate("CREATE USER IF NOT EXISTS '" + newUsername + "'@'%'");
            stat.executeUpdate("SET PASSWORD FOR '" + newUsername + "'@'%'='" + newPassword + "'");
            // 授权
            stat.executeUpdate("GRANT SELECT, INSERT, UPDATE, REFERENCES, DELETE, CREATE, DROP, ALTER, INDEX, " +
                    "TRIGGER, CREATE VIEW, SHOW VIEW, EXECUTE, ALTER ROUTINE, CREATE ROUTINE, CREATE TEMPORARY TABLES, " +
                    "LOCK TABLES, EVENT ON `" + companyCode + "`.* TO '" + newUsername + "'@'%'");
            // 刷新权限
            stat.execute("FLUSH PRIVILEGES");

            // 返回值设置
            CreateDataBaseDto createDataBaseDto = new CreateDataBaseDto();
            createDataBaseDto.setIp(ip);
            createDataBaseDto.setPort(port);
            createDataBaseDto.setUsername(newUsername);
            createDataBaseDto.setPassword(Base64Util.base64Encoder(newPassword));
            createDataBaseDto.setSchemaName(companyCode);
            return createDataBaseDto;
        } catch (Exception e) {
            logger.error("create database error:{}", JsonUtils.obj2json(e));
            throw new ProvisioningException(BundleKey.DB_CREATE_ERROR, BundleKey.DB_CREATE_ERROR_MSG);
        } finally {
            if (stat != null) {
                try {
                    stat.close();
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
