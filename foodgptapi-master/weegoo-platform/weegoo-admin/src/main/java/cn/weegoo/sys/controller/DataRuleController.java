/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cn.weegoo.aop.logging.annotation.ApiLog;
import cn.weegoo.core.query.QueryWrapperGenerator;
import cn.weegoo.sys.constant.CommonConstants;
import cn.weegoo.sys.domain.DataRule;
import cn.weegoo.sys.domain.Menu;
import cn.weegoo.sys.service.DataRuleService;
import cn.weegoo.sys.service.MenuService;
import cn.weegoo.sys.service.dto.DataRuleDTO;
import cn.weegoo.sys.service.dto.MenuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 数据权限Controller
 *
 * @author lgf
 * @version 2021-04-02
 */

@RestController
@RequestMapping("/sys/dataRule")
public class DataRuleController {

    @Autowired
    private DataRuleService dataRuleService;

    @Autowired
    private MenuService menuService;

    /**
     * 查询数据权限列表数据
     *
     * @param dataRule
     * @param page
     * @return
     * @throws Exception
     */
    @ApiLog("查询数据权限列表")
    @GetMapping("list")
    public ResponseEntity <IPage <DataRule>> data(DataRule dataRule, Page <DataRule> page) throws Exception {
        QueryWrapper <DataRule> queryWrapper = QueryWrapperGenerator.buildQueryCondition ( dataRule, DataRule.class );
        IPage <DataRule> result = dataRuleService.page ( page, queryWrapper );
        return ResponseEntity.ok ( ).body ( result );
    }

    /**
     * 根据id查询数据权限
     *
     * @param id
     * @return
     */
    @ApiLog("查询数据权限")
    @GetMapping("queryById")
    public ResponseEntity <DataRule> queryById(String id) {
        DataRule dataRule = dataRuleService.getById ( id );
        return ResponseEntity.ok ( ).body ( dataRule );
    }

    /**
     * 保存数据权限
     *
     * @param dataRule
     * @return
     */
    @ApiLog("保存数据权限")
    @PostMapping("save")
    public ResponseEntity <String> save(@RequestBody DataRule dataRule) {
        dataRuleService.saveOrUpdate ( dataRule );//保存
        return ResponseEntity.ok ( "保存数据权限成功!" );
    }

    /**
     * 删除数据权限
     *
     * @param id
     * @return
     */
    @ApiLog("删除数据权限")
    @DeleteMapping("delete")
    public ResponseEntity <String> delete(String id) {
        dataRuleService.delete ( id );
        return ResponseEntity.ok ( "删除数据权限成功" );
    }


    public List <Map <String, Object>> getTreeMenu(List <Map <String, Object>> list) {
        List rootTree = Lists.newArrayList ( );
        Menu menu = menuService.getById ( "1" );
        Map <String, Object> map = Maps.newHashMap ( );
        map.put ( "id", menu.getId ( ) );
        map.put ( "name", menu.getName ( ) );
        map.put ( "parentId", "0" );
        rootTree.add ( getChildOfTree ( map, list ) );
        return rootTree;
    }

    private Map <String, Object> getChildOfTree(Map <String, Object> menuItem, List <Map <String, Object>> menuList) {
        menuItem.put ( "children", Lists.newArrayList ( ) );
        for (Map <String, Object> child : menuList) {
            if ( child.get ( "parentId" ).equals ( menuItem.get ( "id" ) ) ) {
                ((List) menuItem.get ( "children" )).add ( getChildOfTree ( child, menuList ) );
            }
        }
        return menuItem;
    }

    /**
     * 查询数据权限树表数据
     *
     * @return
     */
    @ApiLog("查询数据权限树表数据")
    @GetMapping("treeData")
    public ResponseEntity <List> treeData() {
        List <Map <String, Object>> mapList = Lists.newArrayList ( );
        List <MenuDTO> list = menuService.findAllWithDataRuleList ( );
        HashSet <String> existIdSet = new HashSet <String> ( );
        for (int i = 0; i < list.size ( ); i++) {
            MenuDTO menuDTO = list.get ( i );
            if ( menuDTO.getIsShow ( ).equals ( CommonConstants.HIDE ) ) {
                continue;
            }

            Map <String, Object> map = Maps.newHashMap ( );
            map.put ( "id", menuDTO.getId ( ) );
            map.put ( "name", menuDTO.getName ( ) );
            map.put ( "parentId", menuDTO.getParentId ( ) );
            map.put ( "parentIds", menuDTO.getParentIds ( ) );
            if ( StrUtil.isNotBlank ( menuDTO.getIcon ( ) ) ) {
                map.put ( "icon", menuDTO.getIcon ( ) );
            }

            boolean existDataRule = false;
            List <DataRuleDTO> dataRuleDTOList = menuDTO.getDataRuleDTOList ( );
            for (DataRuleDTO dataRuleDTO : dataRuleDTOList) {
                Map <String, Object> dataRuleMap = Maps.newHashMap ( );
                dataRuleMap.put ( "id", "dataRule-" + dataRuleDTO.getId ( ) );
                dataRuleMap.put ( "name", dataRuleDTO.getName ( ) );
                dataRuleMap.put ( "parentId", dataRuleDTO.getMenuId ( ) );
                dataRuleMap.put ( "parentIds", dataRuleDTO.getMenuId ( ) );
                mapList.add ( dataRuleMap );
                existDataRule = true;
            }
            if ( existDataRule ) {
                if ( !existIdSet.contains ( menuDTO.getId ( ) ) ) {
                    mapList.add ( map );
                    existIdSet.add ( menuDTO.getId ( ) );
                }

                String[] parentIds = (menuDTO.getParentIds ( ) == null ? new String[0] : menuDTO.getParentIds ( ).split ( "," ));
                for (String parentId : parentIds) {
                    if ( !existIdSet.contains ( parentId ) ) {
                        existIdSet.add ( parentId );
                        Menu parentMenu = menuService.getById ( parentId );
                        if ( parentMenu == null ) {
                            continue;
                        }
                        Map <String, Object> parentMenuMap = Maps.newHashMap ( );
                        parentMenuMap.put ( "id", parentId );
                        parentMenuMap.put ( "name", parentMenu.getName ( ) );
                        parentMenuMap.put ( "parentId", parentMenu.getParentId ( ) );
                        parentMenuMap.put ( "parentIds", parentMenu.getParentIds ( ) );
                        mapList.add ( parentMenuMap );

                    }
                }

            }

        }

        List rootTree = getTreeMenu ( mapList );
        return ResponseEntity.ok ( rootTree );
    }


}
