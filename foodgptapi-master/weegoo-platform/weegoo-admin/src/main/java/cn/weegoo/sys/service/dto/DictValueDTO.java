/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service.dto;


import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据字典Entity
 *
 * @author lgf
 * @version 2021-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictValueDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 标签页
     */
    private String label;

    /**
     * 键值
     */
    private String value;

    /**
     * 排序
     */
    private String sort;

    /**
     * 字典类型
     */
    private DictTypeDTO dictTypeDTO;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 构造函数
     *
     * @param dictTypeDTO
     */
    public DictValueDTO(DictTypeDTO dictTypeDTO) {
        this.dictTypeDTO = dictTypeDTO;
    }

}
