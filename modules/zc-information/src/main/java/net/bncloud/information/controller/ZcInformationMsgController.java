package net.bncloud.information.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.Org;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.common.util.StringUtil;
import net.bncloud.enums.SubjectType;
import net.bncloud.enums.SystemType;
import net.bncloud.enums.TerminalType;
import net.bncloud.information.Enum.InformationReadStatusEnum;
import net.bncloud.information.Enum.MsgTypeEnum;
import net.bncloud.information.Enum.OperStatusEnum;
import net.bncloud.information.entity.ZcInformationMsg;
import net.bncloud.information.feign.ZcContractServiceFeignClient;
import net.bncloud.information.feign.ZcDeliverServiceFeignClient;
import net.bncloud.information.feign.ZcOrderServiceFeignClient;
import net.bncloud.information.param.UpdateReadStatus;
import net.bncloud.information.param.ZcInformationMsgParam;
import net.bncloud.information.service.IZcInformationMsgService;
import net.bncloud.information.service.IZcInformationTagService;
import net.bncloud.information.vo.ZcInformationMsgVo;
import net.bncloud.information.vo.ZcInformationRouteParamVo;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 智采消息表 前端控制器
 * @author dr
 */
@RestController
@RequestMapping("/zc-information-msg")
@Slf4j
public class ZcInformationMsgController {
    @Resource
    private IZcInformationMsgService iZcInformationMsgService;
    @Resource
    private IZcInformationTagService iZcInformationTagService;



    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入ZcInformationMsg")
    public R save(@RequestBody SendMsgParam sendMsgParam){
        log.info("消息接收到了数据={}", JSONObject.toJSONString(sendMsgParam));
        //InformationParam param = new InformationParam();
        iZcInformationMsgService.saveInformationMsg(sendMsgParam);
        return R.success("操作成功");
    }

