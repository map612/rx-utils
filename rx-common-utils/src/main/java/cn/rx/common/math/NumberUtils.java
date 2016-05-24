package cn.rx.common.math;

import java.text.DecimalFormat;

/**
 * 数字格式化
 * <p/>
 * 创建时间: 16/4/25<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class NumberUtils {
    private static DecimalFormat df = new DecimalFormat("###############0.00");

    /**
     * 保留两位小数
     * @param num
     * @return
     */
    public static Double formatNumber(Double num){
        return Double.valueOf(df.format(num).toString());
    }

    /**
     * 格式化数字
     * @param num
     * @return
     */
    public static Double formatNumber(Double num, String format){
        return Double.valueOf(new DecimalFormat(format).format(num).toString());
    }
}
