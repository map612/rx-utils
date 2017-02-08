package cn.rxframework.utils.chart;

import org.jfree.data.general.DefaultPieDataset;
import org.junit.Test;

/**
 * ChartUtils Test
 * <p/>
 * 创建时间: 16/6/16<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */
public class ChartUtilsTest {

    @Test
    public void testCreateChart() throws Exception {
        //二维饼图
        DefaultPieDataset dataset = new DefaultPieDataset();
        //(135.0~150.0) (120.0~135.0) (90.0~120.0) (0.0~90.0)
        dataset.setValue("优秀", 0.0);
        dataset.setValue("良好", 5.1);
        dataset.setValue("及格", 90.0);
        dataset.setValue("不及格", 4.9);
//        drawPieChart("/Users/richard.xu/Documents/Pie.jpg", 640, 480, true, "年级成绩分布情况", dataset);

        //二维环形图
        ChartUtils.drawRingChart("/Users/richard.xu/Documents/Ring.jpg", 640, 480, true, "年级成绩分布情况", dataset);

        //二维柱形图
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(662.5, "总成绩对比", "年级最高分");
//        dataset.addValue(533.5, "总成绩对比", "你的总分");
//        dataset.addValue(474.11, "总成绩对比", "年级平均分");
//        ChartUtils.drawBarChart("/Users/richard.xu/Documents/Bar.jpg", 640, 480, false, "你的总分在年级的水平", "", "分数(分)", dataset);

        //二维柱形图加折线图
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();//你的分数
//        String series1 = "你的分数";
//        dataset.addValue(119, series1, "语文");
//        dataset.setValue(122.5, series1, "英语");
//        dataset.setValue(59, series1, "政治");
//        dataset.setValue(74.5, series1, "地理");
//        dataset.setValue(66.5, series1, "历史");
//        dataset.setValue(112.5, series1, "文科数学");
//
//        DefaultCategoryDataset line1 = new DefaultCategoryDataset();//年级最高分
//        String series2 = "年级最高分";
//        line1.addValue(129, series2, "语文");
//        line1.setValue(142.5, series2, "英语");
//        line1.setValue(79, series2, "政治");
//        line1.setValue(84.5, series2, "地理");
//        line1.setValue(76.5, series2, "历史");
//        line1.setValue(142.5, series2, "文科数学");
//
//        DefaultCategoryDataset line2 = new DefaultCategoryDataset();//年级平均分
//        String series3 = "年级平均分";
//        line2.addValue(109, series3, "语文");
//        line2.setValue(112, series3, "英语");
//        line2.setValue(69, series3, "政治");
//        line2.setValue(77, series3, "地理");
//        line2.setValue(66, series3, "历史");
//        line2.setValue(132, series3, "文科数学");
//        ChartUtils.drawBarAndStrChart("/Users/richard.xu/Documents/Bar2.jpg", 640, 480, true, "你的各科成绩在年级的情况", "", "分数(分)", dataset, line1, line2);

        //二维柱形图多维度
//        DefaultCategoryDataset dataset9 = new DefaultCategoryDataset();
//        String series1 = "题目总分";
//        dataset9.addValue(20, series1, "容易题目");
//        dataset9.setValue(70, series1, "中等题目");
//        dataset9.setValue(10, series1, "较难题目");
//
//        String series2 = "年级平均";
//        dataset9.addValue(19, series2, "容易题目");
//        dataset9.setValue(58, series2, "中等题目");
//        dataset9.setValue(3, series2, "较难题目");
//
//        String series3 = "你的得分";
//        dataset9.addValue(20, series3, "容易题目");
//        dataset9.setValue(65, series3, "中等题目");
//        dataset9.setValue(5, series3, "较难题目");
//
//        ChartUtils.drawBarChart("/Users/richard.xu/Documents/Bar3.jpg", 640, 480, false, "试题难度对比得分情况", "", "得分", dataset9);

    }
}
