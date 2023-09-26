/**
 * Copyright &copy; 2015-2020 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.core.excel;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.weegoo.core.excel.annotation.ExcelFieldProperty;
import cn.weegoo.core.mapstruct.EntityWrapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.mapstruct.factory.Mappers;


/**
 * 字典类型转换
 *
 * @author weegoo
 * @version 2022-08-01
 */

public class ExcelFieldDTOConverter implements Converter <Object> {

    @Override
    public Object convertToJavaData(ReadCellData <?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String key = contentProperty.getField ( ).getAnnotation ( ExcelFieldProperty.class ).value ( );
        String filed = key.substring ( key.lastIndexOf ( "." ) + 1 );
        String serviceClass = contentProperty.getField ( ).getAnnotation ( ExcelFieldProperty.class ).service ( );
        String wrapperClass = contentProperty.getField ( ).getAnnotation ( ExcelFieldProperty.class ).wrapper ( );
        String val = cellData.getStringValue ( );
        QueryWrapper queryWrapper = new QueryWrapper ( );
        queryWrapper.eq ( filed, val );
        Object bean = ((IService) SpringUtil.getBean ( Class.forName ( serviceClass ) )).getOne ( queryWrapper );
        Object dto = ((EntityWrapper) Mappers.getMapper ( Class.forName ( wrapperClass ) )).toDTO ( bean );
        return dto;
    }

    @Override
    public WriteCellData <?> convertToExcelData(Object value, ExcelContentProperty contentProperty,
                                                GlobalConfiguration globalConfiguration) {


        String key = contentProperty.getField ( ).getAnnotation ( ExcelFieldProperty.class ).value ( );
        String filed = key.substring ( key.lastIndexOf ( "." ) + 1 );

        try {
            String val = PropertyUtils.getNestedProperty ( value, filed ).toString ( );
            WriteCellData <Object> objectWriteCellData = new WriteCellData <> ( val );
            return objectWriteCellData;
        } catch (Exception e) {
            WriteCellData <Object> objectWriteCellData = new WriteCellData <> ( "" );
            return objectWriteCellData;
        }

    }

}

