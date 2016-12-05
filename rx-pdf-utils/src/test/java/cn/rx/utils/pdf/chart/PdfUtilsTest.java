package cn.rx.utils.pdf.chart;

import cn.rx.utils.chart.ChartUtils;
import cn.rx.utils.pdf.PdfUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/6/16<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */
public class PdfUtilsTest {
    private static String pathTmp = System.getProperty("java.io.tmpdir");

    @Test
    public void testCreatePDF() throws Exception {
        String pathBase = "/Users/richard.xu/Documents/";
        //概览部分
        String overviewPdf = generateSubjectOverview(pathTmp);
        //各科目部分
        String subjectPdf = generateSubject(pathTmp);
        //合并
        String[] files = new String[]{overviewPdf, subjectPdf};
        String mergedFile = PdfUtils.mergePdfFiles(Arrays.asList(files), pathBase);
        System.out.println("mergedFile : " + mergedFile);
        //写页码有问题,暂时不用
//        String pagedFile = writePageNumber(mergedFile, "%d/%d", pathBase);
//        System.out.println("pagedFile : " + pagedFile);
    }

    /**
     * 全科概览PDF生成 Sample
     */
    private static String generateSubjectOverview(String filePath) throws Exception{
        Document document = new Document(PdfUtils.pageSize, PdfUtils.marginLeft, PdfUtils.marginRight, PdfUtils.marginTop, PdfUtils.marginBottom);
        String overviewPdf = filePath + PdfUtils.getRandomPdfFilename();
        PdfWriter.getInstance(document, new FileOutputStream(new File(overviewPdf)));
        document.open();

        //title
        PdfPTable titleTable = new PdfPTable(1);
        PdfPCell titleCell = new PdfPCell(PdfUtils.zeroBorderCell);
        titleCell.setPhrase(new Phrase("十堰九校联考高一场次学习力评估", PdfUtils.titleFont));
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleTable.addCell(titleCell);
        PdfPCell subTitleCell = new PdfPCell(PdfUtils.zeroBorderCell);
        subTitleCell.setPhrase(new Phrase("高一(16)班 李嘉豪同学全科概览", PdfUtils.subTitleFont));
        subTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleTable.addCell(subTitleCell);
        document.add(titleTable);
        document.add(PdfUtils.getBlankLineElement(10f));

        //成绩单
        document.add(PdfUtils.getClassElement("成绩单"));
        document.add(PdfUtils.getBlankLineElement(10f));
        document.add(PdfUtils.getCjdElement(new String[][]{
                {"语文", "英语", "政治", "地理", "历史", "文科数学", "总分"},
                {"150", "150", "100", "100", "100", "150", "750"},
                {"129", "142.5", "79", "84.5", "76.5", "142.5", "662.5"},
                {"129", "142.5", "79", "84.5", "76.5", "142.5", "662.5"},
                {"129", "142.5", "79", "84.5", "76.5", "142.5", "662.5"},
                {"129", "142.5", "79", "84.5", "76.5", "142.5", "662.5"},
                {"129", "142.5", "79", "84.5", "76.5", "142.5", "662.5"}
        }));
        document.add(PdfUtils.getBlankLineElement(10f));

        //成绩水平
        document.add(PdfUtils.getClassElement("成绩水平"));
        document.add(PdfUtils.getBlankLineElement(10f));
        Double[] levelTops = new Double[]{540D, 474D, 457D, 440.5D, 418D, 394.3D, 360D, 322.5D, 272.5D, 212D};
        document.add(PdfUtils.getLevelElement(levelTops, 200, 99.57));
        document.add(PdfUtils.getBlankLineElement(10f));
        document.add(PdfUtils.getDescElement(new String[]{"你的本次十堰九校联考高一场次考试总分为533.5分（满分750分）。"
                , "在本年级参加考试的1201名同学中国年成绩水平百分位是75.27%。"
                , "分数提高5分，相应成绩水平会提高至78.27%，分数降低5分，相应成绩水平会下滑至72.77%。"}));
        document.add(PdfUtils.getBlankLineElement(10f));

        //年级分析
        document.add(PdfUtils.getClassElement("年级分析"));
        document.add(PdfUtils.getBlankLineElement(10f));
        document.add(getNjfxElement());

        document.close();

        return overviewPdf;
    }

