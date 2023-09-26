/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service.dto;

import cn.weegoo.core.service.dto.TreeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

/**
 * 区域DTO
 *
 * @author weegoo
 * @version 2021-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AreaDTO extends TreeDTO<AreaDTO> {

    private static final long serialVersionUID = 1L;

    /**
     * 区域编码
     */
    @Size(min = 0, max = 64)
    private String code;

    /**
     * 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
     */
    @Size(min = 1, max = 1)
    private String type;

    /**
     * 备注
     */
    private String remarks;

}
