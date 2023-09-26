/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.core.service.dto;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 数据dto类
 *
 * @author weegoo
 * @version 2021-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class TreeDTO<T> extends BaseDTO <T> {

    private static final long serialVersionUID = 1L;

    /**
     * 父级元素
     */
    protected T parent;

    /**
     * 子元素
     */
    private List <T> children = Lists.newArrayList ( );

    /**
     * 所有父级编号
     */
    protected String parentIds;

    /**
     * 名称
     */
    protected String name;

    /**
     * 排序
     */
    protected Integer sort;

    /**
     * 构造函数
     */
    public TreeDTO() {
        super ( );
    }

    /**
     * 构造函数
     *
     * @param id
     */
    public TreeDTO(String id) {
        super ( id );
    }

    /**
     * 获取上级id
     *
     * @return
     */
    public String getParentId() {
        String id = null;
        if ( parent != null ) {
            id = (String) ReflectUtil.getFieldValue ( parent, "id" );
        }
        return StrUtil.isNotBlank ( id ) ? id : getRootId ( );
    }

    /**
     * 获取上级name
     *
     * @return
     */
    public String getParentName() {
        String name = null;
        if ( parent != null ) {
            name = (String) ReflectUtil.getFieldValue ( parent, "name" );
        }
        return StrUtil.isNotBlank ( name ) ? name : "";
    }

    /**
     * 默认根节点
     *
     * @return
     */
    @JsonIgnore
    public static String getRootId() {
        return "0";
    }
}
