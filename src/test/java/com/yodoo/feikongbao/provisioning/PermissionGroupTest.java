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

public class PermissionGroupTest {

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    String uri="localhost:8080/permissionGroup";

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
     * 查询权限组pageNum不传
     *
     */
    @Test
    public  void queryPermissionListWithNoPageNum() {
        int pageNum=10;
        String url="http://localhost:8080/permissionGroup?pageSize=10&pageNum=";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (Exception e) {
            Assert.assertEquals("Error!" + e, 1 == 2);
        }
    }


    /**
     * 查询权限组pageNum is 0
     *
     */
    @Test
    public  void queryPermissionListPageNumIs0() {
        int pageNum=0;
        String url="http://localhost:8080/permissionGroup?pageSize=10&pageNum="+pageNum;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (Exception e) {
            Assert.assertEquals("Error!" + e, 1 == 2);
        }
    }


    /**
     * 查询权限组pageNum is 100,大于实际总数/pageSize
     *
     */
    @Test
    public  void queryPermissionListPageNumIsLarge() {
        int pageNum=100;
        String url="http://localhost:8080/permissionGroup?pageSize=10&pageNum="+pageNum;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (Exception e) {
            Assert.assertEquals("Error!" + e, 1 == 2);
        }
    }

    /**
     * 查询权限组pageNum is 100,大于实际总数/pageSize
     *
     */
    @Test
    public  void queryPermissionListPageSize(){
        int pageNum=100;
        String url="http://localhost:8080/permissionGroup?pageSize=10&pageNum="+pageNum;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.GET, headers, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (Exception e) {
            Assert.assertEquals("Error!" + e, 1 == 2);
        }
    }


    /**
     *Add permission group: success
     *
     */
    @Test
    public  void addPermissionGroup(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_1");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");

