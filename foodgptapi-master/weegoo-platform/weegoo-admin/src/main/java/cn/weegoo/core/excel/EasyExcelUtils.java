package cn.weegoo.core.excel;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.weegoo.core.mapstruct.EntityWrapper;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * @Author weegoo
 * @Description easyExcel导入导出通用工具类
 * @Date 2022-7-29
 * @Param
 * @return
 **/

@Data
public class EasyExcelUtils<T> {

    private IService service;

    private EntityWrapper wrapper;

    public static EasyExcelUtils newInstance() {
        return new EasyExcelUtils ( );
    }

    public static EasyExcelUtils newInstance(IService service, EntityWrapper wrapper) {
        return new EasyExcelUtils ( service, wrapper );
    }

    public EasyExcelUtils() {

    }

    public EasyExcelUtils(IService service, EntityWrapper wrapper) {
        this.service = service;
        this.wrapper = wrapper;
    }


    /**
     * 功能描述：复杂导出Excel，包括文件名以及表名,不创建表头
     *
     * @param list      导出的实体类
     * @param sheetName sheet表名
     * @param pojoClass 映射的实体类
     * @param fileName  文件名
     * @param response
     * @return
     * @author Steel.D
     * @Date 2019-7-31 9:35
     */

    public void exportExcel(List <?> list, String sheetName, Class <?> pojoClass, String fileName, Collection <String> includeColumnFieldNames, HttpServletResponse response) throws IOException {

        response.setContentType ( "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
        response.setCharacterEncoding ( "utf-8" );
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        fileName = URLEncoder.encode ( fileName, "UTF-8" ).replaceAll ( "\\+", "%20" );
        response.setHeader ( "Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx" );
        if ( includeColumnFieldNames != null ) {
            EasyExcel.write ( response.getOutputStream ( ), pojoClass ).includeColumnFieldNames ( includeColumnFieldNames ).sheet ( sheetName ).doWrite ( list );
        } else {
            EasyExcel.write ( response.getOutputStream ( ), pojoClass ).sheet ( sheetName ).doWrite ( list );
        }

    }


    /**
     * 功能描述：根据接收的Excel文件来导入Excel,并封装成实体类
     *
     * @param file      上传的文件
     * @param pojoClass Excel实体类
     * @return
     * @author Steel.D
     * @Date 2019-7-31 11:30
     */

    public String importExcel(MultipartFile file, Class <T> pojoClass) throws IOException {
        if ( file == null ) {
            return "上传文件不能为空!";
        }
        ExcelListener excelListener = new ExcelListener ( this.service, this.wrapper );
        EasyExcel.read ( file.getInputStream ( ), pojoClass, excelListener ).sheet ( ).doRead ( );
        return excelListener.getMessage ( );

    }


}