    /**
     * 各科目学习力评估PDF生成 Sample
     */
    private static String generateSubject(String filePath) throws Exception{
        Document document = new Document(PdfUtils.pageSize, PdfUtils.marginLeft, PdfUtils.marginRight, PdfUtils.marginTop, PdfUtils.marginBottom);
        String subjectPdf = filePath + PdfUtils.getRandomPdfFilename();
        PdfWriter.getInstance(document, new FileOutputStream(new File(subjectPdf)));
        document.open();

        //title
        PdfPTable titleTable = new PdfPTable(1);
        PdfPCell titleCell = new PdfPCell(PdfUtils.zeroBorderCell);
        titleCell.setPhrase(new Phrase("十堰九校联考高一场次学习力评估", PdfUtils.titleFont));
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleTable.addCell(titleCell);
        PdfPCell titleSubCell = new PdfPCell(PdfUtils.zeroBorderCell);
        titleSubCell.setPhrase(new Phrase("语文科", PdfUtils.titleFont));
        titleSubCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleTable.addCell(titleSubCell);
        PdfPCell subTitleCell = new PdfPCell(PdfUtils.zeroBorderCell);
        subTitleCell.setPhrase(new Phrase("高一(16)班 李嘉豪同学", PdfUtils.subTitleFont));
        subTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleTable.addCell(subTitleCell);
        document.add(titleTable);
        document.add(PdfUtils.getBlankLineElement(10f));

        //考试得分
        document.add(PdfUtils.getClassElement("考试得分"));
        document.add(getKsdfElement());

        //年级成绩分布情况
        String chartNjcj = pathTmp + "/chartNjcj.jpg";
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("优秀(135.0~150.0)", 0.0);
        dataset.setValue("及格(120.0~135.0)", 90.0);
        dataset.setValue("良好(90.0~120.0)(你在这里)", 5.1);
        dataset.setValue("不及格(0.0~90.0)", 4.9);
        ChartUtils.drawPieChart(chartNjcj, 580, 300, false, "年级成绩分布情况", dataset);
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = new PdfPCell(PdfUtils.getColoredCell(1, 193, 193, 193));
        cell.setImage(Image.getInstance(chartNjcj));
        table.addCell(cell);
        document.add(table);
        document.add(PdfUtils.getBlankLineElement(10f));
        document.add(PdfUtils.getDescElement(new String[]{"你本次考试的［语文］成绩排在年级良好行列，领先里95.90%的同学，再努力一把，希望下次能够进入优秀的队伍呦！"}));
        document.add(PdfUtils.getBlankLineElement(10f));

        //班级成绩分布情况
        String chartBjcj = pathTmp + "/chartBjcj.jpg";
        dataset.clear();
        dataset.setValue("优秀(135.0~150.0)", 0.0);
        dataset.setValue("及格(120.0~135.0)", 93.0);
        dataset.setValue("良好(90.0~120.0)(你在这里)", 2.0);
        dataset.setValue("不及格(0.0~90.0)", 5.0);
        ChartUtils.drawPieChart(chartBjcj, 580, 300, false, "班级成绩分布情况", dataset);
        table = new PdfPTable(1);
        cell = new PdfPCell(PdfUtils.getColoredCell(1, 193, 193, 193));
        cell.setImage(Image.getInstance(chartNjcj));
        table.addCell(cell);
        document.add(table);
        document.add(PdfUtils.getBlankLineElement(10f));
        document.add(PdfUtils.getDescElement(new String[]{"你本次考试的［语文］成绩排在班级良好行列，领先里98.18%的同学，再努力一把，希望下次能够进入优秀的队伍呦！"}));
        document.add(PdfUtils.getBlankLineElement(10f));

        //难度分析
        document.newPage();
        document.add(PdfUtils.getClassElement("难度分析"));
        String chartNytx = pathTmp + "/chartNytx.jpg";
        dataset.clear();
        dataset.setValue("难", 0.0);
        dataset.setValue("中", 80.0);
        dataset.setValue("易", 20.0);
        ChartUtils.drawPieChart(chartNytx, 580, 300, false, "试题难易题型百分比", dataset);
        table = new PdfPTable(1);
        cell = new PdfPCell(PdfUtils.getColoredCell(1, 193, 193, 193));
        cell.setImage(Image.getInstance(chartNytx));
        table.addCell(cell);
        document.add(table);
        document.add(PdfUtils.getBlankLineElement(10f));

        document.add(PdfUtils.getDescElement(new String[]{"本套语文试卷中，容易题目有4题，占总体20.0%；中等题目有16题，占总体80.0%；较难题目有0题，占总体0.0%。"}));
        document.add(PdfUtils.getBlankLineElement(10f));
        String chartNddb = pathTmp + "/chartNytx.jpg";
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        String series1 = "题目总分";
        categoryDataset.addValue(20, series1, "容易题目");
        categoryDataset.addValue(70, series1, "中等题目");
        categoryDataset.addValue(10, series1, "较难题目");
        String series2 = "年级平均";
        categoryDataset.addValue(19, series2, "容易题目");
        categoryDataset.addValue(58, series2, "中等题目");
        categoryDataset.addValue(3, series2, "较难题目");
        String series3 = "你的得分";
        categoryDataset.addValue(20, series3, "容易题目");
        categoryDataset.addValue(65, series3, "中等题目");
        categoryDataset.addValue(5, series3, "较难题目");
        ChartUtils.drawBarChart(chartNddb, 580, 300, false, "试题难度对比得分情况", "", "得分", categoryDataset);
        table = new PdfPTable(1);
        cell = new PdfPCell(PdfUtils.getColoredCell(1, 193, 193, 193));
        cell.setImage(Image.getInstance(chartNddb));
        table.addCell(cell);
        document.add(table);
        document.add(PdfUtils.getBlankLineElement(10f));
        document.add(PdfUtils.getDescElement(new String[]{"通过得分情况对比，我们发现在你丢失的14分中，8分属于较容易的题目，6分属于难度中等的，0分属于较难的。你在各类型题目上得分均高于年级水平，太棒了！"}));
        document.add(PdfUtils.getBlankLineElement(10f));

        //失分点分析
        document.newPage();
        document.add(PdfUtils.getClassElement("失分点分析"));
        document.add(PdfUtils.getBlankLineElement(10f));
        document.add(PdfUtils.getCommonTable(new String[][]{
                {"题号", "考试知识点", "得分", "失分", "满分", "年级平均得分", "差值"},
                {"8", "文言文阅读，历史事件类", "3", "1", "4", "2.6", "＋0.4"},
                {"9", "文言文阅读，历史事件类", "3", "1", "4", "2.6", "＋0.4"},
                {"10", "文言文阅读，历史事件类", "3", "1", "4", "2.6", "＋0.4"},
                {"11(2)/12(2)", "文言文阅读，历史事件类", "3", "1", "4", "2.6", "＋0.4"}
        }));
        document.add(PdfUtils.getBlankLineElement(10f));
        document.add(PdfUtils.getDescElement(new String[]{
                        "在被刺考试中，共有2道题低于平均分，分别是［10］［11（3）／12（3）］题，需要仔细分析错因。",
                        "根据你的全卷得分率一级所有学生的平均得分率测算，在所有失分的9道题中第［7（3）］［8］［9］题应当属于你的个人能力防卫内可以做得更好的题，需认真分析"}
        ));

        //知识点掌握情况
        document.newPage();
        document.add(PdfUtils.getClassElement("知识点掌握情况"));
        document.add(PdfUtils.getBlankLineElement(10f));
        document.add(PdfUtils.getCommonTable(new String[][]{
                {"序号", "一级知识点", "二级知识点", "三级知识点", "出现次数"}}));

        document.close();

        return subjectPdf;
    }

