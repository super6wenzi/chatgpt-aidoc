/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 日志Entity
 *
 * @author weegoo
 * @version 2021-8-19
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_log")
public class Log extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 日志类型（1：接入日志；2：错误日志）
     */
    @Query(type = QueryType.EQ)
    private String type;

    /**
     * 日志标题
     */
    private String title;

    /**
     * 操作用户的IP地址
     */
    private String remoteAddr;

    /**
     * 操作用户的IP地址
     */
    private String requestUri;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 操作的方式
     */
    private String method;

    /**
     * 操作提交的数据
     */
    private String params;

    /**
     * 返回值
     */
    private String result;
    /**
     * 操作用户代理信息
     */
    private String userAgent;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 耗时
     */
    private Long recordTime;

    /**
     * 更新日期
     */
    @Deprecated
    @TableField(exist = false)
    private Date updateDate;

    /**
     * 更新人
     */
    @Deprecated
    @TableField(exist = false)
    private String updateBy;

    /**
     * 备注
     */
    private String remarks;


}
