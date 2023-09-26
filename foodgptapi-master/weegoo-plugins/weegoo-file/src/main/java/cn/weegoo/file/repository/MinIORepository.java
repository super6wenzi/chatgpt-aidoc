package cn.weegoo.file.repository;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.weegoo.file.utils.CommonUtils;
import cn.weegoo.file.utils.FileUtils;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * minIOOSS存储服务类
 *
 * @author weegoo
 * @date 2020/8/20 14:42
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "config.accessory.type", havingValue = "minIO")
public class MinIORepository implements AccessoryRepository {


    private final Logger logger = LoggerFactory.getLogger ( getClass ( ) );

    @Value("${config.accessory.baseDir}")
    private String accessoryBaseDir;

    @Value("${config.accessory.minIO.endpoint}")
    private String endpoint;
    @Value("${config.accessory.minIO.accessKey}")
    private String accessKey;
    @Value("${config.accessory.minIO.secretKey}")
    private String secretKey;
    @Value("${config.accessory.minIO.bucketName}")
    private String bucketName;


    @Override
    public File get(String path, String fileName) throws FileNotFoundException {
        logger.debug ( "开始从minIO-oss获取文件: {}{}{}", path, "/", fileName );
        long begin = System.currentTimeMillis ( );
        try {
            if ( path == null ) {
                path = "";
            }
            MinioClient client = MinioClient.builder ( ).endpoint ( endpoint ).credentials ( accessKey, secretKey ).build ( );
            GetObjectArgs objectArgs = GetObjectArgs.builder ( ).bucket ( bucketName ).object ( accessoryBaseDir + "/" + path + "/" + fileName ).build ( );
            InputStream is = client.getObject ( objectArgs );
            File localTempDir = new File ( FileUtils.getTempDirectory ( ) + File.separator + accessoryBaseDir + File.separator + path );
            FileUtils.createDirectory ( localTempDir.getPath ( ) );
            File dest = new File ( localTempDir, fileName );
            FileUtils.copyInputStreamToFile ( is, dest );
            is.close ( );
            return dest;
        } catch (Exception e) {
            throw new FileNotFoundException ( "指定的文件不存在" );
        } finally {
            logger.debug ( "完成从minIO-oss获取文件: {}", System.currentTimeMillis ( ) - begin );
        }
    }


    @Override
    public String getURL(String path, String fileName, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        logger.debug ( "开始从minIO-oss获取文件: {}{}{}", path, "/", fileName );
        long begin = System.currentTimeMillis ( );
        URL downloadURL = null;
        try {
            if ( path == null ) {
                path = "";
            }
            MinioClient client = MinioClient.builder ( ).endpoint ( endpoint ).credentials ( accessKey, secretKey ).build ( );
            String url = client.getPresignedObjectUrl ( new GetPresignedObjectUrlArgs.Builder ( )
                    .bucket ( bucketName )
                    .object ( accessoryBaseDir + "/" + path + "/" + fileName )
                    .expiry ( 1, TimeUnit.DAYS )
                    .method ( Method.GET )
                    .build ( ) );
            return url;
        } catch (Exception e) {
            e.printStackTrace ( );
            throw new FileNotFoundException ( "指定的文件不存在" );
        } finally {
            logger.debug ( "完成从minIO-oss获取文件临时授权路径 {}, 耗时: {}", downloadURL, System.currentTimeMillis ( ) - begin );
        }
    }


    /**
     * 保存文件对象
     *
     * @param file
     * @param path
     * @param fileName
     * @Author weegoo
     * @Date 2020/8/20 14:47
     */
    @Override
    public File save(File file, String path, String fileName) {
        logger.debug ( "开始向minIO-oss保存文件: {}", fileName );
        long begin = System.currentTimeMillis ( );
        try {
            MinioClient client = MinioClient.builder ( ).endpoint ( endpoint ).credentials ( accessKey, secretKey ).build ( );
            boolean exists = client.bucketExists ( BucketExistsArgs.builder ( ).bucket ( bucketName ).build ( ) );
            if ( !exists ) {
                client.makeBucket ( MakeBucketArgs.builder ( ).bucket ( bucketName ).build ( ) );

            }
            InputStream in = new FileInputStream ( file );
            //这里用下hutool的工具类-根据文件扩展名获得MimeType
            String mime = FileUtil.getMimeType ( file.getName ( ) );
            if ( mime == null ) {
                //二进制流，不知道下载文件类型
                mime = "application/octet-stream";
            }
            PutObjectArgs putObjectArgs = PutObjectArgs.builder ( ).bucket ( bucketName ).object ( accessoryBaseDir + "/" + path + "/" + fileName ).stream ( in, file.length ( ), -1 ).contentType ( mime ).build ( );
            client.putObject ( putObjectArgs );
        } catch (Exception e) {
            e.printStackTrace ( );
        } finally {
            logger.debug ( "完成向minIO-oss保存文件: {}", System.currentTimeMillis ( ) - begin );
        }

        return file;
    }

