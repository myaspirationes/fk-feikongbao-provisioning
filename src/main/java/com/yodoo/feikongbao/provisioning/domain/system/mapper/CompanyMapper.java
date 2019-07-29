package com.yodoo.feikongbao.provisioning.domain.system.mapper;

import com.yodoo.feikongbao.provisioning.common.mapper.BaseMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import org.apache.ibatis.annotations.Param;

public interface CompanyMapper extends BaseMapper<Company> {

    /**
     * 查询除自已以外是否有相同的数据
     * @param id
     * @param companyCode
     * @param companyName
     * @return
     */
    Company selectCompanyInAdditionToItself(@Param("id") Integer id, @Param("groupId") Integer groupId, @Param("companyName") String companyName, @Param("companyCode") String companyCode);
}