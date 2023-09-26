/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据字典Entity
 *
 * @author lgf
 * @version 2021-01-16
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict_value")
public class DictValue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 标签名
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
     * 类型
     */
    private String dictTypeId;


}
