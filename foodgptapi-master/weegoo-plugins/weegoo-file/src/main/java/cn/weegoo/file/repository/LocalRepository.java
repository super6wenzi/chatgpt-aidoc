package cn.weegoo.file.repository;

import cn.hutool.core.util.StrUtil;
import cn.weegoo.file.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;

/**
 * 本地存储服务类
 *
 * @author weegoo
 * @date 2020/8/20 14:42
 */
@Component
@ConditionalOnProperty(name = "config.accessory.type", havingValue = "local")
public class LocalRepository implements AccessoryRepository {

    private final Logger logger = LoggerFactory.getLogger ( getClass ( ) );

    @Value("${config.accessory.baseDir}")
    private String accessoryBaseDir;

    @Value("${config.accessory.local.location}")
    private String location;

    /**
     * 根据保存的相对路径获取存储的文件对象
     *
     * @param path     指定文件的路径, 不含文件名
     * @param fileName 指定文件的文件名, 不含路径
     * @return java.io.File
     * @Author weegoo
     * @Date 2020/8/20 14:46
     */
    @Override
    public File get(String path, String fileName) throws FileNotFoundException {
        logger.debug ( "开始从本地存储获取文件: {}{}{}", location + File.separator + accessoryBaseDir + File.separator + path, File.separator, fileName );
        long begin = System.currentTimeMillis ( );
        File rootDir = new File ( location + File.separator + accessoryBaseDir );
        if ( path == null ) {
            path = "";
        }
        File fileDir = new File ( rootDir, path );
        if ( !fileDir.exists ( ) || !fileDir.isDirectory ( ) ) {
            throw new FileNotFoundException ( "指定的目录不存在" );
        }
        File srcFile = new File ( fileDir, fileName );
        if ( !srcFile.exists ( ) || !srcFile.isFile ( ) ) {
            throw new FileNotFoundException ( "指定的文件不存在" );
        }
        logger.debug ( "完成从本地存储获取文件: {}", System.currentTimeMillis ( ) - begin );
        return srcFile;
    }

    @Override
    public String getURL(String path, String fileName, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        logger.debug ( "开始从本地存储获取文件: {}{}{}", location + File.separator + accessoryBaseDir + File.separator + path, File.separator, fileName );
        long begin = System.currentTimeMillis ( );
        File rootDir = new File ( location + File.separator + accessoryBaseDir );
        if ( path == null ) {
            path = "";
        }
        File fileDir = new File ( rootDir, path );
        if ( !fileDir.exists ( ) || !fileDir.isDirectory ( ) ) {
            throw new FileNotFoundException ( "指定的目录不存在" );
        }
        File srcFile = new File ( fileDir, fileName );
        if ( !srcFile.exists ( ) || !srcFile.isFile ( ) ) {
            throw new FileNotFoundException ( "指定的文件不存在" );
        }
        this.downRangeFile (srcFile, request, response );
        logger.debug ( "完成从本地存储获取文件: {}", System.currentTimeMillis ( ) - begin );
        return null;
    }

    public void downRangeFile(File downloadFile, HttpServletRequest request, HttpServletResponse response) {

        // 文件不存在
        if (!downloadFile.exists()) {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }catch (Exception e){
                logger.error ( e.getMessage () );
            }

            return;
        }

        long fileLength = downloadFile.length();// 记录文件大小
        long pastLength = 0;// 记录已下载文件大小
        int rangeSwitch = 0;// 0：从头开始的全文下载；1：从某字节开始的下载（bytes=27000-）；2：从某字节开始到某字节结束的下载（bytes=27000-39000）
        long toLength = 0;// 记录客户端需要下载的字节段的最后一个字节偏移量（比如bytes=27000-39000，则这个值是为39000）
        long contentLength = 0;// 客户端请求的字节总量
        String rangeBytes = "";// 记录客户端传来的形如“bytes=27000-”或者“bytes=27000-39000”的内容
        RandomAccessFile raf = null;// 负责读取数据
        OutputStream os = null;// 写出数据
        OutputStream out = null;// 缓冲
        int bsize = 1024;// 缓冲区大小
        byte b[] = new byte[bsize];// 暂存容器

        String range = request.getHeader("Range");
        int responseStatus = 206;
        if (range != null && range.trim().length() > 0 && !"null".equals(range)) {// 客户端请求的下载的文件块的开始字节
            responseStatus =HttpServletResponse.SC_PARTIAL_CONTENT;
//			logger.info("request.getHeader(\"Range\")=" + range);
            rangeBytes = range.replaceAll("bytes=", "");
            if (rangeBytes.endsWith("-")) {// bytes=969998336-
                rangeSwitch = 1;
                rangeBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
                pastLength = Long.parseLong(rangeBytes.trim());
                contentLength = fileLength - pastLength;// 客户端请求的是
                // 969998336之后的字节（包括bytes下标索引为969998336的字节）
            } else {// bytes=1275856879-1275877358
                rangeSwitch = 2;
                String temp0 = rangeBytes.substring(0, rangeBytes.indexOf('-'));
                String temp2 = rangeBytes.substring(rangeBytes.indexOf('-') + 1, rangeBytes.length());
                // bytes=1275856879-1275877358，从第1275856879个字节开始下载
                pastLength = Long.parseLong(temp0.trim());
                toLength = Long.parseLong(temp2);// bytes=1275856879-1275877358，到第
                // 1275877358 个字节结束
                contentLength = toLength - pastLength + 1;// 客户端请求的是
                // 1275856879-1275877358
                // 之间的字节
            }
        } else {// 从开始进行下载
            contentLength = fileLength;// 客户端要求全文下载
        }

