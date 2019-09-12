package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

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
    @ApiModelProperty(value = "类型", required = true, example = "test", position = 1)
    private String type;

    /**
     * 编码
     **/
    @ApiModelProperty(value = "编码", required = true, example = "test_code", position = 2)
    private String code;

    /**
     * 名称
     **/
    @ApiModelProperty(value = "名称", required = true, example = "test_name", position = 3)
    private String name;

    /**
     * 值
     **/
    @ApiModelProperty(value = "值", required = true, example = "test_value", position = 4)
    private String value;

    /**
     * 备注
     **/
    @ApiModelProperty(value = "备注", required = true, example = "test_XX", position = 5)
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
