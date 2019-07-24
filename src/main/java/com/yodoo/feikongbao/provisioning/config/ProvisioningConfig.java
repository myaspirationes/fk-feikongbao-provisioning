package com.yodoo.feikongbao.provisioning.config;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feikongbao.storageclient.config.SwiftStorageClientConfig;
import com.offbytwo.jenkins.JenkinsServer;
import com.yodoo.megalodon.datasource.annotation.*;
import com.yodoo.megalodon.datasource.config.ApolloPortalConfig;
import com.yodoo.megalodon.datasource.config.JenkinsConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author houzhen
 * @Date 17:30 2019/7/24
**/
@Configuration
@Import({SwiftStorageClientConfig.class})
@ComponentScan(basePackages = {"com.yodoo.feikongbao.provisioning"})
@MapperScan(basePackages = "com.yodoo.feikongbao.provisioning.*.*.dao", sqlSessionFactoryRef = ProvisioningConfig.SQL_SESSION_FACTORY_BEAN_NAME)
@EnableTransactionManagement
@EnableRabbitMqConfig
@EnableProvisioningDataSource
@EnableAliYunConfig
@EnableApolloPortalConfig
@EnableJenkinsConfig
@EnableRedisClient
public class ProvisioningConfig {

    private static Logger logger = LoggerFactory.getLogger(ProvisioningConfig.class);

    public static final String TRANSACTION_MANAGER_BEAN_NAME = "provisioningTransactionManager";

    public static final String SQL_SESSION_FACTORY_BEAN_NAME = "provisioningSessionFactory";

    public static final String REDIS_TEMPLATE_BEAN_NAME = "provisioningRedisTemplate";

    @Autowired
    private ApolloPortalConfig apolloPortalConfig;

    @Autowired
    private JenkinsConfig jenkinsConfig;

    /***
     * Json <--> object
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 忽略未知字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean(TRANSACTION_MANAGER_BEAN_NAME)
    public DataSourceTransactionManager provisioningTransactionManager(DataSource provisioningDataSource) {
        return new DataSourceTransactionManager(provisioningDataSource);
    }

    @Bean(SQL_SESSION_FACTORY_BEAN_NAME)
    public SqlSessionFactory provisioningSqlSessionFactory(DataSource provisioningDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        sqlSessionFactoryBean.setEnvironment("development");
        sqlSessionFactoryBean.setDataSource(provisioningDataSource);
        // 下边仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:com/yodoo/feikongbao/provisioning/mapper/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * apolloOpenApiClient bean
     *
     * @return
     */
    @Bean
    public ApolloOpenApiClient apolloOpenApiClient() {
        ApolloOpenApiClient client = ApolloOpenApiClient.newBuilder()
                .withPortalUrl(apolloPortalConfig.portalUrl)
                .withToken(apolloPortalConfig.portalToken)
                .build();
        return client;
    }

    /**
     * jenkins 连接对象
     *
     * @return
     */
    @Bean
    public JenkinsServer jenkinsServer() throws URISyntaxException {
        return new JenkinsServer(new URI(jenkinsConfig.jenkinsUrl), jenkinsConfig.jenkinsUsername,
                jenkinsConfig.jenkinsPassword);
    }

    /**
     * redis访问模板
     **/
    @Bean(REDIS_TEMPLATE_BEAN_NAME)
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate template = new RedisTemplate();
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(jedisConnectionFactory);
        return template;
    }
}