        /**
         * 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。 响应的格式是:
         * Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
         * ServletActionContext.getResponse().setHeader("Content-Length", new
         * Long(file.length() - p).toString());
         */
        // 来清除首部的空白行
        response.reset();
        // 告诉客户端允许断点续传多线程连接下载,响应的格式是:Accept-Ranges: bytes
        response.setHeader("Accept-Ranges", "bytes");
        // 如果是第一次下,还没有断点续传,状态是默认的 200,无需显式设置;响应的格式是:HTTP/1.1
        if (rangeSwitch != 0) {
            response.setStatus(responseStatus);
            // 不是从最开始下载，断点下载响应号为206
            // 响应的格式是:
            // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
//			logger.info("----------------------------片段下载，服务器即将开始断点续传...");
            switch (rangeSwitch) {
                case 1: {// 针对 bytes=27000- 的请求
                    String contentRange = new StringBuffer("bytes ")
                            .append(new Long(pastLength).toString()).append("-")
                            .append(new Long(fileLength - 1).toString())
                            .append("/").append(new Long(fileLength).toString())
                            .toString();
                    response.setHeader("Content-Range", contentRange);
                    break;
                }
                case 2: {// 针对 bytes=27000-39000 的请求
                    String contentRange = range.replace("=", " ") + "/"
                            + new Long(fileLength).toString();
                    response.setHeader("Content-Range", contentRange);
                    break;
                }
                default: {
                    break;
                }
            }
        } else {
            String contentRange = new StringBuffer("bytes ").append("0-")
                    .append(fileLength - 1).append("/").append(fileLength)
                    .toString();
            response.setHeader("Content-Range", contentRange);
            // 是从开始下载
//			logger.info("----------------------------是从开始到最后完整下载！");
        }

        try {
            // /////////////////////////设置文件Content-Type///////////////////////////
            String finalFileName = null;
            //获得浏览器代理信息
            final String userAgent = request.getHeader("USER-AGENT");
            if( StrUtil.contains(userAgent, "MSIE")||StrUtil.contains(userAgent,"Trident")||StrUtil.contains(userAgent,"Edge")){//IE浏览器
                finalFileName = URLEncoder.encode(downloadFile.getName(),"UTF8");
            }else if(StrUtil.contains(userAgent, "Mozilla")){//google,火狐浏览器
                finalFileName = new String(downloadFile.getName().getBytes("GBK"), "iso8859-1");
            }else{
                finalFileName = URLEncoder.encode(downloadFile.getName(),"UTF8");//其他浏览器
            }
            response.setHeader("Content-Disposition" ,"attachment;filename=" +finalFileName+"");//下载文件的名称
            response.setHeader("Content-Length", String.valueOf(contentLength));
            os = response.getOutputStream();
            out = new BufferedOutputStream(os);
            raf = new RandomAccessFile(downloadFile, "r");
            try {
                long outLength = 0;// 实际输出字节数
                switch (rangeSwitch) {
                    case 0: {// 普通下载，或者从头开始的下载
                        // 同1，没有break
                    }
                    case 1: {// 针对 bytes=27000- 的请求
                        raf.seek(pastLength);// 形如 bytes=969998336- 的客户端请求，跳过
                        // 969998336 个字节
                        int n = 0;
                        while ((n = raf.read(b)) != -1) {
                            out.write(b, 0, n);
                            outLength += n;
                        }
                        // while ((n = raf.read(b, 0, 1024)) != -1) {
                        // out.write(b, 0, n);
                        // }
                        break;
                    }
                    case 2: {
                        // 针对 bytes=27000-39000 的请求，从27000开始写数据
                        raf.seek(pastLength);
                        int n = 0;
                        long readLength = 0;// 记录已读字节数
                        while (readLength <= contentLength - bsize) {// 大部分字节在这里读取
                            n = raf.read(b);
                            readLength += n;
                            out.write(b, 0, n);
                            outLength += n;
                        }
                        if (readLength <= contentLength) {// 余下的不足 1024 个字节在这里读取
                            n = raf.read(b, 0, (int) (contentLength - readLength));
                            out.write(b, 0, n);
                            outLength += n;
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
//				logger.info("Content-Length为：" + contentLength + "；实际输出字节数：" + outLength);
                out.flush();
            } catch (IOException ie) {
                /**
                 * 在写数据的时候， 对于 ClientAbortException 之类的异常，
                 * 是因为客户端取消了下载，而服务器端继续向浏览器写入数据时， 抛出这个异常，这个是正常的。
                 * 尤其是对于迅雷这种吸血的客户端软件， 明明已经有一个线程在读取 bytes=1275856879-1275877358，
                 * 如果短时间内没有读取完毕，迅雷会再启第二个、第三个。。。线程来读取相同的字节段， 直到有一个线程读取完毕，迅雷会 KILL
                 * 掉其他正在下载同一字节段的线程， 强行中止字节读出，造成服务器抛 ClientAbortException。
                 * 所以，我们忽略这种异常
                 */
                // ignore
            }
        } catch (Exception e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                }
            }
        }

    }

