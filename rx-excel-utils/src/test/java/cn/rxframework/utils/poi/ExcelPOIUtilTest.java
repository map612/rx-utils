package cn.rxframework.utils.poi;

import cn.rxframework.utils.excel.ExcelPOIUtil;
import org.junit.Test;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 16/5/23<br/>
 *
 * @author richard.xu
 * @since v0.0.1
 */
public class ExcelPOIUtilTest {

    @Test
    public void testReadExcel() throws Exception{
        long s = System.currentTimeMillis();
        // ExcelPOIUtil util = ExcelPOIUtil.getInstance("/Users/richard.xu/Downloads/经营数据.xlsx");
        // String[][] cells = util.getCellValues(1, 1, 10, 8, 10);

        // System.out.println(cells.length);

//		ExcelPOIUtil util2;// =
        // ExcelPOIUtil.getWriteInstance("E:/工作内容/专家系统/线上文件/data4MKT-baidu-(AUTO).xlsx",
        // CreateTyle.NEW);
        // for (int i = 1; i < cells[0].length-1; i++) {
        // util2.createSheet(cells[0][i][0]);
        // }
//		for (int i = 5; i < 8; i++) {// cells.length
//			util2 = ExcelPOIUtil.getWriteInstance("E:/工作内容/专家系统/线上文件/data4MKT-baidu-(AUTO).xlsx", CreateTyle.UPDATE);
//			util2.insertRows(i + 1, 1, 1, cells[i]);
//			cells[i] = null;
//			// System.gc();
//			util2.writeAndClose();
//			System.out.println("write cells[" + (i + 1) + "] ok");
//		}
        // util2.workbook.createSheet("sheet one");
        // util2.writeAndClose();
        System.out.println("cost time : " + (System.currentTimeMillis() - s) + "ms ");
    }
}
