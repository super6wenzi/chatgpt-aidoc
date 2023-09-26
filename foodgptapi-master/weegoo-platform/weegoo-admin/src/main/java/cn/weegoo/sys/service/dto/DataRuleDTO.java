/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service.dto;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据权限Entity
 *
 * @author lgf
 * @version 2021-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataRuleDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 所属菜单
     */
    private String menuId;

    /**
     * 数据规则名称
     */
    private String name;

    /**
     * 实体类名
     */
    private String className;

    /**
     * 规则字段
     */
    private String field;

    /**
     * 规则条件
     */
    private String express;

    /**
     * 规则值
     */
    private String value;

    /**
     * 自定义sql
     */
    private String sqlSegment;

    /**
     * 备注
     */
    private String remarks;


    public String getDataScopeSql() {
        StringBuffer sqlBuffer = new StringBuffer ( );
        if ( StrUtil.isNotBlank ( field ) && StrUtil.isNotBlank ( value ) ) {
            sqlBuffer.append ( field + " " + HtmlUtil.unescape ( express ) + " " + value + " " );
        }
        if ( StrUtil.isNotBlank ( sqlSegment ) ) {
            sqlBuffer.append ( HtmlUtil.unescape ( sqlSegment ) + " " );
        }

        return sqlBuffer.toString ( );
    }
}
