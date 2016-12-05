package cn.rx.utils.chart;

import org.apache.commons.lang3.ArrayUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;

import java.awt.*;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 报表图形生成工具
 * Created by richard.xu on 15/11/9.
 */
public class ChartUtils {
    private static StandardChartTheme theme = new StandardChartTheme("CN");
    static {
        theme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));//设置标题字体
        theme.setLargeFont(new Font("宋书", Font.PLAIN, 15));//设置轴向的字体
        theme.setRegularFont(new Font("宋书", Font.PLAIN, 15));//设置图例的字体
        theme.setSmallFont(new Font("宋书", Font.PLAIN, 12));//
        ChartFactory.setChartTheme(theme);//应用主题样式
    }
    private static Font commonFont = new Font("宋体", Font.PLAIN, 16);//图表通用文字字体

    private static Font titleFont = new Font("宋体", Font.PLAIN, 28);//标题字体
    private static Font lengendFont = commonFont;//图例字体
    private static Font valueFont = commonFont;//图形数值字体
    private static Font labelFont = commonFont;//标签字体
    private static Font axisFont = new Font("宋体", Font.PLAIN, 16);;//X轴Y轴字体

    private static Color[] pieColors = new Color[]{// 饼图颜色组
            new Color(252, 225, 201),
            new Color(247, 163, 92),
            new Color(144, 238, 126),
            new Color(119, 152, 191)};
    private static Color[] barSeriesColors = new Color[]{// 柱形图多series颜色组
            new Color(255, 127, 80),
            new Color(135, 206, 250),
            new Color(218, 112, 214)};
    private static Color[] barColors = new Color[]{// 柱形图单series颜色组
            new Color(255, 153, 51),
            new Color(102, 153, 204),
            new Color(153, 204, 255)};
    private static Color[] barStrColors = new Color[]{// 柱形折线图颜色组
            new Color(137, 165, 78),
            new Color(218, 112, 214),
            new Color(135, 206, 250)};

    /**
     * 图表通用属性
     */
    private static void setCommonProperties(Plot plot){
        plot.setBackgroundPaint(Color.white);//背景颜色
        plot.setBackgroundAlpha(0.0f);//背景色　透明度
        plot.setForegroundAlpha(1.0f);//前景色　透明度
        plot.setOutlinePaint(Color.WHITE); // 设置绘图面板外边的填充颜色
    }

    /**
     * 设置图例样式
     */
    private static void setLengendStyle(LegendTitle legend, boolean showLengend, RectangleEdge position){
        legend.setVisible(showLengend);// 图例是否显示
        legend.setItemFont(lengendFont);// 图例字体
        legend.setPosition(position == null ? RectangleEdge.BOTTOM : position);
        legend.setBorder(0, 0, 0, 0);
    }

    /**
     * 设置图标标题
     */
    private static void setTitleStyle(JFreeChart jfreechart, String chartTitle){
        TextTitle title = new TextTitle(chartTitle);
        title.setFont(titleFont);
        jfreechart.setTitle(title);
    }

    /**
     * 设置柱形图X轴Y轴样式
     */
    private static void setXYStyle(CategoryPlot plot){
        CategoryAxis xAxis = plot.getDomainAxis();
        ValueAxis yAxis = plot.getRangeAxis();
        xAxis.setTickLabelFont(axisFont);//设置X轴坐标上的文字
        xAxis.setLabelFont(axisFont);//设置X轴的标题文字
        yAxis.setTickLabelFont(axisFont);//设置Y轴坐标上的文字
        yAxis.setLabelFont(axisFont);//设置Y轴的标题文字
    }

    /**
     * 二维饼图
     * @param outFile 输出文件绝对路径
     * @param width 图片宽度
     * @param height 图片高度
     * @param chartTitle 图表标题
     * @param dataset 数据
     */
    public static void drawPieChart(String outFile, int width, int height, boolean showLengend, RectangleEdge position, String chartTitle, Color[] colors,
                                    DefaultPieDataset dataset){
        JFreeChart jfreechart = ChartFactory.createPieChart("Pie Chart", dataset, true, true, false);

        PiePlot plot = (PiePlot) jfreechart.getPlot();

        plot.setIgnoreNullValues(false);//忽略空值
        plot.setIgnoreZeroValues(false);//忽略零值

        setTitleStyle(jfreechart, chartTitle);//设置标题
        setLengendStyle(jfreechart.getLegend(), showLengend, position);//图例样式设置
        setCommonProperties(plot);//plot通用样式设置

        plot.setSectionOutlinesVisible(false);//图形轮廓是否显示
        plot.setShadowPaint(Color.WHITE); // 设置绘图面板阴影的填充颜色
        plot.setLabelBackgroundPaint(Color.WHITE); //标签背景色
        plot.setLabelOutlinePaint(Color.WHITE); //标签边框颜色
        plot.setLabelShadowPaint(Color.WHITE); //标签阴影颜色
        plot.setLabelFont(labelFont); //标签字体

        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}：{2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.0%")));
//        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}", NumberFormat.getNumberInstance(), new DecimalFormat("0.0%")));

        for(int i = 0; i < dataset.getKeys().size(); i++){
            plot.setSectionPaint(dataset.getKey(i), colors[i]);
        }

        try {
            FileOutputStream fos_jpg = new FileOutputStream(outFile);
            ChartUtilities.writeChartAsJPEG(fos_jpg, 0.99f, jfreechart, width, height, null);
            fos_jpg.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawPieChart(String outFile, int width, int height, boolean showLengend, String chartTitle,
                                    DefaultPieDataset dataset){
        drawPieChart(outFile, width, height, showLengend, RectangleEdge.BOTTOM, chartTitle, pieColors, dataset);
    }

    /**
     * 二维环形图
     * @param outFile 输出文件绝对路径
     * @param width 图片宽度
     * @param height 图片高度
     * @param chartTitle 图表标题
     * @param dataset 数据
     */
    public static void drawRingChart(String outFile, int width, int height, boolean showLengend, RectangleEdge position, String chartTitle, Color[] colors,
                                     DefaultPieDataset dataset){
        JFreeChart jfreechart = ChartFactory.createRingChart("Ring Chart", dataset, true, true, false);
        RingPlot plot = (RingPlot) jfreechart.getPlot();

        plot.setIgnoreNullValues(false);//忽略空值
        plot.setIgnoreZeroValues(false);//忽略零值

        setTitleStyle(jfreechart, chartTitle);//设置标题
        setLengendStyle(jfreechart.getLegend(), showLengend, position);//图例样式设置
        setCommonProperties(plot);//plot通用样式设置

        plot.setSeparatorsVisible(false);//图形分割线是否显示
        plot.setSectionOutlinesVisible(false);//图形轮廓是否显示
        plot.setSectionDepth(0.7);//环形半径深度
        plot.setShadowPaint(Color.WHITE); // 设置绘图面板阴影的填充颜色
        plot.setLabelBackgroundPaint(Color.WHITE); //标签背景色
        plot.setLabelOutlinePaint(Color.WHITE); //标签边框颜色
        plot.setLabelShadowPaint(Color.WHITE); //标签阴影颜色
        plot.setLabelFont(labelFont); //标签字体

        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}：{2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.0%")));
//        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}", NumberFormat.getNumberInstance(), new DecimalFormat("0.0%")));

        for(int i = 0; i < dataset.getKeys().size(); i++){
            plot.setSectionPaint(dataset.getKey(i), colors[i]);
        }

        try {
            FileOutputStream fos_jpg = new FileOutputStream(outFile);
            ChartUtilities.writeChartAsJPEG(fos_jpg, 0.99f, jfreechart, width, height, null);
            fos_jpg.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawRingChart(String outFile, int width, int height, boolean showLengend, String chartTitle,
                                     DefaultPieDataset dataset){
        drawRingChart(outFile, width, height, showLengend, RectangleEdge.BOTTOM, chartTitle, pieColors, dataset);
    }

    /**
     * 二维柱形图
     * @param outFile 输出文件绝对路径
     * @param width 图片宽度
     * @param height 图片高度
     * @param chartTitle 图表标题
     * @param categoryAxisLabel X轴名称
     * @param valueAxisLabel Y轴名称
     * @param dataset 数据
     */
    public static void drawBarChart(String outFile, int width, int height, boolean showLengend, RectangleEdge position, String chartTitle, Color[] colors,
                                    String categoryAxisLabel, String valueAxisLabel, CategoryDataset dataset) {
        JFreeChart jfreechart = ChartFactory.createBarChart("Bar Chart", categoryAxisLabel, valueAxisLabel, dataset);
        CategoryPlot plot = (CategoryPlot) jfreechart.getPlot();

        setTitleStyle(jfreechart, chartTitle);//设置标题
        setLengendStyle(jfreechart.getLegend(), showLengend, position);//图例样式设置
        setCommonProperties(plot);//plot通用样式设置
        setXYStyle(plot);//X轴Y轴字体设置

        plot.setDomainGridlinesVisible(true);// 设置纵虚线可见
        plot.setDomainGridlinePaint(Color.lightGray);// 虚线色彩
        plot.setRangeGridlinesVisible(true);// 设置横虚线可见
        plot.setRangeGridlinePaint(Color.lightGray);// 虚线色彩

        // 设置柱的颜色
        BarRenderer renderer;
        if (plot.getDataset().getRowCount() > 1) {
            renderer = new BarRenderer();
            for (int i = 0; i < plot.getDataset().getRowCount(); i++) {
                renderer.setSeriesPaint(i, colors[i]);
            }
        } else {
            renderer = new CustomRenderer(colors);
        }
        renderer.setBarPainter(new StandardBarPainter());//去掉柱形颜色渐变
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);// 柱形图的数值是否显示
        renderer.setBaseItemLabelFont(valueFont);// 柱形图的数值字体
        renderer.setShadowVisible(false); //不显示阴影
//        renderer.setItemMargin(-0.9);// 设置每个柱之间距离
        plot.setRenderer(renderer);

        try {
            FileOutputStream fos_jpg = new FileOutputStream(outFile);
            ChartUtilities.writeChartAsJPEG(fos_jpg, 0.99f, jfreechart, width, height, null);
            fos_jpg.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawBarChart(String outFile, int width, int height, boolean showLengend, String chartTitle,
                                    String categoryAxisLabel, String valueAxisLabel, CategoryDataset dataset) {
        Color[] colors = dataset.getRowCount() > 1 ? barSeriesColors : barColors;
        drawBarChart(outFile, width, height, showLengend, RectangleEdge.BOTTOM, chartTitle, colors, categoryAxisLabel, valueAxisLabel, dataset);
    }

    /**
     * 二维柱形图 + 折线图
     * @param outFile 输出文件绝对路径
     * @param width 图片宽度
     * @param height 图片高度
     * @param chartTitle 图表标题
     * @param categoryAxisLabel X轴名称
     * @param valueAxisLabel Y轴名称
     * @param dataset 数据
     * @param lineDatasets 折线数据
     */
    public static void drawBarAndStrChart(String outFile, int width, int height, boolean showLengend, RectangleEdge position, String chartTitle, Color[] colors,
                                          String categoryAxisLabel, String valueAxisLabel, CategoryDataset dataset, CategoryDataset... lineDatasets) {
        JFreeChart jfreechart = ChartFactory.createBarChart("Bar Chart", categoryAxisLabel, valueAxisLabel, dataset);
        CategoryPlot plot = (CategoryPlot) jfreechart.getPlot();

        setTitleStyle(jfreechart, chartTitle);//设置标题
        setLengendStyle(jfreechart.getLegend(), showLengend, position);//图例样式设置
        setCommonProperties(plot);//plot通用样式设置
        setXYStyle(plot);//X轴Y轴字体设置

        plot.setDomainGridlinesVisible(true);// 设置纵虚线可见
        plot.setDomainGridlinePaint(Color.lightGray);// 虚线色彩
        plot.setRangeGridlinesVisible(true);// 设置横虚线可见
        plot.setRangeGridlinePaint(Color.lightGray);// 虚线色彩

        //柱属性
//        CategoryItemRenderer renderer = plot.getRenderer();
        BarRenderer renderer = new BarRenderer();
        renderer.setBarPainter(new StandardBarPainter());//去掉柱形颜色渐变
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);// 柱形图的数值是否显示
        renderer.setBaseItemLabelFont(valueFont);// 柱形图的数值字体

        renderer.setSeriesPaint(0, colors[0]);// 设置柱的颜色
        renderer.setItemMargin(-0.9);// 设置每个柱之间距离
        renderer.setShadowVisible(false); //不显示阴影
        plot.setRenderer(renderer);

        //折线部分
        if (ArrayUtils.isNotEmpty(lineDatasets)) {
            for (int i = 1; i <= lineDatasets.length; i++) {
                plot.setDataset(i, lineDatasets[i - 1]);
                LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
                lineandshaperenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
                lineandshaperenderer.setUseFillPaint(false);//折线折点填充
//                lineandshaperenderer.setSeriesPaint(i, colors[i]);
                plot.setRenderer(i, lineandshaperenderer);
            }
        }
        CategoryAxis categoryaxis = plot.getDomainAxis();
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);//折线在柱面前面显示

        try {
            FileOutputStream fos_jpg = new FileOutputStream(outFile);
            ChartUtilities.writeChartAsJPEG(fos_jpg, 0.99f, jfreechart, width, height, null);
            fos_jpg.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawBarAndStrChart(String outFile, int width, int height, boolean showLengend, String chartTitle,
                                          String categoryAxisLabel, String valueAxisLabel, CategoryDataset dataset, CategoryDataset... lineDatasets) {
        drawBarAndStrChart(outFile, width, height, showLengend, RectangleEdge.BOTTOM, chartTitle, barStrColors, categoryAxisLabel, valueAxisLabel, dataset, lineDatasets);
    }

    public static void main(String[] args){
        //二维饼图
        DefaultPieDataset dataset = new DefaultPieDataset();
        //(135.0~150.0) (120.0~135.0) (90.0~120.0) (0.0~90.0)
        dataset.setValue("优秀", 0.0);
        dataset.setValue("良好", 5.1);
        dataset.setValue("及格", 90.0);
        dataset.setValue("不及格", 4.9);
//        drawPieChart("/Users/richard.xu/Documents/Pie.jpg", 640, 480, true, "年级成绩分布情况", dataset);

        //二维环形图
        drawRingChart("/Users/richard.xu/Documents/Ring.jpg", 640, 480, true, "年级成绩分布情况", dataset);

        //二维柱形图
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(662.5, "总成绩对比", "年级最高分");
//        dataset.addValue(533.5, "总成绩对比", "你的总分");
//        dataset.addValue(474.11, "总成绩对比", "年级平均分");
//        drawBarChart("/Users/richard.xu/Documents/Bar.jpg", 640, 480, false, "你的总分在年级的水平", "", "分数(分)", dataset);

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
//        drawBarAndStrChart("/Users/richard.xu/Documents/Bar2.jpg", 640, 480, true, "你的各科成绩在年级的情况", "", "分数(分)", dataset, line1, line2);

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
//        drawBarChart("/Users/richard.xu/Documents/Bar3.jpg", 640, 480, false, "试题难度对比得分情况", "", "得分", dataset9);

    }
}
