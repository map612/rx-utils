package cn.rxframework.utils.security;

import cn.rxframework.utils.security.AESUtils;
import org.junit.Test;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/5/24<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */
public class AESUtilsTest {

    @Test
    public void testAES() throws Exception {
        String content = "我爱你";
        System.out.println("加密前：" + content);

        String key = "123456";
        System.out.println("加密密钥和解密密钥：" + key);

        String encrypt = AESUtils.aesEncrypt(content, key);
        System.out.println("加密后：" + encrypt);

        String decrypt = AESUtils.aesDecrypt(encrypt, key);
        System.out.println("解密后：" + decrypt);
    }
}
