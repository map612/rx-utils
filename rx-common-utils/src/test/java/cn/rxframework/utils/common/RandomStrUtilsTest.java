package cn.rxframework.utils.common;

import cn.rxframework.utils.common.str.RandomStrUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/7/8<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class RandomStrUtilsTest {

    public static void main(String[] args) throws Exception {

        System.out.println(RandomStrUtils.randomString(6));
        System.out.println(RandomStrUtils.randomNumber(6));
        System.out.println(RandomStrUtils.random(6));

        System.out.println(StringUtils.join(RandomStrUtils.unique("111111", ""), ","));
        System.out.println(RandomStrUtils.unique("111111"));

        for (int i = 0; i < 20; i++) {
            System.out.println(System.currentTimeMillis());
            System.out.println(RandomStrUtils.unique());
            Thread.sleep(1);
        }
    }
}
