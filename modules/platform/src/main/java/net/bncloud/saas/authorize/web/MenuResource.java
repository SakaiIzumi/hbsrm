package net.bncloud.saas.authorize.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.api.R;
import net.bncloud.common.constants.SysType;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.authorize.domain.Menu;
import net.bncloud.saas.authorize.service.MenuService;
import net.bncloud.saas.authorize.service.dto.MenuDTO;
import net.bncloud.saas.authorize.service.query.MenuQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/authorize/menu")
public class MenuResource {

    private final MenuService menuService;

    public MenuResource(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiOperation("返回全部的菜单")
    @GetMapping("/all")
    public R<List<MenuDTO>> all(MenuQuery query) {
        List<MenuDTO> menuDTOS = menuService.queryAll(query);
        return R.data(menuService.buildTree(menuDTOS));
    }

    @ApiOperation("查询菜单")
    @GetMapping
    public R<Page<MenuDTO>> query(MenuQuery criteria, Pageable pageable) throws Exception {
        Page<MenuDTO> menuDtoList = menuService.pageQuery(criteria, pageable);
        return R.data(menuDtoList);
    }


    @ApiOperation("新增菜单")
    @PostMapping
    public ResponseEntity<Void> create(@Validated @RequestBody Menu resources){
        if (resources.getId() != null) {
            throw new IllegalArgumentException("id 不能为空");
        }
        menuService.createMenu(resources);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("修改菜单")
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Menu resources){
        menuService.updateMenu(resources);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        menuService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/current")
    public Set<Menu> currentUser() {
        return SecurityUtils.getLoginInfo()
                .map(LoginInfo::getId)
                .map(menuService::findByUser).orElse(null);
    }
    
    /**
     * zc查询全部菜单树
     * @param query
     * @return
     */
    @GetMapping("/currentMenuTreeToZc")
    public R currentMenuTreeToZc(MenuQuery query) {
        query.setSysType(SysType.ZC);
        List<MenuDTO> menuDTOS = menuService.queryAll(query);
        return R.data(menuService.buildTree(menuDTOS));
    }
    
    /**
     * zc查询全部菜单树
     * @param query
     * @return
     */
    @GetMapping("/currentMenuTreeToZy")
    public R currentMenuTreeToZy(MenuQuery query) {
        query.setSysType(SysType.ZY);
        List<MenuDTO> menuDTOS = menuService.queryAll(query);
        return R.data(menuService.buildTree(menuDTOS));
    }
    
    /**
     * 查询全部菜单树
     * @param query
     * @return
     */
    @GetMapping("/currentMenuTree")
    public R currentMenuTree(MenuQuery query) {
        List<MenuDTO> menuDTOS = menuService.queryAll(query);
        return R.data(menuService.buildTree(menuDTOS));
    }


    @ApiOperation(value = "按类型查询默认菜单")
    @GetMapping("/menuTree/{subjectType}")
    public R menuTree(@PathVariable SubjectType subjectType) {
        return R.data(menuService.menuTreeBySubjectType(subjectType));
    }
}
