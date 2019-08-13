package com.yodoo.feikongbao.provisioning.domain.system.security;

import com.yodoo.feikongbao.provisioning.domain.system.service.PermissionManagerApiService;
import com.yodoo.feikongbao.provisioning.domain.system.service.UserManagerApiService;
import com.yodoo.megalodon.permission.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class ProvisioningUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserManagerApiService userManagerApiService;

    @Autowired
    private PermissionManagerApiService permissionManagerApiService;

    @Override
    public ProvisioningUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        User user = userManagerApiService.getUserByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在，请重新登陆！");
        }
        // 返回用户信息
        ProvisioningUserDetails userInfo = new ProvisioningUserDetails();
        userInfo.setId(user.getId());
        userInfo.setUsername(username);
        userInfo.setPassword(user.getPassword());
        userInfo.setName(user.getName());

        // 查询用户权限
        Set<GrantedAuthority> authoritiesSet = new HashSet<>();

        List<com.yodoo.megalodon.permission.entity.Permission> permissionList = permissionManagerApiService.getPermissionByUserId(user.getId());
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
