package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/26 0026
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DictionaryDto extends BaseDto {

    /**
     * 类型
     **/
    private String type;

    /**
     * 编码
     **/
    private String code;

    /**
     * 名称
     **/
    private String name;

    /**
     * 值
     **/
    private String value;

    /**
     * 备注
     **/
    private String remark;

    public String getType() {
        return type;
    }

    public DictionaryDto setType(String type) {
        this.type = type;
        return this;
    }

    public String getCode() {
        return code;
    }

    public DictionaryDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public DictionaryDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public DictionaryDto setValue(String value) {
        this.value = value;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public DictionaryDto setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
