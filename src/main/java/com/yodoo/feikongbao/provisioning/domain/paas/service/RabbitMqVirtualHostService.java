package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.yodoo.feikongbao.provisioning.domain.paas.dto.MqDto;
import com.yodoo.feikongbao.provisioning.enums.MqResponseEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.megalodon.datasource.config.RabbitMqConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jinjun_luo
 * @date 5/27/2019
 */
@Service
public class RabbitMqVirtualHostService {

    private static Logger logger = LoggerFactory.getLogger(RabbitMqVirtualHostService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    /**
     * 创建 virtualHost
     *
     * @param virtualHostName
     * @return
     * @throws Exception
     */
    public MqResponseEnum createVirtualHost(String virtualHostName) {
        // 判断此 virtualHostName 是否存在,存在不创建
        try {
            MqResponseEnum mqEnum = vhostsNameExist(virtualHostName);
            if (mqEnum.code == MqResponseEnum.NO_EXIST.code) {
                return execute(virtualHostName, HttpMethod.PUT);
            } else if (mqEnum.code == MqResponseEnum.EXIST.code) {
                return MqResponseEnum.EXIST;
            }
        } catch (Exception e) {
            logger.error("创建 Virtual host 失败，vhostName ： {}", virtualHostName, e);
        }
        throw new ProvisioningException(BundleKey.RABBITMQ_VHOST_NAME_FAIL_ERROR, BundleKey.RABBITMQ_VHOST_NAME_FAIL_ERROR_MSG);
    }

    /**
     * 删除virtualHost
     *
     * @param virtualHostName
     * @return
     * @throws UnsupportedEncodingException
     */
    public MqResponseEnum deleteVirtualHost(String virtualHostName) {
        // 判断此 virtualHostName 是否存在,存在不创建
        try {
            MqResponseEnum mqEnum = vhostsNameExist(virtualHostName);
            if (mqEnum.code == MqResponseEnum.EXIST.code) {
                return execute(virtualHostName, HttpMethod.DELETE);
            } else if (mqEnum.code == MqResponseEnum.NO_EXIST.code) {
                throw new ProvisioningException(BundleKey.RABBITMQ_VHOST_NAME_NOT_EXIST_ERROR, BundleKey.RABBITMQ_VHOST_NAME_NOT_EXIST_ERROR_MSG);
            }
        } catch (Exception e) {
            logger.error("删除 Virtual host 失败，vhostName ： {}", virtualHostName, e);
        }
        throw new ProvisioningException(BundleKey.RABBITMQ_VHOST_NAME_FAIL_ERROR, BundleKey.RABBITMQ_VHOST_NAME_FAIL_ERROR_MSG);
    }

    /**
     * 执行 http 请求
     *
     * @param virtualHostName
     * @param method
     * @return
     */
    private MqResponseEnum execute(String virtualHostName, HttpMethod method) {
        StringBuilder createUrl = buildUrl();
        createUrl.append(virtualHostName);
        ParameterizedTypeReference<String> entity = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> exchange = restTemplate.exchange(URI.create(createUrl.toString()), method, buildHttpEntityHeaders(), entity);
        if (exchange.getStatusCodeValue() == MqResponseEnum.CREATE_SUCCESS.code) {
            return MqResponseEnum.CREATE_SUCCESS;
        } else if (exchange.getStatusCodeValue() == MqResponseEnum.DELETE_SUCCESS.code) {
            return MqResponseEnum.DELETE_SUCCESS;
        }
        throw new ProvisioningException(BundleKey.RABBITMQ_VHOST_NAME_FAIL_ERROR, BundleKey.RABBITMQ_VHOST_NAME_FAIL_ERROR_MSG);
    }

    /***
     * 判断rabbitMq 是否已存在 virtualHostName
     * @param virtualHostName
     * @return
     */
    private MqResponseEnum vhostsNameExist(String virtualHostName) {
        if (StringUtils.isBlank(virtualHostName)) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        StringBuilder existUrl = buildUrl();
        ParameterizedTypeReference<List<MqDto>> entity = new ParameterizedTypeReference<List<MqDto>>() {
        };
        ResponseEntity<List<MqDto>> getList = restTemplate.exchange(URI.create(existUrl.toString()), HttpMethod.GET, buildHttpEntityHeaders(), entity);
        if (getList.getStatusCodeValue() <= MqResponseEnum.SUCCESS.code) {
            List<String> collect = getList.getBody().stream()
                    .filter(Objects::nonNull)
                    .filter(b -> StringUtils.isNotBlank(b.getName()))
                    .map(b -> b.getName())
                    .collect(Collectors.toList());
            return collect.contains(virtualHostName) == true ? MqResponseEnum.EXIST : MqResponseEnum.NO_EXIST;
        }
        return MqResponseEnum.FAIL;
    }

    /**
     * 拼接 url
     *
     * @return
     */
    private StringBuilder buildUrl() {
        StringBuilder existUrl = new StringBuilder();
        existUrl.append(rabbitMqConfig.rabbitmqUrlTitle);
        existUrl.append(rabbitMqConfig.rabbitmqUrlHost);
        existUrl.append(":");
        existUrl.append(rabbitMqConfig.rabbitmqUrlClientePort);
        existUrl.append(rabbitMqConfig.rabbitmqApiVhost);
        return existUrl;
    }

    /**
     * 创建请求头
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private HttpEntity<String> buildHttpEntityHeaders() {
        // 设置请求头
        StringBuilder usernameAndPassword = new StringBuilder();
        usernameAndPassword.append(rabbitMqConfig.rabbitmqAdminUsername);
        usernameAndPassword.append(":");
        usernameAndPassword.append(rabbitMqConfig.rabbitmqAdminPassword);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Basic " + buildBase64Encoder(usernameAndPassword.toString()));
        headers.add("Content-Type", "application/json");
        return new HttpEntity<>(headers);
    }

    /**
     * 转 base64
     *
     * @param str
     * @return
     */
    private String buildBase64Encoder(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            return new BASE64Encoder().encode(str.getBytes("UTF-8"));
        } catch (Exception e) {
            logger.error(" RabbitMq username ,password  转 BASE64Encoder 失败 ： {}", str, e);
        }
        return null;
    }
}