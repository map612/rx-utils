package cn.rx.qrcode;

import com.swetake.util.Qrcode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 二维码生成器
 */

public class QREncoder {

    private static Logger logger = LoggerFactory.getLogger(QREncoder.class);

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content
     * @param imgPath
     */
    public static boolean encoderQRCode(String content, String imgPath) {
        logger.info("imgPath: " + imgPath);
        boolean flag = false;
        try {
            BufferedImage bufImg = encoderQRCode(content);
            File imgFile = new File(imgPath);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, "png", imgFile);
            flag = true;
        } catch (Exception e) {
            logger.error("", e);
        }
        return flag;
    }

    public static BufferedImage encoderQRCode(String content) {
        logger.debug("encoderQRCode content: " + content);

        BufferedImage bufImg = null;
        try {
            Qrcode qrcoder = new Qrcode();
            qrcoder.setQrcodeErrorCorrect('M');
            qrcoder.setQrcodeEncodeMode('B');
            qrcoder.setQrcodeVersion(6);

            byte[] contentBytes = content.getBytes("UTF-8");
            bufImg = new BufferedImage(139, 139, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, 139, 139);

            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量 不设置可能导致解析出错
            int pixoff = 8;
            // 输出内容> 二维码
            if (contentBytes.length > 0 && contentBytes.length < 120) {
                boolean[][] codeOut = qrcoder.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                logger.error("QRCode content bytes length = " + contentBytes.length + " not in [ 0,120 ]. ");
            }
            gs.dispose();
            bufImg.flush();
        } catch (Exception e) {
            logger.error("", e);
        }
        return bufImg;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        String content = "https://www.baidu.com/s?wd=fighting";
        String imgPath = "/Users/richard.xu/Downloads/temp/qr_lipengcheng.png";
        long start = System.currentTimeMillis();
        System.out.println("========encoder start...");
        System.out.println("========content length: " + content.length());
        encoderQRCode(content, imgPath);
        System.out.println("========encoder success!!!");
        long end = System.currentTimeMillis();
        System.out.println("花费时间：" + (end - start) + " ms");
    }

}
