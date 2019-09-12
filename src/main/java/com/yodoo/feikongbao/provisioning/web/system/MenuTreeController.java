package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.MenuManagerApiService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import com.yodoo.megalodon.permission.dto.MenuDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
@Api(tags = "MenuTreeController | 菜单")
public class MenuTreeController {

    @Autowired
    private MenuManagerApiService menuManagerApiService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param menuCode
     * @param menuName
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "menuCode", value = "菜单代码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "menuName", value = "菜单名称", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> queryMenuList(int pageNum, int pageSize, String menuCode, String menuName) {
        MenuDto menuDto = new MenuDto();
        menuDto.setPageNum(pageNum);
        menuDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(menuCode)){
            menuDto.setMenuCode(menuCode);
        }
        if (StringUtils.isNotBlank(menuName)){
            menuDto.setMenuName(menuName);
        }
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
    @ApiOperation(value = "查询菜单树", httpMethod = "GET")
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
    @ApiOperation(value = "新增菜单", httpMethod = "POST")
    @ApiImplicitParam(name = "menuDto", value = "菜单 menuDto", required = true, dataType = "MenuDto")
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
    @ApiOperation(value = "更新菜单", httpMethod = "PUT")
    @ApiImplicitParam(name = "menuDto", value = "菜单 menuDto", required = true, dataType = "MenuDto")
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
    @ApiOperation(value = "删除菜单", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "菜单数据库表自增 id", required = true, dataType = "Integer", example = "0")
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
    @ApiOperation(value = "根据用户id查询菜单树", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "菜单数据库表自增 id", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "/getMenuTree/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getMenuTree(@PathVariable Integer userId){
        List<MenuDto> menuTree = menuManagerApiService.getMenuTree(userId);
        return new ProvisioningDto<List<MenuDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, menuTree);
    }
}
