/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import cn.weegoo.aop.demo.annotation.DemoMode;
import cn.weegoo.aop.logging.annotation.ApiLog;
import cn.weegoo.common.utils.ResponseUtil;
import cn.weegoo.core.excel.ExcelOptions;
import cn.weegoo.core.excel.annotation.ExportMode;
import cn.weegoo.core.query.QueryWrapperGenerator;
import cn.weegoo.security.util.SecurityUtils;
import cn.weegoo.sys.domain.User;
import cn.weegoo.sys.service.UserService;
import cn.weegoo.sys.service.dto.UserDTO;
import cn.weegoo.sys.service.mapstruct.UserWrapper;
import cn.weegoo.sys.utils.DictUtils;
import cn.weegoo.sys.utils.MenuUtils;
import cn.weegoo.sys.utils.RouterUtils;
import cn.weegoo.sys.utils.UserUtils;
import cn.weegoo.sys.utils.excel.UserEasyExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author weegoo
 * @version 2021-8-29
 */

@Api(tags = "用户管理")
@RestController
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @ApiLog("查询用户")
    @GetMapping("queryById")
    @ApiOperation(value = "查询用户")
    public ResponseEntity queryById(@RequestParam("id") String id) {
        UserDTO userDTO = userService.get ( id );
        return ResponseEntity.ok ( userDTO );
    }

    /**
     * 查询列表
     *
     * @param userDTO
     * @param page
     * @return
     */
    @ApiLog("用户数据列表")
    @ApiOperation(value = "用户数据列表")
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("list")
    public ResponseEntity list(UserDTO userDTO, Page <UserDTO> page) throws Exception {
        QueryWrapper <UserDTO> queryWrapper = QueryWrapperGenerator.buildQueryCondition ( userDTO, UserDTO.class );
        IPage <UserDTO> result = userService.findPage ( page, queryWrapper );
        return ResponseEntity.ok ( result );
    }

    /**
     * 保存用户
     *
     * @param userDTO
     * @return
     */
    @DemoMode
    @ApiLog("保存用户")
    @PreAuthorize("hasAnyAuthority('sys:user:add', 'sys:user:edit')")
    @ApiOperation(value = "保存用户")
    @PostMapping("save")
    public ResponseEntity save(@Valid @RequestBody UserDTO userDTO) {
        // 如果新密码为空，则不更换密码
        if ( StrUtil.isNotBlank ( userDTO.getNewPassword ( ) ) ) {
            userDTO.setPassword ( SecurityUtils.encryptPassword ( userDTO.getNewPassword ( ) ) );
        }
        if ( !UserUtils.isCheckLoginName ( userDTO.getOldLoginName ( ), userDTO.getLoginName ( ) ) ) {
            return ResponseEntity.badRequest ( ).body ( "保存用户'" + userDTO.getLoginName ( ) + "'失败，登录名已存在!" );
        }
        // 保存用户信息
        userService.saveOrUpdate ( userDTO );
        return ResponseEntity.ok ( "保存用户'" + userDTO.getLoginName ( ) + "'成功!" );
    }


    /**
     * 用户信息显示编辑保存
     *
     * @param userDTO
     * @return
     */
    @ApiLog("修改个人资料")
    @ApiOperation(value = "修改个人资料")
    @PostMapping("saveInfo")
    public ResponseEntity saveInfo(@RequestBody UserDTO userDTO) {
        userService.updateById ( UserWrapper.INSTANCE.toEntity ( userDTO ) );
        UserUtils.deleteCache ( UserUtils.getCurrentUserDTO ( ) );
        return ResponseEntity.ok ( "修改个人资料成功!" );
    }

    /**
     * 批量删除用户
     */
    @DemoMode
    @ApiLog("删除用户")
    @ApiOperation(value = "删除用户")
    @PreAuthorize("hasAuthority('sys:user:del')")
    @DeleteMapping("delete")
    public ResponseEntity delete(String ids) {
        String idArray[] = ids.split ( "," );
        StringBuffer msg = new StringBuffer ( );
        boolean success = true;
        for (String id : idArray) {
            UserDTO userDTO = userService.get ( id );
            if ( UserUtils.getCurrentUserDTO ( ).getId ( ).equals ( userDTO.getId ( ) ) ) {
                success = false;
                msg.append ( "[" + userDTO.getLoginName ( ) + "]删除失败，不允许删除当前用户!<br/>" );
            } else if ( userDTO.isAdmin ( ) ) {
                success = false;
                msg.append ( "[" + userDTO.getLoginName ( ) + "]删除失败，不允许删除超级管理员!<br/>" );//删除用户失败, 不允许删除超级管理员用户
            } else {
                msg.append ( "[" + userDTO.getLoginName ( ) + "]删除成功!<br/>" );
                userService.deleteUser ( userDTO );//删除用户成功
            }
        }
        if ( success ) {
            return ResponseEntity.ok ( msg.toString ( ) );
        } else {
            return ResponseEntity.badRequest ( ).body ( msg.toString ( ) );
        }
    }

    /**
     * 导出用户数据
     *
     * @param userDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出用户数据")
    @PreAuthorize("hasAnyAuthority('sys:user:export')")
    @GetMapping("export")
    @ApiOperation(value = "导出用户excel")
    public void exportFile(UserDTO userDTO, Page <UserDTO> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
        QueryWrapper <UserDTO> queryWrapper = QueryWrapperGenerator.buildQueryCondition ( userDTO, UserDTO.class );
        List <UserDTO> result;
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) {
            result = userService.findPage ( page, queryWrapper ).getRecords ( );
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) {
            queryWrapper.in ( "a.id", options.getSelectIds ( ) );
            result = userService.findPage ( page, queryWrapper ).getRecords ( );
        } else {
            page.setSize ( -1 );
            page.setCurrent ( 0 );
            result = userService.findPage ( page, queryWrapper ).getRecords ( );
        }

        UserEasyExcel.newInstance ( ).exportExcel ( result, options.getSheetName ( ), UserDTO.class, fileName, options.getExportFields ( ), response );
    }

    /**
     * 导入用户数据
     *
     * @return
     */
    @DemoMode
    @PreAuthorize("hasAnyAuthority('sys:user:import')")
    @PostMapping("import")
    @ApiOperation(value = "导入用户excel")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = UserEasyExcel.newInstance ( userService, UserWrapper.INSTANCE ).importExcel ( file, UserDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入用户数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize("hasAnyAuthority('sys:user:import')")
    @GetMapping("import/template")
    @ApiOperation(value = "下载模板")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "用户数据导入模板.xlsx";
        List <UserDTO> list = Lists.newArrayList ( );
        list.add ( UserUtils.getCurrentUserDTO ( ) );
        UserEasyExcel.newInstance ( ).exportExcel ( list, "用户数据", UserDTO.class, fileName, null, response );
    }

    /**
     * 返回用户信息
     *
     * @return
     */
    @ApiLog("获取当前用户信息")
    @GetMapping("info")
    @ApiOperation(value = "获取当前用户信息")
    public ResponseEntity infoData() {
        return ResponseUtil.newInstance ( ).add ( "role", UserUtils.getRoleDTOList ( ) ).add ( "user", UserUtils.getCurrentUserDTO ( ) ).ok ( "获取个人信息成功!" );
    }


    @DemoMode
    @ApiLog("修改密码")
    @RequestMapping("savePwd")
    public ResponseEntity savePwd(String oldPassword, String newPassword) {
        UserDTO userDTO = UserUtils.getCurrentUserDTO ( );
        if ( StrUtil.isNotBlank ( oldPassword ) && StrUtil.isNotBlank ( newPassword ) ) {
            if ( SecurityUtils.validatePassword ( oldPassword, userDTO.getPassword ( ) ) ) {
                User user = new User ( userDTO.getId ( ) );
                user.setPassword ( SecurityUtils.encryptPassword ( newPassword ) );
                userService.updateById ( user );
                UserUtils.deleteCache ( userDTO );
                return ResponseEntity.ok ( "修改密码成功！" );
            } else {
                return ResponseEntity.badRequest ( ).body ( "修改密码失败，旧密码错误！" );
            }
        }
        return ResponseEntity.badRequest ( ).body ( "参数错误！" );
    }


    /**
     * 获取菜单
     *
     * @return
     */
    @ApiLog("获取用户菜单")
    @GetMapping("getMenus")
    public ResponseEntity getMenus() {


//        return ResponseUtil.newInstance ( )
//                .add ( "dictList", DictUtils.getDictMap ( ) ) //获取字典
//                .add ( "permissions", UserUtils.getPermissions ( ) )
//                .add ( "menuList", MenuUtils.getMenus ( ) )
//                .add ( "routerList", RouterUtils.getRoutersByMenu ( ) ).ok ( );

        /**
         * 修改的原因，在某些性能很差的服务器上该方法的执行速度可以提升十几倍。当然在性能优良的服务器上，两种方式耗时几无区别。
         */
        Map map = new HashMap ( );
        map.put ( "dictList", DictUtils.getDictMap ( ) ); //获取字典
        map.put ( "permissions", UserUtils.getPermissions ( ) );
        map.put ( "menuList", MenuUtils.getMenus ( ) );
        map.put ( "routerList", RouterUtils.getRoutersByMenu ( ) );
        String res = new Gson ( ).toJson ( map );
        return ResponseEntity.ok ( res );
    }


}
