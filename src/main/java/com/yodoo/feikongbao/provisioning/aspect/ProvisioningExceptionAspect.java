package com.yodoo.feikongbao.provisioning.aspect;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.megalodon.permission.exception.PermissionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Author houzhen
 * @Date 12:14 2019/5/15
 **/
@Aspect
@Component
@Order(1)
public class ProvisioningExceptionAspect {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Around("execution(* com.yodoo.feikongbao.provisioning..web..*.*(..))")
    public ProvisioningDto handleException(ProceedingJoinPoint joinPoint) {
        ProvisioningDto response = new ProvisioningDto();
        // 方法名称
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        try {
            Object proceedObj = joinPoint.proceed(joinPoint.getArgs());

            if (proceedObj instanceof ProvisioningDto) {
                response = (ProvisioningDto) proceedObj;
            }
        } catch (ProvisioningException e) {
            e.printStackTrace();
            logger.error("service aop,exit PaasExceptionAspect#handleException,method:{},ProvisioningException:{}", methodName, e);
            String message = getExceptionParameter(e.getMessageParams());
            response = new ProvisioningDto(SystemStatus.FAIL.getStatus(), e.getMessageBundleKey(), message);
        } catch (PermissionException e){
            logger.error("service aop,exit PaasExceptionAspect#handleException,method:{},PermissionException:{}", methodName, e);
            String message = getExceptionParameter(e.getPermissionMessageParams());
            response = new ProvisioningDto(SystemStatus.FAIL.getStatus(), e.getPermissionMessageBundleKey(), message);
        }catch (Throwable e) {
            e.printStackTrace();
            logger.error("service aop,exit PaasExceptionAspect#handleException,method:{},exception:{}", methodName, e);
            response = new ProvisioningDto(SystemStatus.FAIL.getStatus(), BundleKey.UNDEFINED, BundleKey.UNDEFINED_MSG);
        } finally {

        }
        return response;
    }

    private String getExceptionParameter(String[] str){
        StringBuilder sb = new StringBuilder();
        String message = "";
        if (str != null && str.length > 0) {
            for (int i = 0; i < str.length; i++) {
                if (i < str.length - 1) {
                    sb.append(str[i] + ",");
                } else {
                    sb.append(str[i]);
                }
            }
            message = sb.toString();
        }
        return message;
    }
}
