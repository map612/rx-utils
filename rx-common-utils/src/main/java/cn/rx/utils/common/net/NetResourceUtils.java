package cn.rx.utils.common.net;

import cn.rx.utils.rest.http.HttpGetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @author richard.xu
 * @version 1.0.0
 * @title URL文件下载
 * @description
 * @serial
 * @since 2013-6-26
 */
public class NetResourceUtils {

    private static Logger log = LoggerFactory.getLogger(NetResourceUtils.class);

    /**
     * 网络资源存储到本地targetPath目录
     *
     * @param urls
     * @param targetPath
     */
    public static void getNetResource(String[] urls, String targetPath) {
        for (String url : urls) {
            getNetResource(url, targetPath);
        }
    }

    /**
     * 网络资源存储到本地targetPath目录
     *
     * @param url
     * @param targetPath
     */
    public static void getNetResource(String url, String targetPath) {
        String localFileName = url.substring(url.lastIndexOf("/") + 1);
        getNetResource(url, targetPath, localFileName);
    }

    /**
     * 网络资源存储到本地targetPath目录
     *
     * @param url
     * @param targetPath
     * @param localFileName 保存在本地的文件名
     */
    public static void getNetResource(String url, String targetPath, String localFileName) {
        FileOutputStream fos = null;
        File srcDir = null;
        log.info("get URL file start ...");
        HttpGetUtils get = null;
        try {
            srcDir = new File(targetPath);
            if (!srcDir.exists()) srcDir.mkdirs();

            String filename = targetPath + File.separator + localFileName;
            File outputFile = new File(filename);
            if (outputFile.exists()) {
                log.debug("Download file failed, file '" + filename + "' exist already");
            }
            fos = new FileOutputStream(outputFile);

            get = new HttpGetUtils();
            //.USE_PROXY ? new HttpGet(SysConstants.LOCAL_PROXY, SysConstants.LOCAL_PORT) : new HttpGet();
            fos.write(get.getBody(url));

        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        log.info("get URL file '" + url + "' successfully.");
    }

    /**
     * 网络资源获取
     *
     * @param url
     */
    public static byte[] getNetResource(String url) {
        log.info("get URL file start ...");
        try {
            byte[] data = new HttpGetUtils().getBody(url);
            log.info("get URL file '" + url + "' successfully.");
            return data;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
