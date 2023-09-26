package cn.weegoo.core.excel;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ExcelOptions {
    /**
     * 选中的数据id
     */
    private List <String> selectIds = Lists.newArrayList ( );

    /**
     * 文件名
     */
    private String filename;
    /**
     * 导出模式 all， current, selected
     */
    private String mode = "all";
    /**
     * sheet name
     */
    private String sheetName;
    /**
     * 导出字段
     */
    private List <String> exportFields = Lists.newArrayList ( );

}
