package net.bncloud.saas.tenant.web;

import cn.hutool.core.lang.Dict;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.saas.purchaser.service.PurchaserService;
import net.bncloud.saas.purchaser.service.dto.PurchaserDTO;
import net.bncloud.saas.purchaser.service.query.PurchaserSmallQuery;
import net.bncloud.saas.supplier.service.SupplierService;
import net.bncloud.saas.supplier.service.SupplierStaffService;
import net.bncloud.saas.supplier.service.dto.SupplierDTO;
import net.bncloud.saas.supplier.service.query.SupplierQuery;
import net.bncloud.saas.supplier.service.query.SupplierStaffQuery;
import net.bncloud.saas.tenant.domain.vo.BatchOrgManagerVO;
import net.bncloud.saas.tenant.service.OrgAdministratorService;
import net.bncloud.saas.tenant.service.OrgEmployeeService;
import net.bncloud.saas.tenant.service.OrganizationService;
import net.bncloud.saas.tenant.service.command.CreateOrgCommand;
import net.bncloud.saas.tenant.service.dto.MaterialDTO;
import net.bncloud.saas.tenant.service.dto.MemberDTO;
import net.bncloud.saas.tenant.service.query.MaterialTempQuery;
import net.bncloud.saas.tenant.service.query.MemberQuery;
import net.bncloud.saas.tenant.service.query.OrgEmployeeQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/platform/purchaser/collaboration")
@AllArgsConstructor
public class PlatformCollaborationController {
    private final OrganizationService organizationService;
    private final OrgAdministratorService orgAdministratorService;
    private final OrgEmployeeService orgEmployeeService;
    private final SupplierService supplierService;
    private final PurchaserService purchaserService;
    private final SupplierStaffService supplierStaffService;

    @ApiOperation("同步创建协助组织")
    @PostMapping("/createOrg")
    public R addAsyncOrg(@RequestBody CreateOrgCommand command) {
        boolean isReload = organizationService.addAsyncOrg(command);
        return R.data(Dict.create().set("isReload", isReload));
    }

    @ApiOperation(value = "同步协助组织")
    @PostMapping("/asyncOrg/{orgId}")
    public R asyncOrg(@PathVariable Long orgId) {
        //organizationService.asyncOrg(orgId);
        return R.success();
    }


    @ApiOperation(value = "[协助管理]物料列表")
    @PostMapping("/material")
    public R material(@RequestBody MaterialTempQuery query) {
        SubjectType subjectType = query.getSubjectType();
        List<MaterialDTO> dtos = Lists.newArrayList();
        Long subjectId = query.getSubjectId();
        if (SubjectType.org.equals(subjectType)) {
            //查询关联采购方
            PurchaserSmallQuery purchaserQuery = new PurchaserSmallQuery();
            purchaserQuery.setOrgId(subjectId);
            QueryParam<PurchaserSmallQuery> purchaserQueryQueryParam = new QueryParam<>();
            purchaserQueryQueryParam.setParam(purchaserQuery);
            purchaserQueryQueryParam.setSearchValue(query.getSearchValue());
            List<PurchaserDTO> purchaserDTOS = purchaserService.smallQuery(purchaserQueryQueryParam);
            if (CollectionUtil.isNotEmpty(purchaserDTOS)) {
                dtos = purchaserDTOS.stream().map(this::purToMaterial).collect(Collectors.toList());
                dtos.forEach(item -> item.setSubjectId(subjectId));
            }
        } else {
            //查询供应商
            SupplierQuery supplierQuery = new SupplierQuery();
            supplierQuery.setOrgId(subjectId);
            supplierQuery.setQs(query.getSearchValue());
            List<SupplierDTO> supplierDTOS = supplierService.allQuery(supplierQuery);
            if (CollectionUtil.isNotEmpty(supplierDTOS)) {
                dtos = supplierDTOS.stream().map(this::supToMaterial).collect(Collectors.toList());
                dtos.forEach(item -> item.setSubjectId(subjectId));
            }
        }
        return R.data(dtos);
    }


    @ApiOperation(value = "[管理员设置]批量添加组织管理员")
    @PostMapping("/batchAddOrgManager")
    public R batchAddOrgManager(@RequestBody BatchOrgManagerVO vo) {
        orgAdministratorService.batchAddOrgManager(vo);
        return R.success();
    }

    @ApiOperation(value = "[成员管理]查询关联的供应商列表")
    @GetMapping("/queryRelateSuppliers")
    public R queryRelateSuppliers() {
        return R.data(supplierService.queryRelateSuppliers());
    }

    @ApiOperation(value = "[成员管理]根据供应商名称查询关联的供应商列表")
    @GetMapping("/queryRelateSuppliersByName")
    public R queryRelateSuppliersByName(@RequestParam("qs") String qs){
        return R.data(supplierService.queryRelateSuppliersByName(qs));
    }

    @ApiOperation(value = "[管理员设置]转让管理员详情回显")
    @GetMapping("/checkTransfer/{orgManagerId}")
    public R checkTransfer(@PathVariable Long orgManagerId) {
        return R.data(orgAdministratorService.checkTransfer(orgManagerId));
    }


    //    @ApiOperation(value = "[管理员设置]转让协作组织管理员")
//    @PostMapping("/transferOrganizationManager")
//    public R transferOrganizationManager(@RequestBody TransOrgManagerVO vo) {
//        //platformCollabOrganizationService.transferOrganizationManager(vo);
//        return R.success();
//    }
//
//
    @ApiOperation(value = "协助组织成员表格-[应用于物料数据查询]")
    @PostMapping("/memberTable")
    public R memberTable(@RequestBody QueryParam<MemberQuery> queryParam, Pageable pageable) {
        MemberQuery param = queryParam.getParam();
        SubjectType subjectType = param.getSubjectType();
        String code = param.getCode();
        Long subjectId = param.getSubjectId();
        if (SubjectType.org.equals(subjectType)) {
            //返回 有这个数据权限的组织成员数据
            OrgEmployeeQuery orgEmployeeQuery = new OrgEmployeeQuery();
            orgEmployeeQuery.setOrgId(subjectId);
            orgEmployeeQuery.setPurchaserCode(code);
            return R.data(orgEmployeeService.queryPageBydataGrant(orgEmployeeQuery, pageable));
        } else {
            //返回供应商成员
            SupplierStaffQuery supplierStaffQuery = new SupplierStaffQuery();
            supplierStaffQuery.setSupplierCode(code);
            QueryParam<SupplierStaffQuery> staffQueryQueryParam = new QueryParam<>();
            staffQueryQueryParam.setParam(supplierStaffQuery);
            return R.data(supplierStaffService.queryPage(staffQueryQueryParam, pageable));
        }
    }

    public MemberDTO memberDTO(SupplierDTO dto) {
        return MemberDTO.builder()
                .id(dto.getId())
                .name(dto.getName())
                .mobile(dto.getManagerMobile())
                .build();
    }

    @ApiOperation("[协助管理]组织分类树")
    @GetMapping("/relateOrg/tree")
    public R relateOrgTree() {
        return R.data(orgEmployeeService.relateOrgTree());
    }


    private MaterialDTO purToMaterial(PurchaserDTO dto) {
        return MaterialDTO.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .subjectType(SubjectType.org.name())
                .build();
    }

    private MaterialDTO supToMaterial(SupplierDTO dto) {
        return MaterialDTO.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .subjectType(SubjectType.supplier.name())
                .build();
    }
}
