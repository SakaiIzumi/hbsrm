package net.bncloud.saas.ding.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2DepartmentCreateRequest;
import com.dingtalk.api.response.OapiV2DepartmentCreateResponse;
import com.taobao.api.ApiException;
import net.bncloud.saas.ding.domain.DingDepartment;
import net.bncloud.saas.ding.domain.DingTalkCorp;
import org.springframework.stereotype.Service;

@Service
public class DingTalkDeptService {

    private final DingTalkCorpService dingTalkCorpService;
    private final DingTalkTokenService dingTalkTokenService;

    public DingTalkDeptService(DingTalkCorpService dingTalkCorpService, DingTalkTokenService dingTalkTokenService) {
        this.dingTalkCorpService = dingTalkCorpService;
        this.dingTalkTokenService = dingTalkTokenService;
    }

    public void createSupplierDept(Long orgId, Long deptId, String deptName) {
        DingTalkCorp corp = dingTalkCorpService.getOrgCorp(orgId);
        if (corp == null) {
            return; // TODO
        }

        try {
            String accessToken = dingTalkTokenService.getCorpInternalAccessToken(corp.getCorpId());

            DingDepartment dept = new DingDepartment();
            dept.setName(deptName);
            dept.setParentId(new Long(1));
            Long dingDeptId = createDingTalkDept(dept.toCreateRequest(), accessToken);

            dept.setSourceIdentifier(String.valueOf(deptId));// TODO

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private Long createDingTalkDept(OapiV2DepartmentCreateRequest req, String access_token) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/create");

        OapiV2DepartmentCreateResponse rsp = client.execute(req, access_token);
        OapiV2DepartmentCreateResponse.DeptCreateResponse result = rsp.getResult();
        return result.getDeptId();
    }
}
