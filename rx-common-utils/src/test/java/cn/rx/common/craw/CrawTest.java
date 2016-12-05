package cn.rx.common.craw;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/7/13<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class CrawTest {
    public static void main(String[] args) throws IOException {
        Map<String, String[]> provinces = new HashMap<String, String[]>();
//        provinces.put("陕西", "西安,铜川,宝鸡,咸阳,渭南,延安,汉中,榆林,安康,商洛,兴平,杨凌".split(","));
//        provinces.put("甘肃", "兰州,嘉峪关,金昌,白银,天水,武威,张掖,平凉,酒泉,庆阳,定西,陇南,临夏,甘南".split(","));
//        provinces.put("青海", "西宁,海东,海北,黄南,海南州,果洛,玉树,海西".split(","));
//        provinces.put("宁夏", "银川,石嘴山,吴忠,固原,中卫".split(","));
//        provinces.put("新疆", "乌鲁木齐,克拉玛依,吐鲁番,哈密,昌吉,博尔塔拉,巴音郭楞,阿克苏,克孜勒苏,喀什,和田,伊犁,塔城,阿勒泰,石河子,奎屯市,乌苏,阿拉尔,图木舒克,五家渠".split(","));
//        provinces.put("河南", "郑州,开封,洛阳,平顶山,安阳,鹤壁,新乡,焦作,濮阳,许昌,漯河,三门峡,南阳,商丘,信阳,周口,驻马店,济源,西平,长葛".split(","));
//        provinces.put("重庆", "重庆".split(","));
//        provinces.put("四川", "成都,自贡,攀枝花,泸州,德阳,绵阳,广元,遂宁,内江,乐山,南充,眉山,宜宾,广安,达州,雅安,巴中,资阳,阿坝,甘孜,凉山,峨眉,西昌,简阳".split(","));
//        provinces.put("贵州", "贵阳,六盘水,遵义,安顺,铜仁,黔西南,毕节,黔东南,黔南".split(","));
//        provinces.put("云南", "昆明,曲靖,玉溪,保山,昭通,楚雄,红河,文山,思茅,西双版纳,大理,德宏,丽江,怒江,迪庆,临沧,普洱".split(","));
//        provinces.put("西藏", "拉萨,昌都,山南,日喀则,那曲,阿里,林芝".split(","));
        provinces.put("湖南", "长沙,株洲,湘潭,衡阳,邵阳,岳阳,常德,张家界,益阳,郴州,永州,怀化,娄底,湘西".split(","));

        String[] dimensions = {"软件", "科技"};

        String url = "http://sou.zhaopin.com/jobs/searchresult.ashx?jl=%s&kw=%s&p=%s&kt=2&isadv=0";

        for(String province : provinces.keySet()){
            String[] cities = provinces.get(province);
            for(String dimension : dimensions){
                ExecutorService executor = Executors.newFixedThreadPool(provinces.size());
                executor.execute(new ProvinceCrawThread(province, dimension, cities, url));
            }
        }
    }


}

class ProvinceCrawThread implements Runnable {

    private String province;
    private String dimension;
    private String[] cities;
    private String url;

    public ProvinceCrawThread(String province, String dimension, String[] cities, String url) {
        this.province = province;
        this.dimension = dimension;
        this.cities = cities;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            Map<String, Integer> companies = new HashMap<String, Integer>();
            for(String city : cities){
                String lLastCompany = "";
                String cLastCompany = "-";
                String lLastPosition = "";
                String cLastPosition = "-";
                int page = 1;
                while (!StringUtils.equals(lLastCompany, cLastCompany) || !StringUtils.equals(lLastPosition, cLastPosition)){
                    String rurl = String.format(url, city, dimension, page);
                    System.out.println(rurl);

                    Document document = Jsoup.connect(rurl).get();
                    Elements elements = document.getElementsByAttributeValue("class", "newlist");
                    if(elements.size()==0){
                        break;
                    }
                    for(int i = 1; i < elements.size(); i++){
                        Element element = elements.get(i);
                        String gsmc = element.getElementsByAttributeValue("class", "gsmc").get(0).getElementsByTag("a").html().replaceAll("\\<.*?>", "");
                        String zwmc = element.getElementsByAttributeValue("class", "zwmc").get(0).getElementsByTag("a").html().replaceAll("\\<.*?>", "");

                        System.out.println(gsmc + " - " + System.currentTimeMillis());
                        String key = StringUtils.join(StringUtils.trimToEmpty(gsmc), ",", city);
                        if(!companies.containsKey(key)){
                            companies.put(key, 0);
                        }
                        companies.put(key, companies.get(key) + 1);

                        if(i == elements.size()-1){
                            lLastCompany = cLastCompany;
                            cLastCompany = gsmc;
                            lLastPosition = cLastPosition;
                            cLastPosition = zwmc;
                        }
                    }
                    page++;
                }
            }

            List<String> list = new ArrayList<String>();
            for(String key : companies.keySet()){
                list.add(StringUtils.join(key, ",", companies.get(key)));
            }

            FileUtils.writeLines(new File("/Users/xule/Downloads/company_list_" + province + "_" + dimension + ".csv"), "GBK", list);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}