    /**
     * 返回概览中年级分析Table
     */
    private static PdfPTable getNjfxElement() throws Exception {
        PdfPCell baseCell = new PdfPCell();
        baseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        baseCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        baseCell.setBorderWidth(0);

        //chart1
        String imageChart1 = pathTmp + "/chart1.jpg";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(662.5, "年级最高分", "年级最高分");
        dataset.addValue(533.5, "你的总分", "你的总分");
        dataset.addValue(474.11, "年级平均分", "年级平均分");
        ChartUtils.drawBarChart(imageChart1, 640, 480, false, "你的总分在年级的水平", "", "分数(分)", dataset);
        //chart2
        String imageChart2 = pathTmp + "/chart2.jpg";
        dataset.clear();
        dataset = new DefaultCategoryDataset();//你的分数
        String series1 = "你的分数";
        dataset.addValue(119, series1, "语文");
        dataset.addValue(122.5, series1, "英语");
        dataset.addValue(59, series1, "政治");
        dataset.addValue(74.5, series1, "地理");
        dataset.addValue(66.5, series1, "历史");
        dataset.addValue(112.5, series1, "文科数学");
        DefaultCategoryDataset line1 = new DefaultCategoryDataset();//年级最高分
        String series2 = "年级最高分";
        line1.addValue(129, series2, "语文");
        line1.addValue(142.5, series2, "英语");
        line1.addValue(79, series2, "政治");
        line1.addValue(84.5, series2, "地理");
        line1.addValue(76.5, series2, "历史");
        line1.addValue(142.5, series2, "文科数学");
        DefaultCategoryDataset line2 = new DefaultCategoryDataset();//年级平均分
        String series3 = "年级平均分";
        line2.addValue(109, series3, "语文");
        line2.addValue(112, series3, "英语");
        line2.addValue(69, series3, "政治");
        line2.addValue(77, series3, "地理");
        line2.addValue(66, series3, "历史");
        line2.addValue(132, series3, "文科数学");
        ChartUtils.drawBarAndStrChart(imageChart2, 640, 480, false, "你的各科成绩在年级的情况", "", "分数(分)", dataset, line1, line2);

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new int[]{48, 4, 48});
        PdfPCell cell_11 = new PdfPCell(baseCell);
        cell_11.setImage(Image.getInstance(imageChart1));
        table.addCell(cell_11);
        table.addCell(new PdfPCell(baseCell));
        PdfPCell cell_12 = new PdfPCell(baseCell);
        cell_12.setImage(Image.getInstance(imageChart2));
        table.addCell(cell_12);
        PdfPCell cell_21 = new PdfPCell(PdfUtils.getDescElement(new String[]{"本次十堰九校联考高一场次考试，你的总分为533.5分，距离年级最高分129分，高出年级平均分59.39分，你的成绩在年级处于中上水平"}));
        table.addCell(cell_21);
        table.addCell(new PdfPCell(baseCell));
        PdfPCell cell_22 = new PdfPCell(PdfUtils.getDescElement(new String[]{"你的语文、英语、政治、地理、历史、文科数学高于年级平均水平，表现不错"}));
        table.addCell(cell_22);
        return table;
    }

    /**
     * 返回考试得分概览Table
     */
    private static PdfPTable getKsdfElement() throws DocumentException {
        PdfPTable scoreTable = new PdfPTable(3);
        scoreTable.setWidths(new int[]{20, 2, 78});
        //  左侧圆圈
        PdfPCell circleCell = new PdfPCell(PdfUtils.zeroBorderCell);
        PdfPTable scoreLftTable = new PdfPTable(1);

        PdfPCell scCell = new PdfPCell(PdfUtils.zeroBorderCell);
        scCell.setPhrase(new Phrase("120.5", PdfUtils.scoreFont));
        scCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        scCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        scCell.setMinimumHeight(60f);
        scoreLftTable.addCell(scCell);

        PdfPCell scTotalCell = new PdfPCell(PdfUtils.zeroBorderCell);
        scTotalCell.setPhrase(new Phrase("试卷总分 : 150分", PdfUtils.commonTextFont));
        scTotalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        scoreLftTable.addCell(scTotalCell);

        circleCell.addElement(scoreLftTable);
        scoreTable.addCell(circleCell);
        //  中间空白
        scoreTable.addCell(new PdfPCell(PdfUtils.zeroBorderCell));
        //  右侧
        PdfPCell scRgtCell = new PdfPCell(PdfUtils.zeroBorderCell);
        PdfPTable scoreRgtTable = new PdfPTable(1);
        scoreRgtTable.addCell(PdfUtils.getSingleCjdElement(new String[]{"129", "106.3", "120.5", "105.1", "2.5"}));
        scCell = new PdfPCell(PdfUtils.zeroBorderCell);
        scCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        scCell.setPhrase(new Phrase("年级人数：1195人  年级里你领先了95.90%的同学", PdfUtils.commonTextFont));
        scCell.setPaddingTop(10f);
        scoreRgtTable.addCell(scCell);
        scCell = new PdfPCell(PdfUtils.zeroBorderCell);
        scCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        scCell.setPhrase(new Phrase("班级人数：55人  班级里你领先了98.18%的同学", PdfUtils.commonTextFont));
        scCell.setPaddingTop(10f);
        scCell.setPaddingBottom(10f);
        scoreRgtTable.addCell(scCell);
        scRgtCell.addElement(scoreRgtTable);
        scoreTable.addCell(scRgtCell);
        return scoreTable;
    }
}
