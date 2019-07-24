package com.yodoo.feikongbao.provisioning.aspect;

import com.yodoo.feikongbao.provisioning.common.dto.response.ProvisioningResponse;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
    public ProvisioningResponse handleException(ProceedingJoinPoint joinPoint) {
        ProvisioningResponse response = new ProvisioningResponse();
        // 方法名称
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        try {
            Object proceedObj = joinPoint.proceed(joinPoint.getArgs());

            if (proceedObj instanceof ProvisioningResponse) {
                response = (ProvisioningResponse) proceedObj;
            }
        } catch (ProvisioningException e) {
            e.printStackTrace();
            logger.error("service aop,exit PaasExceptionAspect#handleException,method:{},ProvisioningException:{}", methodName, e);
            response = new ProvisioningResponse(SystemStatus.FAIL.getStatus(), e.getMessageBundleKey(), e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("service aop,exit PaasExceptionAspect#handleException,method:{},exception:{}", methodName, e);
            response = new ProvisioningResponse(SystemStatus.FAIL.getStatus(), BundleKey.UNDEFINED, BundleKey.UNDEFINED_MSG);
        } finally {

        }
        return response;
    }
}
