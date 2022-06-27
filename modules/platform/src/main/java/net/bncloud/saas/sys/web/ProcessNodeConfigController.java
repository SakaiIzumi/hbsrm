package net.bncloud.saas.sys.web;

import net.bncloud.common.api.R;
import net.bncloud.saas.sys.domain.ProcessNodeConfig;
import net.bncloud.saas.sys.service.ProcessNodeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ddh
 * @version 1.0.0
 * @description 流程节点配置-流程模板信息
 * @since 2022/1/4
 */
@RestController
@RequestMapping("/process-node-config")
public class ProcessNodeConfigController {

    @Autowired
    private ProcessNodeConfigService processNodeConfigService;


    /**
     * 查询参数内容中的流程节点设置列表
     * @param id   参数id
     * @return
     */
    @GetMapping("/getList/{id}")
    @ResponseBody
    public R getList(@PathVariable("id") Long id){
        List<ProcessNodeConfig> processNodeConfigList = processNodeConfigService.getProcessNodeConfigList(id);
        return R.data(processNodeConfigList);
    }


    /**
     * 修改流程节点设置
     * @param processNodeConfig
     * @return
     */
    @PutMapping("/edit")
    public R editProcessNodeConfig(@RequestBody ProcessNodeConfig processNodeConfig){
        processNodeConfigService.edit(processNodeConfig);
        return R.success();
    }

}
