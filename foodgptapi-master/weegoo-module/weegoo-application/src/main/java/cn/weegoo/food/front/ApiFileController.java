package cn.weegoo.food.front;

import cn.hutool.core.util.StrUtil;
import cn.weegoo.common.utils.ResponseUtil;
import cn.weegoo.file.config.FileProperties;
import cn.weegoo.file.repository.AccessoryRepository;
import cn.weegoo.file.repository.Exception.FunctionNotSupportedException;
import cn.weegoo.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

/**
 * @author weegoo
 * @date 2023/5/19 15:32
 **/
@Api(tags = "文件管理")
@RestController
@RequestMapping("/api/file")
public class ApiFileController {

    private String FILE_SERVER = "/file";
    @Autowired
    FileProperties fileProperties;

    @Autowired
    private AccessoryRepository accessoryRepository;

    @Value("${config.accessory.baseDir}")
    private String accessoryBaseDir;



    /**
     * 上传文件
     *
     * @return
     * @throws IOException
     */
    @ApiOperation("上传文件")
    @PostMapping("webupload/upload")
        public ResponseEntity webupload(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws FileNotFoundException, FunctionNotSupportedException {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            String tempUploadPath = request.getParameter("uploadPath");
            if (StrUtil.isBlank(tempUploadPath)) {
                tempUploadPath = "/front";
            }
            String uploadPath = tempUploadPath + "/" + year + "/" + month + "/";
            // 判断文件是否为空
            if (!file.isEmpty()) {
                String name = file.getOriginalFilename();
                if (fileProperties.isAvailable(name)) {
                    // 文件保存路径
                    // 转存文件
                    accessoryRepository.save(file, uploadPath, name);
                    return ResponseUtil.newInstance()
                            .add("url",  "/"+accessoryBaseDir + uploadPath + name)
                            .ok();

                } else {
                    return ResponseEntity.badRequest().body("请勿上传非法文件!");
                }
            } else {
                return ResponseEntity.badRequest().body("文件不存在!");
            }
        }
}
