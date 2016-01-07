package com.czl.zip;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.czl.zip.utils.DateUtil;

/**
 * @author Cao Zhili
 * @date 2015年4月1日
 */
public class ReadExcelTest {

	public static void main(String[] args) throws Exception {
		String filePath = "G:\\DATA\\广州市站工程案例\\市政\\1005\\大岗镇新沙村4、5、6、18队道路工程（改13清单）.xls";
		FileInputStream excelFileStream = new FileInputStream(filePath);
		readExcel(excelFileStream);
	}

	public static void readExcel(InputStream excelFileStream)
			throws IOException {
		POIFSFileSystem excel = new POIFSFileSystem(excelFileStream);
		HSSFWorkbook workbook = new HSSFWorkbook(excel);
		int sheets = workbook.getNumberOfSheets();
		for (int i = 0; i < 1; i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);
			System.out.println(sheet.getSheetName());
			System.out.println("rowNum: " + sheet.getLastRowNum());
			System.out.println("col: " + sheet.getLeftCol());
			int rowNum = sheet.getLastRowNum();
			for (int j = 0; j < rowNum; j++) {
				HSSFRow row = sheet.getRow(j);
				int colNum = row.getPhysicalNumberOfCells();
				for (int k = 0; k < colNum; k++) {
					String colVal = getCellVelue(row, k);
					if(StringUtils.isBlank(colVal)){
						continue;
					}
					System.out.println("colVal:"+j+"-" + colVal);
				}
			}
		}
	}

	private static String getCellVelue(HSSFRow row, int index) {
		if (row.getCell(index) != null
				&& row.getCell(index).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			String value = "";

			if (HSSFDateUtil.isCellDateFormatted(row.getCell(index))) {
				double d = row.getCell(index).getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				value = DateUtil.parseDateToStr(date,
						DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
			} else {
				value = ""
						+ new BigDecimal(row.getCell(index)
								.getNumericCellValue()).toPlainString();
			}
			if (value != null) {
				return value.trim();
			}
		} else {
			// REVIEW-ACCEPT 语句缩进太过凌乱
			// FIXED
			if (row.getCell(index) != null
					&& row.getCell(index).getCellType() == HSSFCell.CELL_TYPE_STRING) {
				String value = row.getCell(index).toString();
				if (value != null) {
					return value.trim();
				}
			} else {
				HSSFRichTextString value = null;
				if (row.getCell(index) != null) {
					value = row.getCell(index).getRichStringCellValue();
				}
				if (value != null) {
					return value.toString();
				}
			}
		}

		return "";
	}
}
