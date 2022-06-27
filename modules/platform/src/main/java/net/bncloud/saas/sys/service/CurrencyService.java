package net.bncloud.saas.sys.service;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.sys.domain.Currency;
import net.bncloud.saas.sys.repository.CurrencyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/5
 */
@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;


    /**
     * 分页查询币种
     * @param pageable  page=1&size=10
     * @param param 币种对象
     * @return 币种的分页对象
     */
    public Page<Currency> page(Pageable pageable, QueryParam<Currency> param) {
        String searchValue = param.getSearchValue();
        Currency currency = param.getParam();
        Page<Currency> currencyPage = currencyRepository.findAll(new Specification<Currency>() {
            @Override
            public Predicate toPredicate(Root<Currency> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                ArrayList<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotBlank(currency.getCurrencyCode())) {
                    Predicate p1 = cb.equal(root.get("currencyCode").as(String.class), currency.getCurrencyCode());
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(currency.getCurrencyName())) {
                    Predicate p2 = cb.equal(root.get("currencyName").as(String.class), currency.getCurrencyName());
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(searchValue)) {
                    Predicate p3 = cb.like(root.get("currencyCode").as(String.class), "%"+searchValue+"%");
                    Predicate p4 = cb.like(root.get("currencyName").as(String.class), "%"+searchValue+"%");
                    Predicate p5 = cb.like(root.get("englishName").as(String.class), "%"+searchValue+"%");
                    Predicate p6 = cb.or(p3, p4, p5);
                    list.add(p6);
                }
                query.where(list.toArray(new Predicate[list.size()]));
                return query.getRestriction();
            }
        }, pageable);
        return currencyPage;
    }


}
