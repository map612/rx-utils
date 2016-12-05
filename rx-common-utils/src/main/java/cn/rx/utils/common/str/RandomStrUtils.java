package cn.rx.utils.common.str;

/**
 * 随即字符串
 * <p/>
 * 创建时间: 16/1/7<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Random;

public class RandomStrUtils {
    /**
     * Shuffled by base letter characters
     * base letter characters: abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
     */
    private static char[] baseLetterChars = new String("rfNTABbhqWuSlCLZKjiQkypzDdPIeYGJncRwmtsMoHgvaEFOVXUx").toCharArray();

    /**
     * Shuffled by base number characters
     * base number characters: 1234567890
     */
    private static char[] baseNumberChars = new String("7564318902").toCharArray();

    private static String key = "";

    public static String randomString(int length){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0 ; i < length; ++i){
            int number = random.nextInt(baseLetterChars.length);
            sb.append(baseLetterChars[number]);
        }
        return sb.toString();
    }

    public static String randomNumber(int length){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0 ; i < length; ++i){
            int number = random.nextInt(baseNumberChars.length);
            sb.append(baseNumberChars[number]);
        }
        return sb.toString();
    }

    public static String random(int length){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        char[] chars = ArrayUtils.addAll(baseLetterChars, baseNumberChars);

        for(int i = 0 ; i < length; ++i){
            int number = random.nextInt(chars.length);
            sb.append(chars[number]);
        }
        return sb.toString();
    }

    public static String[] unique(String text, String key) {
        char[] chars = ArrayUtils.addAll(baseLetterChars, baseNumberChars);
        String hex = md5(text, key);

        String[] rdmstr = new String[4];
        for (int i = 0; i < 4; i++) {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);
            // 这里需要使用 long 型来转换，因为 Integer.parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用long ，则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars += chars[(int) index];
                // 每次循环按位右移 5 位
                lHexLong = lHexLong >> 5;
            }
            // 把字符串存入对应索引的输出数组
            rdmstr[i] = outChars;
        }
        return rdmstr;
    }

    public static String unique(String text) {
        return unique(text, key)[0];
    }

    public static String unique() {
        String time = StringUtils.join(System.currentTimeMillis(), Math.random());
        return unique(time);
    }

    private static String md5(String text, String key){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(URLEncoder.encode(text + key, "UTF-8").getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest.digest()) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
