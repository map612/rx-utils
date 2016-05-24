package cn.rx.common;

import cn.rx.common.file.FilePathUtils;
import org.junit.Test;

import java.io.File;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/5/23<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */
public class FilePathUtilsTest {

    @Test
    public void testFilePath() {
        String fileName = "D:/apache-tomcat-7.0.23/webapps/ROOT/report rets/attachment/report/test/";
        System.out.println(FilePathUtils.getExtension("asdfasfd.PDF"));
        System.out.println(FilePathUtils.getWithoutExtension("D:/apache-tomcat-7.0.23/asdfasfd.PDF"));
        System.out.println(FilePathUtils.getFilename("D:/apache-tomcat-7.0.23/asdfasfd.PDF"));
        System.out.println(FilePathUtils.getFilenameWithExtension("D:/apache-tomcat-7.0.23/asdfasfd.PDF"));
        System.out.println(new File(fileName).getPath());
        String[] paths = {"win", "t", "a", "teswe.te"};
        System.out.println(FilePathUtils.bulidFullPath(paths));
    }
}
