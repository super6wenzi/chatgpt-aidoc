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
import cn.weegoo.food.domain.FoodContentType;
import cn.weegoo.food.service.dto.FoodContentTypeDTO;
import cn.weegoo.food.service.mapstruct.FoodContentTypeWrapper;
import cn.weegoo.food.service.FoodContentTypeService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 内容类型Controller
 * @author weegoo
 * @version 2023-05-25
 */

@Api(tags ="内容类型")
@RestController
@RequestMapping(value = "/food/foodContentType")
public class FoodContentTypeController {

	@Autowired
	private FoodContentTypeService foodContentTypeService;

	@Autowired
	private FoodContentTypeWrapper foodContentTypeWrapper;

	/**
	 * 内容类型列表数据
	 */
	@ApiLog("查询内容类型列表数据")
	@ApiOperation(value = "查询内容类型列表数据")
	@PreAuthorize("hasAuthority('food:foodContentType:list')")
	@GetMapping("list")
	public ResponseEntity<IPage<FoodContentType>> list(FoodContentTypeDTO foodContentTypeDTO, Page<FoodContentType> page) throws Exception {
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodContentTypeDTO, FoodContentTypeDTO.class);
		IPage<FoodContentType> result = foodContentTypeService.page (page, queryWrapper);
		return ResponseEntity.ok (result);
	}


	/**
	 * 根据Id获取内容类型数据
	 */
	@ApiLog("根据Id获取内容类型数据")
	@ApiOperation(value = "根据Id获取内容类型数据")
	@PreAuthorize("hasAnyAuthority('food:foodContentType:view','food:foodContentType:add','food:foodContentType:edit')")
	@GetMapping("queryById")
	public ResponseEntity<FoodContentTypeDTO> queryById(String id) {
		return ResponseEntity.ok ( foodContentTypeWrapper.toDTO ( foodContentTypeService.getById ( id ) ) );
	}

	/**
	 * 保存内容类型
	 */
	@ApiLog("保存内容类型")
	@ApiOperation(value = "保存内容类型")
	@PreAuthorize("hasAnyAuthority('food:foodContentType:add','food:foodContentType:edit')")
	@PostMapping("save")
	public  ResponseEntity <String> save(@Valid @RequestBody FoodContentTypeDTO foodContentTypeDTO) {
		//新增或编辑表单保存
		foodContentTypeService.saveOrUpdate (foodContentTypeWrapper.toEntity (foodContentTypeDTO));
        return ResponseEntity.ok ( "保存内容类型成功" );
	}


	/**
	 * 删除内容类型
	 */
	@ApiLog("删除内容类型")
	@ApiOperation(value = "删除内容类型")
	@PreAuthorize("hasAuthority('food:foodContentType:del')")
	@DeleteMapping("delete")
	public ResponseEntity <String> delete(String ids) {
		String idArray[] = ids.split(",");
        foodContentTypeService.removeByIds ( Lists.newArrayList ( idArray ) );
		return ResponseEntity.ok( "删除内容类型成功" );
	}
	
	/**
     * 导出内容类型数据
     *
     * @param foodContentTypeDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出内容类型数据")
    @PreAuthorize("hasAnyAuthority('food:foodContentType:export')")
    @GetMapping("export")
    public void exportFile(FoodContentTypeDTO foodContentTypeDTO, Page <FoodContentType> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodContentTypeDTO, FoodContentTypeDTO.class);
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) { // 导出当前页数据
            
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) { // 导出选中数据
            queryWrapper.in ( "id", options.getSelectIds () );
        } else { // 导出全部数据
            page.setSize ( -1 );
            page.setCurrent ( 0 );
        }
        List < FoodContentType> result = foodContentTypeService.page ( page, queryWrapper ).getRecords ( );
        EasyExcelUtils.newInstance ( foodContentTypeService, foodContentTypeWrapper ).exportExcel ( result,  options.getSheetName ( ), FoodContentTypeDTO.class, fileName,options.getExportFields (), response );
    }

    /**
     * 导入内容类型数据
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('food:foodContentType:import')")
    @PostMapping("import")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = EasyExcelUtils.newInstance ( foodContentTypeService, foodContentTypeWrapper ).importExcel ( file, FoodContentTypeDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入内容类型数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize ("hasAnyAuthority('food:foodContentType:import')")
    @GetMapping("import/template")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "内容类型数据导入模板.xlsx";
        List<FoodContentTypeDTO> list = Lists.newArrayList();
        EasyExcelUtils.newInstance ( foodContentTypeService, foodContentTypeWrapper ).exportExcel ( list,  "内容类型数据", FoodContentTypeDTO.class, fileName, null, response );
    }

}
