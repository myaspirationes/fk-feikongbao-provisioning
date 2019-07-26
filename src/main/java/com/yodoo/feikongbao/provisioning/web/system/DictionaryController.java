package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.DictionaryDto;
import com.yodoo.feikongbao.provisioning.domain.system.security.ProvisioningAuthenticationProvider;
import com.yodoo.feikongbao.provisioning.domain.system.service.DictionaryService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/26 0026
 */
@RestController
@RequestMapping(value = "/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ProvisioningAuthenticationProvider provider;

    /**
     * 条件分页查询
     * @param dictionaryDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('dictionary')")
    public ProvisioningDto<?> queryDictionaryList(DictionaryDto dictionaryDto){
        PageInfoDto<DictionaryDto> pageInfoDto = dictionaryService.queryDictionaryList(dictionaryDto);
        // 列表item导向
        provider.setItemListLink(pageInfoDto.getList(), DictionaryController.class);
        // 操作资源导向
        provider.setResourceLink(pageInfoDto, DictionaryController.class, Arrays.asList("dictionary"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode());
        return new ProvisioningDto<PageInfoDto<DictionaryDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加:
     * @param dictionaryDto
     * @return
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('dictionary')")
    public ProvisioningDto<?> addDictionary(@RequestBody DictionaryDto dictionaryDto){
        return dictionaryService.addDictionary(dictionaryDto);
    }

}
