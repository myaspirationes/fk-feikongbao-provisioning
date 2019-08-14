package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.MenuManagerApiService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import com.yodoo.megalodon.permission.dto.MenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Description ：菜单
 * @Author ：jinjun_luo
 * @Date ： 2019/8/13 0013
 */
@RestController
@RequestMapping(value = "/menu")
public class MenuTreeController {

    @Autowired
    private MenuManagerApiService menuManagerApiService;


    /**
     * 条件分页查询
     *
     * @param menuDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> queryMenuList(MenuDto menuDto) {
        PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto> pageInfoDto = menuManagerApiService.queryPermissionGroupList(menuDto);

        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), MenuTreeController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, MenuTreeController.class, Arrays.asList("permission_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode(), "getMenuTree", "getAllMenuTree");
        return new ProvisioningDto<PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 查询菜单树
     * @Author houzhen
     * @Date 14:46 2019/8/8
     **/
    @RequestMapping(value = "/getAllMenuTree", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getAllMenuTree(){
        List<MenuDto> allMenuTree = menuManagerApiService.getAllMenuTree();
        return new ProvisioningDto<List<MenuDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, allMenuTree);
    }

    /**
     * 新增菜单
     * @Author houzhen
     * @Date 14:47 2019/8/8
     **/
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> addMenu(@RequestBody MenuDto menuDto) {
        menuManagerApiService.addMenu(menuDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新菜单
     * @Author houzhen
     * @Date 14:48 2019/8/8
     **/
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> updateMenu(@RequestBody MenuDto menuDto) {
        menuManagerApiService.updateMenu(menuDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除菜单
     * @Author houzhen
     * @Date 14:49 2019/8/8
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> deleteMenu(@PathVariable Integer id) {
        menuManagerApiService.deleteMenu(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 根据用户id查询菜单树
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getMenuTree/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getMenuTree(@PathVariable Integer userId){
        List<MenuDto> menuTree = menuManagerApiService.getMenuTree(userId);
        return new ProvisioningDto<List<MenuDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, menuTree);
    }
}
