package net.bncloud.saas.ding.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.dingtalk.api.request.OapiV2DepartmentCreateRequest;
import com.dingtalk.api.request.OapiV2DepartmentDeleteRequest;
import com.dingtalk.api.request.OapiV2DepartmentUpdateRequest;
import com.dingtalk.api.request.OapiV2UserCreateRequest;
import com.dingtalk.api.request.OapiV2UserDeleteRequest;
import com.dingtalk.api.request.OapiV2UserUpdateRequest;
import com.dingtalk.api.response.OapiV2DepartmentCreateResponse;
import com.dingtalk.api.response.OapiV2UserCreateResponse;
import com.dingtalk.api.response.OapiV2UserUpdateResponse;
import com.taobao.api.ApiException;
import net.bncloud.common.util.BeanUtilTwo;
import net.bncloud.ding.helper.AuthHelper;
import net.bncloud.ding.helper.DepartmentHelper;
import net.bncloud.ding.helper.UserHelper;
import net.bncloud.saas.ding.domain.DingNewDepart;
import net.bncloud.saas.ding.domain.DingNewStaff;
import net.bncloud.saas.ding.domain.DingTalkOrgIntegrationConfig;
import net.bncloud.saas.ding.domain.vo.DingDeptVo;
import net.bncloud.saas.ding.repository.DingNewDepartRepository;
import net.bncloud.saas.ding.repository.DingNewStaffRepository;
import net.bncloud.saas.ding.repository.DingTalkOrgIntegrationConfigRepository;
import net.bncloud.saas.tenant.domain.OrgDepartment;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.repository.OrgDepartmentRepository;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.saas.utils.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DingTalkSyncDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DingTalkSyncDataService.class);

    //同步数据至钉钉时的记录日志（Redis-Key）
    private static final String BNC_DING_SYNC_DATA_KEY = "bncDingSyncDataKey";
    //同步数据至钉钉时需要的Token（Redis-Key）
    private static final String BNC_DING_TOKEN_KEY = "bncDingTokenKey";

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private DingTalkOrgIntegrationConfigRepository dingTalkOrgIntegrationConfigRepository;
    @Resource
    private OrgDepartmentRepository orgDepartmentRepository;
    @Resource
    private OrgEmployeeRepository orgEmployeeRepository;
    @Resource
    private DingNewDepartRepository dingNewDepartRepository;
    @Resource
    private DingNewStaffRepository dingNewStaffRepository;

    //初始化数据
    @PostConstruct
    private void initRedisData() {
        //初始化同步日志
        String syncLog = redisUtils.strGetString(BNC_DING_SYNC_DATA_KEY);
        Map<String, String> logMap = new HashMap<>();
        if (BeanUtilTwo.isEmpty(syncLog)) {
            //同步日志为空，从头开始同步
            OrgDepartment smallDepart = orgDepartmentRepository.findSmallDepart();
            if (smallDepart != null) {
                logMap.put("orgId", smallDepart.getOrgId().toString());//当前组织
                logMap.put("parentId", smallDepart.getParentId().toString());//当前父ID（对应着供应商）
                logMap.put("id", smallDepart.getId().toString());//当前主键ID
                redisUtils.strSet(BNC_DING_SYNC_DATA_KEY, JSONObject.toJSONString(logMap));
            }
        } else {
            //同步日志不为空
            JSONObject syncLogJson = JSONObject.parseObject(syncLog);
            logMap = (Map<String, String>) JSON.toJavaObject(syncLogJson, Map.class);
        }

        //初始化Token
        try {
            Map<String, String> tokenMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(tokenMap)) {
                tokenMap.put("orgId", logMap.get("orgId").toString());//当前组织
                String accessToken = "";
                DingTalkOrgIntegrationConfig config = dingTalkOrgIntegrationConfigRepository.findByOrgId(Long.parseLong(logMap.get("orgId").toString()), 1);
                accessToken = AuthHelper.getAccessToken(config.getAppKey(), config.getAppSecret());
                tokenMap.put("token", accessToken.toString());//当前组织Token
                redisUtils.strSetHasTime(BNC_DING_TOKEN_KEY, JSONObject.toJSONString(tokenMap), 3600, TimeUnit.SECONDS);
            }
        } catch (com.taobao.api.ApiException e) {
            throw new RuntimeException("初始化-获取钉钉API access_token 异常");
        }
    }

    //更新同步日志
    public void upSyncLog() {
        String syncLog = redisUtils.strGetString(BNC_DING_SYNC_DATA_KEY);
        JSONObject syncLogJson = JSONObject.parseObject(syncLog);
        Map<String, String> map = (Map<String, String>) JSON.toJavaObject(syncLogJson, Map.class);

        //获取下次需要同步的供应商主部门
        OrgDepartment nextSyncDepart = null;
        nextSyncDepart = orgDepartmentRepository.findGtById(Long.parseLong(map.get("orgId")), Long.parseLong(map.get("parentId")), Long.parseLong(map.get("id")));
        if (BeanUtilTwo.isEmpty(nextSyncDepart)) {//证明该组织已经同步完
            //获取下一个组织的供应商主部门继续同步
            nextSyncDepart = orgDepartmentRepository.findGtByOrgId(Long.parseLong(map.get("orgId")));
        }

        map.put("orgId", nextSyncDepart.getOrgId().toString());//下次要同步的组织
        map.put("parentId", nextSyncDepart.getParentId().toString());//下次要同步的父ID（对应着供应商）
        map.put("id", nextSyncDepart.getId().toString());//下次要同步的主键ID
        redisUtils.strSet(BNC_DING_SYNC_DATA_KEY, JSONObject.toJSONString(map));
    }

    //刷新获取Token
    public String getAccessToken(Map<String, String> map) {
        String accessToken = "";
        try {
            String tokenMax = redisUtils.strGetString(BNC_DING_TOKEN_KEY);
            JSONObject tokenJson = JSONObject.parseObject(tokenMax);
            Map<String, String> tokenMap = (Map<String, String>) JSON.toJavaObject(tokenJson, Map.class);

            if (BeanUtilTwo.isEmpty(tokenMap) || !(tokenMap.get("orgId").toString().equals(map.get("orgId").toString()))) {
                DingTalkOrgIntegrationConfig config = dingTalkOrgIntegrationConfigRepository.findByOrgId(Long.parseLong(map.get("orgId").toString()), 1);
                accessToken = AuthHelper.getAccessToken(config.getAppKey(), config.getAppSecret());
                Map<String, String> newTokenMap = new HashMap<>();
                newTokenMap.put("orgId", config.getOrgId().toString());//当前组织
                newTokenMap.put("token", accessToken.toString());//当前组织Token
                tokenMap = newTokenMap;
                redisUtils.strSetHasTime(BNC_DING_TOKEN_KEY, JSONObject.toJSONString(tokenMap), 3600, TimeUnit.SECONDS);
            }

            accessToken = tokenMap.get("token").toString();
        } catch (com.taobao.api.ApiException e) {
            throw new RuntimeException("获取钉钉API access_token 异常");
        }
        return accessToken;
    }

    @Transactional
    public void syncData() {
        //当前要同步的数据-对标供应商
        String syncLog = redisUtils.strGetString(BNC_DING_SYNC_DATA_KEY);
        JSONObject syncLogJson = JSONObject.parseObject(syncLog);
        Map<String, String> thisSyncMap = (Map<String, String>) JSON.toJavaObject(syncLogJson, Map.class);

        String thisToken = getAccessToken(thisSyncMap);
        String thisOrgId = thisSyncMap.get("orgId").toString();
        //String thisParentId = thisSyncMap.get("parentId").toString();
        String thisId = thisSyncMap.get("id").toString();

        //同步组织级（Top）部门
        OrgDepartment topDept = orgDepartmentRepository.findTopDeptByOrgId(Long.parseLong(thisOrgId));
        DingNewDepart dDept = dingNewDepartRepository.findByDepartId(topDept.getId());
        DingNewDepart newDDept = null;
        if (BeanUtilTwo.isEmpty(dDept)) {//新增
            dDept = new DingNewDepart();
            dDept.setDepartId(topDept.getId());
            dDept.setDdName(topDept.getName());
            dDept.setOrgId(topDept.getOrgId());
            dDept.setLevel(1);
            newDDept = addDept(dDept, thisToken);
        } else {//修改
            dDept.setDepartId(topDept.getId());
            dDept.setDdName(topDept.getName());
            dDept.setOrgId(topDept.getOrgId());
            dDept.setLevel(1);
            newDDept = updateDept(dDept, thisToken);
        }
        //同步组织级（Top）员工
        List<OrgEmployee> topStaffs = orgEmployeeRepository.findByOrgIdAndDeptId(Long.parseLong(thisOrgId), topDept.getId());
        for (OrgEmployee staff : topStaffs) {
            DingNewStaff dSatff = dingNewStaffRepository.findLikeEmployeeId("@" + staff.getId().toString() + "@");
            if (staff.isDeleted() && BeanUtilTwo.isNotEmpty(dSatff) && BeanUtilTwo.isNotEmpty(dSatff.getDdId())) {
                dSatff.setEmployeeIds(staff.getId().toString());
                dSatff.setDdeptIds(newDDept.getDdId().toString());
                refStaff(dSatff, thisToken, "del", new DingNewStaff());
            } else {
                if (BeanUtilTwo.isEmpty(dSatff)) {//新增
                    dSatff = new DingNewStaff();
                    dSatff.setOrgId(Long.parseLong(thisOrgId));
                    dSatff.setEmployeeIds(staff.getId().toString());
                    dSatff.setDdName(staff.getName());
                    dSatff.setDdMobile(staff.getMobile());
                    dSatff.setDdeptIds(newDDept.getDdId().toString());
                    refStaff(dSatff, thisToken, "add", new DingNewStaff());
                } else {//修改
                    //要修改的值
                    DingNewStaff newVal = new DingNewStaff();
                    newVal.setDdName(staff.getName());
                    newVal.setDdMobile(staff.getMobile());
                    refStaff(dSatff, thisToken, "update", newVal);
                }
            }
        }

        //待同步的数据-供应商级部门及旗下部门、员工等
        List<DingDeptVo> vos = getDeptsById(Long.parseLong(thisId));
        for (DingDeptVo vo : vos) {
            //同步部门
            DingNewDepart dDeptTwo = dingNewDepartRepository.findByDepartId(vo.getId());
            DingNewDepart newDDeptTwo = null;
            if (vo.getDel()) {//删除当前部门和级联删除旗下部门、员工等
                delDeptAndStaff(vo.getId(), thisToken);
            } else {
                if (BeanUtilTwo.isEmpty(dDeptTwo)) {//新增
                    dDeptTwo = new DingNewDepart();
                    dDeptTwo.setDepartId(vo.getId());
                    dDeptTwo.setDdName(vo.getName());
                    dDeptTwo.setOrgId(vo.getOrgId());
                    dDeptTwo.setLevel(vo.getLevel() + 1);
                    dDeptTwo.setParentId(dingNewDepartRepository.findByDepartId(vo.getParentId()).getId());
                    newDDeptTwo = addDept(dDeptTwo, thisToken);
                } else {//修改
                    dDeptTwo.setDepartId(vo.getId());
                    dDeptTwo.setDdName(vo.getName());
                    dDeptTwo.setOrgId(vo.getOrgId());
                    dDeptTwo.setLevel(vo.getLevel() + 1);
                    dDeptTwo.setParentId(dingNewDepartRepository.findByDepartId(vo.getParentId()).getId());
                    newDDeptTwo = updateDept(dDeptTwo, thisToken);
                }
                //同步员工
                List<OrgEmployee> topStaffsTwo = orgEmployeeRepository.findByOrgIdAndDeptId(Long.parseLong(thisOrgId), vo.getId());
                for (OrgEmployee staff : topStaffsTwo) {
                    DingNewStaff dSatff = dingNewStaffRepository.findLikeEmployeeId("@" + staff.getId().toString() + "@");
                    if (staff.isDeleted() && BeanUtilTwo.isNotEmpty(dSatff) && BeanUtilTwo.isNotEmpty(dSatff.getDdId())) {
                        dSatff.setEmployeeIds(staff.getId().toString());
                        dSatff.setDdeptIds(newDDeptTwo.getDdId().toString());
                        refStaff(dSatff, thisToken, "del", new DingNewStaff());
                    } else {
                        if (BeanUtilTwo.isEmpty(dSatff)) {//新增
                            dSatff = new DingNewStaff();
                            dSatff.setOrgId(Long.parseLong(thisOrgId));
                            dSatff.setEmployeeIds(staff.getId().toString());
                            dSatff.setDdName(staff.getName());
                            dSatff.setDdMobile(staff.getMobile());
                            dSatff.setDdeptIds(newDDeptTwo.getDdId().toString());
                            refStaff(dSatff, thisToken, "add", new DingNewStaff());
                        } else {//修改
                            //要修改的值
                            DingNewStaff newVal = new DingNewStaff();
                            newVal.setDdName(staff.getName());
                            newVal.setDdMobile(staff.getMobile());
                            refStaff(dSatff, thisToken, "update", newVal);
                        }
                    }
                }
            }
        }

        //更新同步日志
        upSyncLog();
    }

    //根据主键ID级联获取本身及旗下的部门集合-平层List-带部门层级并根据层级数字从小到大排序
    public List<DingDeptVo> getDeptsById(Long id) {
        List<OrgDepartment> depts = orgDepartmentRepository.findAndAllChildByPid(id);
        List<DingDeptVo> vos = new ArrayList<>();
        for (OrgDepartment dept : depts) {
            DingDeptVo vo = new DingDeptVo();
            vo.setId(dept.getId());
            vo.setOrgId(dept.getOrgId());
            vo.setParentId(dept.getParentId());
            vo.setName(dept.getName());
            vo.setDel(dept.isDeleted());
            vos.add(vo);
        }
        //构建树型List-并带有部门层级
        List<DingDeptVo> treeVos = buildTree(vos);
        //转回平层List
        List<DingDeptVo> listVos = treeToList(treeVos);
        //根据部门层级数字从小到大排序
        listVos.sort((x, y) -> Integer.compare(x.getLevel(), y.getLevel()));
        return listVos;
    }

    //新增钉钉部门-连带钉钉平台数据
    @Transactional
    public DingNewDepart addDept(DingNewDepart dingNewDepart, String accessToken) {
        try {
            OapiV2DepartmentCreateRequest createRequest = new OapiV2DepartmentCreateRequest();
            if (BeanUtilTwo.isEmpty(dingNewDepart.getParentId())) {
                createRequest.setParentId(1L);
            } else {
                DingNewDepart parent = dingNewDepartRepository.findById(dingNewDepart.getParentId()).get();
                if (BeanUtilTwo.isNotEmpty(parent)) {
                    createRequest.setParentId(parent.getDdId());
                } else {//父部门为空-代表该部门是组织级部门
                    createRequest.setParentId(1L);
                }
            }
            createRequest.setName(dingNewDepart.getDdName());
            createRequest.setOuterDeptOnlySelf(false);
            createRequest.setOuterDept(false);
            OapiV2DepartmentCreateResponse response = DepartmentHelper.createDepartment(createRequest, accessToken);
            if (response.getErrcode() == 0) {
                dingNewDepart.setDdId(response.getResult().getDeptId());
                DingNewDepart save = dingNewDepartRepository.save(dingNewDepart);
                return save;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //更新钉钉部门-连带钉钉平台数据
    @Transactional
    public DingNewDepart updateDept(DingNewDepart dingNewDepart, String accessToken) {
        try {
            OapiV2DepartmentUpdateRequest request = new OapiV2DepartmentUpdateRequest();
            request.setDeptId(dingNewDepart.getDdId());
            request.setName(dingNewDepart.getDdName());
            if (BeanUtilTwo.isEmpty(dingNewDepart.getParentId())) {
                request.setParentId(1L);
            } else {
                DingNewDepart parent = dingNewDepartRepository.findById(dingNewDepart.getParentId()).get();
                if (BeanUtilTwo.isNotEmpty(parent)) {
                    request.setParentId(parent.getDdId());
                } else {//父部门为空-代表该部门是组织级部门
                    request.setParentId(1L);
                }
            }
            Long res = DepartmentHelper.updateDepartment(request, accessToken);
            if (res == 0) {
                DingNewDepart save = dingNewDepartRepository.save(dingNewDepart);
                return save;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return dingNewDepart;
    }

    /**
     * 刷新钉钉员工-连带钉钉平台数据
     *
     * @param staff       传参媒介
     * @param accessToken 钉钉token
     * @param operType    del-删除 update-修改 add-新增
     * @param newVal      修改时候用（新内容）
     */
    @Transactional
    public void refStaff(DingNewStaff staff, String accessToken, String operType, DingNewStaff newVal) {
        try {
            DingNewStaff now = dingNewStaffRepository.findByOrgAndMobile(staff.getOrgId(), staff.getDdMobile());

            if ("del".equals(operType) && BeanUtilTwo.isNotEmpty(now)) {//删除
                if (now.getDdeptIds().equals(staff.getDdeptIds())) {
                    //硬删
                    OapiV2UserDeleteRequest request = new OapiV2UserDeleteRequest();
                    request.setUserid(now.getDdId());
                    Long res = UserHelper.deleteUser(request, accessToken);
                    if (res == 0) {
                        dingNewStaffRepository.deleteById(now.getId());
                    }
                } else if (now.getDdeptIds().contains(staff.getDdeptIds())) {
                    //修改
                    List<String> nowDepts = new ArrayList<>(Arrays.asList(now.getDdeptIds().split(",")));
                    nowDepts.removeIf(s -> s.equals(staff.getDdeptIds()));
                    String nowDeptStr = nowDepts.stream().map(String::valueOf).collect(Collectors.joining(","));
                    OapiV2UserUpdateRequest request = new OapiV2UserUpdateRequest();
                    request.setUserid(now.getDdId());
                    request.setDeptIdList(nowDeptStr);
                    OapiV2UserUpdateResponse response = UserHelper.updateUser(request, accessToken);
                    if (response.getErrcode() == 0) {
                        List<String> nowEmployeeIds = new ArrayList<>(Arrays.asList(now.getEmployeeIds().split(",")));
                        nowDepts.removeIf(s -> s.equals("@" + staff.getEmployeeIds() + "@"));
                        String nowEmployeeStr = nowEmployeeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                        now.setEmployeeIds(nowEmployeeStr);
                        now.setDdeptIds(nowDeptStr);
                        dingNewStaffRepository.save(now);
                    }
                }
            }

            if ("update".equals(operType)
                    && BeanUtilTwo.isNotEmpty(now)
                    && BeanUtilTwo.isNotEmpty(staff)
                    && BeanUtilTwo.isNotEmpty(newVal)) {
                //修改
                OapiV2UserUpdateRequest request = new OapiV2UserUpdateRequest();
                if (BeanUtilTwo.isNotEmpty(newVal.getDdName())) {
                    now.setDdName(newVal.getDdName());
                    request.setName(newVal.getDdName());
                }
                if (BeanUtilTwo.isNotEmpty(newVal.getDdMobile())) {
                    now.setDdMobile(newVal.getDdMobile());
                    request.setMobile(newVal.getDdMobile());
                }
                //刷新对应员工和部门
                List<OrgEmployee> nowEmployees = orgEmployeeRepository.findByOrgAndMobile(now.getOrgId(), now.getDdMobile());
                String newEmployeeIds = "";
                List<Long> departIds = new ArrayList<>();
                for (OrgEmployee nowEmployee : nowEmployees) {
                    newEmployeeIds += ("@" + nowEmployee.getId() + "@,");
                    departIds.add(nowEmployee.getDepartment().getId());
                }
                newEmployeeIds = newEmployeeIds.substring(0, newEmployeeIds.length() - 1);
                List<Long> DDdeptIds = dingNewDepartRepository.findDdIdByDepartIdIn(departIds);
                String newDdeptIds = DDdeptIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                now.setEmployeeIds(newEmployeeIds);
                now.setDdeptIds(newDdeptIds);
                request.setDeptIdList(newDdeptIds);
                request.setUserid(now.getDdId());
                OapiV2UserUpdateResponse response = UserHelper.updateUser(request, accessToken);
                if (response.getErrcode() == 0) {
                    dingNewStaffRepository.save(now);
                }
            }

            if ("add".equals(operType)
                    && BeanUtilTwo.isEmpty(now)
                    && BeanUtilTwo.isNotEmpty(staff)
                    && BeanUtilTwo.isNotEmpty(staff.getOrgId())
                    && BeanUtilTwo.isNotEmpty(staff.getEmployeeIds())
                    && BeanUtilTwo.isEmpty(staff.getId())) {
                OapiV2UserCreateRequest createRequest = new OapiV2UserCreateRequest();
                createRequest.setName(staff.getDdName());
                createRequest.setMobile(staff.getDdMobile());
                createRequest.setDeptIdList(staff.getDdeptIds());
                OapiV2UserCreateResponse response = UserHelper.createUser(createRequest, accessToken);
                if (response.getErrcode() == 40103) {
                    //需用户同意，未同步成功
                } else if (response.getErrcode() == 0) {
                    //同步成功
                    String dUserId = response.getResult().getUserid();
                    staff.setEmployeeIds("@" + staff.getEmployeeIds() + "@");
                    staff.setDdId(dUserId);
                    dingNewStaffRepository.save(staff);
                } else if (response.getErrcode() == 60104) {
                    //已存在
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //级联删除该部门及旗下的员工（钉钉相关表及钉钉平台数据）
    @Transactional
    public void delDeptAndStaff(Long departId, String accessToken) {
        try {
            List<DingNewDepart> dDeparts = getAndChildAllDing(departId);

            //删除部门请求
            OapiV2DepartmentDeleteRequest deleteDeptRequest = new OapiV2DepartmentDeleteRequest();
            for (DingNewDepart dDepart : dDeparts) {
                List<DingNewStaff> staffs = dingNewStaffRepository.findLikeDdepartId(dDepart.getDdId().toString());
                //删除用户请求
                for (DingNewStaff staff : staffs) {
                    OrgEmployee employee = orgEmployeeRepository.findByDeptAndMobile(dDepart.getDepartId(), staff.getDdMobile(), dDepart.getOrgId());
                    staff.setEmployeeIds(employee.getId().toString());
                    staff.setDdeptIds(dDepart.getDdId().toString());
                    refStaff(staff, accessToken, "del", new DingNewStaff());
                }
                deleteDeptRequest.setDeptId(dDepart.getDdId());
                Long res = DepartmentHelper.deleteDepartment(deleteDeptRequest, accessToken);
                if (res == 0) {
                    dingNewDepartRepository.deleteById(dDepart.getId());
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    //根据业务库部门ID级联获取钉钉部门（包括子集）最子级部门在前
    public List<DingNewDepart> getAndChildAllDing(Long departId) {
        List<DingNewDepart> ds = new ArrayList<>();
        DingNewDepart d = dingNewDepartRepository.findByDepartId(departId);
        if (BeanUtilTwo.isNotEmpty(d)) {
            ds = dingNewDepartRepository.findAndAllChildByPid(d.getId());
        }
        return ds;
    }

    //将符合父子结构的List构建成树结构List-带层级
    private static List<DingDeptVo> buildTree(List<DingDeptVo> vos) {
        List<DingDeptVo> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (DingDeptVo vo : vos) {
            ids.add(vo.getId());
        }
        for (Iterator<DingDeptVo> iterator = vos.iterator(); iterator.hasNext(); ) {
            DingDeptVo vo = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!ids.contains(vo.getParentId())) {
                vo.setLevel(1);
                recursionFn(vos, vo, 1);
                trees.add(vo);
            }
        }
        if (trees.size() == 0) {
            trees = vos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    //递归列表
    private static void recursionFn(List<DingDeptVo> list, DingDeptVo t, Integer level) {
        level++;
        // 得到子节点列表
        List<DingDeptVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (DingDeptVo tChild : childList) {
            tChild.setLevel(level);
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild, level);
            }
        }
    }

    //判断是否有子节点
    private static boolean hasChild(List<DingDeptVo> list, DingDeptVo t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    //得到子节点列表
    private static List<DingDeptVo> getChildList(List<DingDeptVo> list, DingDeptVo t) {
        List<DingDeptVo> tlist = new ArrayList<DingDeptVo>();
        Iterator<DingDeptVo> it = list.iterator();
        while (it.hasNext()) {
            DingDeptVo n = it.next();
            if (n.getParentId() == t.getId()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    //将树型List转成普通List
    private static List<DingDeptVo> treeToList(List<DingDeptVo> vos) {
        List<DingDeptVo> result = new ArrayList<>();
        for (DingDeptVo vo : vos) {
            List<DingDeptVo> c = vo.getChildren();
            result.add(vo);
            if (!CollectionUtils.isEmpty(c)) {
                result.addAll(treeToList(c));
                vo.setChildren(null);
            }
        }
        return result;
    }
}
