/**
 * Copyright &copy; 2021-2025 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.controller.app;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cn.weegoo.sys.controller.UserController;
import cn.weegoo.sys.domain.Office;
import cn.weegoo.sys.domain.User;
import cn.weegoo.sys.service.OfficeService;
import cn.weegoo.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author weegoo
 * @version 2021-8-29
 */
@RestController
@RequestMapping("/app/sys/user")
public class AppUserController extends UserController {

    @Autowired
    OfficeService officeService;
    @Autowired
    UserService userService;

    /**
     * 获取机构JSON数据。
     *
     * @return
     */
    @GetMapping("treeData")
    public ResponseEntity treeData() {
        List <Office> list = officeService.list ( );
        List rootTree = getRootTree ( list );
        return ResponseEntity.ok ( rootTree );
    }


    private List <Map> getRootTree(List <Office> list) {
        List <Map> offices = Lists.newArrayList ( );
        List <Office> rootTrees = officeService.treeData ( );
        for (Office root : rootTrees) {
            offices.add ( getChildOfTree ( root, list ) );
        }
        return offices;
    }

    private Map getChildOfTree(Office officeItem, List <Office> officeList) {
        Map oMap = new HashMap ( );
        oMap.put ( "id", "o_" + officeItem.getId ( ) );
        oMap.put ( "type", "office" );
        oMap.put ( "label", officeItem.getName ( ) );
        List children = Lists.newArrayList ( );
        oMap.put ( "children", children );
        List <User> list = userService.lambdaQuery ( ).eq ( User::getOfficeId, officeItem.getId ( ) ).list ( );
        for (int i = 0; i < list.size ( ); i++) {
            User e = list.get ( i );
            Map <String, Object> map = Maps.newHashMap ( );
            map.put ( "id", e.getId ( ) );
            map.put ( "type", "user" );
            map.put ( "label", e.getName ( ) );
            children.add ( map );
        }
        for (Office child : officeList) {
            if ( child.getParentId ( ).equals ( officeItem.getId ( ) ) ) {
                children.add ( getChildOfTree ( child, officeList ) );
            }
        }
        return oMap;
    }


}
