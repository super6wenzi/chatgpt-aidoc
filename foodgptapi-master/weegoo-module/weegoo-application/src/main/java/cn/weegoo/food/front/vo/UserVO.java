package cn.weegoo.food.front.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author damin
 */
@Data
public class UserVO {

    private String id;
    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 微信标识
     */
    @ApiModelProperty("微信标识")
    private String openid;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
    /**
     * 会员等级
     */
    @ApiModelProperty("会员等级")
    private String vip;
    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private String userType;

    private Integer times;
}
