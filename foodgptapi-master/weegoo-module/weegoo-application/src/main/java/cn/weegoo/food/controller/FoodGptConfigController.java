/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import cn.weegoo.utils.R;
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
import cn.weegoo.food.domain.FoodGptConfig;
import cn.weegoo.food.service.dto.FoodGptConfigDTO;
import cn.weegoo.food.service.mapstruct.FoodGptConfigWrapper;
import cn.weegoo.food.service.FoodGptConfigService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * gpt账号配置Controller
 * @author xx
 * @version 2023-05-29
 */

@Api(tags ="gpt账号配置")
@RestController
@RequestMapping(value = "/food/foodGptConfig")
public class FoodGptConfigController {

	@Autowired
	private FoodGptConfigService foodGptConfigService;

	@Autowired
	private FoodGptConfigWrapper foodGptConfigWrapper;

	/**
	 * gpt账号配置列表数据
	 */
	@ApiLog("查询gpt账号配置列表数据")
	@ApiOperation(value = "查询gpt账号配置列表数据")
	@PreAuthorize("hasAuthority('food:foodGptConfig:list')")
	@GetMapping("list")
	public ResponseEntity<IPage<FoodGptConfig>> list(FoodGptConfigDTO foodGptConfigDTO, Page<FoodGptConfig> page) throws Exception {
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodGptConfigDTO, FoodGptConfigDTO.class);
		IPage<FoodGptConfig> result = foodGptConfigService.page (page, queryWrapper);
		return ResponseEntity.ok (result);
	}


	/**
	 * 根据Id获取gpt账号配置数据
	 */
	@ApiLog("根据Id获取gpt账号配置数据")
	@ApiOperation(value = "根据Id获取gpt账号配置数据")
	@PreAuthorize("hasAnyAuthority('food:foodGptConfig:view','food:foodGptConfig:add','food:foodGptConfig:edit')")
	@GetMapping("queryById")
	public ResponseEntity<FoodGptConfigDTO> queryById(String id) {
		return ResponseEntity.ok ( foodGptConfigWrapper.toDTO ( foodGptConfigService.getById ( id ) ) );
	}

	/**
	 * 保存gpt账号配置
	 */
	@ApiLog("保存gpt账号配置")
	@ApiOperation(value = "保存gpt账号配置")
	@PreAuthorize("hasAnyAuthority('food:foodGptConfig:add','food:foodGptConfig:edit')")
	@PostMapping("save")
	public  ResponseEntity <String> save(@Valid @RequestBody FoodGptConfigDTO foodGptConfigDTO) {
		//新增或编辑表单保存
		foodGptConfigService.saveOrUpdate (foodGptConfigWrapper.toEntity (foodGptConfigDTO));
        return ResponseEntity.ok ( "保存gpt账号配置成功" );
	}


	/**
	 * 删除gpt账号配置
	 */
	@ApiLog("删除gpt账号配置")
	@ApiOperation(value = "删除gpt账号配置")
	@PreAuthorize("hasAuthority('food:foodGptConfig:del')")
	@DeleteMapping("delete")
	public ResponseEntity <String> delete(String ids) {
		String idArray[] = ids.split(",");
        foodGptConfigService.removeByIds ( Lists.newArrayList ( idArray ) );
		return ResponseEntity.ok( "删除gpt账号配置成功" );
	}
	
	/**
     * 导出gpt账号配置数据
     *
     * @param foodGptConfigDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出gpt账号配置数据")
    @PreAuthorize("hasAnyAuthority('food:foodGptConfig:export')")
    @GetMapping("export")
    public void exportFile(FoodGptConfigDTO foodGptConfigDTO, Page <FoodGptConfig> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodGptConfigDTO, FoodGptConfigDTO.class);
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) { // 导出当前页数据
            
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) { // 导出选中数据
            queryWrapper.in ( "id", options.getSelectIds () );
        } else { // 导出全部数据
            page.setSize ( -1 );
            page.setCurrent ( 0 );
        }
        List < FoodGptConfig> result = foodGptConfigService.page ( page, queryWrapper ).getRecords ( );
        EasyExcelUtils.newInstance ( foodGptConfigService, foodGptConfigWrapper ).exportExcel ( result,  options.getSheetName ( ), FoodGptConfigDTO.class, fileName,options.getExportFields (), response );
    }

    /**
     * 导入gpt账号配置数据
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('food:foodGptConfig:import')")
    @PostMapping("import")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = EasyExcelUtils.newInstance ( foodGptConfigService, foodGptConfigWrapper ).importExcel ( file, FoodGptConfigDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入gpt账号配置数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize ("hasAnyAuthority('food:foodGptConfig:import')")
    @GetMapping("import/template")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "gpt账号配置数据导入模板.xlsx";
        List<FoodGptConfigDTO> list = Lists.newArrayList();
        EasyExcelUtils.newInstance ( foodGptConfigService, foodGptConfigWrapper ).exportExcel ( list,  "gpt账号配置数据", FoodGptConfigDTO.class, fileName, null, response );
    }

	/**
	 * 获取余额
	 * @param apiKey chatGpt的key
	 * @return
	 */
	@GetMapping("getBalance")
	public R<String> getBalance(String apiKey){
		return R.data(foodGptConfigService.getBalance(apiKey));
	}

}
