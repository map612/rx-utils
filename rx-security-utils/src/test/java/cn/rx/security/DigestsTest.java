package cn.rx.security;

import org.junit.Test;

import java.util.Collections;

/**
 * Created by Michael on 11/6/14.
 */
public class DigestsTest {

    @Test
    public void digestString() {
        String input = "111111";
        System.out.println(new String(input.getBytes()));

        byte[] md5Result = Digests.md5(input.getBytes());
        System.out.println("md5 in byte result                               :" + Encodes.encodeHex(md5Result));



        byte[] sha1Result = Digests.sha1(input.getBytes());
        System.out.println("sha1 in byte result                               :" + Encodes.encodeHex(sha1Result));

        byte[] salt = Digests.generateSalt(8);
        System.out.println("salt in byte                                      :" + Encodes.encodeHex(salt));
        sha1Result = Digests.sha1(input.getBytes(), salt);
        System.out.println("sha1 in byte result with salt                     :" + Encodes.encodeHex(sha1Result));

        sha1Result = Digests.sha1(input.getBytes(), salt, 1024);
        System.out.println("sha1 in byte result with salt and 1024 interations:" + Encodes.encodeHex(sha1Result));

    }
}
