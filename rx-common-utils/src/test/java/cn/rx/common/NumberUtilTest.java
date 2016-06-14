package cn.rx.common;

import cn.rx.common.math.NumberUtils;
import org.junit.Test;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/5/27<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class NumberUtilTest {

    public static void main(String[] args) {
        System.out.println(NumberUtils.format2str(88888888.888d));
        System.out.println(NumberUtils.format2str(88888888.888d, ".00"));
    }
}
