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
import cn.weegoo.food.domain.FoodConfig;
import cn.weegoo.food.service.dto.FoodConfigDTO;
import cn.weegoo.food.service.mapstruct.FoodConfigWrapper;
import cn.weegoo.food.service.FoodConfigService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * model配置Controller
 * @author weegoo
 * @version 2023-06-01
 */

@Api(tags ="model配置")
@RestController
@RequestMapping(value = "/food/foodConfig")
public class FoodConfigController {

	@Autowired
	private FoodConfigService foodConfigService;

	@Autowired
	private FoodConfigWrapper foodConfigWrapper;

	/**
	 * model配置列表数据
	 */
	@ApiLog("查询model配置列表数据")
	@ApiOperation(value = "查询model配置列表数据")
	@PreAuthorize("hasAuthority('food:foodConfig:list')")
	@GetMapping("list")
	public ResponseEntity<IPage<FoodConfig>> list(FoodConfigDTO foodConfigDTO, Page<FoodConfig> page) throws Exception {
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodConfigDTO, FoodConfigDTO.class);
		IPage<FoodConfig> result = foodConfigService.page (page, queryWrapper);
		return ResponseEntity.ok (result);
	}


	/**
	 * 根据Id获取model配置数据
	 */
	@ApiLog("根据Id获取model配置数据")
	@ApiOperation(value = "根据Id获取model配置数据")
	@PreAuthorize("hasAnyAuthority('food:foodConfig:view','food:foodConfig:add','food:foodConfig:edit')")
	@GetMapping("queryById")
	public ResponseEntity<FoodConfigDTO> queryById(String id) {
		return ResponseEntity.ok ( foodConfigWrapper.toDTO ( foodConfigService.getById ( id ) ) );
	}

	/**
	 * 保存model配置
	 */
	@ApiLog("保存model配置")
	@ApiOperation(value = "保存model配置")
	@PreAuthorize("hasAnyAuthority('food:foodConfig:add','food:foodConfig:edit')")
	@PostMapping("save")
	public  ResponseEntity <String> save(@Valid @RequestBody FoodConfigDTO foodConfigDTO) {
		//新增或编辑表单保存
		foodConfigService.saveOrUpdate (foodConfigWrapper.toEntity (foodConfigDTO));
        return ResponseEntity.ok ( "保存model配置成功" );
	}


	/**
	 * 删除model配置
	 */
	@ApiLog("删除model配置")
	@ApiOperation(value = "删除model配置")
	@PreAuthorize("hasAuthority('food:foodConfig:del')")
	@DeleteMapping("delete")
	public ResponseEntity <String> delete(String ids) {
		String idArray[] = ids.split(",");
        foodConfigService.removeByIds ( Lists.newArrayList ( idArray ) );
		return ResponseEntity.ok( "删除model配置成功" );
	}
	
	/**
     * 导出model配置数据
     *
     * @param foodConfigDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出model配置数据")
    @PreAuthorize("hasAnyAuthority('food:foodConfig:export')")
    @GetMapping("export")
    public void exportFile(FoodConfigDTO foodConfigDTO, Page <FoodConfig> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodConfigDTO, FoodConfigDTO.class);
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) { // 导出当前页数据
            
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) { // 导出选中数据
            queryWrapper.in ( "id", options.getSelectIds () );
        } else { // 导出全部数据
            page.setSize ( -1 );
            page.setCurrent ( 0 );
        }
        List < FoodConfig> result = foodConfigService.page ( page, queryWrapper ).getRecords ( );
        EasyExcelUtils.newInstance ( foodConfigService, foodConfigWrapper ).exportExcel ( result,  options.getSheetName ( ), FoodConfigDTO.class, fileName,options.getExportFields (), response );
    }

    /**
     * 导入model配置数据
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('food:foodConfig:import')")
    @PostMapping("import")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = EasyExcelUtils.newInstance ( foodConfigService, foodConfigWrapper ).importExcel ( file, FoodConfigDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入model配置数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize ("hasAnyAuthority('food:foodConfig:import')")
    @GetMapping("import/template")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "model配置数据导入模板.xlsx";
        List<FoodConfigDTO> list = Lists.newArrayList();
        EasyExcelUtils.newInstance ( foodConfigService, foodConfigWrapper ).exportExcel ( list,  "model配置数据", FoodConfigDTO.class, fileName, null, response );
    }

}
