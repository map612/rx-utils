package cn.rxframework.utils.security;

import cn.rxframework.utils.security.MD5Utils;
import org.junit.Test;

public class MD5UtilsTest {

    @Test
    public void md5str(){
        System.out.println(MD5Utils.digest("111111"));
        System.out.println(MD5Utils.digestUpper32("111111"));
        System.out.println(MD5Utils.digestLower32("111111"));
        System.out.println(MD5Utils.digestUpper16("111111"));
        System.out.println(MD5Utils.digestLower16("111111"));
    }
}