    /**
     * 保存文件对象
     *
     * @param file     需要保存的文件对象
     * @param path     存储的中段路径
     * @param fileName 存储的文件名
     * @Author weegoo
     * @Date 2020/8/20 14:47
     */
    @Override
    public File save(File file, String path, String fileName) {
        logger.debug ( "开始向本地存储保存文件: {}{}{}", location + File.separator + accessoryBaseDir + File.separator + path, File.separator, fileName );
        long begin = System.currentTimeMillis ( );
        try {
            File destFile = createDestFile ( path, fileName );
            checkFile ( destFile );
            FileUtils.copyFile ( file, destFile );
            return destFile;
        } catch (IOException e) {
            e.printStackTrace ( );
        } finally {
            logger.debug ( "完成从本地存储获取文件: {}", System.currentTimeMillis ( ) - begin );
        }
        return null;
    }

    /**
     * 保存文件流文件为文件对象
     *
     * @param file     需要保存的文件对象
     * @param path     存储的中段路径
     * @param fileName 存储的文件名
     * @Author weegoo
     * @Date 2020/8/20 14:49
     */
    @Override
    public File save(MultipartFile file, String path, String fileName) {
        logger.debug ( "开始向本地存储保存文件: {}{}{}", location + File.separator + accessoryBaseDir + File.separator + path, File.separator, fileName );
        long begin = System.currentTimeMillis ( );
        try {
            File destFile = createDestFile ( path, fileName );
            checkFile ( destFile );
            FileUtils.copyInputStreamToFile ( file.getInputStream ( ), destFile );
            return destFile;
        } catch (IOException e) {
            e.printStackTrace ( );
        } finally {
            logger.debug ( "完成从本地存储获取文件: {}", System.currentTimeMillis ( ) - begin );
        }
        return null;
    }

    /**
     * 保存输入流为文件对象
     *
     * @param is       需要保存的文件对象
     * @param path     存储的中段路径
     * @param fileName 存储的文件名
     * @Author weegoo
     * @Date 2020/8/20 14:47
     */
    @Override
    public File save(InputStream is, String path, @NotNull String fileName) {
        logger.debug ( "开始向本地存储保存文件: {}{}{}", location + File.separator + accessoryBaseDir + File.separator + path, File.separator, fileName );
        long begin = System.currentTimeMillis ( );
        try {
            File destFile = createDestFile ( path, fileName );
            checkFile ( destFile );
            FileUtils.copyInputStreamToFile ( is, destFile );
            return destFile;
        } catch (IOException e) {
            e.printStackTrace ( );
        } finally {
            logger.debug ( "完成从本地存储获取文件: {}", System.currentTimeMillis ( ) - begin );
        }
        return null;
    }

    @Override
    public String writeByUrl(MultipartFile file, String url) {
        return null;
    }

    /**
     * 根据存储地址, 根路径和文件名, 创建本地文件
     *
     * @param path     存储的中段路径
     * @param fileName 存储的文件名
     * @return java.io.File
     * @Author weegoo
     * @Date 2020/8/26 11:57
     **/
    private File createDestFile(String path, String fileName) {
        File rootDir = new File ( location + File.separator + accessoryBaseDir );
        if ( path == null ) {
            path = "";
        }
        File destDir = new File ( rootDir, path );
        FileUtils.createDirectory ( destDir.getAbsolutePath ( ) );
        return new File ( destDir, fileName );
    }

    /**
     * 校验文件是否存在, 并给出异常信息
     *
     * @param file 需要校验的文件
     * @Author weegoo
     * @Date 2020/8/26 11:57
     **/
    private void checkFile(File file) {
        if ( file.exists ( ) ) {
            logger.warn ( "指定的文件已存在, 本次操作将会覆盖: {}", file.getAbsolutePath ( ) );
            if ( file.isDirectory ( ) ) {
                logger.error ( "指定的文件是文件夹, 保存文件操作无法进行." );
                throw new RuntimeException ( "指定的文件是文件夹, 保存文件操作无法进行." );
            }
        }
    }
}
