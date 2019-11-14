package com.yodoo.feikongbao.provisioning;

import com.feikongbao.storageclient.util.RestTemplateUtils;
import com.google.gson.JsonObject;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProvisioningConfig.class, ProvisioningSecurityConf.class})
@WebAppConfiguration
public class CompanyManageTest {
    @Autowired
    private RestTemplateUtils restTemplateUtils;


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
    * Add Company Step 1 : company name is  existed
    * */
    @Test
    public  void AddCompanyStep1CompanyNameIsExisted(){
        String url = "http://localhost:8080/company/addCompany/";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("tid", "0");
        requestJson.put("groupId", "0");
        requestJson.put("companyName", "test_company_name1");
        requestJson.put("companyCode", "test_company_code1");
        requestJson.put("updateCycle", "1");
        requestJson.put("nextUpdateDate", "2019-10-08");
        requestJson.put("expireDate", "2019-11-08");

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
     * Add Company Step 2 :
     * */
    @Test
    public  void AddCompanyStep2useDbSchema(){
        String url = "http://localhost:8080/company/useDbSchema";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        String companyId="19";
        String dbGroupId="3";
        String targetVersion="R1908";

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"?companyId="+companyId+"&dbGroupId="+dbGroupId +"&targetVersion="+targetVersion, HttpMethod.GET, headers, String.class);
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
     * Add Company Step 3 :
     * */
    @Test
    public  void AddCompanyStep3useRedisInstance(){
        String url = "http://localhost:8080/company/useRedisInstance";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        String companyId="19";
        String redisGroupId ="1";


        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"?companyId="+companyId+"&redisGroupId ="+redisGroupId, HttpMethod.GET, headers, String.class);
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
     * Add Company Step 4 :
     * */
    @Test
    public  void AddCompanyStep4useSwiftProject(){
        String url = "http://localhost:8080/company/useRedisInstance";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("tid", "0");
        requestJson.put("projectName", "TIANWAIFEIXIAN");
        requestJson.put("maxSize", "1024");
        requestJson.put("userSize", "1");
        requestJson.put("ip", "dev.feikongbao.cn");
        requestJson.put("port", "5000");
        requestJson.put("companyId", "19");



        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers,requestJson, String.class);
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
     * Add Company Step 5 :
     * */

    @Test
    public  void AddCompanyStep5useMqVhost(){
        String url = "http://localhost:8080/company/useMqVhost";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        String companyId="19";


        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"?companyId="+companyId, HttpMethod.GET, headers, String.class);
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
     * Add Company Step 6 :
     * */
    @Test
    public  void AddCompanyStep6useNeo4jInstance(){
        String url = "http://localhost:8080/company/useNeo4jInstance";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        String companyId="19";
       String neo4jInstanceId="2";


        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"?companyId="+companyId+"&neo4jInstanceId="+neo4jInstanceId , HttpMethod.GET, headers, String.class);
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
     * Add Company Step 7 :
     * */
    @Test
    public  void AddCompanyStep7createPublishProjects(){
        String url = "http://localhost:8080/company/useRedisInstance";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject listJson = new JSONObject();
        listJson.put("vmInstanceId","2");
        listJson.put("projectName","test");

        JSONObject requestJson = new JSONObject();
        requestJson.put("tid", "0");
        requestJson.put("companyId", "19");
        //requestJson.put("publishProjectDetailsList", new String[]{listJson});



        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers,requestJson, String.class);
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
     * Add Company Step 8 :
     * */
    @Test
    public  void AddCompanyStep8createSuperUser(){
        String url = "http://localhost:8080/company/createSuperUser";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        String companyId="19";
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/"+companyId, HttpMethod.POST, headers, String.class);
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
     * Add Company Step 9 :
     * */
    @Test
   public  void AddCompanyStep9publishProjects(){
        String url = "http://localhost:8080/company/publishProjects";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");
        String companyId="19";
        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url+"/"+companyId, HttpMethod.POST, headers, String.class);
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
     * 启用和禁用功能模块, 0 禁用， 1 启用
     *
     */
    @Test
    public void companyFunctionModuleOpen(){
        String url="http://localhost:8080/companyFunctionModule";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("tid","");
        requestJson.put("functionModuleId","1");
        requestJson.put("companyId","18");
        requestJson.put("status","0");

        try {
            ResponseEntity<String> exchange = restTemplateUtils.exchange(url, HttpMethod.POST, headers,requestJson, String.class);
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
     * 添加功能模块
     */
    @Test
    public void functionModuleAdd() {
        String url = "http://localhost:8080/functionModule";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("tid", "");
        requestJson.put("parentId", "1");
        requestJson.put("name", "test_name");
        requestJson.put("description", "test_description");
        requestJson.put("orderNo", "test_order_no");

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
     * 添加数据库组
     */
    @Test
    public void dbGroupAdd(){
        String url = "http://localhost:8080/dbGroup";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("tid", "");
        requestJson.put("groupCode", "Test_Group_1");
        requestJson.put("groupName", "Test_Name_1");

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
     * 添加数据库
     */
    @Test
    public void dbSchemaAdd(){
        String url = "http://localhost:8080/dbSchema";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("tid", "");
        requestJson.put("dbInstanceId", "1");
        requestJson.put("dbGroupId", "9");
        requestJson.put("schemaName", "Test_Name_1");
        requestJson.put("username", "Test_Name_1");
        requestJson.put("password", "Test_Name_1");
        requestJson.put("status", "0");
        requestJson.put("type", "0");
        requestJson.put("companyId", "18");
        requestJson.put("companyCode", "test_company_20190927");
        requestJson.put("targetVersion", "Test_Name_1");

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
     * 添加db实例
     */
    @Test
    public void dbInstanceAdd(){
        String url = "http://localhost:8080/dbInstance";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("tid", "");
        requestJson.put("ip", "127.0.0.1");
        requestJson.put("port", "8989");

        requestJson.put("username", "Test_Name_1");
        requestJson.put("password", "Test_Name_1");
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
     * 添加字典表
     * Dictionary Controller
     */
    @Test
    public void dictionaryAdd(){
        String url = "http://localhost:8080/dictionary";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic bGlsZWk6eW9kb28xMjM=");

        JSONObject requestJson = new JSONObject();
        requestJson.put("tid", "");
        requestJson.put("type", "test");
        requestJson.put("code", "test_001");
        requestJson.put("remark", "Test_remark_1");
        requestJson.put("name", "Test_Name_1");
        requestJson.put("value", "Test_value_1");
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



}
