/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.google.common.collect.Lists;
import cn.weegoo.core.excel.EasyExcelUtils;
import cn.weegoo.core.excel.ExcelOptions;
import cn.weegoo.core.excel.annotation.ExportMode;
import cn.weegoo.core.query.QueryWrapperGenerator;
import cn.weegoo.aop.logging.annotation.ApiLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.weegoo.food.domain.FoodUser;
import cn.weegoo.food.service.dto.FoodUserDTO;
import cn.weegoo.food.service.mapstruct.FoodUserWrapper;
import cn.weegoo.food.service.FoodUserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 用户Controller
 * @author weegoo
 * @version 2023-05-26
 */

@Api(tags ="用户")
@RestController
@RequestMapping(value = "/food/foodUser")
public class FoodUserController {

	@Autowired
	private FoodUserService foodUserService;

	@Autowired
	private FoodUserWrapper foodUserWrapper;

	/**
	 * 用户列表数据
	 */
	@ApiLog("查询用户列表数据")
	@ApiOperation(value = "查询用户列表数据")
	@PreAuthorize("hasAuthority('food:foodUser:list')")
	@GetMapping("list")
	public ResponseEntity<IPage<FoodUser>> list(FoodUserDTO foodUserDTO, Page<FoodUser> page) throws Exception {
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodUserDTO, FoodUserDTO.class);
		IPage<FoodUser> result = foodUserService.page (page, queryWrapper);
		return ResponseEntity.ok (result);
	}


	/**
	 * 根据Id获取用户数据
	 */
	@ApiLog("根据Id获取用户数据")
	@ApiOperation(value = "根据Id获取用户数据")
	@PreAuthorize("hasAnyAuthority('food:foodUser:view','food:foodUser:add','food:foodUser:edit')")
	@GetMapping("queryById")
	public ResponseEntity<FoodUserDTO> queryById(String id) {
		return ResponseEntity.ok ( foodUserWrapper.toDTO ( foodUserService.getById ( id ) ) );
	}

	/**
	 * 保存用户
	 */
	@ApiLog("保存用户")
	@ApiOperation(value = "保存用户")
	@PreAuthorize("hasAnyAuthority('food:foodUser:add','food:foodUser:edit')")
	@PostMapping("save")
	public  ResponseEntity <String> save(@Valid @RequestBody FoodUserDTO foodUserDTO) {
		//新增或编辑表单保存
		foodUserService.saveOrUpdate (foodUserWrapper.toEntity (foodUserDTO));
        return ResponseEntity.ok ( "保存用户成功" );
	}


	/**
	 * 删除用户
	 */
	@ApiLog("删除用户")
	@ApiOperation(value = "删除用户")
	@PreAuthorize("hasAuthority('food:foodUser:del')")
	@DeleteMapping("delete")
	public ResponseEntity <String> delete(String ids) {
		String idArray[] = ids.split(",");
        foodUserService.removeByIds ( Lists.newArrayList ( idArray ) );
		return ResponseEntity.ok( "删除用户成功" );
	}
	
	/**
     * 导出用户数据
     *
     * @param foodUserDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出用户数据")
    @PreAuthorize("hasAnyAuthority('food:foodUser:export')")
    @GetMapping("export")
    public void exportFile(FoodUserDTO foodUserDTO, Page <FoodUser> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodUserDTO, FoodUserDTO.class);
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) { // 导出当前页数据
            
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) { // 导出选中数据
            queryWrapper.in ( "id", options.getSelectIds () );
        } else { // 导出全部数据
            page.setSize ( -1 );
            page.setCurrent ( 0 );
        }
        List < FoodUser> result = foodUserService.page ( page, queryWrapper ).getRecords ( );
        EasyExcelUtils.newInstance ( foodUserService, foodUserWrapper ).exportExcel ( result,  options.getSheetName ( ), FoodUserDTO.class, fileName,options.getExportFields (), response );
    }

    /**
     * 导入用户数据
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('food:foodUser:import')")
    @PostMapping("import")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = EasyExcelUtils.newInstance ( foodUserService, foodUserWrapper ).importExcel ( file, FoodUserDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入用户数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize ("hasAnyAuthority('food:foodUser:import')")
    @GetMapping("import/template")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "用户数据导入模板.xlsx";
        List<FoodUserDTO> list = Lists.newArrayList();
        EasyExcelUtils.newInstance ( foodUserService, foodUserWrapper ).exportExcel ( list,  "用户数据", FoodUserDTO.class, fileName, null, response );
    }

}