    /**
     * 保存文件流文件为文件对象
     *
     * @param file
     * @param path
     * @param fileName
     * @Author weegoo
     * @Date 2020/8/20 14:49
     */
    @Override
    public File save(MultipartFile file, String path, String fileName) {
        logger.debug ( "开始向minIO-oss保存文件: {}", fileName );
        long begin = System.currentTimeMillis ( );
        try {
            MinioClient client = MinioClient.builder ( ).endpoint ( endpoint ).credentials ( accessKey, secretKey ).build ( );
            boolean exists = client.bucketExists ( BucketExistsArgs.builder ( ).bucket ( bucketName ).build ( ) );
            if ( !exists ) {
                client.makeBucket ( MakeBucketArgs.builder ( ).bucket ( bucketName ).build ( ) );

            }
            InputStream is = file.getInputStream ( );
            PutObjectArgs putObjectArgs = PutObjectArgs.builder ( ).bucket ( bucketName ).contentType ( file.getContentType ( ) ).object ( accessoryBaseDir + "/" + path + "/" + fileName ).stream ( is, is.available ( ), -1 ).build ( );
            client.putObject ( putObjectArgs );
            is.close ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        logger.debug ( "完成向minIO-oss保存文件: {}", System.currentTimeMillis ( ) - begin );
        return null;
    }

    /**
     * 保存输入流为文件对象
     *
     * @param is
     * @param path
     * @param fileName
     * @Author weegoo
     * @Date 2020/8/20 14:47
     */
    @Override
    public File save(InputStream is, String path, @NotNull String fileName) {
        logger.debug ( "开始向minIO-oss保存文件: {}", fileName );
        long begin = System.currentTimeMillis ( );
        try {
            MinioClient client = MinioClient.builder ( ).endpoint ( endpoint ).credentials ( accessKey, secretKey ).build ( );
            boolean exists = client.bucketExists ( BucketExistsArgs.builder ( ).bucket ( bucketName ).build ( ) );
            if ( !exists ) {
                client.makeBucket ( MakeBucketArgs.builder ( ).bucket ( bucketName ).build ( ) );

            }
            PutObjectArgs putObjectArgs = PutObjectArgs.builder ( ).bucket ( bucketName ).object ( accessoryBaseDir + "/" + path + "/" + fileName ).stream ( is, is.available ( ), -1 ).build ( );
            client.putObject ( putObjectArgs );
            is.close ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        logger.debug ( "完成向minIO-oss保存文件: {}", System.currentTimeMillis ( ) - begin );
        return null;
    }


    /**
     * 重写文件
     *
     * @param file
     * @param url
     * @return
     */
    public String writeByUrl(MultipartFile file, String url) {
        if ( endpoint.endsWith ( "/" ) ) {
            url = url.replace ( endpoint, "" );
        } else {
            url = url.replace ( endpoint + "/", "" );
        }
        String customBucket = url.split ( "/" )[0];
        String bizPath = url.replace ( customBucket + "/", "" );
        String filename = bizPath.substring ( bizPath.lastIndexOf ( "/" ) + 1 );
        bizPath = bizPath.substring ( 0, bizPath.lastIndexOf ( "/" ) );

        return upload ( file, bizPath, customBucket, filename, true );
    }


    /**
     * 上传文件
     *
     * @param file
     * @param bizPath
     * @param customBucket 桶名
     * @return
     */
    private String upload(MultipartFile file, String bizPath, String customBucket, String fileName, boolean isWrite) {
        String file_url = "";
        String newBucket = bucketName;
        if ( StrUtil.isNotEmpty ( customBucket ) ) {
            newBucket = customBucket;
        }
        MinioClient minioClient = MinioClient.builder ( ).endpoint ( endpoint ).credentials ( accessKey, secretKey ).build ( );
        try {
            // 检查存储桶是否已经存在
            if ( minioClient.bucketExists ( BucketExistsArgs.builder ( ).bucket ( newBucket ).build ( ) ) ) {
                log.info ( "Bucket already exists." );
            } else {
                // 创建一个名为ota的存储桶
                minioClient.makeBucket ( MakeBucketArgs.builder ( ).bucket ( newBucket ).build ( ) );
                minioClient.setBucketPolicy ( SetBucketPolicyArgs.builder ( ).config ( "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + newBucket + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\"],\"Resource\":[\"arn:aws:s3:::" + newBucket + "/*\"]}]}" ).bucket ( newBucket ).build ( ) );
                log.info ( "create a new bucket." );
            }
            InputStream stream = file.getInputStream ( );
            String orgName;
            if ( StrUtil.isNotBlank ( fileName ) ) {
                orgName = fileName;
            } else {
                // 获取文件名
                orgName = file.getOriginalFilename ( );
                orgName = CommonUtils.getFileName ( orgName );
            }

            String objectName;
            if ( !isWrite ) {
                objectName = bizPath + "/" + orgName.substring ( 0, orgName.lastIndexOf ( "." ) ) + "_" + System.currentTimeMillis ( ) + orgName.substring ( orgName.indexOf ( "." ) );

            } else {
                objectName = bizPath + "/" + orgName;
            }

            // 使用putObject上传一个本地文件到存储桶中。
            PutObjectArgs putObjectArgs = PutObjectArgs.builder ( ).bucket ( newBucket ).object ( objectName ).stream ( stream, stream.available ( ), -1 ).build ( );
            minioClient.putObject ( putObjectArgs );
            stream.close ( );
            file_url = newBucket + "/" + objectName;
        } catch (Exception e) {
            log.error ( e.getMessage ( ), e );
        }
        file_url = file_url.startsWith ( "/" ) ? file_url : "/" + file_url;
        return file_url;
    }


}
