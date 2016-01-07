package com.czl.zip.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 
 * excel的工具类 使用POI
 * @author Administrator
 *
 */
public class ReadExcel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ReadExcel.class);
	
	private int rowCount;
	private Integer columnCount;
	
	private HSSFCellStyle style = null;
	private HSSFCellStyle style_red = null;
	private HSSFCellStyle style_ok = null;
	private HSSFPatriarch patr = null;

	public ReadExcel(){}
	
	public ReadExcel(int columnCount){
		this.columnCount=columnCount;
	}
	
	private void setCellComment(HSSFCell cell,String commentMsg){
		
		HSSFComment comment = 
			patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short) 6, 5));
		comment.setString(new HSSFRichTextString(commentMsg));
		comment.setAuthor(commentMsg);
		cell.setCellComment(comment);
		cell.setCellStyle(style_red);
	}	
	
	private String getCellVelue(HSSFRow row,int index){
			if(row.getCell(index)!=null&& row.getCell(index).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
				 String value="";
				
				 if(HSSFDateUtil.isCellDateFormatted(row.getCell( index))){
				     double d = row.getCell( index).getNumericCellValue(); 
			         Date date = HSSFDateUtil.getJavaDate(d); 
			         value=DateUtil.parseDateToStr(date, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
				 }else{
					 value=""+new BigDecimal(row.getCell(index).getNumericCellValue()).toPlainString();
				 }
				 if(value!=null){
					 return value.trim();
				 }
			} else {
				// REVIEW-ACCEPT 语句缩进太过凌乱
				//FIXED
				if(row.getCell(index)!=null && row.getCell(index).getCellType()==HSSFCell.CELL_TYPE_STRING){
					String value=row.getCell( index).toString();
					if(value!=null)	{
						return value.trim();
					}
				}else{
					HSSFRichTextString value=null;
					if(row.getCell( index)!=null){
						value=row.getCell( index).getRichStringCellValue();
					}
					if(value!=null){	
						return value.toString();
					}
				}
			}
		
		return "";
	}
	
	public List readExcel(String filePath) throws IOException{
		// REVIEW-ACCEPT 注意，这个close可能无法达到，应当使用try catch finally来进行
		//FIXED
		FileInputStream excelFileStream = new FileInputStream(filePath);
		try {
			List data= readExcel(excelFileStream);
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally{
			excelFileStream.close();
			excelFileStream=null;
		}
		return null;
	}
	/**从流中读出EXCEL数据，保存在LIST中返回
	 * @author hkq
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public  List readExcel(InputStream excelFileStream) throws IOException{

		POIFSFileSystem excel = new POIFSFileSystem(excelFileStream);
		HSSFWorkbook workbook = new HSSFWorkbook(excel);
		HSSFSheet sheet = workbook.getSheetAt(0);
	   
	    List tableData=new ArrayList();
		int lastRowNum=sheet.getLastRowNum();
		for(int i=0;i<=lastRowNum;i++){
				HSSFRow row=sheet.getRow(i);
				List rowData=new ArrayList();
				for(int j=0;j<columnCount;j++){
					String cellValue=getCellVelue(row, j);
					rowData.add(cellValue);
				}
				tableData.add(rowData);
		}
		
		return tableData;
	}
	
   public static void main(String args[]){
	  ReadExcel readExcel=new ReadExcel();
	  readExcel.setColumnCount(222);
	  try{
		int i=0;
		//
		//E:\\doc\\workspace\\bp_drp\\WebRoot\\drp\\uploadmarketinfo\\MARKET_INFO_MODEL.xls
        List	data=readExcel.readExcel("c:\\test.xls");
        System.out.println(new Date());
	    for(Iterator it=data.iterator();it.hasNext();){
		   List row=(List)it.next();
		 
		   for(Iterator jt=row.iterator();jt.hasNext();){
			  String result=(String) jt.next();
			  System.out.print(result+",");
			
		   }
		   i++;
		
		  System.out.println();
	    }
	    System.out.println("i:"+i);
	    System.out.println(new Date());
	  }catch(Exception e){
		e.printStackTrace();
	  }
   }
/**
 * 
 * @描述:
 * @公司:广东数据通信网络有限公司
 * @作者:hkq
 * @日期:Nov 17, 2010 9:45:00 AM	
 * @param filePath
 * @param errorPath
 * @param templatePath
 * @return
 * @throws IOException
 */
// REVIEW 该方法是否还有效？
	public List<String> validateExcel(String filePath,String errorPath,String templatePath) throws IOException{
	
		FileInputStream excelFileStream = new FileInputStream(filePath);
		
		FileInputStream templateFileStream = new FileInputStream(templatePath);
		POIFSFileSystem templateexcel = new POIFSFileSystem(templateFileStream);
		HSSFWorkbook templateworkbook = new HSSFWorkbook(templateexcel);
		System.out.println("----"+templatePath);
		List<String> errorList=new ArrayList<String>();	
		
		try{
			//初始化枚举参数
			
			POIFSFileSystem excel = new POIFSFileSystem(excelFileStream);
			HSSFWorkbook workbook = new HSSFWorkbook(excel);
			HSSFSheet sheet = workbook.getSheetAt(0);
	
			HSSFSheet templatesheet = templateworkbook.getSheetAt(0);
			style = templatesheet.getRow(0).getCell(11).getCellStyle();
			style_red = templatesheet.getRow(0).getCell( 12).getCellStyle();
			patr = templatesheet.createDrawingPatriarch();

			String areaname=workbook.getSheetName(0);
			templateworkbook.setSheetName(0, areaname);

			int lastRowNum=sheet.getLastRowNum();
			int limitCount=0;
			
			//有效数据从第四行开始
			for(int i=1;i<=lastRowNum;i++){
				try{
					HSSFRow row=sheet.getRow(i);
					HSSFRow newrow=templatesheet.createRow(i);
					
					String ipStart=getCellVelue(row, 0);
					String ipEnd=getCellVelue(row, 1);
					String mask=getCellVelue(row, 2);
					
					//如果前3个单元格为空则忽略本行
					if("".equals(ipStart) && "".equals(ipEnd) && "".equals(mask))
						continue;
					
					limitCount++;

					String iptype=getCellVelue(row, 3);
					String iptypeStr=iptype;
					if("静态".equals(iptype))iptype="S";
					else if("动态".equals(iptype))iptype="P";
					else iptype="N";
					
					String useclass=getCellVelue(row, 4);
					String ip_gateway = getCellVelue(row, 5);
					String usedate="";
					try{
						Date udate=row.getCell( 6).getDateCellValue();
						usedate=new SimpleDateFormat("yyyy-MM-dd").format(udate);
					}
					catch(Exception e){
						usedate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					}
					String txtIpAddress=getCellVelue(row, 7);
					String unitnameen=getCellVelue(row, 8);
					unitnameen=unitnameen.replaceAll("，", ",");
					unitnameen=unitnameen.replaceAll("＃", "#");
					String addressen=getCellVelue(row, 9);
					addressen=addressen.replaceAll("，", ",");
					addressen=addressen.replaceAll("＃", "#");

					newrow.createCell(0).setCellValue(new HSSFRichTextString(ipStart));
					newrow.createCell(1).setCellValue(new HSSFRichTextString(ipEnd));
					newrow.createCell(2).setCellValue(new HSSFRichTextString(mask));
					newrow.createCell(3).setCellValue(new HSSFRichTextString(iptypeStr));

					HSSFCell cell0 = newrow.getCell( 0);
					HSSFCell cell1 = newrow.getCell(1);
					HSSFCell cell2 = newrow.getCell(2);
					HSSFCell cell3 = newrow.getCell( 3);

					style_ok=cell0.getCellStyle();
					cell0.setCellStyle(style);
					cell1.setCellStyle(style);
					cell2.setCellStyle(style);
					cell3.setCellStyle(style);

					StringBuffer errorMsg=new StringBuffer();
					errorMsg.append("第"+(i+1)+"行发现错误：");
					boolean existError=false;
					
					
					try {
						if(!ipStart.substring(0, ipStart.length()-3).equals(ipEnd.substring(0, ipEnd.length()-3))){
							existError=true;
							setCellComment( cell1, "IP地址大于1个C;");
						}
					}catch(Exception e){}

					
					//存在错误则添加到错误列表
			        if(existError){
			        	newrow.createCell(12).setCellValue(new HSSFRichTextString("错误"));
						newrow.getCell(12).setCellStyle(style);
			        	errorList.add(errorMsg.toString());						
					}else{
			        	newrow.createCell(12).setCellValue(new HSSFRichTextString("正确"));
			        	cell0.setCellStyle(style_ok);
						cell1.setCellStyle(style_ok);
						cell2.setCellStyle(style_ok);
						cell3.setCellStyle(style_ok);
			        }
				}
				catch(Exception e){
					logger.error("其它类型地址校验:", e);
					e.printStackTrace();
				}
			}
			
			FileOutputStream out = new FileOutputStream(errorPath);  
			templateworkbook.write(out);
			out.close();
			templateFileStream.close();
			
			if (logger.isDebugEnabled()) {
				logger.debug("其它类型地址校验:"); 
				logger.debug("lastRowNum="+lastRowNum); 
				logger.debug(errorList); 
			}
		}catch(Exception e){
			logger.error(e);
			errorList.add("<font color=red>错误的文件格式,解析文件失败.</font>");
			e.printStackTrace();
			FileOutputStream out = new FileOutputStream(errorPath);  
			templateworkbook.write(out);
			out.close();
		}
		
		templateFileStream.close();
		excelFileStream.close();
	
		return errorList;		
	}
	
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public Integer getColumnCount() {
		return columnCount;
	}
	public void setColumnCount(Integer columnCount) {
		this.columnCount = columnCount;
	}
}
