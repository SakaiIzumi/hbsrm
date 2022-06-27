package net.bncloud.saas.sys.web;

import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.sys.domain.Currency;
import net.bncloud.saas.sys.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author ddh
 * @version 1.0.0
 * @description 币种前端控制器
 * @since 2022/1/5
 */
@RestController
@RequestMapping("/sys/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;


    /**
     * 分页查询币种
     *
     * @param pageable   page=1&size=10
     * @param queryParam 币种的通用查询
     * @return 分页对象
     */
    @PostMapping("/page")
    @ResponseBody
    public R<Page<Currency>> getCurrencyList(Pageable pageable, @RequestBody QueryParam<Currency> queryParam) {
        return R.data(currencyService.page(pageable, queryParam));
    }


}
