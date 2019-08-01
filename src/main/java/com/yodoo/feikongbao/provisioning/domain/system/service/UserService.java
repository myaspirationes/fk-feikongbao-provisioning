package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.domain.system.entity.User;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.UserMapper;
import com.yodoo.feikongbao.provisioning.util.RequestPrecondition;
import com.yodoo.feikongbao.provisioning.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Date 2019/7/26 11:49
 * @Created by houzhen
 */
@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据账号查询用户
     *
     * @Author houzhen
     * @Date 13:09 2019/7/26
     **/
    public User getUserByAccount(String account) {
        logger.info("UserService.getUserByAccount account:{}", account);
        // 验证参数
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(account));
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("account", account);
        example.and(criteria);
        return userMapper.selectOneByExample(example);
    }


}
