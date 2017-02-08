package cn.rxframework.utils.common.craw;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 显示规划局 建设用地规划许可证 公示信息专区
 * <p/>
 * 创建时间: 16/7/13<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class CrawTest2 {
    public static void main(String[] args) throws Exception {

        for(int page = 0; page < 30; page++){
            Thread.sleep(500);
            String url = "http://www.xaghj.gov.cn/ptl/def/def/index_915_6681.jsp?recid=4358886&_cimake=false&pageNumber=%s";
            String rurl = String.format(url, page);
            System.out.println(rurl);

            Document document = Jsoup.connect(rurl).timeout(5000).post();
            Elements elements = document.getElementsByTag("tr");
            if(elements.size()==0){
                break;
            }
            for(int i = 1; i < elements.size(); i++){
                Element element = elements.get(i);
                String bianhao = element.child(0).text();
                String company = element.child(1).text();
                String project = element.child(2).text();

                System.out.println(String.format("P%d # %s # %s # %s", page, bianhao, company, project));
            }
        }
    }


}

