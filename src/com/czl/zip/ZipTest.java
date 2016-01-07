package com.czl.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.czl.zip.utils.DateUtil;
import com.czl.zip.utils.DeleteDirectory;

/**
 * @author Cao Zhili
 * @date 2015年3月31日
 */
public class ZipTest {

	private static final String TEMP_DIR = "G:\\temp";

	public static void main(String[] args) {
		try {
			// String file = "G:\\DATA\\前进村控制价EXCEL.zip";
			// String file = "G:\\DATA\\广州市站工程案例.zip";
			String file = "G:\\DATA\\广州市站工程案例\\市政\\1005\\EXCEL.zip";

			boolean falg = DeleteDirectory.deleteDir(TEMP_DIR);
			if (falg) {
				System.out.println("删除成功");
			} else {
				System.out.println("删除失败111111111111111111");
			}
			// Thread.sleep(5000);
			readZipExcelFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 保存文件 InputStream --> File
	 * 
	 * @param ins
	 * @param file
	 * @throws Exception
	 */
	public static void inputstreamtofile(InputStream ins, File file)
			throws Exception {
		OutputStream os = null;
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (ins != null) {
					ins.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 读取文件
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void readZipExcelFile(File file) throws Exception {
		org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(
				file, System.getProperty("sun.jnu.encoding"));
		// org.apache.tools.zip.ZipFile zipFile = new
		// org.apache.tools.zip.ZipFile(zipPathName,System.getProperty("GBK"));
		Enumeration<? extends org.apache.tools.zip.ZipEntry> zipEntrys = zipFile
				.getEntries();
		while (zipEntrys.hasMoreElements()) {
			org.apache.tools.zip.ZipEntry zipEntry = zipEntrys.nextElement();
			String name = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				System.out.println("目录:" + name);
				continue;
			}
			if (name.endsWith(".zip")) {
				System.out.println("迭代解析");
				File tempFile = new File(TEMP_DIR, name);
				inputstreamtofile(zipFile.getInputStream(zipEntry), tempFile);
				readZipExcelFile(tempFile);
			} else if (name.endsWith(".xml")) {
				System.out.println("xmlfile: " + name);
			} else if (name.endsWith(".xls")) {
				// excel解析
				System.out.println("xlsfile: " + name);
				long size = zipEntry.getSize();
				if (size > 0) {
					readExcel(zipFile.getInputStream(zipEntry));
					// ReadExcel readExcel = new ReadExcel();
					// List list =
					// readExcel.readExcel(zipFile.getInputStream(zipEntry));
					// System.out.println(list.size());
				}
			}
		}
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
					System.out.println("colVal:" + colVal);
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

	/**
	 * 读取文件
	 * 
	 * @param zipPathName
	 * @throws Exception
	 */
	public static void readZipExcelFile(String zipPathName) throws Exception {
		readZipExcelFile(new File(zipPathName));
	}

	/**
	 * 判断文件名是否以.zip为后缀
	 * 
	 * @param fileName
	 *            需要判断的文件名
	 * @return 是zip文件返回true,否则返回false
	 */
	public static boolean isEndsWithZip(String fileName) {
		boolean flag = false;
		if (fileName != null && !"".equals(fileName.trim())) {
			if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 加压缩
	 * 
	 * @param file
	 * @param saveFileDir
	 */
	public static void unZip(File file, String saveFileDir) {
		if (file.exists()) {
			InputStream is = null;
			// can read Zip archives
			ZipArchiveInputStream zais = null;
			try {
				is = new FileInputStream(file);

				// 设置zip编码，处理中文路径乱码问题
				zais = new ZipArchiveInputStream(is,
						System.getProperty("sun.jnu.encoding"));

				ArchiveEntry archiveEntry = null;
				// 把zip包中的每个文件读取出来
				// 然后把文件写到指定的文件夹
				while ((archiveEntry = zais.getNextEntry()) != null) {
					// 获取文件名
					String entryFileName = archiveEntry.getName();
					// 构造解压出来的文件存放路径
					String entryFilePath = saveFileDir + entryFileName;
					entryFilePath = entryFilePath.replaceAll("\\\\", "/");
					entryFilePath = entryFilePath.replaceAll("\\/\\/", "/");
					// 判断路径是否存在,不存在则创建文件路径
					File outPathFile = new File(entryFilePath.substring(0,
							entryFilePath.lastIndexOf("/")));
					if (!outPathFile.exists()) {
						outPathFile.mkdirs();
					}
					// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
					if (new File(entryFilePath).isDirectory()) {
						continue;
					}

					byte[] content = new byte[1024];
					int num = 0;
					OutputStream os = null;
					FileOutputStream fileOutputStream = null;
					try {
						File entryFile = new File(entryFilePath);
						fileOutputStream = new FileOutputStream(entryFile);
						os = new BufferedOutputStream(fileOutputStream);
						while ((num = zais.read(content)) != -1) {
							os.write(content, 0, num);
						}
					} catch (IOException e) {
						throw new IOException(e);
					} finally {
						if (os != null) {
							os.flush();
							os.close();
						}

						if (null != fileOutputStream) {
							fileOutputStream.flush();
							fileOutputStream.close();
						}
					}

				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (zais != null) {
						zais.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * 把zip文件解压到指定的文件夹
	 * 
	 * @param zipFilePath
	 *            zip文件路径, 如 "D:/test/aa.zip"
	 * @param saveFileDir
	 *            解压后的文件存放路径, 如"D:/test/"
	 */
	public static void unZip(String zipFilePath, String saveFileDir) {
		if (isEndsWithZip(zipFilePath)) {
			File zipFile = new File(zipFilePath);
			unZip(zipFile, saveFileDir);
		}
	}
}
