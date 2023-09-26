package cn.weegoo.food.front.vo;

import lombok.Data;

import java.util.List;

/**
 * @author damin
 */
@Data
public class ContentTypeVO {

    private String id;
    /**
     * 名称
     */
    private String name;

    /**
     * 显示顺序
     */
    private String sort;
    /**
     * 子列表
     */
    private List<BuildTypeVO> buildType;

    /**
     * 生成标题
     */
    private String buildTitle;
    /**
     * 生成提示
     */
    private String buildTips;
}
