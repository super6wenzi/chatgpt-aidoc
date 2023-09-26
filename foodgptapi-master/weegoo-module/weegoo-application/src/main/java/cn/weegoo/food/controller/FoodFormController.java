/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.controller;

import java.io.IOException;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.weegoo.food.service.mapstruct.FoodFormWrapper;
import cn.weegoo.food.service.dto.FoodFormDTO;
import cn.weegoo.food.service.FoodFormService;

/**
 * 表单配置Controller
 * @author xx
 * @version 2023-06-15
 */
@Api(tags ="表单配置")
@RestController
@RequestMapping(value = "/food/foodForm")
public class FoodFormController {

	@Autowired
	private FoodFormService foodFormService;
	
	@Autowired
	private FoodFormWrapper foodFormWrapper;

	/**
	 * 表单配置列表数据
	 */
	@ApiLog("查询表单配置列表数据")
	@ApiOperation(value = "查询表单配置列表数据")
	@PreAuthorize("hasAuthority('food:foodForm:list')")
	@GetMapping("list")
	public ResponseEntity<IPage<FoodFormDTO>> list(FoodFormDTO foodFormDTO, Page<FoodFormDTO> page) throws Exception {
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodFormDTO, FoodFormDTO.class);
		IPage<FoodFormDTO> result = foodFormService.findPage (page, queryWrapper);
		return ResponseEntity.ok (result);
	}


	/**
	 * 根据Id获取表单配置数据
	 */
	@ApiLog("根据Id获取表单配置数据")
	@ApiOperation(value = "根据Id获取表单配置数据")
	@PreAuthorize("hasAnyAuthority('food:foodForm:view','food:foodForm:add','food:foodForm:edit')")
	@GetMapping("queryById")
	public ResponseEntity<FoodFormDTO> queryById(String id) {
		return ResponseEntity.ok ( foodFormService.findById ( id ) );
	}

	/**
	 * 保存表单配置
	 */
	@ApiLog("保存表单配置")
	@ApiOperation(value = "保存表单配置")
	@PreAuthorize("hasAnyAuthority('food:foodForm:add','food:foodForm:edit')")
	@PostMapping("save")
	public  ResponseEntity <String> save(@Valid @RequestBody FoodFormDTO foodFormDTO) {
		//新增或编辑表单保存
		foodFormService.saveOrUpdate (foodFormDTO);
        return ResponseEntity.ok ( "保存表单配置成功" );
	}


	/**
	 * 删除表单配置
	 */
	@ApiLog("删除表单配置")
	@ApiOperation(value = "删除表单配置")
	@PreAuthorize("hasAuthority('food:foodForm:del')")
	@DeleteMapping("delete")
	public ResponseEntity <String> delete(String ids) {
		String idArray[] = ids.split(",");
		for(String id: idArray){
			foodFormService.removeById ( id );
		}
		return ResponseEntity.ok( "删除表单配置成功" );
	}
	
	/**
     * 导出表单配置数据
     *
     * @param foodFormDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出表单配置数据")
    @PreAuthorize("hasAnyAuthority('food:foodForm:export')")
    @GetMapping("export")
    public void exportFile(FoodFormDTO foodFormDTO, Page <FoodFormDTO> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodFormDTO, FoodFormDTO.class);
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) { // 导出当前页数据
            
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) { // 导出选中数据
            queryWrapper.in ( "a.id", options.getSelectIds () );
        } else { // 导出全部数据
            page.setSize ( -1 );
            page.setCurrent ( 0 );
        }
        List<FoodFormDTO> result = foodFormService.findPage ( page, queryWrapper ).getRecords ( );
        EasyExcelUtils.newInstance ( foodFormService, foodFormWrapper ).exportExcel ( result,  options.getSheetName ( ), FoodFormDTO.class, fileName,options.getExportFields (), response );
    }

    /**
     * 导入表单配置数据
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('food:foodForm:import')")
    @PostMapping("import")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = EasyExcelUtils.newInstance ( foodFormService, foodFormWrapper ).importExcel ( file, FoodFormDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入表单配置数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize ("hasAnyAuthority('food:foodForm:import')")
    @GetMapping("import/template")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "表单配置数据导入模板.xlsx";
        List<FoodFormDTO> list = Lists.newArrayList();
        EasyExcelUtils.newInstance ( foodFormService, foodFormWrapper ).exportExcel ( list,  "表单配置数据", FoodFormDTO.class, fileName, null, response );
    }	

}
