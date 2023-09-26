package cn.weegoo.sys.utils.excel;

import com.alibaba.excel.EasyExcel;
import cn.weegoo.core.excel.EasyExcelUtils;
import cn.weegoo.sys.service.UserService;
import cn.weegoo.sys.service.dto.UserDTO;
import cn.weegoo.sys.service.mapstruct.UserWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class UserEasyExcel extends EasyExcelUtils <UserDTO> {

    public static UserEasyExcel newInstance() {
        return new UserEasyExcel ( );
    }

    public static UserEasyExcel newInstance(UserService service, UserWrapper wrapper) {
        return new UserEasyExcel ( service, wrapper );
    }

    public UserEasyExcel() {
        super ( );
    }

    public UserEasyExcel(UserService service, UserWrapper wrapper) {
        super ( service, wrapper );
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

    public String importExcel(MultipartFile file, Class <UserDTO> pojoClass) throws IOException {
        if ( file == null ) {
            return "上传文件不能为空!";
        }
        UserExcelListener userExcelListener = new UserExcelListener ( this.getService ( ), this.getWrapper ( ) );
        EasyExcel.read ( file.getInputStream ( ), pojoClass, userExcelListener ).sheet ( ).doRead ( );
        return userExcelListener.getMessage ( );

    }

}
