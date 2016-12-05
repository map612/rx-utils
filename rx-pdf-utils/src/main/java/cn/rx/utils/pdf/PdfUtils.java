package cn.rx.utils.pdf;

import cn.rx.utils.common.math.NumberUtils;
import cn.rx.utils.security.MD5Utils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 智阅卷报表PDF生成工具
 * Created by richard.xu on 15/11/6.
 */
public class PdfUtils {
    private static Log log = LogFactory.getLog(PdfUtils.class);
    /// 中文字体
    public static BaseFont bfChinese;
    static {
        try {
            //中文 (字体名称, 文字编码, 是否嵌入字体到PDF)
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        }catch (Exception e){
            log.error("error while set chinese font", e);
        }
    }
    /// 报表title字体
    public static Font titleFont = new Font(bfChinese, 24, Font.BOLD, BaseColor.BLACK);
    /// 报表title字体
    public static Font subTitleFont = new Font(bfChinese, 20, Font.NORMAL, BaseColor.BLACK);
    /// 报表中单项标题字体
    public static Font classFont = new Font(bfChinese, 18, Font.NORMAL, BaseColor.BLACK);
    /// 报表各科目得分字体
    public static Font scoreFont = new Font(bfChinese, 20, Font.NORMAL, BaseColor.BLACK);
    /// 报表通用字体
    public static Font commonTextFont = new Font(bfChinese, 16, Font.NORMAL, BaseColor.BLACK);
    /// 报表成绩水平图表描述内容字体
    public static Font levelTextFont = new Font(bfChinese, 14, Font.BOLD, BaseColor.BLACK);
    /// 报表成绩水平label字体
    public static Font levelLabelFont = new Font(bfChinese, 14, Font.NORMAL, BaseColor.BLACK);

    /// 报表成绩水平中各区域颜色
    public static BaseColor[] levelColors = new BaseColor[]{
            new BaseColor(28,73,24), new BaseColor(14,104,55), new BaseColor(89,126,27),
            new BaseColor(102,149,67), new BaseColor(110,174,14), new BaseColor(101,157,134),
            new BaseColor(94,145,141), new BaseColor(86,184,130), new BaseColor(97,174,150),
            new BaseColor(156,190,120) };

    /// 科目难度标识映射
    public static Map<String, String> kmndMap = new HashMap<>();
    static {
        kmndMap.put("H", "困难");
        kmndMap.put("N", "正常");
        kmndMap.put("E", "简单");
    }

    public static Rectangle pageSize = PageSize.A3;
    public static float marginLeft = -50.0F;
    public static float marginRight = -50.0F;
    public static float marginTop = 30.0F;
    public static float marginBottom = 36.0F;

    /// 无边框表格单元格
    public static PdfPCell zeroBorderCell;
    static {
        zeroBorderCell = new PdfPCell();
        zeroBorderCell.setBorder(0);
        zeroBorderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    }

    /**
     * 返回单项条目中的绿框标题Table
     */
    public static PdfPTable getClassElement(String text) {
        PdfPCell classCell = new PdfPCell();
        classCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        classCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        classCell.setBorderWidth(1);
        classCell.setBorderColor(new BaseColor(148, 210, 197));
        classCell.setMinimumHeight(40f);

        int colnum = 6;
        PdfPTable table = new PdfPTable(colnum);
        PdfPCell cjdCell = new PdfPCell(classCell);
        cjdCell.setPhrase(new Phrase(text, PdfUtils.classFont));
        table.addCell(cjdCell);

        for (int i = 0; i < colnum - 1; i++) {
            table.addCell(PdfUtils.zeroBorderCell);
        }
        return table;
    }

    /**
     * [概览]返回成绩单Table
     */
    public static PdfPTable getCjdElement(String[][] data) {
        PdfPCell baseCell = new PdfPCell();
        baseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        baseCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        baseCell.setBorderWidth(1);
        baseCell.setBorderColor(BaseColor.BLACK);
        baseCell.setMinimumHeight(30f);
        baseCell.setPaddingBottom(5f);

        String[] types = {"科目", "满分", "班级最高分", "班级平均分", "年级最高分", "年级平均分", "你的成绩"};
        PdfPTable table = new PdfPTable(data[0].length + 1);
        for (int i = 0; i < types.length; i++) {//行
            //列1
            PdfPCell cjdCell = new PdfPCell(baseCell);
            cjdCell.setPhrase(new Phrase(types[i], PdfUtils.commonTextFont));
            cjdCell.setHorizontalAlignment(i == 0 ? Element.ALIGN_CENTER : Element.ALIGN_LEFT);
            table.addCell(cjdCell);

            //其他列
            for (String text : data[i]) {
                PdfPCell cjdOtherCell = new PdfPCell(baseCell);
                cjdOtherCell.setPhrase(new Phrase(text, PdfUtils.commonTextFont));
                table.addCell(cjdOtherCell);
            }
        }
        return table;
    }

