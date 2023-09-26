package cn.weegoo.sys.utils.excel;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.weegoo.common.beanvalidator.BeanValidators;
import cn.weegoo.core.excel.ExcelListener;
import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.security.util.SecurityUtils;
import cn.weegoo.sys.service.UserService;
import cn.weegoo.sys.service.dto.UserDTO;
import cn.weegoo.sys.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;

@Slf4j
public class UserExcelListener extends ExcelListener <UserDTO> {

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param service
     */
    public UserExcelListener(IService service, EntityWrapper wrapper) {
        super ( service, wrapper );
    }


    /**
     * 加上存储数据库
     */
    public void saveData() {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder ( );
            List <UserDTO> list = this.getCachedDataList ( );
            for (UserDTO user : list) {
                try {
                    if ( UserUtils.isCheckLoginName ( "", user.getLoginName ( ) ) ) {
                        user.setPassword ( SecurityUtils.encryptPassword ( "123456" ) );
                        Validator validator = SpringUtil.getBean ( Validator.class );
                        BeanValidators.validateWithException ( validator, user );
                        ((UserService) this.getService ( )).saveOrUpdate ( user );
                        successNum++;
                    } else {
                        failureMsg.append ( "<br/>登录名 " + user.getLoginName ( ) + " 已存在; " );
                        failureNum++;
                    }
                } catch (ConstraintViolationException ex) {
                    failureMsg.append ( "<br/>登录名 " + user.getLoginName ( ) + " 导入失败：" );
                    List <String> messageList = BeanValidators.extractPropertyAndMessageAsList ( ex, ": " );
                    for (String message : messageList) {
                        failureMsg.append ( message + "; " );
                        failureNum++;
                    }
                } catch (Exception ex) {
                    failureNum++;
                    failureMsg.append ( "<br/>登录名 " + user.getLoginName ( ) + " 导入失败：" + ex.getMessage ( ) );
                }
                if ( failureNum > 0 ) {
                    failureMsg.insert ( 0, "，失败 " + failureNum + " 条用户，导入信息如下：" );
                }
            }
            this.setMessage ( "已成功导入 " + successNum + " 条用户" + failureMsg );
            log.info ( this.getMessage ( ) );
        } catch (Exception e) {
            e.printStackTrace ( );
            this.setMessage ( "导入用户失败！失败信息：" + e.getMessage ( ) );
            log.info ( this.getMessage ( ) );
        }
    }
}
