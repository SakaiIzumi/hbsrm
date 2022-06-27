package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * 智能工作流
 */
public final class ProcessHelper {
    /**
     * 发起审批实例
     */
    public static OapiProcessinstanceCreateResponse createProcessInstance(OapiProcessinstanceCreateRequest request, String accessToken) throws ApiException {
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/create");
        return client.execute(request, accessToken);
    }

    /**
     *
     * 批量获取审批实例ID
     */
    public static OapiProcessinstanceListidsResponse getProcessInstanceListIds(String processCode, LocalDate startTime, LocalDate endTime,
                                                                               Long cursor, Long size, List<String> userIds, String accessToken) throws ApiException {
        if (StringUtils.isBlank(processCode)) {
            throw new IllegalArgumentException("processCode不能为空");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("开始时间不能为空");
        }
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/listids");
        OapiProcessinstanceListidsRequest req = new OapiProcessinstanceListidsRequest();
        req.setProcessCode(processCode);
        req.setStartTime(startTime.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        if (endTime != null) {
            req.setEndTime(endTime.plusDays(1).atStartOfDay().minusSeconds(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }

        if (cursor != null) {
            req.setSize(size);
            req.setCursor(cursor);
        }
        if (size != null) {
            req.setSize(size);
        }

        if (userIds != null && !userIds.isEmpty()) {
            req.setUseridList(String.join(",", userIds));
        }
        return client.execute(req, accessToken);
    }

    /**
     * 获取审批实例详情
     * @param processInstanceId 审批实例ID
     */
    public static OapiProcessinstanceGetResponse getProcessInstance(String processInstanceId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/get");
        OapiProcessinstanceGetRequest request = new OapiProcessinstanceGetRequest();
        request.setProcessInstanceId(processInstanceId);
        return client.execute(request, accessToken);
    }

    /**
     * 获取用户待审批数量
     * @param userId 用户ID
     */
    public static OapiProcessGettodonumResponse getUserProcessTodoNum(String userId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/process/gettodonum");
        OapiProcessGettodonumRequest req = new OapiProcessGettodonumRequest();
        req.setUserid(userId);
        return client.execute(req, accessToken);
    }

    /**
     * 获取用户可见的审批模板
     * @param userId 用户id
     * @param cursor 分页游标，从0开始。根据返回结果里的next_cursor是否为空来判断是否还有下一页，且再次调用时offset设置成next_cursor的值
     * @param size 分页大小，最大可设置成100
     */
    public static OapiProcessListbyuseridResponse getProcessListByUserId(String userId, Long cursor, Long size, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/process/listbyuserid");
        OapiProcessListbyuseridRequest request = new OapiProcessListbyuseridRequest();
        request.setUserid(userId);
        request.setOffset(cursor);
        request.setSize(size);
        return client.execute(request, accessToken);
    }

    /**
     * 获取审批钉盘空间ID
     * @param userId 用户id
     */
    public static OapiProcessinstanceCspaceInfoResponse processInstanceSpaceInfo(String userId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/cspace/info");
        OapiProcessinstanceCspaceInfoRequest req = new OapiProcessinstanceCspaceInfoRequest();
        req.setUserId(userId);
        return client.execute(req, accessToken);
    }

    /**
     * 预览审批附件<br/>
     * 此接口需配合钉盘JSAPI使用，实现在企业应用内预览、下载审批附件的功能
     * @return 审批在钉盘里的spaceId
     */
    public static OapiProcessinstanceCspacePreviewResponse processInstanceSpacePreview(Long agentid, String processInstanceId, String fileId, String userId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/cspace/preview");
        OapiProcessinstanceCspacePreviewRequest req = new OapiProcessinstanceCspacePreviewRequest();
        OapiProcessinstanceCspacePreviewRequest.GrantCspaceRequest obj1 = new OapiProcessinstanceCspacePreviewRequest.GrantCspaceRequest();
        obj1.setAgentid(agentid);
        obj1.setProcessInstanceId(processInstanceId);
        obj1.setFileId(fileId);
        obj1.setUserid(userId);
        req.setRequest(obj1);
        return client.execute(req, accessToken);
    }
}