    /**
     * 成绩水平图
     * @param levelTops 成绩分界线
     * @param stuScoreSum 学生成绩
     * @param betterRate 优秀率 1～100
     * @return
     * @throws DocumentException
     */
    public static PdfPTable getLevelElement(Double[] levelTops, double stuScoreSum, double betterRate) throws DocumentException {
        PdfPCell baseColorCell = new PdfPCell();
        baseColorCell.setBorderWidth(0);
        baseColorCell.setMinimumHeight(23f);

        PdfPCell baseWhiteCell = new PdfPCell();
        baseWhiteCell.setBorderWidth(0);
        baseWhiteCell.setMinimumHeight(2f);
        baseWhiteCell.setColspan(5);

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new int[]{22, 6, 44, 6, 22});

        PdfPCell topC1 = new PdfPCell(baseColorCell);
        topC1.setColspan(2);
        topC1.setPhrase(new Phrase("水平", PdfUtils.commonTextFont));
        topC1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(topC1);
        table.addCell(new PdfPCell(baseColorCell));
        PdfPCell topC3 = new PdfPCell(baseColorCell);
        topC3.setColspan(2);
        topC3.setPhrase(new Phrase("成绩", PdfUtils.commonTextFont));
        topC3.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(topC3);

        for (int i = 0; i < levelTops.length; i++) {
            boolean isThisLine = (stuScoreSum >= (i == levelTops.length - 1 ? 0 : levelTops[i + 1]) && stuScoreSum < levelTops[i]);//成绩是否在当前行
            boolean isFstOrLast = (i == 0 || i == levelTops.length - 1);//是否首行或末行
            PdfPCell cell1 = new PdfPCell(baseColorCell);
            if (isThisLine) {
                cell1.setPhrase(new Phrase(NumberUtils.format(betterRate, "#.00") + "% ______", PdfUtils.levelLabelFont));
                cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            }
            if(!isFstOrLast){
                cell1.setColspan(2);
            }
            table.addCell(cell1);

            if (isFstOrLast) {
                PdfPCell cell2 = new PdfPCell(baseColorCell);
                cell2.setPhrase(new Phrase(i == 0 ? "100%" : "0%", PdfUtils.levelLabelFont));
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell2.setVerticalAlignment(i == 0 ? Element.ALIGN_TOP : Element.ALIGN_BOTTOM);
                cell2.setUseAscender(true);
                cell2.setUseDescender(true);
                table.addCell(cell2);
            }

            PdfPCell cell3 = new PdfPCell(baseColorCell);
            cell3.setBackgroundColor(levelColors[i % 10]);
            cell3.setPhrase(new Phrase("", PdfUtils.commonTextFont));
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(baseColorCell);
            cell4.setPhrase(new Phrase(NumberUtils.format2str(levelTops[i]), PdfUtils.levelLabelFont));
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell4.setVerticalAlignment(Element.ALIGN_TOP);
            cell4.setUseAscender(true);
            cell4.setUseDescender(true);
            table.addCell(cell4);

            PdfPCell cell5 = new PdfPCell(baseColorCell);
            if (isThisLine) {
                cell5.setPhrase(new Phrase("______ " + stuScoreSum, PdfUtils.levelLabelFont));
                cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            }
            table.addCell(cell5);

            table.addCell(new PdfPCell(baseWhiteCell));
        }

        PdfPCell bottomCell = new PdfPCell(baseColorCell);
        bottomCell.setColspan(5);
        bottomCell.setPhrase(new Phrase("每个色块代表10％的人数，横线代表你所在的位置，数值越往上，排名越靠前。", PdfUtils.levelTextFont));
        bottomCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(bottomCell);

