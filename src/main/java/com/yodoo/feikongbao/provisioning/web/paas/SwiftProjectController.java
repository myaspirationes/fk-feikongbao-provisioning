package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.SwiftProjectDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.SwiftProjectService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：存储
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/swiftProject")
public class SwiftProjectController {

    @Autowired
    private SwiftProjectService swiftProjectService;

    /**
     * 条件分页查询
     *
     * @param swiftProjectDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> querySwiftProjectList(SwiftProjectDto swiftProjectDto) {
        PageInfoDto<SwiftProjectDto> pageInfoDto = swiftProjectService.querySwiftProjectList(swiftProjectDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), SwiftProjectController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, SwiftProjectController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<SwiftProjectDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getSwiftProjectDetails(@PathVariable Integer id) {
        SwiftProjectDto swiftProjectDto = swiftProjectService.getSwiftProjectDetails(id);
        return new ProvisioningDto<SwiftProjectDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, swiftProjectDto);
    }

    /**
     * 创建存储对象
     *
     * @param swiftProjectDto
     * @return
     */
    @RequestMapping(value = "/useSwiftProject", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useSwiftProject(@RequestBody SwiftProjectDto swiftProjectDto) {
        return swiftProjectService.useSwiftProject(swiftProjectDto);
    }


}
