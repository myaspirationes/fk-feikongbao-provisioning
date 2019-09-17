package com.yodoo.feikongbao.provisioning;

import com.feikongbao.storageclient.util.RestTemplateUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.config.ProvisioningSecurityConf;

import com.yodoo.megalodon.permission.dto.UserDto;
import net.sf.json.JSONArray;
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
import tk.mybatis.mapper.genid.GenId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProvisioningConfig.class, ProvisioningSecurityConf.class})
@WebAppConfiguration
public class UserGroup {

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    String url = "http://localhost:8080/userGroup";

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
     * 添加用户组 1: groupCode不存在
     * user_group code
     */
    @Test
    public void addUserGroupCodeIsNotExist() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_2");
        requestJson.put("groupName", "YODOO_TEST_1");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 添加用户组 2: groupCode存在
     * user_group code
     */
    @Test
    public void addUserGroupCodeIsExist() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_2");
        requestJson.put("groupName", "YODOO_TEST_1");
        requestJson.put("permissionGroupIds", "[]");
        requestJson.put("searchConditionIds", "[]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("用户组已存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);

        }


    }

    /**
     * 添加用户组 3: groupCode is null
     * user_group code
     */
    @Test
    public void addUserGroupCodeIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", null);
        requestJson.put("groupName", "YODOO_TEST_1");
        requestJson.put("permissionGroupIds", "[1,2]");
        requestJson.put("searchConditionIds", "[2,3]");
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
     * 添加用户组 4: groupCode is “”
     * user_group code
     */
    @Test
    public void addUserGroupCodeIsNothing() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "");
        requestJson.put("groupName", "YODOO_TEST_1");
        requestJson.put("permissionGroupIds", "[1,2]");
        requestJson.put("searchConditionIds", "[2,3]");
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
     * 添加用户组 5: groupCode is too long
     * user_group code
     */
    @Test
    public void addUserGroupCodeIsTooLong() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "123456789009876543211234567890098765432109876543211");
        requestJson.put("groupName", "YODOO_TEST_1");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[2,3]");
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
     * 添加用户组 6: groupCode is missed
     * user_group code
     */
    @Test
    public void addUserGroupCodeIsMissed() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        JSONObject requestJson = new JSONObject();
        //requestJson.put("groupCode", "123456789009876543211234567890098765432109876543211");
        requestJson.put("groupName", "YODOO_TEST_1");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[2,3]");
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
     * 添加用户组 7: groupName不存在
     * user_group code
     */
    @Test
    public void addUserGroupNameIsNotExist() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_3");
        requestJson.put("groupName", "YODOO_TEST_3");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 添加用户组 8: groupName缺失
     * user_group code
     */
    @Test
    public void addUserGroupNameIsMissed() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_2");
        //requestJson.put("groupName", "YODOO_TEST_3");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 添加用户组 9: groupName is null
     * user_group code
     */
    @Test
    public void addUserGroupNameIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_2");
        requestJson.put("groupName", null);
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 添加用户组 10: groupName is ""
     * user_group code
     */
    @Test
    public void addUserGroupNameIsNothing() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_4");
        requestJson.put("groupName", "");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 添加用户组 10: groupName is " "
     * user_group code
     */
    @Test
    public void addUserGroupNameIsSpace() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_5");
        requestJson.put("groupName", " ");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 添加用户组 11: permissionGroupIds is ""
     *
     */
    @Test
    public void addUserGroupPermissionGroupIdsIsSpace() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_4");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 添加用户组 12: permissionGroupIds is null
     * 预期 请求成功
     */
    @Test
    public void addUserGroupPermissionGroupIdsIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_4");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", null);
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 添加用户组 13: permissionGroupIds is not exist
     *
     */
    @Test
    public void addUserGroupPermissionGroupIdsIsNotExist() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_6");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[1,2]");
        requestJson.put("searchConditionIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限组不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户组 14: permissionGroupIds is   existed
     *
     */
    @Test
    public void addUserGroupPermissionGroupIdsIsExisted() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_6");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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

    //SearchConditionIds

    /**
     * 添加用户组 15: SearchConditionIds is ""
     *
     */
    @Test
    public void addUserGroupSearchConditionIdsSpace() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_7");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "");

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
     * 添加用户组 16: SearchConditionIds is null
     * 预期 请求成功
     */
    @Test
    public void addUserGroupSearchConditionIdsIsNull() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_7");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", null);

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
     * 添加用户组 17: SearchConditionIds is not exist
     *
     */
    @Test
    public void addUserGroupSearchConditionIdsIsNotExist() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_8");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[1,2]");
        requestJson.put("searchConditionIds", "[111,222]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限组不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 添加用户组 18: SearchConditionIds is   existed
     *
     */
    @Test
    public void addUserGroupSearchConditionIdsIsExisted() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("groupCode", "GroupCode_test_8");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 1：Id Is Not Exist
     */
    @Test
    public  void editUserGroupIdIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "888");
        requestJson.put("groupCode", "GroupCode_test_8");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("用户组不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);

        }
    }

    /**
     * 修改用户 2：Id Is  null
     */
    @Test
    public  void editUserGroupIdIsNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", null);
        requestJson.put("groupCode", "GroupCode_test_8");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 3： change GroupCode to null
     */
    @Test
    public  void editUserGroupCodeIsNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", null);
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 4：change  GroupCode Is  exist
     */
    @Test
    public  void editUserGroupCodeIsExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_2");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("用户组已存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);

        }
    }


    /**
     * 修改用户 5：change  GroupCode ""
     */
    @Test
    public  void editUserGroupCodeIsNothing(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 6：   GroupCode not exist
     */
    @Test
    public  void editUserGroupCodeIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 7：   GroupCode is missed
     */
    @Test
    public  void editUserGroupCodeIsMissed(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        //requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 8：   GroupName is missed
     */
    @Test
    public  void editUserGroupNameIsMissed(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        //requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 9：   GroupName is null
     */
    @Test
    public  void editUserGroupNameIsNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", null);
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 10：   GroupName is existed
     */
    @Test
    public  void editUserGroupNameIsExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "测试组");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 11：   GroupName is too long
     */
    @Test
    public  void editUserGroupNameIsLong(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "a123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890123456789009876543211234567890");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * 修改用户 12：   PermissionGroupIds is Missed
     */
    @Test
    public  void editUserGroupPermissionGroupIdsIsMissed(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "yoarefine");
        //requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 13：   PermissionGroupIds is Null
     *
     * 是不是权限被删除了？？？
     *
     */
    @Test
    public  void editUserGroupPermissionGroupIdsIsNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "yoarefine");
        requestJson.put("permissionGroupIds", null);
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 14：   PermissionGroupIds is not exist
     */
    @Test
    public  void editUserGroupPermissionGroupIdsIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "yoarefine");
        requestJson.put("permissionGroupIds", "[8,99]");
        requestJson.put("searchConditionIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限组不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 修改用户 15：   PermissionGroupIds is  exist
     */
    @Test
    public  void editUserGroupPermissionGroupIdsIsExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_122");
        requestJson.put("groupName", "yoarefine");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 修改用户 16：   SearchConditionIds is Missed
     */
    @Test
    public  void editUserGroupSearchConditionIdsIsMissed(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "yoarefine");
        requestJson.put("permissionGroupIds", "[4,6]");
        //requestJson.put("searchConditionIds", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 修改用户 17：   SearchConditionIds is Null
     *
     * ？？？
     *
     */
    @Test
    public  void editUserGroupSearchConditionIdsIsNull(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "yoarefine");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds",null);

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("服务异常,请联系管理员", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 修改用户 18：   SearchConditionIds is not exist
     */
    @Test
    public  void editUserGroupSearchConditionIdsIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_12");
        requestJson.put("groupName", "yoarefine");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[111,222]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.PUT, headers, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("查询条件不存在", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     * 修改用户组 19：   SearchConditionIds is  exist
     */
    @Test
    public  void editUserGroupSearchConditionIdsIsExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "16");
        requestJson.put("groupCode", "GroupCode_test_122");
        requestJson.put("groupName", "yoarefine");
        requestJson.put("permissionGroupIds", "[4,6]");
        requestJson.put("searchConditionIds", "[1,2]");

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
     * 删除用户组 1：   Id is  exist
     */
    @Test
    public  void deleteUserGroupIdIsExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        String id= "16";
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/"+id, HttpMethod.DELETE, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     * 删除用户组 2：   Id is not exist
     */
    @Test
    public  void deleteUserGroupIdIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        String id= "16";
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/"+id, HttpMethod.DELETE, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }


    /**
     *用户组批处理 1： Id is not exist
     *
     */
    @Test
    public  void userGroupBatchProcessingUserGroupIdIsNotExist(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        String id= "93";
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/userGroupBatchProcessing?userGroupId="+id, HttpMethod.POST, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }




}