package cn.weegoo.sys.constant.enums;

/**
 * 菜单类型
 *
 * @author weegoo
 * @version 2021-5-15
 */
public enum MenuTypeEnum {
    FOLDER ( "0", "目录" ),
    MENU ( "1", "菜单" ),
    BUTTON ( "2", "按钮" ),
    ROUTER ( "3", "路由" );

    /**
     * 类型值
     */
    private String value;

    /**
     * 类型标签
     */
    private String label;

    MenuTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }


    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
