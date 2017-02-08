package cn.rxframework.utils.common;

import cn.rxframework.utils.rest.http.HttpGetUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/5/23<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */
public class HttpUtilsTest {

    @Test
    public void testHttpGet() throws IOException {
//		HttpGet http = new HttpGet("192.168.20.6", 3128);
        HttpGetUtils http = new HttpGetUtils();

        Map map = new HashMap();
        String url = "http://test.base.zhisland.com/api/user/login";

        // System.out.println(URLEncoder.encode("map612@163.com","UTF-8"));

        map.put("app_key", "a22d3ssd4sdf1sf8");
        map.put("login_type", "email");
        map.put("login_account", "111.com");
        map.put("password", "111");

        String str = http.getBodyToString(url, "UTF-8", map, "POST");

        System.out.println(str);
    }
}
