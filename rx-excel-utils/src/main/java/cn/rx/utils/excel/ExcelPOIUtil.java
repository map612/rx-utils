/**
 * Copyright (c) 2015, All rights reserved.
 * You can quote the code anywhere.
 * And if necessary, send me an email(map612@163.com) while any fault is found.
 */
package cn.rx.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * POI 读写excel 2007(.xlsx)文件
 * 
 * @author richard.xu add 2012-3-27
 * @description 需要jar文件： dom4j-1.6.1.jar, poi-3.8-20120326.jar,
 *              poi-ooxml-3.8-20120326.jar, poi-ooxml-schemas-3.8-20120326.jar,
 *              xmlbeans-2.3.0.jar 写文件时需要调用方法writeAndClose()执行写入与关闭
 */
public class ExcelPOIUtil {

	private static Logger log = LoggerFactory.getLogger(ExcelPOIUtil.class);

	public static enum CreateTyle {
		NEW, UPDATE
	};

	private XSSFWorkbook workbook;

	private OutputStream os;

	private ExcelPOIUtil() {
	}

	private ExcelPOIUtil(InputStream is) throws IOException {
		workbook = new XSSFWorkbook(is);
	}

	private ExcelPOIUtil(File file) throws FileNotFoundException, IOException {
		workbook = new XSSFWorkbook(new FileInputStream(file));
	}

	private ExcelPOIUtil(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	private ExcelPOIUtil(XSSFWorkbook workbook, OutputStream os) throws FileNotFoundException, IOException {
		this.workbook = workbook;
		this.os = os;
	}

	public static ExcelPOIUtil getInstance(InputStream is) throws IOException {
		return new ExcelPOIUtil(is);
	}

	public static ExcelPOIUtil getInstance(File file) throws FileNotFoundException, IOException {
		if (!file.exists()) {
			log.error("ExcelJXLUtil getInstance error, file \"" + file.getPath() + "\" does not exist");
			return null;
		}
		return new ExcelPOIUtil(file);
	}

	public static ExcelPOIUtil getInstance(String filePath) throws FileNotFoundException, IOException {
		return ExcelPOIUtil.getInstance(new File(filePath));
	}

	/**
	 * Create a new writable instance[in memory]
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static ExcelPOIUtil getWriteInstance() throws FileNotFoundException, IOException {
		return new ExcelPOIUtil(new XSSFWorkbook());
	}

	/**
	 * Create a new writable instance with default CreateTyle.UPDATE
	 * 
	 * @param filePath
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static ExcelPOIUtil getWriteInstance(String filePath) throws FileNotFoundException, IOException {
		return ExcelPOIUtil.getWriteInstance(new File(filePath));
	}

	/**
	 * Create a new writable instance with parameter type
	 * 
	 * @param filePath
	 * @param type
	 *            eg.CreateTyle.NEW
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static ExcelPOIUtil getWriteInstance(String filePath, CreateTyle type) throws FileNotFoundException, IOException {
		return ExcelPOIUtil.getWriteInstance(new File(filePath), type);
	}

	/**
	 * Create a new writable instance with default CreateTyle.UPDATE
	 * 
	 * @param file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static ExcelPOIUtil getWriteInstance(File file) throws FileNotFoundException, IOException {
		return getWriteInstance(file, CreateTyle.UPDATE);
	}

	/**
	 * Create a new writable instance with parameter type
	 * 
	 * @param file
	 * @param type
	 *            eg.CreateTyle.NEW
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static ExcelPOIUtil getWriteInstance(File file, CreateTyle type) throws FileNotFoundException, IOException {

		XSSFWorkbook workbook;
		if (type.equals(CreateTyle.UPDATE)) {
			// if not exist
			if (!file.exists()) {
				log.error("ExcelJXLUtil getWriteInstance error, file \"" + file.getParent() + "\" not exists");
				throw new FileNotFoundException();
			}
			workbook = new XSSFWorkbook(new FileInputStream(file));
		} else {
			// delete file if exist
			if (file.exists() && file.delete()) {
				log.info("ExcelJXLUtil getWriteInstance, delete old file \"" + file.getPath() + "\" successfully");
			}
			workbook = new XSSFWorkbook();
			log.info("ExcelJXLUtil getWriteInstance, create new file \"" + file.getPath() + "\" successfully");
		}

		return new ExcelPOIUtil(workbook, new FileOutputStream(file));
	}

	/**
	 * 获取Excel单元格对象
	 *
	 * @param sheetIndex
	 *            sheet序号 start by 1
	 * @param rowIndex
	 *            行号 start by 1
	 * @param colIndex
	 *            列号 start by 1
	 * @return 单元格Cell对象
	 */
	public XSSFCell getCell(int sheetIndex, int rowIndex, int colIndex) {
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
		XSSFCell cell = sheet.getRow(rowIndex - 1).getCell(colIndex - 1);
		return cell;
	}

	/**
	 * 获取Excel单元格对象 按坐标获取
	 *
	 * @param rowIndex
	 *            行号 start by 1
	 * @param colIndex
	 *            列号 start by 1
	 * @return 单元格Cell对象
	 */
	public XSSFCell getCell(XSSFSheet sheet, int rowIndex, int colIndex) {
		XSSFCell cell = null;
		XSSFRow row = sheet.getRow(rowIndex - 1);
		if(row == null){
			return null;
		}
		cell = sheet.getRow(rowIndex - 1).getCell(colIndex - 1);
		return cell;
	}

	/**
	 * 获取Excel单元格某区域值组
	 *
	 * @param sheetIndex
	 *            sheet序号 start by 1
	 * @param from_x
	 *            行号 start by 1
	 * @param from_y
	 *            列号 start by 1
	 * @param to_x
	 *            行号 start by 1
	 * @param to_y
	 *            列号 start by 1
	 * @return String[][]单元格值组
	 */
	public String[][] getCellValues(int sheetIndex, int from_x, int from_y, int to_x, int to_y) {
		String[][] target = new String[(to_y - from_y) + 1][(to_x - from_x) + 1];
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
		for (int y = from_y; y < to_y + 1; y++) {
			for (int x = from_x; x < to_x + 1; x++) {
				target[y - from_y][x - from_x] = getCellValue(getCell(sheet, y, x));
			}
		}
		return target;
	}

	/**
	 * 获取Excel指定表单（Sheet）指定行的数据
	 * 
	 * @param sheetIndex
	 *            sheet序号 start by 1
	 * @param row_index
	 *            行号 start by 1
	 * @param colomn_m
	 *            列号 start by 1
	 * @param colomn_n
	 *            列号 start by 1
	 * @return String[][]单元格值组
	 */
	public String[] getRowValues(int sheetIndex, int row_index, int colomn_m, int colomn_n) {
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
		return getRowValues(sheet, row_index, colomn_m, colomn_n);
	}

	/**
	 * 获取Excel指定表单（Sheet）指定行的数据
	 *
	 * @param row_index
	 *            行号 start by 1
	 * @param colomn_m
	 *            列号 start by 1
	 * @param colomn_n
	 *            列号 start by 1
	 * @return String[][]单元格值组
	 */
	public String[] getRowValues(XSSFSheet sheet, int row_index, int colomn_m, int colomn_n) {
		String[] target = new String[(colomn_n - colomn_m) + 1];
		for (int x = colomn_m; x < colomn_n + 1; x++) {
			target[x - 1] = getCellValue(getCell(sheet, row_index, x));
		}
		return target;
	}

	/**
	 * 获取Excel单元格内的值
	 *
	 * @param sheet
	 *            Excel表单对象
	 * @param x
	 *            sheet中单元格横坐标 start by 1
	 * @param y
	 *            sheet中单元格纵坐标 start by 1
	 * @return String单元格值
	 */
	public String getCellValue(XSSFSheet sheet, int x, int y) {
		return getCellValue(sheet.getRow(y - 1).getCell(x - 1));
	}

	/**
	 * 获取Excel单元格内的值
	 *
	 * @return String单元格值
	 */
	public String getCellValue(XSSFCell cell) {
		if (cell == null)
			return "";
		String cellContent = "";

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			cellContent = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			cellContent = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellContent = cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_BLANK:
			cellContent = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellContent = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			cellContent = String.valueOf(cell.getErrorCellValue());
			break;
		default:
			break;
		}

		return cellContent;
	}