    try{
        ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
        String body = exchange.getBody();
        JSONObject response = JSONObject.fromObject(body);
        String message = response.getString("message");
        Assert.assertEquals("请求成功", message);

    }catch(RestClientException e){
        Assert.assertTrue("Error!" + e, 1 == 2);
    }

    }


    /**
     *Add permission group: groupCode is existed
     *
     */
    @Test
    public  void addPermissionGroupIdIsExisted(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_2");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限组已存在", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: groupCode is missed
     *
     */
    @Test
    public  void addPermissionGroupCodeIsMissed(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        //requestJson.put("groupCode", "permisson_group_code_2");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: groupCode is NULL
     *
     */
    @Test
    public  void addPermissionGroupCodeIsNull(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", null);
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: groupCode is Nothing
     *
     */
    @Test
    public  void addPermissionGroupCodeIsNothing(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: groupCode is Space
     *
     */
    @Test
    public  void addPermissionGroupCodeIsSpace(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", " ");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: groupName is Existed
     *
     */
    @Test
    public  void addPermissionGroupNameIsExisted(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_45");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }


    /**
     *Add permission group: GroupName is missed
     *
     */
    @Test
    public  void addPermissionGroupNameIsMissed(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_4");
        //requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: GroupName is NULL
     *
     */
    @Test
    public  void addPermissionGroupNameIsNull(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_4");
        requestJson.put("groupName", null);
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: GroupName is Nothing
     *
     */
    @Test
    public  void addPermissionGroupNameIsNothing(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_4");
        requestJson.put("groupName", "");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: GroupName is Space
     *
     */
    @Test
    public  void addPermissionGroupNameIsSpace(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_4");
        requestJson.put("groupName", " ");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }


    /**
     *Add permission group: PermissionIds is Existed
     *
     */
    @Test
    public  void addPermissionPermissionIdsIsExisted(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_5");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }


    /**
     *Add permission group: PermissionIds is missed
     *
     */
    @Test
    public  void addPermissionPermissionIdsIsMissed(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_7");
        requestJson.put("groupName", "manager");
        //requestJson.put("permissionIds", "[31,2]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: PermissionIds is NULL
     *
     */
    @Test
    public  void addPermissionPermissionIdsIsNull(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_7");
        requestJson.put("groupName", null);
        requestJson.put("permissionIds", null);

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: PermissionIds is Nothing
     *
     */
    @Test
    public  void addPermissionPermissionIdsIsNothing(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_5");
        requestJson.put("groupName", "");
        requestJson.put("permissionIds", "");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: PermissionIds is Space
     *
     */
    @Test
    public  void addPermissionPermissionIdsIsSpace(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_4");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", " ");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(Exception e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }

    /**
     *Add permission group: PermissionIds is not Existed
     *
     */
    @Test
    public  void addPermissionPermissionIdsIsNotExisted(){
        String url="http://localhost:8080/permissionGroup";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id", "0");
        requestJson.put("groupCode", "permisson_group_code_5");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[31,32]");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.POST,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限不存在", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }

    }


    /**3
     *Update permission group : id is notexisted
     *
     */
    @Test
    public void editPermissionGroupIdIsNotExisted(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",123);
        requestJson.put("groupCode", "permisson_group_code_53");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限组不存在", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }

    /**
     *Update permission group : id is null
     *
     */
    @Test
    public void editPermissionGroupIdIsNull(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",null);
        requestJson.put("groupCode", "permisson_group_code_5");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }



    /**
     *Update permission group : id is Missed
     *
     */
    @Test
    public void editPermissionGroupIdIsMissed(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        //requestJson.put("id",null);
        requestJson.put("groupCode", "permisson_group_code_5");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }


    /**
     *Update permission group : groupCode is Missed
     *
     */
    @Test
    public void editPermissionGroupCodeIsMissed(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",22);
        //requestJson.put("groupCode", "permisson_group_code_5");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }

    /**
     *Update permission group : groupCode is existed
     *
     */
    @Test
    public void editPermissionGroupCodeIsExisted(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",22);
        requestJson.put("groupCode", "permisson_group_code_45");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限组已存在", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }

    /**
     *Update permission group : groupCode is null
     *
     */
    @Test
    public void editPermissionGroupCodeIsNull(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",22);
        requestJson.put("groupCode", null);
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }

    /**
     *Update permission group : groupName is null
     *
     */
    @Test
    public void editPermissionGroupNameIsNull(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",22);
        requestJson.put("groupCode", "permisson_group_code_88");
        requestJson.put("groupName", null);
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }

    /**
     *Update permission group : groupName is missed
     *
     */
    @Test
    public void editPermissionGroupNameIsMissed(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",22);
        requestJson.put("groupCode", "permisson_group_code_88");
        //requestJson.put("groupName", null);
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }

    /**
     *Update permission group : groupName is existed
     *
     */
    @Test
    public void editPermissionGroupNameIsExist(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",22);
        requestJson.put("groupCode", "permisson_group_code_88");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }


    /**
     *Update permission group : PermissionIds is null
     *
     */
    @Test
    public void editPermissionPermissionIdsIsNull(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",22);
        requestJson.put("groupCode", "permisson_group_code_188");
        requestJson.put("groupName", "hjelll");
        requestJson.put("permissionIds",null );
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }

    /**
     *Update permission group : groupName is missed
     *
     */
    @Test
    public void editPermissionPermissionIdsIsMissed(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",22);
        requestJson.put("groupCode", "permisson_group_code_188");
        requestJson.put("groupName", "shahng");
        //requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }

    /**
     *Update permission group : groupName is existed
     *
     */
    @Test
    public void editPermissionPermissionIdsIsExist(){
        String url="http://localhost:8080/permissionGroup";

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("id",22);
        requestJson.put("groupCode", "permisson_group_code_78");
        requestJson.put("groupName", "manager");
        requestJson.put("permissionIds", "[1,2]");
        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url,HttpMethod.PUT,headers,requestJson,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);

        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }



    }

    /**
     *Delete permission group : id is not existed
     *
     */
    @Test
    public void editPermissionIdsIsNotExist(){
        String url="http://localhost:8080/permissionGroup";
        int id=199;

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url+"/"+id,HttpMethod.DELETE,headers,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("权限组不存在", message);
        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     *Delete permission group : id is  existed
     *
     */
    @Test
    public void editPermissionIdsIsExist(){
        String url="http://localhost:8080/permissionGroup";
        int id=16;

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url+"/"+id,HttpMethod.DELETE,headers,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        }catch(RestClientException e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

    /**
     *Delete permission group : id is MISSED
     *
     */
    @Test
    public void editPermissionIdsIsMissed(){
        String url="http://localhost:8080/permissionGroup";
        int id=199;

        Map<String,String> headers= new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try{
            ResponseEntity <String > exchange=restTemplateUtils.exchange(url+"/",HttpMethod.DELETE,headers,String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        }catch(Exception e){
            Assert.assertTrue("Error!" + e, 1 == 2);
        }
    }

}
