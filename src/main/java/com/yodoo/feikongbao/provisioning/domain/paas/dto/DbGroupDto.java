package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

import java.util.List;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
public class DbGroupDto extends BaseDto {

    /** 组code **/
    private String groupCode;

    /** 组名称 **/
    private String groupName;

    /** DB 数据库表 **/
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
