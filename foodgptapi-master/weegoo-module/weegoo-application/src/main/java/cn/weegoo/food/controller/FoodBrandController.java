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
import cn.weegoo.food.service.dto.FoodBrandDTO;
import cn.weegoo.food.service.mapstruct.FoodBrandWrapper;
import cn.weegoo.food.service.FoodBrandService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 品牌Controller
 * @author weegoo
 * @version 2023-05-26
 */

@Api(tags ="品牌")
@RestController
@RequestMapping(value = "/food/foodBrand")
public class FoodBrandController {

	@Autowired
	private FoodBrandService foodBrandService;

	@Autowired
	private FoodBrandWrapper foodBrandWrapper;

	/**
	 * 品牌列表数据
	 */
	@ApiLog("查询品牌列表数据")
	@ApiOperation(value = "查询品牌列表数据")
	@PreAuthorize("hasAuthority('food:foodBrand:list')")
	@GetMapping("list")
	public ResponseEntity<IPage<FoodBrandDTO>> list(FoodBrandDTO foodBrandDTO, Page<FoodBrandDTO> page) throws Exception {
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodBrandDTO, FoodBrandDTO.class);
		IPage<FoodBrandDTO> result = foodBrandService.findPage (page, queryWrapper);
		return ResponseEntity.ok (result);
	}


	/**
	 * 根据Id获取品牌数据
	 */
	@ApiLog("根据Id获取品牌数据")
	@ApiOperation(value = "根据Id获取品牌数据")
	@PreAuthorize("hasAnyAuthority('food:foodBrand:view','food:foodBrand:add','food:foodBrand:edit')")
	@GetMapping("queryById")
	public ResponseEntity<FoodBrandDTO> queryById(String id) {
		return ResponseEntity.ok ( foodBrandService.findById ( id ) );
	}

	/**
	 * 保存品牌
	 */
	@ApiLog("保存品牌")
	@ApiOperation(value = "保存品牌")
	@PreAuthorize("hasAnyAuthority('food:foodBrand:add','food:foodBrand:edit')")
	@PostMapping("save")
	public  ResponseEntity <String> save(@Valid @RequestBody FoodBrandDTO foodBrandDTO) {
		//新增或编辑表单保存
		foodBrandService.saveOrUpdate (foodBrandWrapper.toEntity (foodBrandDTO));
        return ResponseEntity.ok ( "保存品牌成功" );
	}


	/**
	 * 删除品牌
	 */
	@ApiLog("删除品牌")
	@ApiOperation(value = "删除品牌")
	@PreAuthorize("hasAuthority('food:foodBrand:del')")
	@DeleteMapping("delete")
	public ResponseEntity <String> delete(String ids) {
		String idArray[] = ids.split(",");
        foodBrandService.removeByIds ( Lists.newArrayList ( idArray ) );
		return ResponseEntity.ok( "删除品牌成功" );
	}
	
	/**
     * 导出品牌数据
     *
     * @param foodBrandDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出品牌数据")
    @PreAuthorize("hasAnyAuthority('food:foodBrand:export')")
    @GetMapping("export")
    public void exportFile(FoodBrandDTO foodBrandDTO, Page <FoodBrandDTO> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodBrandDTO, FoodBrandDTO.class);
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) { // 导出当前页数据
            
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) { // 导出选中数据
            queryWrapper.in ( "a.id", options.getSelectIds () );
        } else { // 导出全部数据
            page.setSize ( -1 );
            page.setCurrent ( 0 );
        }
        List<FoodBrandDTO> result = foodBrandService.findPage ( page, queryWrapper ).getRecords ( );
        EasyExcelUtils.newInstance ( foodBrandService, foodBrandWrapper ).exportExcel ( result,  options.getSheetName ( ), FoodBrandDTO.class, fileName,options.getExportFields (), response );
    }

    /**
     * 导入品牌数据
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('food:foodBrand:import')")
    @PostMapping("import")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = EasyExcelUtils.newInstance ( foodBrandService, foodBrandWrapper ).importExcel ( file, FoodBrandDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入品牌数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize ("hasAnyAuthority('food:foodBrand:import')")
    @GetMapping("import/template")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "品牌数据导入模板.xlsx";
        List<FoodBrandDTO> list = Lists.newArrayList();
        EasyExcelUtils.newInstance ( foodBrandService, foodBrandWrapper ).exportExcel ( list,  "品牌数据", FoodBrandDTO.class, fileName, null, response );
    }

}
