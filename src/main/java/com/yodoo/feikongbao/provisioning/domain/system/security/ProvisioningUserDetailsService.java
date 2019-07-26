package com.yodoo.feikongbao.provisioning.domain.system.security;

import com.yodoo.feikongbao.provisioning.domain.system.entity.Permission;
import com.yodoo.feikongbao.provisioning.domain.system.entity.User;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.PermissionMapper;
import com.yodoo.feikongbao.provisioning.domain.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Date 2019/7/9 17:57
 * @Created by houzhen
 */
@Service
public class ProvisioningUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        User user = userService.getUserByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在，请重新登陆！");
        }
        // 返回用户信息
        ProvisioningUserDetails userInfo = new ProvisioningUserDetails();
        userInfo.setUsername(username);
        userInfo.setPassword(user.getPassword());

        // 查询用户权限
        Set<GrantedAuthority> authoritiesSet = new HashSet<>();

        List<Permission> permissionList = permissionMapper.getPermissionByUserId(user.getId());
        if (!CollectionUtils.isEmpty(permissionList)) {
            permissionList.forEach(permission -> {
                GrantedAuthority authority = new SimpleGrantedAuthority(permission.getPermissionCode());
                authoritiesSet.add(authority);
            });
        }
        userInfo.setAuthorities(authoritiesSet);
        return userInfo;

    }
}
