/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service.dto;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import cn.weegoo.core.excel.annotation.ExcelDictProperty;
import cn.weegoo.core.excel.converter.ExcelDictDTOConverter;
import cn.weegoo.core.excel.converter.ExcelOfficeDTOConverter;
import cn.weegoo.core.excel.converter.ExcelPostListDTOConverter;
import cn.weegoo.core.excel.converter.ExcelRoleListDTOConverter;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import cn.weegoo.core.service.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Entity
 *
 * @author weegoo
 * @version 2021-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;


    /**
     * 登录名
     */
    @Length(min = 1, max = 100)
    @Query
    @ExcelProperty("登录名")
    private String loginName;

    /**
     * 密码
     */
    @JsonIgnore
    @Length(min = 1, max = 100)
    @ExcelIgnore
    private String password;


    /**
     * 姓名
     */
    @Length(min = 1, max = 100)
    @Query(tableColumn = "a.name")
    @ExcelProperty("姓名")
    private String name;


    /**
     * 工号
     */
    @ExcelProperty("工号")
    private String no;

    /**
     * 归属公司
     */
    @NotNull(message = "公司不能为空")
    @Query(type = QueryType.EQ, tableColumn = "c.id", javaField = "companyDTO.id")
    @ApiModelProperty(hidden = true)
    @ExcelProperty(value = "公司", converter = ExcelOfficeDTOConverter.class)
    private OfficeDTO companyDTO;

    /**
     * 归属部门
     */
    @NotNull(message = "部门不能为空")
    @Query(type = QueryType.EQ, tableColumn = "o.id", javaField = "officeDTO.id")
    @ApiModelProperty(hidden = true)
    @ExcelProperty(value = "部门", converter = ExcelOfficeDTOConverter.class)
    private OfficeDTO officeDTO;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Length(min = 0, max = 100)
    @ExcelProperty("邮箱")
    private String email;

    /**
     * 电话
     */
    @Length(min = 0, max = 100)
    @ExcelProperty("电话")
    private String phone;

    /**
     * 手机
     */
    @Length(min = 0, max = 100)
    @ExcelProperty("手机")
    private String mobile;

    /**
     * 最后登录IP
     */
    @ApiModelProperty(hidden = true)
    @ExcelIgnore
    private String loginIp;

    /**
     * 最后登录日期
     */
    @ApiModelProperty(hidden = true)
    @ExcelIgnore
    private Date loginDate;

    /**
     * 是否允许登录
     */
    @ExcelProperty(value = "是否允许登录", converter = ExcelDictDTOConverter.class)
    @ExcelDictProperty("yes_no")
    private String loginFlag;

    /**
     * 头像
     */
    @ExcelIgnore
    private String photo;

    /**
     * 二维码
     */
    @ExcelIgnore
    private String qrCode;

    /**
     * 原登录名
     */
    @ExcelIgnore
    private String oldLoginName;

    /**
     * 新密码
     */
    @ExcelIgnore
    private String newPassword;

    /**
     * 签名
     */
    @ExcelIgnore
    private String sign;


    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remarks;

    /**
     * 超级管理员标志
     */
    @ExcelIgnore
    private boolean isAdmin;

    /**
     * 根据角色查询用户条件
     */
    @Query(type = QueryType.EQ, javaField = "roleDTO.id", tableColumn = "r.id")
    @ApiModelProperty(hidden = true)
    @ExcelIgnore
    private RoleDTO roleDTO;

    /**
     * 根据岗位查询用户
     */
    @ExcelIgnore
    @Query(type = QueryType.EQ, javaField = "postDTO.id", tableColumn = "p.id")
    private PostDTO postDTO;

    /**
     * 拥有角色列表
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @ExcelProperty(value = "角色", converter = ExcelRoleListDTOConverter.class)
    private List <RoleDTO> roleDTOList = Lists.newArrayList ( );

    /**
     * 拥有岗位列表
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @ExcelProperty(value = "岗位", converter = ExcelPostListDTOConverter.class)
    private List <PostDTO> postDTOList = Lists.newArrayList ( );

    public UserDTO() {
        super ( );
    }

    public UserDTO(String id) {
        super ( id );
    }


    /**
     * 获取包含的角色id列表
     *
     * @return
     */
    public List <String> getRoleIdList() {
        if ( roleDTOList == null ) {
            return Lists.newArrayList ( );
        }
        List <String> roleIdList = roleDTOList.stream ( ).map ( roleDTO -> roleDTO.getId ( ) ).collect ( Collectors.toList ( ) );
        return roleIdList;
    }

    /**
     * 设置角色
     *
     * @param roleIdList
     */
    public void setRoleIdList(List <String> roleIdList) {
        for (String roleId : roleIdList) {
            RoleDTO roleDTO = new RoleDTO ( roleId );
            roleDTOList.add ( roleDTO );
        }
    }

    /**
     * 获取包含的岗位id列表
     *
     * @return
     */
    public List <String> getPostIdList() {
        if ( postDTOList == null ) {
            return Lists.newArrayList ( );
        }
        List <String> postIdList = postDTOList.stream ( ).map ( postDTO -> postDTO.getId ( ) ).collect ( Collectors.toList ( ) );
        return postIdList;
    }

    /**
     * 设置岗位
     *
     * @param postIdList
     */
    public void setPostIdList(List <String> postIdList) {
        for (String postId : postIdList) {
            PostDTO postDTO = new PostDTO ( postId );
            postDTOList.add ( postDTO );
        }
    }


    /**
     * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
     */
    public String getRoleNames() {
        List <String> roleNames = roleDTOList.stream ( ).map ( roleDTO -> roleDTO.getName ( ) ).collect ( Collectors.toList ( ) );
        return StrUtil.join ( ",", roleNames );
    }

    public String getRoleIds() {
        List <String> roleIds = roleDTOList.stream ( ).map ( roleDTO -> roleDTO.getId ( ) ).collect ( Collectors.toList ( ) );
        return StrUtil.join ( ",", roleIds );
    }


}
