package com.yodoo.feikongbao.provisioning.domain.system.mapper;

import com.yodoo.feikongbao.provisioning.common.mapper.BaseMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Groups;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface GroupsMapper extends BaseMapper<Groups> {

    /**
     * 查询除了自已以外是否有相同数据
     *
     * @param id
     * @param groupName
     * @param groupCode
     * @return
     */
    Groups selectGroupsInAdditionToItself(@Param("id") Integer id, @Param("groupName") String groupName, @Param("groupCode") String groupCode);

    /**
     * 查询id以外的数据
     * @param groupIds
     * @return
     */
    List<Groups> selectGroupInNotIn(@Param("groupIds") Set<Integer> groupIds);
}