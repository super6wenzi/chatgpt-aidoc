/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import cn.hutool.core.util.StrUtil;
import cn.weegoo.food.service.FoodMessageDetailService;
import cn.weegoo.food.service.dto.FoodMessageDetailDTO;
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
import cn.weegoo.food.service.mapstruct.FoodMessageWrapper;
import cn.weegoo.food.service.dto.FoodMessageDTO;
import cn.weegoo.food.service.FoodMessageService;

/**
 * 聊天记录Controller
 * @author xx
 * @version 2023-06-07
 */
@Api(tags ="聊天记录")
@RestController
@RequestMapping(value = "/food/foodMessage")
public class FoodMessageController {

	@Autowired
	private FoodMessageService foodMessageService;
	
	@Autowired
	private FoodMessageWrapper foodMessageWrapper;

	@Autowired
	private FoodMessageDetailService foodMessageDetailService;

	/**
	 * 聊天记录列表数据
	 */
	@ApiLog("查询聊天记录列表数据")
	@ApiOperation(value = "查询聊天记录列表数据")
//	@PreAuthorize("hasAuthority('food:foodMessage:list')")
	@GetMapping("list")
	public ResponseEntity<IPage<FoodMessageDTO>> list(FoodMessageDTO foodMessageDTO, Page<FoodMessageDTO> page) throws Exception {
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodMessageDTO, FoodMessageDTO.class);
		IPage<FoodMessageDTO> result = foodMessageService.findPage (page, queryWrapper);

		//查询会话明细
		result.getRecords().forEach(a->{
			List<FoodMessageDetailDTO> list = foodMessageDetailService.findList(a.getId(), foodMessageDTO.getType());
			a.setFoodMessageDetailDTOList(list);
		});
		return ResponseEntity.ok (result);
	}


	/**
	 * 根据Id获取聊天记录数据
	 */
	@ApiLog("根据Id获取聊天记录数据")
	@ApiOperation(value = "根据Id获取聊天记录数据")
	@PreAuthorize("hasAnyAuthority('food:foodMessage:view','food:foodMessage:add','food:foodMessage:edit')")
	@GetMapping("queryById")
	public ResponseEntity<FoodMessageDTO> queryById(String id) {
		return ResponseEntity.ok ( foodMessageService.findById ( id ) );
	}

	/**
	 * 保存聊天记录
	 */
	@ApiLog("保存聊天记录")
	@ApiOperation(value = "保存聊天记录")
	@PreAuthorize("hasAnyAuthority('food:foodMessage:add','food:foodMessage:edit')")
	@PostMapping("save")
	public  ResponseEntity <String> save(@Valid @RequestBody FoodMessageDTO foodMessageDTO) {
		//新增或编辑表单保存
		foodMessageService.saveOrUpdate (foodMessageDTO);
        return ResponseEntity.ok ( "保存聊天记录成功" );
	}


	/**
	 * 删除聊天记录
	 */
	@ApiLog("删除聊天记录")
	@ApiOperation(value = "删除聊天记录")
	@PreAuthorize("hasAuthority('food:foodMessage:del')")
	@DeleteMapping("delete")
	public ResponseEntity <String> delete(String ids) {
		String idArray[] = ids.split(",");
		for(String id: idArray){
			foodMessageService.removeById ( id );
		}
		return ResponseEntity.ok( "删除聊天记录成功" );
	}
	
	/**
     * 导出聊天记录数据
     *
     * @param foodMessageDTO
     * @param page
     * @param response
     * @throws Exception
     */
    @ApiLog("导出聊天记录数据")
    @PreAuthorize("hasAnyAuthority('food:foodMessage:export')")
    @GetMapping("export")
    public void exportFile(FoodMessageDTO foodMessageDTO, Page <FoodMessageDTO> page, ExcelOptions options, HttpServletResponse response) throws Exception {
        String fileName = options.getFilename ( );
		QueryWrapper queryWrapper = QueryWrapperGenerator.buildQueryCondition (foodMessageDTO, FoodMessageDTO.class);
        if ( ExportMode.current.equals ( options.getMode ( ) ) ) { // 导出当前页数据
            
        } else if ( ExportMode.selected.equals ( options.getMode ( ) ) ) { // 导出选中数据
            queryWrapper.in ( "a.id", options.getSelectIds () );
        } else { // 导出全部数据
            page.setSize ( -1 );
            page.setCurrent ( 0 );
        }
        List<FoodMessageDTO> result = foodMessageService.findPage ( page, queryWrapper ).getRecords ( );
        EasyExcelUtils.newInstance ( foodMessageService, foodMessageWrapper ).exportExcel ( result,  options.getSheetName ( ), FoodMessageDTO.class, fileName,options.getExportFields (), response );
    }

    /**
     * 导入聊天记录数据
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('food:foodMessage:import')")
    @PostMapping("import")
    public ResponseEntity importFile(MultipartFile file) throws IOException {
        String result = EasyExcelUtils.newInstance ( foodMessageService, foodMessageWrapper ).importExcel ( file, FoodMessageDTO.class );
        return ResponseEntity.ok ( result );
    }

    /**
     * 下载导入聊天记录数据模板
     *
     * @param response
     * @return
     */
    @PreAuthorize ("hasAnyAuthority('food:foodMessage:import')")
    @GetMapping("import/template")
    public void importFileTemplate(HttpServletResponse response) throws IOException {
        String fileName = "聊天记录数据导入模板.xlsx";
        List<FoodMessageDTO> list = Lists.newArrayList();
        EasyExcelUtils.newInstance ( foodMessageService, foodMessageWrapper ).exportExcel ( list,  "聊天记录数据", FoodMessageDTO.class, fileName, null, response );
    }	

}
