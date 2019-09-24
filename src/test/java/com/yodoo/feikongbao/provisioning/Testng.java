package com.yodoo.feikongbao.provisioning;

import com.feikongbao.storageclient.config.SwiftStorageClientConfig;
import com.feikongbao.storageclient.util.RestTemplateUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.config.ProvisioningSecurityConf;

import net.sf.json.JSONObject;

import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProvisioningConfig.class, ProvisioningSecurityConf.class})
@WebAppConfiguration
public class Testng {

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    String url = "http://localhost:8080/user";

    /**
     * 登录一个用户
     */
    @BeforeClass
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
        requestJson.put("parentId", "2");
        requestJson.put("account", "YODOO_TEST_1");
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
            assertEquals("用户已存在", message);
        } catch (RestClientException e) {
            assertTrue("Error!" + e, 1 == 2);
        }


    }


    /**
     * 查询菜单列表第一页,一页20个，共14条记录。
     */
    @Test
    public void queryMenuListFirstPage() {
        String uri = "http://localhost:8080/menu?pageNum=1&pageSize=20";
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true); // 设置该连接是可以输出的
            connection.setRequestMethod("GET"); // 设置请求方式
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=--------------------------519592953080038764776192");
            connection.addRequestProperty("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) { // 读取数据
                result.append(line + "\n");
            }
            connection.disconnect();
            //System.out.println(result.toString());

            String res = result.toString();
            JsonParser parse =new JsonParser();
            JsonObject object = (JsonObject) parse.parse(res);

            //这是以一个有嵌套的json，需要判断第二层的list的元素中包含正确的数据，设置断言"menuName=客户管理"。

            JsonObject data=(JsonObject)object.get("data");
            JsonArray list=(JsonArray) data.get("list");
            JsonObject list1=(JsonObject)list.get(0);
            String menuName=list1.get("menuName").getAsString() ;
            //System.out.println(menuName);

            assertEquals("客户管理",menuName);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("Erorr! 查询菜单列表请求失败",1==2);
        }
    }



}
