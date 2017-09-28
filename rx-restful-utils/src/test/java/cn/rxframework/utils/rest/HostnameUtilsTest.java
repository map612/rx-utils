package cn.rxframework.utils.rest;

import java.io.IOException;

import org.junit.Test;

import cn.rxframework.utils.rest.common.HostnameUtils;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/5/23<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */
public class HostnameUtilsTest {

    @Test
    public void test() throws IOException {
        System.out.println(HostnameUtils.getHostname());
        System.out.println(HostnameUtils.getShortHostname());
    }
}