    /**
    * 通过id删除
    */
    @PutMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String[] ids){

//            String[] idsStrs = ids.split(",");
            for (String id : ids) {
                iZcInformationMsgService.removeById(Long.parseLong(id));
            }

        return R.success("操作成功");
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改读取状态", notes = "传入zcInformationMsg")
    public R updateById(@RequestBody UpdateReadStatus updateReadStatus){
        BaseUserEntity user = AuthUtil.getUser();
        String[] idsStrs = {};
        if (!StringUtil.isEmpty(updateReadStatus.getIds())){
            idsStrs = updateReadStatus.getIds().split(",");
        }
        // 全部已读
        if (updateReadStatus.getType()==null) {
//            List<ZcInformationMsg> list = iZcInformationMsgService.list(Condition.getQueryWrapper(new ZcInformationMsg().setGetUid(user.getUserId()).setMsgType(0)));
            List<ZcInformationMsg> list = iZcInformationMsgService.list(Condition.getQueryWrapper(new ZcInformationMsg()
                    .setMsgType(MsgTypeEnum.remind.getCode()).setGetUid(user.getUserId())
                    .setSystemType(SystemType.ZC.getCode())
                    .setTerminalType(TerminalType.PC.getCode())
                    .setIsRead(InformationReadStatusEnum.UNREAD.getCode())));
            for (ZcInformationMsg zcInformationMsg : list) {
                zcInformationMsg.setIsRead(InformationReadStatusEnum.READ.getCode());
                iZcInformationMsgService.updateById(zcInformationMsg);
            }
        }else if (updateReadStatus.getType() == 1){  //标记已读
            for (String id : idsStrs) {
                ZcInformationMsg byId = iZcInformationMsgService.getById(id);
                byId.setIsRead(InformationReadStatusEnum.READ.getCode());
                iZcInformationMsgService.updateById(byId);
            }
        }else if (updateReadStatus.getType() == 3){  //标记未读
            for (String id : idsStrs) {
                ZcInformationMsg byId = iZcInformationMsgService.getById(id);
                byId.setIsRead(InformationReadStatusEnum.UNREAD.getCode());
                iZcInformationMsgService.updateById(byId);
            }
        }
        return R.success("操作成功");
    }

    /**
     * 修改钉钉端消息
     */
    @PutMapping("/updateDD")
    @ApiOperation(value = "修改读取状态", notes = "传入zcInformationMsg")
    public R updateDDById(@RequestBody UpdateReadStatus updateReadStatus){
        BaseUserEntity user = AuthUtil.getUser();
        String[] idsStrs = {};
        if (!StringUtil.isEmpty(updateReadStatus.getIds())){
            idsStrs = updateReadStatus.getIds().split(",");
        }
        // 全部已读
        if (updateReadStatus.getType()==null) {
//            List<ZcInformationMsg> list = iZcInformationMsgService.list(Condition.getQueryWrapper(new ZcInformationMsg().setGetUid(user.getUserId()).setMsgType(0)));
            List<ZcInformationMsg> list = iZcInformationMsgService.list(Condition.getQueryWrapper(new ZcInformationMsg()
                    .setMsgType(MsgTypeEnum.remind.getCode())
                    .setGetUid(user.getUserId())
                    .setSystemType(SystemType.ZC.getCode())
                   .setTerminalType(TerminalType.DD.getCode())
                    .setIsRead(InformationReadStatusEnum.UNREAD.getCode())));
            for (ZcInformationMsg zcInformationMsg : list) {
                zcInformationMsg.setIsRead(InformationReadStatusEnum.READ.getCode());
                iZcInformationMsgService.updateById(zcInformationMsg);
            }
        }else if (updateReadStatus.getType() == 1){  //标记已读
            for (String id : idsStrs) {
                ZcInformationMsg byId = iZcInformationMsgService.getById(id);
                byId.setIsRead(InformationReadStatusEnum.READ.getCode());
                iZcInformationMsgService.updateById(byId);
            }
        }else if (updateReadStatus.getType() == 3){  //标记未读
            for (String id : idsStrs) {
                ZcInformationMsg byId = iZcInformationMsgService.getById(id);
                byId.setIsRead(InformationReadStatusEnum.UNREAD.getCode());
                iZcInformationMsgService.updateById(byId);
            }
        }
        return R.success("操作成功");
    }

    /**
     * 修改
     */
    @PutMapping("/updateEntity")
    @ApiOperation(value = "修改", notes = "传入zcInformationMsg")
    public R updateById(@RequestBody ZcInformationMsg zcInformationMsg){
        BaseUserEntity user = AuthUtil.getUser();
        zcInformationMsg.setLastModifiedDate(new Date());
        zcInformationMsg.setLastModifiedBy(user.getUserId());
        iZcInformationMsgService.updateById(zcInformationMsg);
        return R.success("操作成功");
    }

    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入ZcInformationMsg")
    public R<ZcInformationMsg> getById(@PathVariable(value = "id") Long id){
        return R.data(iZcInformationMsgService.getById(id));
    }

    /**
     * 通过id查询
     */
    @GetMapping("/getHandleUrl/{id}")
    @ApiOperation(value = "获取路由办理", notes = "传入ZcInformationMsg")
    public R<Object> getHandleUrl(@PathVariable(value = "id") Long id){
//        ZcInformationMsg zcInformationMsg1 = iZcInformationMsgService.getById(id);
//        zcInformationMsg1.setIsRead(InformationReadStatusEnum.READ.getCode());
//        iZcInformationMsgService.updateById(zcInformationMsg1);
        List<ZcInformationRouteParamVo> zcInformationMsg = iZcInformationMsgService.getHandleUrl(id);

        return R.data(zcInformationMsg);
    }


//    /**
//     * 办理
//     */
//    @GetMapping("/handle/{id}")
//    @ApiOperation(value = "根据ID查询", notes = "传入ZcInformationMsg")
//    public R<String> handle(@PathVariable(value = "id") Long id){
//
//        ZcInformationMsg zcInformationMsg = iZcInformationMsgService.getById(id);
//        ZcInformationTagVo tag = iZcInformationTagService.findByTag(zcInformationMsg.getTag());
//
//        return R.data(iZcInformationMsgService.getById(id));
//    }

    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入zcInformationMsg")
    public R list(@RequestBody ZcInformationMsg zcInformationMsg ){
        List<ZcInformationMsg> list = iZcInformationMsgService.list(Condition.getQueryWrapper(zcInformationMsg));
        return R.data(list);
    }

    /**
     * 统计查询列表
     */
    @PostMapping("/count")
    @ApiOperation(value = "统计查询列表", notes = "传入zcInformationMsg")
    public R count(@RequestBody ZcInformationMsg zcInformationMsg ){

        int count = iZcInformationMsgService.count(Condition.getQueryWrapper(zcInformationMsg));
        return R.data(count);
    }


    /**
     * 消息数量
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "消息数量", notes = "传入DeliveryNoteParam")
    public R statistics(){
        BaseUserEntity user = AuthUtil.getUser();
        Long userId = user.getUserId();

        String terminalType = TerminalType.PC.getCode();
        String sendType = SubjectType.sup.getCode();

        return R.data(
                iZcInformationMsgService.statisticsOrg(userId,sendType,terminalType,user.getCurrentOrg().getId())
        );
    }

    /**
     * 消息数量
     * @return
     */
    @GetMapping("/statisticsDD")
    @ApiOperation(value = "消息数量", notes = "传入DeliveryNoteParam")
    public R statisticsDD(){
        BaseUserEntity user = AuthUtil.getUser();
        Long userId = user.getUserId();
        String sendType = SubjectType.sup.getCode();
        String terminalType = TerminalType.DD.getCode();


        return R.data(
                iZcInformationMsgService.statisticsOrg(userId,sendType,terminalType,user.getCurrentOrg().getId())
        );
    }

    /**
     * 待办消息数量
     * @return
     */
    @GetMapping("/waitHandle")
    @ApiOperation(value = "待办消息数量", notes = "传入DeliveryNoteParam")
    public R waitHandle(){

        BaseUserEntity user = AuthUtil.getUser();
        Long userId = user.getUserId();
        String terminalType = TerminalType.DD.getCode();
        String sendType = SubjectType.sup.getCode();

        return R.data(
                iZcInformationMsgService.waitHandle(userId,sendType,terminalType)
        );
    }

    /**
     * 消息查看
     * @return
     */
    @GetMapping("/changeStatus/{id}")
    @ApiOperation(value = "消息查看", notes = "传入DeliveryNoteParam")
    public R changeStatus(@PathVariable("id") Long id){
        iZcInformationMsgService.changeStatus(id);
        return R.data(true);

    }


    /**
    * zc分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入ZcInformationMsgParam")
    public R page(Pageable pageable, @RequestBody QueryParam<ZcInformationMsgParam> queryParam){

		//TODO:目前只能获取当前登录者的uid，还待公共模块整改，加入供应商的一些信息，这里同步更改
        BaseUserEntity user = AuthUtil.getUser();
        if (user==null) {
            throw new RuntimeException("用户信息异常");
        }

        ZcInformationMsgParam param = queryParam.getParam();
        //TODO:还待公共模块整改，才可放开此行代码，目前注释掉方便测试
        param.setGetUid(user.getUserId());
        Optional<Org> org = Optional.ofNullable(user.getCurrentOrg());
        if (org.isPresent()) {
            param.setReceiverSubjectCode(org.get().getId().toString());
        }else{
            throw new ApiException(500,"无当前组织");
        }



        param.setSendType(SubjectType.sup.getCode());
        param.setTerminalType(TerminalType.PC.getCode());
        if (MsgTypeEnum.handle.getCode().equals(queryParam.getParam().getMsgType())) {
            param.setOperStatus(OperStatusEnum.UNHANDLED.getCode());
        }
        queryParam.setParam(param);



        IPage<ZcInformationMsgVo> zcInformationMsgVoIPage = iZcInformationMsgService.selectPage(PageUtils.toPage(pageable), queryParam);


		return R.data(PageUtils.result(zcInformationMsgVoIPage));
    }



    @Autowired
    private ZcContractServiceFeignClient zcContractFeignClient;
    @Autowired
    private ZcDeliverServiceFeignClient zcDeliverServiceFeignClient;
    @Autowired
    private ZcOrderServiceFeignClient zcOrderServiceFeignClient;

    @GetMapping("/pageTotal")
    public R<Object> pageTotal(){
        R resultContract = zcContractFeignClient.count();
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        Integer contractCount=0 ;
        Integer deliverCount=0 ;
        Integer orderCount=0 ;
        Integer statement=0;
        if (resultContract.isSuccess()&&resultContract.getCode()==200) {
            LinkedHashMap<String,Integer> zcContract = (LinkedHashMap<String,Integer>)resultContract.getData();
            Iterator iter = zcContract.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                contractCount+=(Integer)entry.getValue();
            }
        }
        stringIntegerHashMap.put("zcContract",contractCount);
        R resultDeliver =  zcDeliverServiceFeignClient.getMsgCount();

        if (resultDeliver.isSuccess()&&resultDeliver.getCode()==200) {
            LinkedHashMap<String,Integer> zcContract = (LinkedHashMap<String,Integer>)resultDeliver.getData();
            Iterator iter = zcContract.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                deliverCount+=(Integer)entry.getValue();
            }

        }
        stringIntegerHashMap.put("zcDeliver",deliverCount);
        R resultOrder =  zcOrderServiceFeignClient.getMsgCount();

        if (resultOrder.isSuccess()&&resultOrder.getCode()==200) {
            LinkedHashMap<String,Integer> zcContract = (LinkedHashMap<String,Integer>)resultOrder.getData();
            Iterator iter = zcContract.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                orderCount+=(Integer)entry.getValue();
            }
        }
        stringIntegerHashMap.put("zcOrder",orderCount);
        stringIntegerHashMap.put("statement",statement);
        R<HashMap<String,Integer>>  map =new R<HashMap<String,Integer>>();
        map.setData(stringIntegerHashMap);
        return R.data(map);
    }

    /**
     * zc
     * 分页查询
     */
    @PostMapping("/pageDDTerminal")
    @ApiOperation(value = "查询列表", notes = "传入ZcInformationMsgParam")
    public R pageDDTerminal(Pageable pageable, @RequestBody QueryParam<ZcInformationMsgParam> queryParam){

        //TODO:目前只能获取当前登录者的uid，还待公共模块整改，加入供应商的一些信息，这里同步更改
        BaseUserEntity user = AuthUtil.getUser();
        ZcInformationMsgParam param = queryParam.getParam();
//        param.setSystemType(main.java.net.bncloud.enums.SystemType.ZC.getCode());
        param.setSendType(SubjectType.sup.getCode());
        param.setTerminalType(TerminalType.DD.getCode());
        param.setReceiverSubjectCode(user.getCurrentOrg().getId().toString());
        //TODO:还待公共模块整改，才可放开此行代码，目前注释掉方便测试
        param.setGetUid(user.getUserId());
        //TODO:查询待办
        if (MsgTypeEnum.handle.getCode().equals(queryParam.getParam().getMsgType())) {
            param.setOperStatus(OperStatusEnum.UNHANDLED.getCode());
        }

        queryParam.setParam(param);
        IPage<ZcInformationMsgVo> zcInformationMsgVoIPage = iZcInformationMsgService.selectPage(PageUtils.toPage(pageable), queryParam);
        return R.data(PageUtils.result(zcInformationMsgVoIPage));
    }



}
