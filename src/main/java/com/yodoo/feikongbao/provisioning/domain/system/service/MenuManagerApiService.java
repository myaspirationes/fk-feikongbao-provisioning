package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.megalodon.permission.api.MenuManagerApi;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.dto.MenuDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：菜单
 * @Author ：jinjun_luo
 * @Date ： 2019/8/13 0013
 */
@Service
public class MenuManagerApiService {

    @Autowired
    private MenuManagerApi menuManagerApi;

    /**
     * 条件分页查询权限组列表
     * @param menuDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto> queryPermissionGroupList(MenuDto menuDto){
        com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto> pageInfoDto = new com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<>();
        PageInfoDto<MenuDto> menuDtoPageInfoDto = menuManagerApi.queryMenuList(menuDto);
        if (menuDtoPageInfoDto != null){
            BeanUtils.copyProperties(menuDtoPageInfoDto, pageInfoDto);
        }
        if (menuDtoPageInfoDto != null && !CollectionUtils.isEmpty(menuDtoPageInfoDto.getList())){
            List<com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto> collect = menuDtoPageInfoDto.getList().stream()
                    .filter(Objects::nonNull)
                    .map(menuDtoPermission -> {
                        com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto menuDtoResponse = new com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto();
                        BeanUtils.copyProperties(menuDtoPermission, menuDtoResponse);
                        menuDtoResponse.setTid(menuDtoPermission.getId());
                        return menuDtoResponse;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            pageInfoDto.setList(collect);
        }
        return pageInfoDto;
    }

    /**
     * 查询菜单树
     * @Author houzhen
     * @Date 14:46 2019/8/8
     **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<MenuDto> getAllMenuTree(){
        return menuManagerApi.getAllMenuTree();
    }

    /**
     * 新增菜单
     * @Author houzhen
     * @Date 14:47 2019/8/8
     **/
    public void addMenu(MenuDto menuDto) {
        menuManagerApi.addMenu(menuDto);
    }

    /**
     * 更新菜单
     * @Author houzhen
     * @Date 14:48 2019/8/8
     **/
    public void updateMenu(MenuDto menuDto) {
        menuManagerApi.updateMenu(menuDto);
    }

    /**
     * 删除菜单
     * @Author houzhen
     * @Date 14:49 2019/8/8
     **/
    public void deleteMenu(Integer id) {
        menuManagerApi.deleteMenu(id);
    }

    /**
     * 根据用户id查询菜单树
     * @param userId
     * @return
     */
    public List<MenuDto> getMenuTree(Integer userId){
        return menuManagerApi.getMenuTree(userId);
    }
}
