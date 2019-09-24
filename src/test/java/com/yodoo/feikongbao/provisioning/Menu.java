package com.yodoo.feikongbao.provisioning;

import com.feikongbao.storageclient.util.RestTemplateUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.config.ProvisioningSecurityConf;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProvisioningConfig.class, ProvisioningSecurityConf.class})
@WebAppConfiguration
public class Menu {

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    /**
     * 登录一个用户
     */
    @Test
    public void userLogin(){
        String uri="http://localhost:8080/login";
        try {
            URL url = new URL(uri );
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

            Assert.assertEquals("客户管理",menuName);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Erorr! 查询菜单列表请求失败",1==2);
        }
    }

    /**
     * 查询菜单列表第2页,一页20个，共14条记录。list 应该为空。
     */
    @Test
    public void  queryMenuListSecondPage(){
        String url="http://localhost:8080/menu?pageNum=2&pageSize=20";
        Map<String,String> header=new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        ResponseEntity<String> exchange=restTemplateUtils.exchange(url,HttpMethod.GET,header,String.class);
        if (exchange.getStatusCodeValue()==200){
            String body=exchange.getBody();
            System.out.println(body);

            //这是以一个有嵌套的json，需要判断第二层的list是否为空。
            JSONObject object=JSONObject.fromObject(body);
            JSONArray list=(JSONArray) object.get("list");

            Assert.assertTrue("Error!第2页应该没有内容！",list.size()==0);
        }else{
            Assert.assertTrue("Error!响应错误！",1==2);
        }
    }

    /**
     * 查询菜单列表,不传递PageNum，搜索所有菜单项。
     */
    @Test
    public void queryMenuListPageNumIsNull() {
        String url = "http://localhost:8080/menu?&pageSize=2";

        Map<String,String> header=new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try{
            ResponseEntity<String> exchange=restTemplateUtils.exchange(url,HttpMethod.GET,header,String.class);
            //System.out.println(exchange.getStatusCodeValue());
            Assert.assertTrue("Bad Request",exchange.getStatusCodeValue()==200);
        }catch(RestClientException e){
                System.out.println(e.toString());
            Assert.assertTrue("Erorr! 查询菜单列表请求失败",1==2);
            }
    }

    /**
     *查询菜单树
     */
    @Test
    public void getAllMenuTree(){
        String url="http://localhost:8080/menu/getAllMenuTree";
        Map<String,String> header=new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        ResponseEntity<String> exchange=restTemplateUtils.exchange(url,HttpMethod.GET,header,String.class);
        if (exchange.getStatusCodeValue()==200){
            String body=exchange.getBody();
            System.out.println(body);

            //这是以一个有嵌套的json。
            JSONObject object=JSONObject.fromObject(body);

            JSONArray list=(JSONArray) object.get("data");
            JSONObject list1=(JSONObject)list.get(0);
            String menuName= list1.getString("menuName");

            Assert.assertEquals ("客户管理",menuName);
        }else{
            Assert.assertTrue("Error!请求或响应错误！",1==2);
        }
    }

    /**
     *添加菜单 : 1. menuCode 不重复
     */
    @Test
    public void addMenu(){
        String url="http://localhost:8080/menu";

        Map<String,String> header= new HashMap<>();
        header.put("Authorization","Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type","application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId","2");
        requestJson.put("menuCode","customer2");
        requestJson.put("menuName","产品质量管理");
        requestJson.put("menuTarget","/menu");
        requestJson.put("orderNo","100.03");
        requestJson.put("permissionIdList","[1,2]");

        try{
            ResponseEntity<String> exchange=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchange.getBody();
            JSONObject response=JSONObject.fromObject(body);

            String message= response .getString("message");
            Assert.assertEquals("请求成功",response);

            System.out.println(message);
        }catch (RestClientException e){
            System.out.println(e);
        }

    }

