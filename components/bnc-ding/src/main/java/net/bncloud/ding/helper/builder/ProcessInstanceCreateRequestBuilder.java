package net.bncloud.ding.helper.builder;

import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessInstanceCreateRequestBuilder {
    private OapiProcessinstanceCreateRequest request;

    private final List<OapiProcessinstanceCreateRequest.FormComponentValueVo> formComponentValues = new ArrayList<>();

    private final List<OapiProcessinstanceCreateRequest.ProcessInstanceApproverVo> approvers = new ArrayList<>();

    private ProcessInstanceCreateRequestBuilder(String processCode) {
        this.request = new OapiProcessinstanceCreateRequest();
        this.request.setProcessCode(processCode);
    }

    public static ProcessInstanceCreateRequestBuilder builder(String processCode) {
        return new ProcessInstanceCreateRequestBuilder(processCode);
    }

    public OapiProcessinstanceCreateRequest build() {
        if (!formComponentValues.isEmpty()) {
            request.setFormComponentValues(formComponentValues);
        }
        if (!approvers.isEmpty()) {
            request.setApproversV2(approvers);
        }
        return request;
    }

    public ProcessInstanceCreateRequestBuilder agentId(Long agentId) {
        request.setAgentId(agentId);
        return this;
    }

    public ProcessInstanceCreateRequestBuilder addComponentValue(String name, String value) {
        return this.addComponentValue(name, value, null);
    }

    public ProcessInstanceCreateRequestBuilder addComponentValue(String name, String value, String ext_value) {
        OapiProcessinstanceCreateRequest.FormComponentValueVo cv = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
        cv.setName(name);
        cv.setValue(value);
        cv.setExtValue(ext_value);
        this.formComponentValues.add(cv);
        return this;
    }

    public ProcessInstanceCreateRequestBuilder approvers(String approvers) {
        this.request.setApprovers(approvers);
        return this;
    }

    public ProcessInstanceCreateRequestBuilder addApprovers(List<OapiProcessinstanceCreateRequest.ProcessInstanceApproverVo> approvers) {
        this.approvers.addAll(approvers);
        return this;
    }

    public ProcessInstanceCreateRequestBuilder addApprover(OapiProcessinstanceCreateRequest.ProcessInstanceApproverVo approver) {
        this.approvers.add(approver);
        return this;
    }

    public ProcessInstanceCreateRequestBuilder addApprover(String taskActionType, String... userIds) {
        OapiProcessinstanceCreateRequest.ProcessInstanceApproverVo vo = new OapiProcessinstanceCreateRequest.ProcessInstanceApproverVo();
        vo.setTaskActionType(taskActionType);
        vo.setUserIds(Arrays.asList(userIds));
        this.addApprover(vo);
        return this;
    }

    public ProcessInstanceCreateRequestBuilder ccList(List<String> userIdList) {
        this.request.setCcList(String.join(",", userIdList));
        return this;
    }

    public ProcessInstanceCreateRequestBuilder ccPosition(String position) {
        this.request.setCcPosition(position);
        return this;
    }

    public ProcessInstanceCreateRequestBuilder originatorUserId(String userId) {
        this.request.setOriginatorUserId(userId);
        return this;
    }

    public ProcessInstanceCreateRequestBuilder deptId(Long deptId) {
        if (deptId == null) {
            deptId = -1L;
        }
        this.request.setDeptId(deptId);
        return this;
    }
}