/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service.dto;

import com.google.common.collect.Lists;
import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 数据字典Entity
 *
 * @author lgf
 * @version 2021-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictTypeDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    private String type;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 子表列表
     */
    private List <DictValueDTO> dictValueDTOList = Lists.newArrayList ( );
}
