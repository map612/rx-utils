package cn.rx.utils.security;

import org.junit.Test;

/**
 * Created by Michael on 11/6/14.
 */
public class DigestUtilsTest {

    @Test
    public void digestString() {
        String input = "111111";
        System.out.println(new String(input.getBytes()));

        byte[] md5Result = DigestUtils.md5(input.getBytes());
        System.out.println("md5 in byte result                               :" + EncodeUtils.encodeHex(md5Result));



        byte[] sha1Result = DigestUtils.sha1(input.getBytes());
        System.out.println("sha1 in byte result                               :" + EncodeUtils.encodeHex(sha1Result));

        byte[] salt = DigestUtils.generateSalt(8);
        System.out.println("salt in byte                                      :" + EncodeUtils.encodeHex(salt));
        sha1Result = DigestUtils.sha1(input.getBytes(), salt);
        System.out.println("sha1 in byte result with salt                     :" + EncodeUtils.encodeHex(sha1Result));

        sha1Result = DigestUtils.sha1(input.getBytes(), salt, 1024);
        System.out.println("sha1 in byte result with salt and 1024 interations:" + EncodeUtils.encodeHex(sha1Result));

    }
}
