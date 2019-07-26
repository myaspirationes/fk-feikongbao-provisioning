package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

import java.util.List;

/**
 * @Author houzhen
 * @Date 15:15 2019/7/3
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDto extends BaseDto {

    /**
     * 账号
     **/
    private String account;

    /**
     * 用户名
     **/
    private String name;

    /**
     * 功能集合
     **/
    private List<MenuDto> menuTree;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuDto> getMenuTree() {
        return menuTree;
    }

    public void setMenuTree(List<MenuDto> menuTree) {
        this.menuTree = menuTree;
    }
}
