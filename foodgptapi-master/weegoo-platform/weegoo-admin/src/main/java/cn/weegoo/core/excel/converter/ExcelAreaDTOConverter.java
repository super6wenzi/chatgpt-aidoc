/**
 * Copyright &copy; 2015-2020 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.core.excel.converter;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import cn.weegoo.sys.domain.Area;
import cn.weegoo.sys.service.AreaService;
import cn.weegoo.sys.service.dto.AreaDTO;
import cn.weegoo.sys.service.mapstruct.AreaWrapper;

/**
 * 字段类型转换
 *
 * @author weegoo
 * @version 2022-03-10
 */

public class ExcelAreaDTOConverter implements Converter <AreaDTO> {

    @Override
    public Class <?> supportJavaTypeKey() {
        return AreaDTO.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public AreaDTO convertToJavaData(ReadCellData <?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String val = cellData.getStringValue ( );
        AreaDTO areaDTO = AreaWrapper.INSTANCE.toDTO ( SpringUtil.getBean ( AreaService.class ).lambdaQuery ( ).eq ( Area::getName, val ).one ( ) );
        return areaDTO;
    }

    @Override
    public WriteCellData <?> convertToExcelData(AreaDTO value, ExcelContentProperty contentProperty,
                                                GlobalConfiguration globalConfiguration) {

        if ( value != null && value.getName ( ) != null ) {
            return new WriteCellData <> ( value.getName ( ) );
        }
        return new WriteCellData <> ( "" );
    }


}

