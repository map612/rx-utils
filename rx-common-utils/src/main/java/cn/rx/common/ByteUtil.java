package cn.rx.common;

/**
 * Byte[] String hexString 互转
 * <p/>
 * 创建时间: 16/1/7<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class ByteUtil {

    /**
     * byte[] 转为String
     */
    public static String Bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * String 转为 HexString
     */
    public static String Str2HexString(String string) {
        return Bytes2HexString(string.getBytes());
    }

    /**
     * 把16进制字符串转换成字符串
     *
     * @param hexString String
     * @return byte[]
     */
    public static byte[] HexString2Bytes(String hexString) {
        int len = (hexString.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexString.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
}
