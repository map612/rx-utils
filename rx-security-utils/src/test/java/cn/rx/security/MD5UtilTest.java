package cn.rx.security;

import org.junit.Test;

public class MD5UtilTest {

    @Test
    public void md5str(){
        System.out.println(MD5Util.digest("111111"));
        System.out.println(MD5Util.digestUpper32("111111"));
        System.out.println(MD5Util.digestLower32("111111"));
        System.out.println(MD5Util.digestUpper16("111111"));
        System.out.println(MD5Util.digestLower16("111111"));
    }
}
