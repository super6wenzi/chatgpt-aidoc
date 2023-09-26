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
import cn.weegoo.food.service.dto.FoodBuildTypeDTO;
import cn.weegoo.food.service.mapstruct.FoodBuildTypeWrapper;
import cn.weegoo.food.service.FoodBuildTypeService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 生成类型Controller
 * @author weegoo
 * @version 2023-05-26
 */

@Api(tags ="生成类型")
@RestController
@RequestMapping(value = "/food/foodBuildType")
public class FoodBuildTypeController {

	@Autowired
	private FoodBuildTypeService foodBuildTypeService;

	@Autowired
	private FoodBuildTypeWrapper foodBuildTypeWrapper;

	/**
	 * 生成类型列表数据
	 */
	@ApiLog("查询生成类型列表数据")
	@ApiOperation(value = "查询生成类型列表数据")
	@PreAuthorize("hasAuthority('food:foodBuildType:list')")
	@GetMapping("list")
	public ResponseEntity<IPage<FoodBuildTypeDTO>> list(FoodBuildTypeDTO foodBuildTypeDTO, Page<FoodBuildTypeDTO> page) throws Exception {
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodBuildTypeDTO, FoodBuildTypeDTO.class);
		IPage<FoodBuildTypeDTO> result = foodBuildTypeService.findPage (page, queryWrapper);
		return ResponseEntity.ok (result);
	}


	/**
	 * 根据Id获取生成类型数据
	 */
	@ApiLog("根据Id获取生成类型数据")
	@ApiOperation(value = "根据Id获取生成类型数据")
	@PreAuthorize("hasAnyAuthority('food:foodBuildType:view','food:foodBuildType:add','food:foodBuildType:edit')")
	@GetMapping("queryById")
	public ResponseEntity<FoodBuildTypeDTO> queryById(String id) {
		return ResponseEntity.ok ( foodBuildTypeService.findById ( id ) );
	}

	/**
	 * 保存生成类型
	 */
	@ApiLog("保存生成类型")
	@ApiOperation(value = "保存生成类型")
	@PreAuthorize("hasAnyAuthority('food:foodBuildType:add','food:foodBuildType:edit')")
	@PostMapping("save")
	public  ResponseEntity <String> save(@Valid @RequestBody FoodBuildTypeDTO foodBuildTypeDTO) {
		//新增或编辑表单保存
		foodBuildTypeService.saveOrUpdate (foodBuildTypeWrapper.toEntity (foodBuildTypeDTO));
        return ResponseEntity.ok ( "保存生成类型成功" );
	}


	/**
	 * 删除生成类型
	 */
	@ApiLog("删除生成类型")
	@ApiOperation(value = "删除生成类型")
	@PreAuthorize("hasAuthority('food:foodBuildType:del')")
	@DeleteMapping("delete")
	public ResponseEntity <String> delete(String ids) {
		String idArray[] = ids.split(",");
        foodBuildTypeService.removeByIds ( Lists.newArrayList ( idArray ) );
		return ResponseEntity.ok( "删除生成类型成功" );
	}
	
	/**
     * 导出生成类型数据
     *
     * @param foodBuildTypeDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出生成类型数据")
    @PreAuthorize("hasAnyAuthority('food:foodBuildType:export')")
    @GetMapping("export")
    public void exportFile(FoodBuildTypeDTO foodBuildTypeDTO, Page <FoodBuildTypeDTO> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodBuildTypeDTO, FoodBuildTypeDTO.class);
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) { // 导出当前页数据
            
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) { // 导出选中数据
            queryWrapper.in ( "a.id", options.getSelectIds () );
        } else { // 导出全部数据
            page.setSize ( -1 );
            page.setCurrent ( 0 );
        }
        List<FoodBuildTypeDTO> result = foodBuildTypeService.findPage ( page, queryWrapper ).getRecords ( );
        EasyExcelUtils.newInstance ( foodBuildTypeService, foodBuildTypeWrapper ).exportExcel ( result,  options.getSheetName ( ), FoodBuildTypeDTO.class, fileName,options.getExportFields (), response );
    }

    /**
     * 导入生成类型数据
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('food:foodBuildType:import')")
    @PostMapping("import")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = EasyExcelUtils.newInstance ( foodBuildTypeService, foodBuildTypeWrapper ).importExcel ( file, FoodBuildTypeDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入生成类型数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize ("hasAnyAuthority('food:foodBuildType:import')")
    @GetMapping("import/template")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "生成类型数据导入模板.xlsx";
        List<FoodBuildTypeDTO> list = Lists.newArrayList();
        EasyExcelUtils.newInstance ( foodBuildTypeService, foodBuildTypeWrapper ).exportExcel ( list,  "生成类型数据", FoodBuildTypeDTO.class, fileName, null, response );
    }

}
