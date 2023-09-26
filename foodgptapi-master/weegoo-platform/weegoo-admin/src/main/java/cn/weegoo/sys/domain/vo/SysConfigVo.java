package cn.weegoo.sys.domain.vo;

import lombok.Data;

@Data
public class SysConfigVo {
    /*
     * 默认主题
     */
    private String defaultTheme;
    /**
     * 默认布局
     */
    private String defaultLayout;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品logo
     */
    private String logo;

}
