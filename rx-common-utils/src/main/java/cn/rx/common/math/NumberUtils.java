package cn.rx.common.math;

import java.text.DecimalFormat;

/**
 * 数字格式化
 * <p/>
 * 创建时间: 16/4/25<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */
public class NumberUtils {
    public static DecimalFormat df = new DecimalFormat("###############0.00");

    /**
     * 保留两位小数
     * @param num
     * @return
     */
    public static Double format(Double num){
        return Double.valueOf(format2str(num));
    }

    public static String format2str(Double num){
        return df.format(num);
    }

    /**
     * 格式化数字
     * @param num
     * @return
     */
    public static Double format(Double num, String format){
        return Double.valueOf(format2str(num, format).toString());
    }

    public static String format2str(Double num, String format){
        DecimalFormat formatter = new DecimalFormat(format);
        return formatter.format(num);
    }
}
