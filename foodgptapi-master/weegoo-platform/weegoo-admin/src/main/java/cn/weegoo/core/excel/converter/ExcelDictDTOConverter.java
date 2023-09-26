/**
 * Copyright &copy; 2015-2020 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.core.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import cn.weegoo.core.excel.annotation.ExcelDictProperty;
import cn.weegoo.sys.utils.DictUtils;

/**
 * 字典类型转换
 *
 * @author weegoo
 * @version 2022-08-01
 */

public class ExcelDictDTOConverter implements Converter <String> {


    @Override
    public String convertToJavaData(ReadCellData <?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String dictType = contentProperty.getField ( ).getAnnotation ( ExcelDictProperty.class ).value ( );
        String val = cellData.getStringValue ( );
        return DictUtils.getDictValue ( val, dictType, "-" );
    }

    @Override
    public WriteCellData <?> convertToExcelData(String value, ExcelContentProperty contentProperty,
                                                GlobalConfiguration globalConfiguration) {


        String dictType = contentProperty.getField ( ).getAnnotation ( ExcelDictProperty.class ).value ( );

        WriteCellData <Object> objectWriteCellData = new WriteCellData <> ( DictUtils.getDictLabel ( value, dictType, "-" ) );
        return objectWriteCellData;
    }


}

