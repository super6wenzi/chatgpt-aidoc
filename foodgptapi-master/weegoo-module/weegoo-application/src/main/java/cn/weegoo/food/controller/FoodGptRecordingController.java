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
import cn.weegoo.food.domain.FoodGptRecording;
import cn.weegoo.food.service.dto.FoodGptRecordingDTO;
import cn.weegoo.food.service.mapstruct.FoodGptRecordingWrapper;
import cn.weegoo.food.service.FoodGptRecordingService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 生成记录表Controller
 * @author weegoo
 * @version 2023-05-26
 */

@Api(tags ="生成记录表")
@RestController
@RequestMapping(value = "/food/foodGptRecording")
public class FoodGptRecordingController {

	@Autowired
	private FoodGptRecordingService foodGptRecordingService;

	@Autowired
	private FoodGptRecordingWrapper foodGptRecordingWrapper;

	/**
	 * 生成记录表列表数据
	 */
	@ApiLog("查询生成记录表列表数据")
	@ApiOperation(value = "查询生成记录表列表数据")
	@PreAuthorize("hasAuthority('food:foodGptRecording:list')")
	@GetMapping("list")
	public ResponseEntity<IPage<FoodGptRecording>> list(FoodGptRecordingDTO foodGptRecordingDTO, Page<FoodGptRecording> page) throws Exception {
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodGptRecordingDTO, FoodGptRecordingDTO.class);
		IPage<FoodGptRecording> result = foodGptRecordingService.page (page, queryWrapper);
		return ResponseEntity.ok (result);
	}


	/**
	 * 根据Id获取生成记录表数据
	 */
	@ApiLog("根据Id获取生成记录表数据")
	@ApiOperation(value = "根据Id获取生成记录表数据")
	@PreAuthorize("hasAnyAuthority('food:foodGptRecording:view','food:foodGptRecording:add','food:foodGptRecording:edit')")
	@GetMapping("queryById")
	public ResponseEntity<FoodGptRecordingDTO> queryById(String id) {
		return ResponseEntity.ok ( foodGptRecordingWrapper.toDTO ( foodGptRecordingService.getById ( id ) ) );
	}

	/**
	 * 保存生成记录表
	 */
	@ApiLog("保存生成记录表")
	@ApiOperation(value = "保存生成记录表")
	@PreAuthorize("hasAnyAuthority('food:foodGptRecording:add','food:foodGptRecording:edit')")
	@PostMapping("save")
	public  ResponseEntity <String> save(@Valid @RequestBody FoodGptRecordingDTO foodGptRecordingDTO) {
		//新增或编辑表单保存
		foodGptRecordingService.saveOrUpdate (foodGptRecordingWrapper.toEntity (foodGptRecordingDTO));
        return ResponseEntity.ok ( "保存生成记录表成功" );
	}


	/**
	 * 删除生成记录表
	 */
	@ApiLog("删除生成记录表")
	@ApiOperation(value = "删除生成记录表")
	@PreAuthorize("hasAuthority('food:foodGptRecording:del')")
	@DeleteMapping("delete")
	public ResponseEntity <String> delete(String ids) {
		String idArray[] = ids.split(",");
        foodGptRecordingService.removeByIds ( Lists.newArrayList ( idArray ) );
		return ResponseEntity.ok( "删除生成记录表成功" );
	}
	
	/**
     * 导出生成记录表数据
     *
     * @param foodGptRecordingDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出生成记录表数据")
    @PreAuthorize("hasAnyAuthority('food:foodGptRecording:export')")
    @GetMapping("export")
    public void exportFile(FoodGptRecordingDTO foodGptRecordingDTO, Page <FoodGptRecording> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodGptRecordingDTO, FoodGptRecordingDTO.class);
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) { // 导出当前页数据
            
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) { // 导出选中数据
            queryWrapper.in ( "id", options.getSelectIds () );
        } else { // 导出全部数据
            page.setSize ( -1 );
            page.setCurrent ( 0 );
        }
        List < FoodGptRecording> result = foodGptRecordingService.page ( page, queryWrapper ).getRecords ( );
        EasyExcelUtils.newInstance ( foodGptRecordingService, foodGptRecordingWrapper ).exportExcel ( result,  options.getSheetName ( ), FoodGptRecordingDTO.class, fileName,options.getExportFields (), response );
    }

    /**
     * 导入生成记录表数据
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('food:foodGptRecording:import')")
    @PostMapping("import")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = EasyExcelUtils.newInstance ( foodGptRecordingService, foodGptRecordingWrapper ).importExcel ( file, FoodGptRecordingDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入生成记录表数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize ("hasAnyAuthority('food:foodGptRecording:import')")
    @GetMapping("import/template")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "生成记录表数据导入模板.xlsx";
        List<FoodGptRecordingDTO> list = Lists.newArrayList();
        EasyExcelUtils.newInstance ( foodGptRecordingService, foodGptRecordingWrapper ).exportExcel ( list,  "生成记录表数据", FoodGptRecordingDTO.class, fileName, null, response );
    }

}
