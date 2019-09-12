package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description ：功能模块
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
public class FunctionModuleDto extends BaseDto {

    /**
     * 父类id
     **/
    @ApiModelProperty(value = "父类id", required = false, example = "1", position = 1)
    private Integer parentId;

    /**
     * 功能模块名称
     **/
    @ApiModelProperty(value = "功能模块名称", required = true, example = "test_name", position = 2)
    private String name;

    /**
     * 描述
     **/
    @ApiModelProperty(value = "描述", required = true, example = "test_description", position = 3)
    private String description;

    /**
     * 序号
     **/
    @ApiModelProperty(value = "序号", required = true, example = "test_order_no", position = 4)
    private String orderNo;

    public Integer getParentId() {
        return parentId;
    }

    public FunctionModuleDto setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getName() {
        return name;
    }

    public FunctionModuleDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FunctionModuleDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public FunctionModuleDto setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }
}
