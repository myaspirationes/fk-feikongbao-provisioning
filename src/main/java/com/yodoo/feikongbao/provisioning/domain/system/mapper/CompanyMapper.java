package com.yodoo.feikongbao.provisioning.domain.system.mapper;

import com.yodoo.feikongbao.provisioning.common.mapper.BaseMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 公司
 * @Date 2019/6/10 20:03
 * @Author by houzhen
 */
public interface CompanyMapper extends BaseMapper<Company> {

    /**
     * 查询除自已以外是否有相同的数据
     *
     * @param id
     * @param groupId
     * @param companyName
     * @param companyCode
     * @return
     */
    Company selectCompanyInAdditionToItself(@Param("id") Integer id, @Param("groupId") Integer groupId, @Param("companyName") String companyName, @Param("companyCode") String companyCode);

    /**
     * 查询除id以外
     * @param companyIds
     * @return
     */
    List<Company> selectCompanyNotInIds(@Param("companyIds") Set<Integer> companyIds);
}