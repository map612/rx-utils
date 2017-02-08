package cn.rxframework.utils.qrcode;

import cn.rxframework.utils.qrcode.QRCodeUtils;
import cn.rxframework.utils.qrcode.QRDecoder;
import org.junit.Test;

public class QRCodeUtilsTest {

    @Test
    public void testQRCodeUtil(){
        QRCodeUtils qrCoder = new QRCodeUtils(300);
        // encode
        String content = "https://www.baidu.com/s?wd=richard.xu";
        String imgPath = "/Users/richard.xu/Downloads/temp/qrcode_test.png";
        long start = System.currentTimeMillis();
        System.out.println("========encoder start...");
        System.out.println("========content length: " + content.length());
        qrCoder.encoderQRCode(content, imgPath);
        System.out.println("========encoder success!!!");
        long end = System.currentTimeMillis();
        System.out.println("花费时间：" + (end - start) + " ms");

        //decode
        start = System.currentTimeMillis();
        System.out.println("========decoder start...");
        String decoderContent = qrCoder.decoderQRCode(imgPath);
        System.out.println("解析结果：" + decoderContent);
        System.out.println("========decoder success!!!");
        end = System.currentTimeMillis();
        System.out.println("花费时间：" + (end - start) + " ms");
    }

    @Test
    public void testQRDecoder() {
        String imgPath = "/Users/richard.xu/Downloads/temp/qr_test2.png";
        long start = System.currentTimeMillis();
        System.out.println("========decoder start...");
        String decoderContent = QRDecoder.decoderQRCode(imgPath);
        System.out.println("解析结果：" + decoderContent);
        System.out.println("========decoder success!!!");
        long end = System.currentTimeMillis();
        System.out.println("花费时间：" + (end - start) + " ms");
    }
}
