package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbGroupDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.DbGroupService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：数据库组
 * @Author ：jinjun_luo
 * @Date ： 2019/8/28 0028
 */
@RestController
@RequestMapping(value = "/dbGroup")
public class DbGroupController {

    @Autowired
    private DbGroupService dbGroupService;

    /**
     * 条件分页查询
     *
     * @param dbGroupDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> queryDbGroupList(DbGroupDto dbGroupDto) {
        PageInfoDto<DbGroupDto> pageInfoDto = dbGroupService.queryDbGroupList(dbGroupDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), DbGroupController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, DbGroupController.class, Arrays.asList("db_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<DbGroupDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加
     * @param dbGroupDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> addDbGroup(@RequestBody DbGroupDto dbGroupDto){
        dbGroupService.addDbGroup(dbGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改
     * @param dbGroupDto
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> editDbGroup(@RequestBody DbGroupDto dbGroupDto){
        dbGroupService.editDbGroup(dbGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> deleteDbGroup(@PathVariable Integer id){
        dbGroupService.deleteDbGroup(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
