/**
 * Copyright &copy; 2015-2020 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.core.excel.converter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.google.common.collect.Lists;
import cn.weegoo.sys.domain.Role;
import cn.weegoo.sys.service.RoleService;
import cn.weegoo.sys.service.dto.RoleDTO;
import cn.weegoo.sys.service.mapstruct.RoleWrapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色类型转换
 *
 * @author weegoo
 * @version 2016-03-10
 */

public class ExcelRoleListDTOConverter implements Converter <List <RoleDTO>> {

    @Override
    public Class <?> supportJavaTypeKey() {
        return List.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public List <RoleDTO> convertToJavaData(ReadCellData <?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {

        List <RoleDTO> roleList = Lists.newArrayList ( );
        String val = cellData.getStringValue ( );
        for (String roleName : StrUtil.split ( val, "," )) {
            RoleDTO roleDTO = RoleWrapper.INSTANCE.toDTO ( SpringUtil.getBean ( RoleService.class ).lambdaQuery ( ).eq ( Role::getName, roleName ).one ( ) );
            roleList.add ( roleDTO );
        }
        return roleList.size ( ) > 0 ? roleList : null;
    }

    @Override
    public WriteCellData <?> convertToExcelData(List <RoleDTO> roleDTOList, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        List <String> roleNames = roleDTOList.stream ( ).map ( roleDTO -> roleDTO.getName ( ) ).collect ( Collectors.toList ( ) );
        return new WriteCellData <> ( StrUtil.join ( ",", roleNames ) );
    }


}

