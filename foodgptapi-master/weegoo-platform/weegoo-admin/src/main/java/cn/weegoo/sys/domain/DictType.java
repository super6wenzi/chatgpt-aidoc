/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import cn.weegoo.core.query.Query;
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
@TableName("sys_dict_type")
public class DictType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    @Query
    private String type;

    /**
     * 备注
     */
    private String remarks;
}
