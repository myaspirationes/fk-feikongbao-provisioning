package com.yodoo.feikongbao.provisioning.util;


import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;

/**
 * @Date 2019/7/9 17:28
 * @Author by houzhen
 */
public class RequestPrecondition {

    public static void checkArgumentsNotEmpty(String... args) {
        checkArguments(!StringUtils.isContainEmpty(args));
    }

    public static void checkModel(boolean valid) {
        checkArguments(valid);
    }

    public static void checkArguments(boolean expression) {
        if (!expression) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
    }
}
