package com.yodoo.feikongbao.provisioning;

import com.feikongbao.storageclient.util.RestTemplateUtils;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.config.ProvisioningSecurityConf;
import net.sf.json.JSONObject;
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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

//import org.Testng.annotations.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProvisioningConfig.class, ProvisioningSecurityConf.class})
@WebAppConfiguration

public class User {

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    String url = "http://localhost:8080/user";

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
     * 添加用户 1: account已经存在
     */
    @Test
    public void addUserAccountExist() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "0");
        requestJson.put("account", "YODOO_TEST_1");
        requestJson.put("name", "张三");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("用户已存在", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }


    }

    /**
     * 添加用户 2: account is null
     */
    @Test
    public void addUserAccountIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", null);
        requestJson.put("name", "张三");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }


    }

    /**
     * 添加用户 3: account is ""
     */
    @Test
    public void addUserAccountIsNothing() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "张三");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 添加用户 4: account is missed
     */
    @Test
    public void addUserAccountIsMissed() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        //requestJson.put("account", null);
        requestJson.put("name", "张三");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }


    }

    /**
     * 添加用户 5: account is too long
     */
    @Test
    public void addUserAccountIsLong() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "09876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890");
        requestJson.put("name", "张三");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }


    }

    /**
     * 添加用户 6: account is new
     */
    @Test
    public void addUserAccountIsNew() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_2");
        requestJson.put("name", "张三");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 1: parentId is not exist
     */
    @Test
    public void addUserParentIdIsNotEixst() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "102");
        requestJson.put("account", "YODOO_TEST_3");
        requestJson.put("name", "张三");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 2: parentId is null
     */
    @Test
    public void addUserParentIdIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", null);
        requestJson.put("account", "YODOO_TEST_4");
        requestJson.put("name", "张三");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 3: parentId is ""
     */
    @Test
    public void addUserParentIdIsNothing() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "");
        requestJson.put("account", "YODOO_TEST_5");
        requestJson.put("name", "张三");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 1: name is ""
     */
    @Test
    public void addUserNmaeIsNothing() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_6");
        requestJson.put("name", "");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 2: name is null
     */
    @Test
    public void addUserNmaeIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_6");
        requestJson.put("name", null);
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 3: name is missed
     */
    @Test
    public void addUserNmaeIsMissed() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_6");
        //requestJson.put("name", null);
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 添加用户 4: name is existed
     * 可以重名
     */
    @Test
    public void addUserNmaeIsExisted() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_6");
        requestJson.put("name", "admin");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 5: name is too long
     * 超过150字符
     */
    @Test
    public void addUserNmaeIsTooLong() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_7");
        requestJson.put("name", "123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 添加用户 1: passwort is ""
     */
    @Test
    public void addUserPasswordIsNothing() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_8");
        requestJson.put("name", "Jim");
        requestJson.put("password", "");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 2: password is null
     */
    @Test
    public void addUserPasswordIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_9");
        requestJson.put("name", "Jim");
        requestJson.put("password", null);
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 添加用户 3: password is space
     */
    @Test
    public void addUserPasswordIsSpace() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_10");
        requestJson.put("name", "Jim");
        requestJson.put("password", " ");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 4: password is less than required
     */
    @Test
    public void addUserPasswordIsSmall() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_111");
        requestJson.put("name", "Jim");
        requestJson.put("password", "3");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 添加用户 5: password is too long,large than 150 char
     */
    @Test
    public void addUserPasswordIsTooLong() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_12");
        requestJson.put("name", "Jim");
        requestJson.put("password", "123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 添加用户 1: Email is ""
     */
    @Test
    public void addUserEmailIsNothing() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_13");
        requestJson.put("name", "zhangang");
        requestJson.put("password", "2");
        requestJson.put("email", "");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 2: Email is null
     */
    @Test
    public void addUserEmailIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_14");
        requestJson.put("name", null);
        requestJson.put("password", "2");
        requestJson.put("email", null);
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 3: Email is missed
     */
    @Test
    public void addUserEmailIsMissed() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_15");
        requestJson.put("name", null);
        requestJson.put("password", "2");
        //requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 添加用户 4: Email is existed
     * 可以重名
     */
    @Test
    public void addUserEmailIsExisted() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_16");
        requestJson.put("name", "admin");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 5: Email is too long
     * 超过150字符
     */
    @Test
    public void addUserEmailIsTooLong() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_17");
        requestJson.put("name", "123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890@126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 6: Email is illegal
     * 超过150字符
     */
    @Test
    public void addUserEmailIsIllegal() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_18");
        requestJson.put("name", "@126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }



    /**
     * 添加用户 1: Sex is ""
     */
    @Test
    public void addUserSexIsNothing() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_19");
        requestJson.put("name", "zhangang");
        requestJson.put("password", "2");
        requestJson.put("email", "123@123.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 2: Sex  is null
     */
    @Test
    public void addUserSexIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_20");
        requestJson.put("name", "haha");
        requestJson.put("password", "2");
        requestJson.put("email", "123@123.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", null);
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 3: Sex  is missed
     */
    @Test
    public void addUserSexIsMissed() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_21");
        requestJson.put("name", null);
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        //requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 添加用户 4: Sex  is not in [0,1,2]
     * 可以重名
     */
    @Test
    public void addUserSexIsNotExisted() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_22");
        requestJson.put("name", "admin");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "3");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 5: Sex is too long
     * 超过1字符
     */
    @Test
    public void addUserSexIsTooLong() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_23");
        requestJson.put("name", "126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "11");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 6: Sex is not  int
     * 超过1字符
     */
    @Test
    public void addUserSexIsNotInt() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_26");
        requestJson.put("name", "126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "r");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String status = response.getString("status");

            assertEquals("400", status);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 1 ： id is missed
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserIdIsMissed(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        //requestJson.put("id","13");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "88222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 2 ： id is not existed
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserIdIsNotExisted(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","33");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "88222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("用户不存在", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 更新用户 3 ： id is minus
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserIdIsMinus(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","-33");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "88222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 更新用户 4 ： id is null
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserIdIsNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",null);

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "88222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 更新用户 5 ： id is ""
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserIdIsNothing(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",null);

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "88222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 6 ： name change to null
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserNameChangeToNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", null);
        requestJson.put("password", "2");
        requestJson.put("email", "88222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 7 ： name change to ""
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserNameChangeToNothing(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "");
        requestJson.put("password", "2");
        requestJson.put("email", "88222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 8 ： name change to space
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserNameChangeToSpace(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", " ");
        requestJson.put("password", "2");
        requestJson.put("email", "88222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 更新用户 9 ： name change to too long
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserNameChangeToTooLong(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "1");
        requestJson.put("name", "123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890");
        requestJson.put("password", "2");
        requestJson.put("email", "88222@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 10 ： Email change to null
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserEmailChangeToNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "hello");
        requestJson.put("password", "2");
        requestJson.put("email", null);
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 11 ： Email change to ""
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserEmailChangeToNothing(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "name");
        requestJson.put("name", "nema");
        requestJson.put("password", "2");
        requestJson.put("email", "");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 12 ： Email change to space
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserEmailChangeToSpace(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "shanghao");
        requestJson.put("password", "2");
        requestJson.put("email", " ");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 更新用户 13 ： Email change to too long
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserEmailChangeToTooLong(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "1");
        requestJson.put("name", "shanghai");
        requestJson.put("password", "2");
        requestJson.put("email", "123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户 14: Email is illegal
     * 超过150字符
     */
    @Test
    public void updateUserEmailIsIllegal() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "32");
        requestJson.put("account", "YODOO_TEST_18");
        requestJson.put("name", "@126.com");
        requestJson.put("password", "2");
        requestJson.put("email", "146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "1");
        requestJson.put("birthday", "1972-09-09");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 15 ： Birthday change to null
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserBirthdayChangeToNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "hello");
        requestJson.put("password", "2");
        requestJson.put("email", "123@12.COM");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", null);
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 16 ： Birthday change to ""
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserBirthdayChangeToNothing(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "name");
        requestJson.put("name", "nema");
        requestJson.put("password", "2");
        requestJson.put("email", "");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 17 ： Birthday change to space
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserBirthdayChangeToSpace(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "shanghao");
        requestJson.put("password", "2");
        requestJson.put("email", " ");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", " ");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 更新用户 18 ： Birthday change to too long
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserBirthdayChangeToTooLong(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "1");
        requestJson.put("name", "shanghai");
        requestJson.put("password", "2");
        requestJson.put("email", "123@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890");
        requestJson.put("phone", "13838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }



    /**
     * 更新用户 19 ： Phone change to null
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserPhoneChangeToNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "hello");
        requestJson.put("password", "2");
        requestJson.put("email", "123@12.COM");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "2019-08-08");
        requestJson.put("phone", null);
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 20 ： Phone change to ""
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserPhoneChangeToNothing(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "name");
        requestJson.put("name", "nema");
        requestJson.put("password", "2");
        requestJson.put("email", "");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "2019-08-08");
        requestJson.put("phone", "");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 更新用户 17 ： Phone change to space
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserPhoneChangeToSpace(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "");
        requestJson.put("name", "shanghao");
        requestJson.put("password", "2");
        requestJson.put("email", " ");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "2019-08-08");
        requestJson.put("phone", " ");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 更新用户 18 ： Phone change to too long
     * name ,email,birthday,phone
     */
    @Test
    public void updateUserPhoneChangeToTooLong(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id","32");

        requestJson.put("parentId", "2");
        requestJson.put("account", "1");
        requestJson.put("name", "shanghai");
        requestJson.put("password", "2");
        requestJson.put("email", "123@146.com");
        requestJson.put("region", "SH");
        requestJson.put("post", "2");
        requestJson.put("sex", "0");
        requestJson.put("birthday", "2019-08-08");
        requestJson.put("phone", "1383838438813838384388138383843881383838438813838384388");
        requestJson.put("status", "1");
        requestJson.put("userGroupIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");

            assertEquals("电话格式错误", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     *修改用户密码 1：user id is existed
     *修改为：abc123456
     */
    @Test
    public void updateUserPasswordUserIdIsExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("password","abc123456");
        requestJson.put("id","32");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/updateUserPassword", HttpMethod.POST, headers,requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     *修改用户密码 2：user id is not existed
     *修改为：abc123456
     */
    @Test
    public void updateUserPasswordUserIdIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("password","abc123456");
        requestJson.put("id","132");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/updateUserPassword", HttpMethod.POST, headers,requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("用户不存在", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     *修改用户密码 3：user id is null
     *修改为：abc123456
     */
    @Test
    public void updateUserPasswordUserIdIsNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("password","abc123456");
        requestJson.put("id",null);
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/updateUserPassword", HttpMethod.POST, headers,requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     *修改用户密码 4：user id is “”
     *修改为：abc123456
     */
    @Test
    public void updateUserPasswordUserIdIsNothing(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("password","abc123456");
        requestJson.put("id","");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/updateUserPassword", HttpMethod.POST, headers,requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     *修改用户密码 5：user id is space
     *修改为：abc123456
     */
    @Test
    public void updateUserPasswordUserIdIsSpace(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("password","abc123456");
        requestJson.put("id"," ");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/updateUserPassword", HttpMethod.POST, headers,requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     *修改用户密码 6：user id is minus
     *修改为：abc123456
     */
    @Test
    public void updateUserPasswordUserIdToMinus(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("password","abc123456");
        requestJson.put("id","-1");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/updateUserPassword", HttpMethod.POST, headers,requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     *修改用户密码 6：change pwd to ""
     *修改为：null
     */
    @Test
    public void updateUserPasswordToNothing(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("password","");
        requestJson.put("id","32");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/updateUserPassword", HttpMethod.POST, headers,requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     *修改用户密码 7：change pwd to null
     *修改为：null
     */
    @Test
    public void updateUserPasswordToNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("password",null);
        requestJson.put("id","32");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/updateUserPassword", HttpMethod.POST, headers,requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     *修改用户密码 8：change pwd to long
     *修改为：null
     */
    @Test
    public void updateUserPasswordToLong(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("password","1383838438813838384388138383843881383838438813838384388138383843881383838438813838384388138383843881383838438813838384388138383843881383838438813838384");
        requestJson.put("id","32");
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/updateUserPassword", HttpMethod.POST, headers,requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     *重置用户密码 1：id is existed
     *重置为：yodoo123
     */
    @Test
    public void resetUserPasswordIdIsExisted(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id=32;
        String uri="http://localhost:8080/user/resetUserPassword/"+id;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     *重置用户密码 2：id is minus
     *重置为：yodoo123
     */
    @Test
    public void resetUserPasswordIdIsMinus(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id = -1;
        String uri="http://localhost:8080/user/resetUserPassword/"+id;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     *重置用户密码 3：id is not exist
     *重置为：yodoo123
     */
    @Test
    public void resetUserPasswordIdIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id = 221;
        String uri="http://localhost:8080/user/resetUserPassword/"+id;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("用户不存在", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 用户的启用和停用 1 ：id is not existed
     *
     */
    @Test
    public void updateUserStatusIdIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id = 221;
        int status=0;
        String uri="http://localhost:8080/user/updateUserStatus/"+id+"/"+status;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("用户不存在", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 用户的启用和停用 2 ：id is minus
     *
     */
    @Test
    public void updateUserStatusIdIsMinus(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id = -21;
        int status=0;
        String uri="http://localhost:8080/user/updateUserStatus/"+id+"/"+status;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 用户的启用和停用 3 ：status is minus
     *
     */
    @Test
    public void updateUserStatuIsMinus(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id = 21;
        int status=-1;
        String uri="http://localhost:8080/user/updateUserStatus/"+id+"/"+status;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 用户的启用和停用 4 ：status is not exist
     *
     */
    @Test
    public void updateUserStatuIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id = 21;
        int status=3;
        String uri="http://localhost:8080/user/updateUserStatus/"+id+"/"+status;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("参数异常", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 用户的启用和停用 5 ：changer status from 0 to 1
     *
     */
    @Test
    public void updateUserStatusFrom1To0(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id = 21;
        int status=0;
        String uri="http://localhost:8080/user/updateUserStatus/"+id+"/"+status;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 用户的启用和停用 6 ：changer status from 1 to 0
     *
     */
    @Test
    public void updateUserStatusFrom0To1(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id = 21;
        int status=1;
        String uri="http://localhost:8080/user/updateUserStatus/"+id+"/"+status;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 用户的启用和停用 7 ：changer status from 0 to 0
     *
     */
    @Test
    public void updateUserStatusFrom0To0(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        int id = 21;
        int status=0;
        String uri="http://localhost:8080/user/updateUserStatus/"+id+"/"+status;

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(uri, HttpMethod.PUT, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            assertEquals("请求成功", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }
    }


}





