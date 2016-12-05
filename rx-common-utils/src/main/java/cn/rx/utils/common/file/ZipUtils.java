package cn.rx.utils.common.file;

import cn.rx.utils.common.net.NetResourceUtils;
import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;


/**
 * @author richard.xu
 * @version 1.0.0
 * @title URL文件下载、文件文件夹压缩
 * @description
 * @serial
 * @since 2013-6-26
 */
public class ZipUtils {

    private static Logger log = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * 压缩网络资源到本地
     *
     * @param netFileurls 网络源文件路径数组
     * @param tmpPath     临时存储目录
     * @param targetPath  目标路径
     * @param zipFileName 目标文件名称（无后缀）
     * @param deleteTmp   是否删除临时文件夹
     */
    public static void zipNetfile(String[] netFileurls, String tmpPath, String targetPath, String zipFileName, boolean deleteTmp) {
        try {
            log.info("zip netFileurls start ...");
            NetResourceUtils.getNetResource(netFileurls, tmpPath);
            File[] tmpurls = getTmpUrls(tmpPath, netFileurls);
            zipFiles(tmpurls, targetPath, zipFileName);
            if (deleteTmp) {
                FileUtils.deleteDirectory(new File(tmpPath));
            }
            log.info("zip netFileurls end .");
        } catch (Exception e) {
            log.error("zip netFileurls failed", e);
        }
    }

    private static File[] getTmpUrls(String tmpPath, String[] netFileurls) {
        List<File> list = new ArrayList<File>();
        for (String netUrl : netFileurls) {
            String filename = netUrl.substring(netUrl.lastIndexOf("/"));
            list.add(new File(tmpPath + filename));
        }
        return list.toArray(new File[]{});
    }

    /**
     * 整个文件夹压缩
     *
     * @param srcPath     源文件夹路径
     * @param targetPath  目标路径
     * @param zipFileName 目标文件名称（无后缀）
     * @param delete      压缩后删除源目录
     */
    public static void zipDir(String srcPath, String targetPath, String zipFileName, boolean delete) {
        File srcDir = new File(srcPath);
        try {
            log.info("zip dir start ...");

            if (srcDir.exists() && srcDir.isDirectory()) {
                zipDir(srcDir, targetPath, zipFileName);
                log.info("zip dir '" + srcPath + "' to '" + zipFileName + "' successfully.");

                if (delete)
                    srcDir.delete();
            } else {
                log.info("zip dir error, directory '" + srcPath + "' not exists");
            }

            log.info("zip dir end ...");
        } catch (Exception e) {
            log.error("zip dir '" + srcPath + "' failed", e);
        }
    }

    private static void zipDir(File srcPath, String targetPath,
                               String zipFileName) throws Exception {
        ZipOutputStream out = new ZipOutputStream(
                new FileOutputStream(targetPath + "/" + zipFileName));
        out.setEncoding("GBK");
        zip(out, srcPath, "", true);
        out.close();
        System.out.println("zip done");
    }

    private static void zip(ZipOutputStream out, File f, String base, boolean flag)
            throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            if (!flag) {
                out.putNextEntry(new ZipEntry(base + "/"));
            }
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                String filename = new String(fl[i].getName().getBytes(System.getProperty("file.encoding")), "GBK");
                zip(out, fl[i], base + filename, false);
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(in);
            int b;
            System.out.println(base);
            while ((b = bis.read()) != -1) {
                out.write(b);
            }
            bis.close();
            in.close();
        }
    }

    /**
     * 本地目标文件列表压缩
     *
     * @param files       源文件数组
     * @param targetPath  目标路径
     * @param zipFileName 目标文件名称（无后缀）
     */
    public static String zipFiles(File[] files, String targetPath, String zipFileName) {
        return zipFiles(files, targetPath, zipFileName, false);
    }

    public static String zipFiles(List<File> files, String targetPath, String zipFileName) {
        return zipFiles(files.toArray(new File[]{}), targetPath, zipFileName);
    }

    public static String zipFiles(List<File> files, String targetPath, String zipFileName, boolean delete) {
        return zipFiles(files.toArray(new File[]{}), targetPath, zipFileName, delete);
    }

    /**
     * 本地目标文件列表压缩
     *
     * @param files       源文件数组
     * @param targetPath  目标路径
     * @param zipFileName 目标文件名称（无后缀）
     * @param delete      压缩后删除源文件
     */
    public static String zipFiles(File[] files, String targetPath, String zipFileName, boolean delete) {
        File targetDir = new File(targetPath);
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }

        String zipFilename = targetPath + File.separator + zipFileName + ".zip";
        try {
            log.info("zip files start ...");

            FileOutputStream fos = new FileOutputStream(zipFilename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            ZipOutputStream zos = new ZipOutputStream(bos);// 压缩包
            zos.setLevel(Deflater.BEST_COMPRESSION);
            byte[] buf = new byte[1024];
            int len;
            for (File file : files) {
                if (file.exists()) {
                    log.info("zipping file -- " + file.getAbsolutePath());
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    ZipEntry ze = new ZipEntry(file.getName());// 这是压缩包名里的文件名
                    zos.putNextEntry(ze);// 写入新的 ZIP 文件条目并将流定位到条目数据的开始处

                    while ((len = bis.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                        zos.flush();
                    }
                    if (fis != null) fis.close();
                    if (bis != null) bis.close();
                } else {
                    log.error("zip file not exists, file path : " + file.getAbsolutePath());
                }
            }
            if (zos != null) zos.close();

            for (File file : files) {
                if (delete && file.exists()) {
                    FileUtils.deleteQuietly(file);
                }
            }

            log.info("zip files end. " + files.length + " file zipped");
            log.info("zip files as " + zipFilename);
        } catch (Exception e) {
            log.error("zip files failed", e);
        }

        return zipFilename;
    }
}
