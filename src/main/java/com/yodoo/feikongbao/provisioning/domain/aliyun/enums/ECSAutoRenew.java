package com.yodoo.feikongbao.provisioning.domain.aliyun.enums;

/**
 * @Author houzhen
 * @Date 10:36 2019/5/15
**/
public enum ECSAutoRenew {

    /** 自动续费 **/
    YES(1),
    /** 不自动续费 **/
    NO(0);

    private Integer code;

    public Integer getCode() {
        return code;
    }

    private ECSAutoRenew(Integer code){
        this.code = code;
    }
}
