/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import cn.weegoo.core.query.Query;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色Entity
 *
 * @author weegoo
 * @version 2021-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @Query
    private String name;

    /**
     * 英文名称
     */
    private String enName;

    /**
     * 是否系统数据
     */
    @TableField("is_sys")
    private String sysData;

    /**
     * 是否可用
     */
    private String useable;

    /**
     * 备注
     */
    private String remarks;

}
