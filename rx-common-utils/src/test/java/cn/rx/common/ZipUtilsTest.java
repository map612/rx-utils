package cn.rx.common;

import cn.rx.common.file.ZipUtils;
import org.junit.Test;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/5/23<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class ZipUtilsTest {
    @Test
    public void main(String[] args) {
//		String fileDownloadPath = "d:/aaa";
//		String fileZipPath = "d:/ccc";
//		String[] urls = {
//				"http://oss.aliyuncs.com/cdc-feedback/125/335/823/49ac8cd822c85f94356d7407eaecbb0f.jpg",
//				"http://oss.aliyuncs.com/cdc-feedback/125/335/823/f340d75a82067bffc2ae7504ec5b170f.jpg"
//				};

//		String[] localurls = {
//				"d:/aaa/f340d75a82067bffc2ae7504ec5b170f.jpg",
//				"d:/aaa/49ac8cd822c85f94356d7407eaecbb0f.jpg" };
//		getNetResource("http://oss.aliyuncs.com/cdc-feedback/125/335/823/49ac8cd822c85f94356d7407eaecbb0f.jpg", fileDownloadPath);
//		getNetResource(urls, fileDownloadPath);
        long s = System.currentTimeMillis();
        ZipUtils.zipDir("E:/aa/581_内部演示BBS_复旦大学", "E:/aa", "581_内部演示BBS_复旦大学.zip", false);
        System.out.println(System.currentTimeMillis()-s);
//		File[] files = new File[]{new File(localurls[0]), new File(localurls[1])};
//		zipFiles(files, fileZipPath, "zipfile", true);
//		zipNetfile(urls, fileDownloadPath, fileZipPath, "zipNetfile", true);

//		String dir = "E:/工作内容/校园反馈需求/消息和压缩部分";
//		zipDir(dir + "/sendwarning", dir, "sendwarning.zip", false);
    }
}
