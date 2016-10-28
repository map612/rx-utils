package cn.rx.common.math;

import java.math.RoundingMode;
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
    public static String dfmt = "#0.00";
    public static DecimalFormat df = new DecimalFormat(dfmt);

    static {
        df.setRoundingMode(RoundingMode.HALF_UP);
    }

    public static Double format(Double num) {
        return Double.valueOf(df.format(num).toString());
    }

    public static Double format(Double num, String format) {
        DecimalFormat cdf = new DecimalFormat(format);
        cdf.setRoundingMode(RoundingMode.HALF_UP);
        return Double.valueOf(cdf.format(num).toString());
    }

    public static String format2str(Double num) {
        return format2str(num, true);
    }

    public static String format2str(Double num, boolean groupingUsed) {
        return format2str(num, dfmt, groupingUsed);
    }

    public static String format2str(Double num, String format, boolean groupingUsed) {
        DecimalFormat df = new DecimalFormat(format);
        df.setGroupingUsed(groupingUsed);
        df.setGroupingSize(3);
        return df.format(num);
    }
}