        return table;
    }

    /**
     * [单科]返回单科成绩表Table
     */
    public static PdfPTable getSingleCjdElement(String[] data) {
        PdfPCell baseCell = new PdfPCell();
        baseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        baseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        baseCell.setBorderWidth(1);
        baseCell.setBorderColor(BaseColor.BLACK);
        baseCell.setMinimumHeight(30f);

        String[] types = {"年级最高分", "年级平均分", "班级最高分", "班级平均分", "试卷难度"};
        PdfPTable table = new PdfPTable(types.length);
        for (int i = 0; i < types.length; i++) {//行1
            PdfPCell cjdCell = new PdfPCell(baseCell);
            cjdCell.setPhrase(new Phrase(types[i], PdfUtils.commonTextFont));
            table.addCell(cjdCell);
        }
        for (int i = 0; i < data.length; i++) {//行2
            PdfPCell cjdCell = new PdfPCell(baseCell);
            if(i != data.length - 1){
                cjdCell.setPhrase(new Phrase(data[i] + "分", PdfUtils.commonTextFont));
            }else{
                cjdCell.setPhrase(new Phrase(kmndMap.get(data[i]), PdfUtils.commonTextFont));
            }
            table.addCell(cjdCell);
        }
        return table;
    }

    /**
     * 返回通用表格Table
     */
    public static PdfPTable getCommonTable(String[][] data) {
        PdfPCell baseCell = new PdfPCell();
        baseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        baseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        baseCell.setBorderWidth(1);
        baseCell.setBorderColor(BaseColor.BLACK);
        baseCell.setMinimumHeight(22f);

        PdfPTable table = new PdfPTable(data[0].length);
        for(String[] row : data){
            for(String cell : row){
                PdfPCell cjdCell = new PdfPCell(baseCell);
                cjdCell.setPhrase(new Phrase(cell, PdfUtils.commonTextFont));
                table.addCell(cjdCell);
            }
        }
        return table;
    }

    /**
     * 获取一个指定高度的空行Table
     */
    public static PdfPTable getBlankLineElement(float height){
        PdfPTable table = new PdfPTable(1);
        PdfPCell blankCell = new PdfPCell(new Phrase(""));
        blankCell.setBorderWidth(0);
        blankCell.setMinimumHeight(height);
        table.addCell(blankCell);
        return table;
    }

    /**
     * 获取一个单行带边框的文字描述Table
     */
    public static PdfPTable getDescElement(String[] texts) {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = new PdfPCell(new Phrase(StringUtils.join(texts, "\n"), PdfUtils.commonTextFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(1);
        cell.setBorderColor(new BaseColor(193, 193, 193));
        cell.setPaddingTop(7f);
        cell.setPaddingBottom(7f);
        cell.setPaddingLeft(3f);
        table.addCell(cell);
        return table;
    }

    /**
     * 获取指定颜色指定宽度边框的Cell
     */
    public static PdfPCell getColoredCell(int border, int red, int green, int blue){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(new BaseColor(red, green, blue));
        return cell;
    }

    /**
     * 获取随即的PDF文件名
     */
    public static String getRandomPdfFilename(){
        return getRandomFilename("PDF");
    }

    /**
     * 获取指定后缀的文件名
     * @param suffix 文件扩展名
     * @return
     */
    public static String getRandomFilename(String suffix){
        String key = StringUtils.join(new Object[]{System.currentTimeMillis(), Math.random()}, "-");
        return StringUtils.join(MD5Utils.digest(key), ".", suffix);
    }

    /**
     * 合并多个文件
     * @param files 待合并的文件
     * @param descPath 合并后文件的存放路径
     * @return 合并后的文件
     */
    public static String mergePdfFiles(java.util.List<String> files, String descPath) {
        String destFile = descPath + PdfUtils.getRandomPdfFilename();
        try {
            Document document = new Document(new PdfReader(files.get(0)).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(destFile));
            document.open();

            for (int i = 0; i < files.size(); i++) {
                PdfReader reader = new PdfReader(files.get(i));
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
            document.close();
        } catch (IOException e) {
            log.error("", e);
        } catch (DocumentException e) {
            log.error("", e);
        }
        return destFile;
    }

    /**
     * 为PDF添加页码
     * @param pdfFile
     * @param format 页码格式，如 "%d/%d"
     */
    public static String writePageNumber(String pdfFile, String format, String descPath) {
        String destFile = descPath + PdfUtils.getRandomPdfFilename();
        try {
            Document document = new Document(new PdfReader(pdfFile).getPageSize(1));
            FileOutputStream fos = new FileOutputStream(destFile);
            PdfCopy copy = new PdfCopy(document, fos);

            //添加页脚
            Footer footer = new Footer(format, new PdfReader(pdfFile).getNumberOfPages());
            copy.setPageEvent(footer);

            document.open();

            PdfReader reader = new PdfReader(pdfFile);
            int n = reader.getNumberOfPages();
            for (int j = 1; j <= n; j++) {
                document.newPage();
                PdfImportedPage page = copy.getImportedPage(reader, j);
                copy.addPage(page);
            }

            document.close();
        } catch (IOException e) {
            log.error("", e);
        } catch (DocumentException e) {
            log.error("", e);
        }
        return destFile;
    }

    private static class Footer extends PdfPageEventHelper {
        private String format;
        private Integer pageSize;

        public Footer(String format, Integer pageSize) {
            this.format = format;
            this.pageSize = pageSize;
        }

        @Override
        public void onEndPage(PdfWriter pdfWriter, Document document) {
            ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER,
                    new Phrase(String.format(format, pdfWriter.getPageNumber(), pageSize)), 300, 50, 0);
        }
    }
}
