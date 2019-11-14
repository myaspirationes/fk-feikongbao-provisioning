package com.yodoo.feikongbao.provisioning;

import com.feikongbao.storageclient.util.RestTemplateUtils;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.config.ProvisioningSecurityConf;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

public class backstageManage {
    @Autowired
    private RestTemplateUtils restTemplateUtils;

    @Test
    public void initDefinitionPage(){
        String url = "http://192.168.1.18:5001/pageInit/initDefinitionPage";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("messageValue");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


}