	/**
	 * 工作簿插入sheet
	 * 
	 * @param sheetName
	 */
	public boolean createSheet(String sheetName) {
		XSSFSheet sheet = this.workbook.createSheet(sheetName);
		return sheet != null;
	}

	/**
	 * 指定位置插入一行数据
	 * 
	 * @param sheetIndex
	 *            sheet序号 start by 1
	 * @param rowIndex
	 *            行号 start by 1
	 * @param startX
	 *            列号 start by 1
	 * @param rowValues
	 *            值
	 */
	public void insertRow(int sheetIndex, int rowIndex, int startX, String[] rowValues) {
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
		XSSFRow row = sheet.createRow(rowIndex - 1);
		for (int i = 0; i < rowValues.length; i++) {
			row.createCell(startX - 1 + i).setCellValue(rowValues[i] == null ? "" : rowValues[i].trim());
		}
	}

	public void insertStringRow(int sheetIndex, int rowIndex, int startX, String[] rowValues) {
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
		XSSFRow row = sheet.createRow(rowIndex - 1);
		for (int i = 0; i < rowValues.length; i++) {
			XSSFCell cell = row.createCell(startX - 1 + i);
			
			XSSFCellStyle cellStyle = workbook.createCellStyle();   
			XSSFDataFormat format = workbook.createDataFormat();   
			cellStyle.setDataFormat(format.getFormat("@"));   
            cell.setCellStyle(cellStyle);  
			
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new XSSFRichTextString(rowValues[i] == null ? "" : rowValues[i].trim()));
		}
	}

	/**
	 * 指定位置作为区域起点插入多组数据
	 * 
	 * @param sheetIndex
	 *            sheet序号 start by 1
	 * @param rowIndex
	 *            行号 start by 1
	 * @param colIndex
	 *            列号 start by 1
	 * @param values
	 *            值
	 */
	public void insertRows(int sheetIndex, int rowIndex, int colIndex, String[][] values) {
		for (int i = 0; i < values.length; i++) {
			insertRow(sheetIndex, rowIndex + i, colIndex, values[i]);
		}
	}

	/**
	 * 删除行
	 * @param sheetIndex
	 * @param rowIndex
	 */
	public void deleteRow(int sheetIndex, int rowIndex) {
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
		//unmerge merged rows while same with delete row
		int mergeCellNum = sheet.getNumMergedRegions();
		for (int i = 0; i < mergeCellNum; i++) {
			CellRangeAddress mergedCell = sheet.getMergedRegion(i);
			if ((rowIndex - 1) >= mergedCell.getFirstRow() && (rowIndex - 1) <= mergedCell.getLastRow()) {
				sheet.removeMergedRegion(i);
			}
		}
		sheet.shiftRows(rowIndex, sheet.getLastRowNum(), -1);
	}

	/**
	 * 写入修改，关闭excel Workbook对象[不写入磁盘的话可以不调用]
	 * 
	 * @throws IOException
	 */
	public void writeAndClose() throws IOException {
		this.workbook.write(os);// 写入Exel工作表
		this.os.close();
	}

	public XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}
}