    /**
     * 添加菜单 : 2. menuCode 重复
     */
    @Test
    public void addMenuMenuCodeExist() {
        String url = "http://localhost:8080/menu";

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode", "customer1");
        requestJson.put("menuName", "产品质量管理");
        requestJson.put("menuTarget", "/menu3");
        requestJson.put("orderNo", "100.04");
        requestJson.put("permissionIdList", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);

            String message = response.getString("message");
            Assert.assertEquals("菜单已存在", message);
        } catch (RestClientException e) {
            //System.out.println("Error!"+e);
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 3.menuCode 不传递。
     */
     @Test
     public void addMendCodeIsMissed(){
         String url = "http://localhost:8080/menu";

         Map<String, String> header = new HashMap<>();
         header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
         header.put("Content-Type", "application/json");

         JSONObject requestJson = new JSONObject();
         requestJson.put("parentId", "2");
         //requestJson.put("menuCode", "customer1");
         requestJson.put("menuName", "产品质量管理");
         requestJson.put("menuTarget", "/menu3");
         requestJson.put("orderNo", "100.04");
         requestJson.put("permissionIdList", "[1,2]");

         try {
             ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
             String body = exchange.getBody();
             JSONObject response = JSONObject.fromObject(body);
             System.out.println(response);

             String message = response.getString("message");
             Assert.assertEquals("参数异常", message);
         } catch (RestClientException e) {
             //System.out.println("Error!"+e);
             Assert.assertTrue("Error!"+e,1==2);
         }
    }

    /**
     *添加菜单 : 4.menuCode is null。
     */
    @Test
    public void addMendCodeIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode",null);
        requestJson.put("menuName", "产品质量管理");
        requestJson.put("menuTarget", "/menu4");
        requestJson.put("orderNo", "100.04");
        requestJson.put("permissionIdList", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);

            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            //System.out.println("Error!"+e);
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 5. menuCode is minus。
     */
    @Test
    public void addMendCodeIsMinus(){
        String url = "http://localhost:8080/menu";

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode",-1);
        requestJson.put("menuName", "产品质量管理");
        requestJson.put("menuTarget", "/menu4");
        requestJson.put("orderNo", "100.04");
        requestJson.put("permissionIdList", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);

            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            //System.out.println("Error!"+e);
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 6. menuCode is ""。
     */
    @Test
    public void addMendCodeValueIsNothing(){
        String url = "http://localhost:8080/menu";

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","");
        requestJson.put("menuName", "产品质量管理");
        requestJson.put("menuTarget", "/menu4");
        requestJson.put("orderNo", "100.04");
        requestJson.put("permissionIdList", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);

            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            //System.out.println("Error!"+e);
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 :1. menuName is existed。
     * 表：menu
     */
    @Test
    public void addMenuNameIsExisted(){
        String url = "http://localhost:8080/menu";

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_2");
        requestJson.put("menuName", "产品质量管理组1");
        requestJson.put("menuTarget", "/menu5");
        requestJson.put("orderNo", "112");
        requestJson.put("permissionIdList", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);

            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            //System.out.println("Error!"+e);
            Assert.assertTrue("Error!"+e,1==2);
        }

    }

    /**
     *添加菜单 : 2.menuName is missed。
     * 表：menu
     */
    @Test
    public void addMenuNameIsMissed(){
        String url = "http://localhost:8080/menu";

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_3");
        //requestJson.put("menuName", "产品质量管理组1");
        requestJson.put("menuTarget", "/menu5");
        requestJson.put("orderNo", "113");
        requestJson.put("permissionIdList", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);

            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            //System.out.println("Error!"+e);
            Assert.assertTrue("Error!"+e,1==2);
        }

    }

    /**
     *添加菜单 :3. menuName is null。
     * 表：menu
     */
    @Test
    public void addMenuNameIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_3");
        requestJson.put("menuName", null);
        requestJson.put("menuTarget", "/menu5");
        requestJson.put("orderNo", "113");
        requestJson.put("permissionIdList", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);

            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            //System.out.println("Error!"+e);
            Assert.assertTrue("Error!"+e,1==2);
        }

    }

    /**
     *添加菜单 : 4. menuName is ""。
     * null是没有地址
     * ""是有地址但是里面的内容是空的
     * 表：menu
     */
    @Test
    public void addMenuNameIsNothing(){
        String url = "http://localhost:8080/menu";

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_3");
        requestJson.put("menuName", "");
        requestJson.put("menuTarget", "/menu5");
        requestJson.put("orderNo", "113");
        requestJson.put("permissionIdList", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);

            String message = response.getString("message");
            Assert.assertEquals("参数异常", message);
        } catch (RestClientException e) {
            //System.out.println("Error!"+e);
            Assert.assertTrue("Error!"+e,1==2);
        }

    }

    /**
     *添加菜单 :5. menuName is new。
     * 表：menu
     */
    @Test
    public void addMenuNameIsNew(){
        String url = "http://localhost:8080/menu";

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_3");
        requestJson.put("menuName", "QA_质量管理");
        requestJson.put("menuTarget", "/menu5");
        requestJson.put("orderNo", "113");
        requestJson.put("permissionIdList", "[1,2]");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, header, requestJson, String.class);
            String body = exchange.getBody();
            JSONObject response = JSONObject.fromObject(body);
            System.out.println(response);
            String message = response.getString("message");
            Assert.assertEquals("请求成功", message);
        } catch (RestClientException e) {
            //System.out.println("Error!"+e);
            Assert.assertTrue("Error!"+e,1==2);
        }

    }

    /**
     *添加菜单 :1. menuTarget is missed。
     * 表：menu
     */
    @Test
    public void addMenuMenuTargetIsMissed(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_4");
        requestJson.put("menuName", "QA_质量管理4");
        //requestJson.put("menuTarget", "menu5");
        requestJson.put("orderNo", "114");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }

    }

    /**
     *添加菜单 :2. menuTarget is null。
     * 表：menu
     */
    @Test
    public void addMenuMenuTargetIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_4");
        requestJson.put("menuName", "QA_质量管理4");
        requestJson.put("menuTarget",null);
        requestJson.put("orderNo", "114");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 :3. menuTarget is ""。
     * 表：menu
     */
    @Test
    public void addMenuMenuTargetIsNothin(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_4");
        requestJson.put("menuName", "QA_质量管理4");
        requestJson.put("menuTarget","");
        requestJson.put("orderNo", "114");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 4. menuTarget is existed。
     * 表：menu
     */
    @Test
    public void addMenuMenuTargetIsExist(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_5");
        requestJson.put("menuName", "QA_质量管理4");
        requestJson.put("menuTarget","/menu5");
        requestJson.put("orderNo", "115");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 5. menuTarget is new。
     * 表：menu
     */
    @Test
    public void addMenuMenuTargetIsNew(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_6");
        requestJson.put("menuName", "QA_质量管理4");
        requestJson.put("menuTarget","/menu6");
        requestJson.put("orderNo", "116");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }


    /**
     *添加菜单 : 1. OrderNum Is Missed。
     * 表：menu
     */
    @Test
    public void addMenuOrderNumIsMiss(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_6");
        requestJson.put("menuName", "QA_质量管理4");
        requestJson.put("menuTarget","/menu6");
        //requestJson.put("orderNo", "116");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 2. OrderNum Is null。
     * 表：menu
     */
    @Test
    public void addMenuOrderNumIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_6");
        requestJson.put("menuName", "QA_质量管理4");
        requestJson.put("menuTarget","/menu6");
        requestJson.put("orderNo", null);
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 3. OrderNum Is ""。
     * 表：menu
     */
    @Test
    public void addMenuOrderNumIsNothing(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_6");
        requestJson.put("menuName", "QA_质量管理4");
        requestJson.put("menuTarget","/menu6");
        requestJson.put("orderNo", "");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 4. OrderNum Is sapce。
     * 表：menu
     */
    @Test
    public void addMenuOrderNumIsSpace(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_8");
        requestJson.put("menuName", "QA_质量管理4");
        requestJson.put("menuTarget","/menu6");
        requestJson.put("orderNo", "117");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 5. OrderNum Is existed。
     * 表：menu
     */
    @Test
    public void addMenuOrderNumIsExisted(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_10");
        requestJson.put("menuName", "QA_质量管理7");
        requestJson.put("menuTarget","/menu8");
        requestJson.put("orderNo", "116");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 6. OrderNum Is new。
     * 表：menu
     */
    @Test
    public void addMenuOrderNumIsNew(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_9");
        requestJson.put("menuName", "QA_质量管理6");
        requestJson.put("menuTarget","/menu7");
        requestJson.put("orderNo", "216");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 1. permissionIdList Is null。不添加权限也可以？？？？
     * 表：menu
     */
    @Test
    public void addMenuPermissionIdListIsNULL(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_15");
        requestJson.put("menuName", "QA_质量管理6");
        requestJson.put("menuTarget","/menu17");
        requestJson.put("orderNo", "216");
        requestJson.put("permissionIdList", null);

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 2. permissionIdList Is missed。
     * 表：menu
     */
    @Test
    public void addMenuPermissionIdListIsMissed(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_12");
        requestJson.put("menuName", "QA_质量管理12");
        requestJson.put("menuTarget","/menu12");
        requestJson.put("orderNo", "216");
        //requestJson.put("permissionIdList", null);

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!请求错误："+e,1==2);
        }
    }

    /**
     *添加菜单 : 3. permissionIdList Is NOT EXIST。
     * 表：menu
     */
    @Test
    public void addMenuPermissionIdListIsNotExist(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_20");
        requestJson.put("menuName", "QA_质量管理12");
        requestJson.put("menuTarget","/menu20");
        requestJson.put("orderNo", "118");
        requestJson.put("permissionIdList","[100,123]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("权限不存在", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     *添加菜单 : 4. permissionIdList Is  EXIST。
     * 表：menu
     */
    @Test
    public void addMenuPermissionIdListIsExist(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_15");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.POST,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }


    /**
     * 更新菜单: 1 . id is missed
     * */
    @Test
    public void updateMenuIdIsMissed(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        //requestJson.put("id", "2");
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_15");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 2 . id is not exist
     * */
    @Test
    public void updateMenuIdIsNotExist(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "2000");
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_15");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("菜单不存在", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 3 . id is null
     * */
    @Test
    public void updateMenuIdIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", null);
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_15");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 4 . id is ""
     * */
    @Test
    public void updateMenuIdIsNothing(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "");
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_15");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 5 . id is space
     * */
    @Test
    public void updateMenuIdIsSpace(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", " ");
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_15");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 6 . menuCode is null
     * */
    @Test
    public void updateMenuMenuCodeIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "2");
        requestJson.put("menuCode",null);
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 7 . menuCode is Existed
     * */
    @Test
    public void updateMenuMenuCodeIsExisted(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","test_wang_1");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("菜单已存在", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }


    /**
     * 更新菜单: 8 . menuCode is ""
     * */
    @Test
    public void updateMenuMenuCodeIsNothing(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "2");
        requestJson.put("menuCode","");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 9 . menuCode is " "
     * */
    @Test
    public void updateMenuMenuCodeIsSpace(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "2");
        requestJson.put("menuCode"," ");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }


    /**
     * 更新菜单: 10 .update  parentId from 2 to 0
     * 不能越级修改？？？？？？？？？？？？？？？
     * */
    @Test
    public void updateMenuParentId2to0 (){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "0");
        requestJson.put("menuCode","test_wang_16");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }


    /**
     * 更新菜单: 11 .update  parentId is null
     *
     * */
    @Test
    public void updateMenuParentIdIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId",null);
        requestJson.put("menuCode","test_wang_19");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu116");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 12 .update  parentId is not exist
     *
     * */
    @Test
    public void updateMenuParentIdIsNotExist(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "13");
        requestJson.put("menuCode","test_wang_19");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }
    /**
     * 更新菜单: 13.update  menuName is null
     *
     * */
    @Test
    public void updateMenuMenuNameIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "0");
        requestJson.put("menuCode","test_wang_16");
        requestJson.put("menuName", null);
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }
    /**
     * 更新菜单: 14 .update  menuTarget is null
     *
     * */
    @Test
    public void updateMenuMenuTargetIsNULL(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "1");
        requestJson.put("menuCode","test_wang_18");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget",null);
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 15 .update  orderNo is NULL
     *
     * */
    @Test
    public void updateMenuOrderNoIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "1");
        requestJson.put("menuCode","test_wang_19");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu15");
        requestJson.put("orderNo", null);
        requestJson.put("permissionIdList", "[1,2]");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("参数异常", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 更新菜单: 16 .update  permissionIdList is null
     *
     * */
    @Test
    public void updateMenupermissionIdListIsNull(){
        String url = "http://localhost:8080/menu";

        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        header.put("Content-Type", "application/json");

        JSONObject requestJson= new JSONObject();
        requestJson.put("id", "39");
        requestJson.put("parentId", "0");
        requestJson.put("menuCode","test_wang_26");
        requestJson.put("menuName", "QA_质量管理15");
        requestJson.put("menuTarget","/menu16");
        requestJson.put("orderNo", "119");
        requestJson.put("permissionIdList", null);

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.PUT,header,requestJson,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 删除菜单: 1 .id   is NULL
     *
     * */
    @Test
    public void deleteMenuIdIsNULL(){
        String uri = "http://localhost:8080/menu";
        String id=null;
        String url = uri+"/"+id;
        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.DELETE,header,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("status");

            System.out.println(response);
            Assert.assertEquals("400", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 删除菜单: 2 .id   NotExist
     *
     * */
    @Test
    public void deleteMenuIdIsNotExist(){
        String uri = "http://localhost:8080/menu";
        String id="1000";
        String url = uri+"/"+id;
        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.DELETE,header,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("菜单不存在", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 删除菜单: 3 .id   is ""
     *
     * */
    @Test
    public void deleteMenuIdIsNothing(){
        String uri = "http://localhost:8080/menu";
        String id="";
        String url = uri+"/"+id;
        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.DELETE,header,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("status");

            System.out.println(response);
            Assert.assertEquals("405", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 删除菜单: 4 .id   父菜单，在使用中
     *
     * */
    @Test
    public void deleteMenuIdIsInUse(){
        String uri = "http://localhost:8080/menu";
        String id="２";
        String url = uri+"/"+id;
        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.DELETE,header,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("菜单还在使用存在", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 删除菜单: ５ .id   末尾子菜单
     *
     * */
    @Test
    public void deleteMenuIsIsLasOne(){
        String uri = "http://localhost:8080/menu";
        String id="２";
        String url = uri+"/"+id;
        Map<String,String> header= new HashMap();
        header.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        try{
            ResponseEntity<String> exchage=restTemplateUtils.exchange(url,HttpMethod.DELETE,header,String.class);
            String body=exchage.getBody();
            JSONObject response= JSONObject.fromObject(body);
            String message =response.getString("message");

            System.out.println(response);
            Assert.assertEquals("请求成功", message);
        }catch (RestClientException e){
            Assert.assertTrue("Error!"+e,1==2);
        }
    }

    /**
     * 删除菜单：6
     *
     */

}
