package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DbGroupDto extends BaseDto {

    /**
     * 组code
     **/
    @ApiModelProperty(value = "数据库组代码", required = true, example = "dev_test_group", position = 1)
    private String groupCode;

    /**
     * 组名称
     **/
    @ApiModelProperty(value = "数据库组名称", required = true, example = "开发测试组", position = 2)
    private String groupName;

    /**
     * DB 数据库表
     **/
    @ApiModelProperty(hidden = true)
    private List<DbSchemaDto> dbSchemaDtoList;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public List<DbSchemaDto> getDbSchemaDtoList() {
        return dbSchemaDtoList;
    }

    public DbGroupDto setDbSchemaDtoList(List<DbSchemaDto> dbSchemaDtoList) {
        this.dbSchemaDtoList = dbSchemaDtoList;
        return this;
    }
}
