package cn.rxframework.utils.rest.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by richard.xu on 15/11/11.
 */
public class HttpPostUtils {
    private static Log log = LogFactory.getLog(HttpPostUtils.class);

    /**
     * 提交文件Form
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, Map<String, Object> data) {
        String response = null;
        HttpPost post = new HttpPost(url);
        try {
            log.debug(StringUtils.join("Post form start ..."));
            log.debug(StringUtils.join("Post form url : ", url));
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求的编码格式
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
            for (String field : data.keySet()) {
                Object obj = data.get(field);
                if (obj instanceof File) {
                    File file = (File) obj;
                    FileBody fileBody = new FileBody(file);
                    builder.addPart("file", fileBody);
                } else {
                    //注意指定参数名称编码方式
                    builder.addPart(field, new StringBody(
                            String.valueOf(obj), ContentType.create(
                            "text/plain", Consts.UTF_8)));
                }
            }
            HttpEntity reqEntity = builder.build();
            post.setEntity(reqEntity);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            System.out.println(httpResponse.getStatusLine());
            // 获取响应对象
            HttpEntity resEntity = httpResponse.getEntity();
            if (resEntity != null) {
                response = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                // 打印响应长度
                System.out.println("Response content length: " + response.length());
                // 打印响应内容
                System.out.println(response);
            }
            // 销毁
            EntityUtils.consume(resEntity);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            post.releaseConnection();
        }
        return response;
    }

}
