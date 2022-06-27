package net.bncloud.saas.sys.service;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.security.BncUserDetails;
import net.bncloud.common.security.Company;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.saas.sys.domain.SearchHistory;
import net.bncloud.saas.sys.repository.SearchHistoryRepository;
import net.bncloud.saas.sys.service.command.ClearSearchHistoryCommand;
import net.bncloud.saas.sys.service.command.CreateSearchHistoryCommand;
import net.bncloud.saas.sys.service.query.SearchHistoryQuery;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * @ClassName SearchHistoryService
 * @Description: 搜索历史service
 * @Author Administrator
 * @Date 2021/5/10
 * @Version V1.0
 **/
@Service
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }


    public Page<SearchHistory> pageQuery(QueryParam<SearchHistoryQuery> query, Pageable page) {
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize(),Sort.by(new Sort.Order(Sort.Direction.DESC,"createdDate")));
        return searchHistoryRepository.findAll(buildSpecification(query),pageable);
    }

    private Specification<SearchHistory> buildSpecification(QueryParam<SearchHistoryQuery> query){
        SearchHistoryQuery param = query.getParam();
        Company company = SecurityUtils.getCurrentCompany().orElse(null);
        BncUserDetails bncUserDetails = SecurityUtils.getCurrentUser().orElse(null);
        SecurityUtils.getLoginInfo().ifPresent(loginInfo -> param.setPlatform(loginInfo.getPlatform()));
        return (Specification<SearchHistory>) (root, criteriaQuery, criteriaBuilder) -> {
            final Predicate predicate = criteriaBuilder.conjunction();
            if(company != null){
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("companyId"),company.getId()));
            }
            if(bncUserDetails != null){
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("userId"),bncUserDetails.getId()));
            }
            if(param==null){
                return predicate;
            }

            if(param.getPlatform() !=null){
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("platform"),param.getPlatform()));
            }

            if(param.getModule() != null){
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("module"),param.getModule()));
            }
            return predicate;
        };
    }

    public void save(CreateSearchHistoryCommand command) {
        SecurityUtils.getCurrentUser().ifPresent(bncUserDetails -> command.setUserId(bncUserDetails.getId()));
        SecurityUtils.getCurrentCompany().ifPresent(company -> command.setCompanyId(company.getId()));
        SecurityUtils.getLoginInfo().ifPresent(loginInfo -> command.setPlatform(loginInfo.getPlatform()));
        SearchHistory searchHistory = command.create();
        searchHistory.setCreatedDate(null);
        searchHistory.setLastModifiedDate(null);
        boolean exists = searchHistoryRepository.exists(Example.of(searchHistory));
        if(exists){
          return;
        }
        searchHistoryRepository.save(command.create());
    }

    @Transactional
    public void clear(ClearSearchHistoryCommand command) {
        SecurityUtils.getCurrentUser().ifPresent(bncUserDetails -> command.setUserId(bncUserDetails.getId()));
        SecurityUtils.getCurrentCompany().ifPresent(company -> command.setCompanyId(company.getId()));
        SecurityUtils.getLoginInfo().ifPresent(loginInfo -> command.setPlatform(loginInfo.getPlatform()));
        SearchHistory searchHistory = command.create(true);
        List<SearchHistory> all = searchHistoryRepository.findAll(Example.of(searchHistory));
        if(CollectionUtil.isNotEmpty(all)){
            for (SearchHistory history : all) {
                searchHistoryRepository.deleteById(history.getId());
            }
        }

    }
}
