package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.DictionaryDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.DictionaryService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/26 0026
 */
@RestController
@RequestMapping(value = "/dictionary")
@Api(tags = "DictionaryController | 字典表")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public ProvisioningDto<?> queryDictionaryList(int pageNum, int pageSize) {
        DictionaryDto dictionaryDto = new DictionaryDto();
        dictionaryDto.setPageNum(pageNum);
        dictionaryDto.setPageSize(pageSize);
        PageInfoDto<DictionaryDto> pageInfoDto = dictionaryService.queryDictionaryList(dictionaryDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), DictionaryController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, DictionaryController.class, Arrays.asList("dictionary_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<DictionaryDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加:
     *
     * @param dictionaryDto
     * @return
     */
    @ApiOperation(value = "添加", httpMethod = "POST")
    @ApiImplicitParam(name = "dictionaryDto", value = "字典 dictionaryDto", required = true, dataType = "DictionaryDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public ProvisioningDto<?> addDictionary(@RequestBody DictionaryDto dictionaryDto) {
        dictionaryService.addDictionary(dictionaryDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新
     *
     * @param dictionaryDto
     * @return
     */
    @ApiOperation(value = "更新", httpMethod = "PUT")
    @ApiImplicitParam(name = "dictionaryDto", value = "字典 dictionaryDto", required = true, dataType = "DictionaryDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public ProvisioningDto<?> editDictionary(@RequestBody DictionaryDto dictionaryDto) {
        dictionaryService.editDictionary(dictionaryDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "字典表数据库自增 id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public ProvisioningDto<?> deleteDictionary(@PathVariable Integer id) {
        dictionaryService.deleteDictionary(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询详情", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "字典表数据库自增 id", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public ProvisioningDto<?> getDictionaryDetails(@PathVariable Integer id) {
        DictionaryDto dictionaryDetails = dictionaryService.getDictionaryDetails(id);
        return new ProvisioningDto<DictionaryDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, dictionaryDetails);
    }
}
