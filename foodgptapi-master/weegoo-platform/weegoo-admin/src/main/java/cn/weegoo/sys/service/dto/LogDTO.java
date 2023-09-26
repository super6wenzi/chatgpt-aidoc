/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service.dto;

import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import cn.weegoo.core.service.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
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
public class LogDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 日志类型（1：接入日志；2：错误日志）
     */
    @Query(type = QueryType.EQ, tableColumn = "a.type")
    private String type;

    /**
     * 日志标题
     */
    @Query(tableColumn = "a.title")
    private String title;

    /**
     * 操作用户的IP地址
     */
    private String remoteAddr;

    /**
     * 操作的URI
     */
    @Query(tableColumn = "a.request_uri")
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

    @Query(tableColumn = "u.name", javaField = "createBy.name", type = QueryType.LIKE)
    protected UserDTO createBy;

    @ApiModelProperty(hidden = true)
    @Query(tableColumn = "a.create_time", type = QueryType.BETWEEN)
    protected Date createTime;

    /**
     * 备注
     */
    private String remarks;

}
