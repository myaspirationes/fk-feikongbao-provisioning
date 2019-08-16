package com.yodoo.feikongbao.provisioning.common.entity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

/**
 * @Date 2019/6/10 20:03
 * @Created by houzhen
 */
public class BaseEntity {
    /**
     * id 主键
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建人
     **/
    private Integer createdBy;

    /**
     * 创建日期
     **/
    private Instant createdTime;

    /**
     * 更新人
     **/
    private Integer lastModifiedBy;

    /**
     * 最后一次修改时间
     **/
    private Instant lastModifiedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Integer lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public void setCreatedBy() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.createdBy = (Integer) authentication.getPrincipal();
        }
    }

    public void setLastModifiedBy() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.lastModifiedBy = (Integer) authentication.getPrincipal();
            this.lastModifiedTime = Instant.now();
        }
    }

}
