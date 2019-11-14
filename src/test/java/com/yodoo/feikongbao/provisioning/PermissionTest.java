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
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProvisioningConfig.class, ProvisioningSecurityConf.class})
@WebAppConfiguration

public class PermissionTest {

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    String uri = "http://localhost:8080/permission";

    /**
     * 登录一个用户
     */
    @Test
    public void userLogin() {
        String uri = "http://localhost:8080/login";
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true); // 设置该连接是可以输出的
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=--------------------------519592953080038764776192");
            connection.addRequestProperty("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) { // 读取数据
                result.append(line + "\n");
            }
            connection.disconnect();
            System.out.println(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add permission :id is MISSED
     */
    @Test
    public void addPermissionIdsIsMissed() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        //requestJson.put("id", "0");
        requestJson.put("permissionCode", "permission_code_4");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Add permission :id is NULL
     */
    @Test
    public void addPermissionIdsIsNull() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("permissionCode", "permission_code_5");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Add permission :id is exist
     */
    @Test
    public void addPermissionIdsIsExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "1");
        requestJson.put("permissionCode", "permission_code_8");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Add permission :id is not exist
     */
    @Test
    public void addPermissionIdsIsNotExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "129");
        requestJson.put("permissionCode", "permission_code_6");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Add permission :permissionCode is not exist
     */
    @Test
    public void addPermissionCodeIsNotExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "");
        requestJson.put("permissionCode", "permission_code_7");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Add permission :permissionCode is exist
     */
    @Test
    public void addPermissionCodeIsExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "");
        requestJson.put("permissionCode", "permission_code_7");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限已存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Add permission :permissionCode is null
     */
    @Test
    public void addPermissionCodeIsNull() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "");
        requestJson.put("permissionCode", null);
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * Add permission :permissionCode is missed
     */
    @Test
    public void addPermissionCodeIsMissed() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "");
        //requestJson.put("permissionCode", "permission_code_10");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Add permission :permissionName is not exist
     */
    @Test
    public void addPermissionNameIsNotExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "");
        requestJson.put("permissionCode", "permission_code_11");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Add permission :permissionName is exist
     */
    @Test
    public void addPermissionNameIsExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "");
        requestJson.put("permissionCode", "permission_code_12");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限已存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Add permission :permissionName is null
     */
    @Test
    public void addPermissionNameIsNull() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "");
        requestJson.put("permissionCode", "permission_code_12");
        requestJson.put("permissionName", null);

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * Add permission :permissionName is missed
     */
    @Test
    public void addPermissionNameIsMissed() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "");
        requestJson.put("permissionCode", "permission_code_10");
        //requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * Edit permission :id is MISSED
     */
    @Test
    public void editPermissionIdsIsMissed() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        //requestJson.put("id", "0");
        requestJson.put("permissionCode", "permission_code_4");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :id is NULL
     */
    @Test
    public void editPermissionIdsIsNull() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("permissionCode", "permission_code_5");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :id is exist
     */
    @Test
    public void editPermissionIdsIsExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "1");
        requestJson.put("permissionCode", "permission_code_8");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :id is not exist
     */
    @Test
    public void editPermissionIdsIsNotExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "1029");
        requestJson.put("permissionCode", "permission_code_6");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :permissionCode is not exist
     */
    @Test
    public void editPermissionCodeIsNotExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "130");
        requestJson.put("permissionCode", "permission_code_17");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :change permissionCode to a  exist one
     */
    @Test
    public void editPermissionCodeIsExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "130");
        requestJson.put("permissionCode", "permission_code_6");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限已存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :permissionCode is null
     */
    @Test
    public void editPermissionCodeIsNull() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "130");
        requestJson.put("permissionCode", null);
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * Edit permission :permissionCode is missed
     */
    @Test
    public void editPermissionCodeIsMissed() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "130");
        //requestJson.put("permissionCode", "permission_code_10");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :permissionName is not exist
     */
    @Test
    public void editPermissionNameIsNotExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "130");
        requestJson.put("permissionCode", "permission_code_11");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :permissionName is exist
     */
    @Test
    public void editPermissionNameIsExist() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "130");
        requestJson.put("permissionCode", "permission_code_7");
        requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :permissionName is null
     */
    @Test
    public void editPermissionNameIsNull() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "130");
        requestJson.put("permissionCode", "permission_code_7");
        requestJson.put("permissionName", null);

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * Edit permission :permissionName is missed
     */
    @Test
    public void editPermissionNameIsMissed() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "130");
        requestJson.put("permissionCode", "permission_code_10");
        //requestJson.put("permissionName", "permission_name_1");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Edit permission :permissionName and permissionCode changed
     */
    @Test
    public void editPermissionNameAndCodeChanged() {

        String url = "http://localhost:8080/permission";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "130");
        requestJson.put("permissionCode", "permission_code_13");
        requestJson.put("permissionName", "permission_name_13");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * Delete permission : id  is not existed
     */
    @Test
    public void deletePermissionIdIsNotExist() {

        String url = "http://localhost:8080/permission";
        String id = "124";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.DELETE, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Delete permission : id  is existed
     */
    @Test
    public void deletePermissionIdIsExist() {

        String url = "http://localhost:8080/permission";
        String id = "124";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.DELETE, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Delete permission : permission is used
     */
    @Test
    public void deletePermissionIdIsInUsed() {

        String url = "http://localhost:8080/permission";
        String id = "124";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.DELETE, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * 更新用户管理目标用户数据
     * id is null
     * 查询user_permission_target_user_details 表查看关联权限
     */

    @Test
    public void updateUserPermissionTargetUserIdIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * userid is null
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetUserUserIdIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", null);
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * userid is not exist
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetUserUserIdIsNotExist() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "133");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("用户权限不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * userid is missed
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetUserUserIdIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        //requestJson.put("userId","133");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * permissionId is null
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetUserPermissionIdIdIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", null);
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Permissionid is not exist
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetUserPermissionIdIsNotExist() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "333");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals(" 用户权限不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * Permissionid is missed
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetUserPermissionIdIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        // requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * targetIds is null
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetUserTargetIdsIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", 3);
        requestJson.put("targetIds", null);
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * targetIds is not exist
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetUserTargetIdsNotExist() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[111,333]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals(" 用户不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * targetIds is missed
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetUserTargetIdsIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetUser/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "3");
        // requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * 变更用户管理目标集团
     * id is missed
     */
    @Test
    public void updateUserPermissionTargetGroupsIdsIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        //requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "5");
        requestJson.put("targetIds", "[1]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /*
     * userid is missed
     */
    @Test
    public void updateUserPermissionTargetGroupsUserIdsIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        //requestJson.put("userId","13");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[1]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * userid is null
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetGroupsUserIdIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups//";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", null);
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * userid is not exist
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetGroupsUserIdIsNotExist() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "133");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("用户权限不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * permissionId is null
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetGroupsPermissionIdIdIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", null);
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Permissionid is not exist
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetGroupsPermissionIdIsNotExist() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "333");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals(" 用户权限不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * Permissionid is missed
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetGroupsPermissionIdIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        // requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * targetIds is null
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetGroupsTargetIdsIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", 3);
        requestJson.put("targetIds", null);
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * targetIds is not exist
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetGroupsTargetIdsNotExist() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[111,333]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals(" 集团不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * targetIds is missed
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetGroupsTargetIdsIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetGroups/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "3");
        // requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * 更新用户管理目标公司数据
     * id is missed
     */
    @Test
    public void updateUserPermissionTargetCompanyIdsIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        //requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "5");
        requestJson.put("targetIds", "[5]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /*
     * userid is missed
     */
    @Test
    public void updateUserPermissionTargetCompanyUserIdsIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        //requestJson.put("userId","13");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[5]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * userid is null
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetCompanysUserIdIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", null);
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[5,6]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * userid is not exist
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetCompanyUserIdIsNotExist() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "133");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[5,6]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("用户权限不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * permissionId is null
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetCompanyPermissionIdIdIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", null);
        requestJson.put("targetIds", "[5,6]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * Permissionid is not exist
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetCompanyPermissionIdIsNotExist() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "333");
        requestJson.put("targetIds", "[5,6]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals(" 用户权限不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * Permissionid is missed
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetCompanyPermissionIdIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        // requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[5,6]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * targetIds is null
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetCompanyTargetIdsIsNull() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", 3);
        requestJson.put("targetIds", null);
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * targetIds is not exist
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetCompanyTargetIdsNotExist() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "3");
        requestJson.put("targetIds", "[111,333]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("公司不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * targetIds is missed
     * 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void updateUserPermissionTargetCompanyTargetIdsIsMissed() {
        String url = "http://localhost:8080/permission/updateUserPermissionTargetCompany/";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("userId", "13");
        requestJson.put("permissionId", "3");
        // requestJson.put("targetIds", "[1,3]");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * 在用户列表中点击权限,获取目标集团、目标公司、目标用户的list  /permission/actionPermissionInUserList/{userId}
     * id 存在
     */
    @Test
    public void actionPermissionInUserList() {

        String url = "http://localhost:8080/permission/actionPermissionInUserList/";
        String id = "1";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * 在用户列表中点击权限,获取目标集团、目标公司、目标用户的list  /permission/actionPermissionInUserList/{userId}
     * user表 user_id 不存在
     */
    @Test
    public void actionPermissionInUserListIdNotExist() {

        String url = "http://localhost:8080/permission/actionPermissionInUserList/";
        String id = "13";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 在用户列表中点击权限,获取目标集团、目标公司、目标用户的list  /permission/actionPermissionInUserList/{userId}
     * user表 user_id 没有关联权限, 查询user_permission_target_user_details 表查看关联权限
     */
    @Test
    public void actionPermissionInUserListIdHasNoPermission() {

        String url = "http://localhost:8080/permission/actionPermissionInUserList/";
        String id = "13";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("data").substring(1, 1);

            Assert.assertTrue("error!!! Id must be null!!!!", message.isEmpty());
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询可管理的目标集团
     * user id is existed
     */
    @Test
    public void getAvailableGroupsByUserIdIsExisted() {

        String url = "http://localhost:8080/permission/getAvailableGroupsByUserId/";
        String id = "13";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询可管理的目标集团
     */
    @Test
    public void getAvailableGroupsByUserIdIsNotExisted() {

        String url = "http://localhost:8080/permission/getAvailableGroupsByUserId/";
        String id = "13";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询可管理的目标集团
     */
    @Test
    public void getAvailableGroupsByUserIdIsNull() {

        String url = "http://localhost:8080/permission/getAvailableGroupsByUserId/";
        String id = "13";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询可管理的目标公司
     * user id is existed
     */
    @Test
    public void getAvailableTargetCompanyByUserIdIsExisted() {

        String url = "http://localhost:8080/permission/getAvailableTargetCompanyByUserId/";
        String id = "13";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询可管理的目标公司
     */
    @Test
    public void getAvailableTargetCompanyByUserIdIsNotExisted() {

        String url = "http://localhost:8080/permission/getAvailableTargetCompanyByUserId";
        String id = "138";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询可管理的目标公司
     * userID is missed
     */
    @Test
    public void getAvailableTargetCompanyByUserIdIsMissed() {

        String url = "http://localhost:8080/permission/getAvailableTargetCompanyByUserId/";
        String id = "13";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (Exception e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * 通过用户id查询可管理的目标集团
     * user id is existed
     */
    @Test
    public void getTargetGroupsByUserIdIsExisted() {

        String url = "http://localhost:8080/permission/getTargetGroupsByUserId/";
        String id = "13";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询可管理的目标集团
     * user id is existed but has no managed group
     */
    @Test
    public void getTargetGroupsByUserIdHasNoManagedGroup() {

        String url = "http://localhost:8080/permission/getTargetGroupsByUserId/";
        String id = "35";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            String data = response.getString("data");
            Assert.assertEquals("请求成功", message);
            Assert.assertEquals("null", data);


        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询可管理的目标集团
     */
    @Test
    public void getTargetGroupsByUserIdIsNotExisted() {

        String url = "http://localhost:8080/permission/getTargetGroupsByUserId/";
        String id = "133";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            String data = response.getString("data");
            Assert.assertEquals("请求成功", message);
            Assert.assertEquals("null", data);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询可管理的目标集团
     */
    @Test
    public void getTargetGroupsByUserIdIsNull() {

        String url = "http://localhost:8080/permission/getTargetGroupsByUserId/";
        String id = null;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * 通过用户id查询已管理的目标公司
     * user id is existed
     */
    @Test
    public void getUserManageTargetCompanyByUserIdIsExisted() {

        String url = "http://localhost:8080/permission/getUserManageTargetCompanyListByUserId/";
        String id = "13";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询已管理的目标公司
     * *user id is  not existed
     */
    @Test
    public void getUserManageTargetCompanyByUserIdIsNotExisted() {

        String url = "http://localhost:8080/permission/getUserManageTargetCompanyListByUserId/";
        String id = "138";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + "/" + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 通过用户id查询已管理的目标公司
     * userID is missed   getUserManageTargetCompanyListByUserId/{userId}
     * 通过用户id
     */
    @Test
    public void getUserManageTargetCompanyByUserIdIsMissed() {

        String url = "http://localhost:8080/permission/getUserManageTargetCompanyListByUserId/";
        String id = "";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (Exception e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /*根据userId查询权限
     *User id is not existed
     */
    @Test
    public void getPermissionByUserIdIsNotExisted() {
        String url = "http://localhost:8080/permission/getPermissionByUserId/";
        Map<String, String> headers = new HashMap<>();
        String id = "555";
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            String data = response.getString("data");
            Assert.assertEquals("请求成功", message);
            Assert.assertEquals("null", data);
        } catch (Exception e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }


    }

    /*根据userId查询权限
     *User id is null
     */
    @Test
    public void getPermissionByUserIdIsNull() {
        String url = "http://localhost:8080/permission/getPermissionByUserId/";
        Map<String, String> headers = new HashMap<>();
        String id = "";
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            String data = response.getString("data");
            Assert.assertEquals("请求成功", message);
            Assert.assertEquals("null", data);
        } catch (Exception e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /*根据userId查询权限
     *User id is existed
     */
    @Test
    public void getPermissionByUserIdIsExisted() {
        String url = "http://localhost:8080/permission/getPermissionByUserId/";
        Map<String, String> headers = new HashMap<>();
        String id = "13";
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            String data = response.getString("data");
            Assert.assertEquals("请求成功", message);
            Assert.assertTrue("错误！不应该为空！！", data.isEmpty());
        } catch (Exception e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /*根据userId查询权限
     *User id is existed 没有权限
     */
    @Test
    public void getPermissionByUserIdHasNoPermission() {
        String url = "http://localhost:8080/permission/getPermissionByUserId/";
        Map<String, String> headers = new HashMap<>();
        String id = "35";
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url + id, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            System.out.println(response);
            String message = response.getString("message");
            String data = response.getString("data");
            Assert.assertEquals("请求成功", message);
            Assert.assertEquals("null", data);
        } catch (Exception e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

}

