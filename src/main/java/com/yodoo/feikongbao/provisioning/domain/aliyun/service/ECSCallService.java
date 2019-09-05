package com.yodoo.feikongbao.provisioning.domain.aliyun.service;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.JsonUtils;
import com.yodoo.megalodon.datasource.config.AliYunConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Date 2019/6/13 10:40
 * @Created by houzhen
 */
@Service
public class ECSCallService {

    private static Logger logger = LoggerFactory.getLogger(ECSCallService.class);

    @Autowired
    private AliYunConfig aliYunConfig;

    /**
     * 调用OpenAPI的方法，这里进行了错误处理
     * @param request AcsRequest, Open API的请求
     * @param <T> AcsResponse 请求所对应返回值
     * @return 返回OpenAPI的调用结果. 如果调用失败，则会返回null
     */
    public <T extends AcsResponse> T callOpenApi(AcsRequest<T> request) {
        IAcsClient client = initClient();
        try {
            T response = client.getAcsResponse(request, false, 0);
            logger.info(String.format("Success. OpenAPI Action: %s call successfully.", request.getSysActionName()));
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            logger.error("Fail. Something with your connection with Aliyun go incorrect:{}", JsonUtils.obj2json(e));
            throw new ProvisioningException(BundleKey.ALI_CALL_ERROR, BundleKey.ALI_CALL_ERROR_MSG);
        } catch (ClientException e) {
            e.printStackTrace();
            logger.error("Fail. Business error:{}", JsonUtils.obj2json(e));
            throw new ProvisioningException(BundleKey.ALI_CALL_ERROR, BundleKey.ALI_CALL_ERROR_MSG);
        }
    }

    /**
     * 需要填充账号的AccessKey ID，以及账号的Access Key Secret
     */
    private IAcsClient initClient() {
        IClientProfile profile = DefaultProfile.getProfile(aliYunConfig.regionId, aliYunConfig.accessKeyId, aliYunConfig.accessSecret);
        return new DefaultAcsClient(profile);
    }
}
