package cn.rx.common;

import cn.rx.common.math.NumberUtils;
import org.junit.Test;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/5/27<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */
public class NumberUtilTest {

    public static void main(String[] args) {
        System.out.println(NumberUtils.format(67.425));
        System.out.println(NumberUtils.format(0.425, "#0.00"));
        System.out.println(NumberUtils.format2str(12313.425));
        System.out.println(NumberUtils.format2str(12313.425, false));
    }
}
