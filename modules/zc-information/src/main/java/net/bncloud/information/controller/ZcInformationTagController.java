package net.bncloud.information.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.BeanUtilTwo;
import net.bncloud.common.util.StringUtil;
import net.bncloud.information.Enum.InformationStatusEnum;
import net.bncloud.information.entity.ZcInformationRoute;
import net.bncloud.information.entity.ZcInformationTag;
import net.bncloud.information.param.ZcInformationMsgParam;
import net.bncloud.information.param.ZcInformationTagParam;
import net.bncloud.information.service.IZcInformationRouteService;
import net.bncloud.information.service.IZcInformationTagService;
import net.bncloud.information.vo.ZcInformationTagVo;
import net.bncloud.information.wrapper.ZcInformationTagWrapper;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 智采消息标签 前端控制器
 * @author dr
 */
@RestController
@RequestMapping("/zc-information-tag")
public class ZcInformationTagController {
    @Resource
    private IZcInformationTagService iZcInformationTagService;
    @Resource
    private IZcInformationRouteService iZcInformationRouteService;
    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入ZcInformationTag")
    public R save(@RequestBody ZcInformationTagParam zcInformationTag){
        String tag = BeanUtilTwo.randomString(8);
        ZcInformationTag queryParam = new ZcInformationTag();
        queryParam.setTag(tag);
        queryParam.setStatus(InformationStatusEnum.invalid.getCode());
        queryParam.setIsDeleted(0);
        int i = iZcInformationTagService.count(Condition.getQueryWrapper(queryParam));
        if(i>0){
            return R.success("标签编号重复，请重新添加");
        }

        BaseUserEntity user = AuthUtil.getUser();
        zcInformationTag.setTag(tag);
        zcInformationTag.setIsDeleted(0);
        zcInformationTag.setCreatedDate(new Date());
        zcInformationTag.setCreatedBy(user.getUserId());
        zcInformationTag.setLastModifiedDate(new Date());
        zcInformationTag.setLastModifiedBy(user.getUserId());

        try {
            JSONObject jsonObject = JSONObject.parseObject(zcInformationTag.getMsgTemplate());
            String MessageValue= jsonObject.get("消息内容").toString();
            zcInformationTag.setMessageTemplate(MessageValue);
        } catch (Exception e) {
            return R.success("参数模板格式有误");

        }


        iZcInformationTagService.save(zcInformationTag);

        for (ZcInformationRoute zcInformationRoute : zcInformationTag.getRouteList()) {
            zcInformationRoute.setTagId(zcInformationTag.getId());
            zcInformationRoute.setDisabled(zcInformationRoute.getDisabled()==null?false:zcInformationRoute.getDisabled());
            iZcInformationRouteService.save(zcInformationRoute);
        }





        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String ids){
        String[] idsStrs = ids.split(",");
        for (String id:idsStrs){
            iZcInformationTagService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入zcInformationTag")
    public R updateById(@RequestBody ZcInformationTagParam zcInformationTag){
        BaseUserEntity user = AuthUtil.getUser();
        zcInformationTag.setLastModifiedDate(new Date());
        zcInformationTag.setLastModifiedBy(user.getUserId());
        //添加路由
        try {
            JSONObject jsonObject = JSONObject.parseObject(zcInformationTag.getMsgTemplate());
            String MessageValue= jsonObject.get("消息内容").toString();
            zcInformationTag.setMessageTemplate(MessageValue);
        } catch (Exception e) {
            throw new ApiException(500,"参数模板格式有误");
//            return R.success("参数模板格式有误");

        }


        if (zcInformationTag.getRouteList()!=null) {
            List<String> terminalType = new ArrayList<String>();
            for (ZcInformationRoute zcInformationRoute : zcInformationTag.getRouteList()) {
                //获取模板里面@@参数 与 参数是否匹配
                if (zcInformationRoute.getId()==null) {
                    zcInformationRoute.setTagId(zcInformationTag.getId());
                    zcInformationRoute.setDisabled(zcInformationRoute.getDisabled()==null?false:zcInformationRoute.getDisabled());
                    iZcInformationRouteService.save(zcInformationRoute);
                }else{
                    iZcInformationRouteService.updateById(zcInformationRoute);
                }
                //设置展示
                if(zcInformationRoute.getDisabled()!=null&&!zcInformationRoute.getDisabled()){
                    terminalType.add(zcInformationRoute.getRouteType());
                }


            }
            zcInformationTag.setTerminalType(String.join(",", terminalType));
        }

        iZcInformationTagService.updateById(zcInformationTag);





        return R.success();
    }

    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入ZcInformationTag")
    public R<ZcInformationTag> getById(@PathVariable(value = "id") Long id){
        ZcInformationTag zcInformationTag = iZcInformationTagService.getById(id);
        ZcInformationRoute queryParam = new ZcInformationRoute();
        queryParam.setTagId(id);
        List<ZcInformationRoute> list = iZcInformationRouteService.list(Condition.getQueryWrapper(queryParam));
        ZcInformationTagVo zcInformationTagVo = BeanUtil.copy(zcInformationTag,ZcInformationTag.class,ZcInformationTagVo.class);
        zcInformationTagVo.setRouteList(list);

        return R.data(zcInformationTagVo);
    }

    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入zcInformationTag")
    public R list(@RequestBody ZcInformationTag zcInformationTag ){
        List<ZcInformationTag> list = iZcInformationTagService.list(Condition.getQueryWrapper(zcInformationTag));
        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入ZcInformationTagParam")
    public R page(Pageable pageable, @RequestBody QueryParam<ZcInformationTagParam> queryParam){

        //TODO:目前只能获取当前登录者的uid，还待公共模块整改，加入供应商的一些信息，这里同步更改
        BaseUserEntity user = AuthUtil.getUser();
        ZcInformationTagParam param = queryParam.getParam();
        queryParam.setParam(param);

        IPage<ZcInformationTagVo> zcInformationMsgVoIPage = iZcInformationTagService.selectPage(PageUtils.toPage(pageable), queryParam);

		return R.data(PageUtils.result(zcInformationMsgVoIPage));
    }

    public static void main(String[] args) {


        String str="{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"客户：@customerName@于@addTime@，撤回合同：@contractCode@。请及时查看234563233232131231231。\"}";

        cutString(str, "@", "#");
    }

    public static String cutString(String str, String start, String end) {
        if (StringUtil.isBlank(str)) {
            return str;
        }
        String reg= start + "(.*)" + end;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            str = matcher.group(1);
            System.out.println(str);
        }
        return str;
    }

}