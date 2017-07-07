package com.pactera.pisa.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.pactera.pisa.common.core.Constants;
import com.pactera.pisa.common.core.CustomizedPropertyConfigurer;

@SuppressWarnings("all")
public class ExcelExport {
	
	private static Logger logger = Logger.getLogger(ExcelExport.class);
	
	private final static String FILE_TYPE_XLS = ".xls";
	private final static String FILE_TYPE_ZIP = ".zip";
	private final static Integer EXPANDED_ROW_COUNT = 65000;
	
	//表单名
	private String sheetName;

	//列名
	private String[] colName;

	//列名描述 
	private String[] colName_CN;

	/**
	 * excel模版数据初始化
	 * 
	 * @param modelId 模版配置ID(reportExportConfig.properties文件的key前缀)
	 */
	public ExcelExport(String modelId) {
		//工作表名字 （创建工作表时用，无模板导出时与导出文件名相同）
		sheetName = (String)CustomizedPropertyConfigurer.getContextProperty(CommonUtil.append(modelId, "_SHEETNAME"));
		
		//列名（从Map中取值时用）
		String value = (String)CustomizedPropertyConfigurer.getContextProperty(CommonUtil.append(modelId, "_COLNAME"));
		if (value != null && value.length() > 0) {
			colName = value.split(",");
		}
		
		//中文列名（无模板导出时显示用）
		value = (String)CustomizedPropertyConfigurer.getContextProperty(CommonUtil.append(modelId, "_COLNAME_CN"));
		if (value != null && value.length() > 0) {
			colName_CN = value.split(",");
		}
	}
	
	/**
	 * excel模版数据初始化
	 * 
	 * @param modelId 模版配置ID(reportExportConfig.properties文件的key前缀)
	 */
//	public ExcelExport(String modelId) {
//		Properties properties = new Properties();
//		try {
//			properties.load(this.getClass().getResourceAsStream("/msg/reportExportConfig.properties")); 
//			
//			//工作表名字 （创建工作表时用，无模板导出时与导出文件名相同）
//			sheetName = properties.getProperty(CommonUtil.append(modelId, "_SHEETNAME"));
//			
//			//列名（从Map中取值时用）
//			String value = properties.getProperty(CommonUtil.append(modelId, "_COLNAME"));
//			if (value != null && value.length() > 0) {
//				colName = value.split(",");
//			}
//			
//			//中文列名（无模板导出时显示用）
//			value = properties.getProperty(CommonUtil.append(modelId, "_COLNAME_CN"));
//			if (value != null && value.length() > 0) {
//				colName_CN = value.split(",");
//			}
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 写头XML信息
	 * @param sb
	 * @throws Exception
	 */
	public static void writeHeader(StringBuilder sb) throws Exception{
		sb.append("<?xml version= \"1.0\" encoding=\"UTF-8\"?>");
		sb.append("\n");
		sb.append("<?mso-application progid=\"Excel.Sheet\"?>");
		sb.append("\n");
		sb.append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
		sb.append("\n");
		sb.append("  xmlns:o=\"urn:schemas-microsoft-com:office:office\"");
		sb.append("\n");
		sb.append(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"");
		sb.append("\n");
		sb.append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"");
		sb.append("\n");
		sb.append(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">");
		sb.append("\n");
		sb.append("<DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">");
		sb.append("\n");
		sb.append("  <Author>zz</Author>");
		sb.append("\n");
		sb.append("  <Company>pms</Company>");
		sb.append("\n");
		sb.append("  <Version>1.0</Version>");
		sb.append("\n");
		sb.append(" </DocumentProperties>");
		sb.append("\n");
		sb.append(" <ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\">");
		sb.append("\n");
		sb.append("  <WindowHeight>8955</WindowHeight>");
		sb.append("\n");
		sb.append("  <WindowWidth>11355</WindowWidth>");
		sb.append("\n");
		sb.append("  <WindowTopX>480</WindowTopX>");
		sb.append("\n");
		sb.append("  <WindowTopY>15</WindowTopY>");
		sb.append("\n");
		sb.append("  <ProtectStructure>False</ProtectStructure>");
		sb.append("\n");
		sb.append("  <ProtectWindows>False</ProtectWindows>");
		sb.append("\n");
		sb.append(" </ExcelWorkbook>");
		sb.append("\n");
	}

	/**
	 * 写整体样式
	 * @param sb
	 * @throws Exception
	 */
	public static void writeStyle(StringBuilder sb) throws Exception {
		sb.append(" <Styles>\n");
		sb.append("  <Style ss:ID=\"Default\" ss:Name=\"Normal\">\n");
		sb.append("   <Alignment ss:Vertical=\"Center\"/>\n");
		sb.append("   <Borders/>\n");
		sb.append("   <Font ss:FontName=\"宋体\" x:CharSet=\"134\" ss:Size=\"12\"/>\n");
		sb.append("   <Interior/>\n");
		sb.append("   <NumberFormat/>\n");
		sb.append("   <Protection/>\n");
		sb.append("  </Style>\n");
		sb.append("<Style ss:ID=\"row1\">\n");
		sb.append("<Font ss:FontName=\"宋体\" x:CharSet=\"134\" ss:Bold=\"1\"/>\n");   
		sb.append("</Style>");
		sb.append("<Style ss:ID=\"rown\">\n");
		sb.append("<Font ss:FontName=\"宋体\" />\n");   
		sb.append("</Style>");
		sb.append("<Style ss:ID=\"cell1\">"); 
		sb.append("<Borders>");   
		sb.append("<Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");    
		sb.append("<Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");    
		sb.append("<Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");    
		sb.append("<Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");    
		sb.append("</Borders>");   
		sb.append("<Font ss:FontName=\"宋体\" ss:Bold=\"1\"/>");   
		sb.append("</Style>");  
		sb.append("<Style ss:ID=\"celln\">"); 
		sb.append("<Borders>");   
		sb.append("<Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");    
		sb.append("<Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");    
		sb.append("<Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");    
		sb.append("<Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");    
		sb.append("</Borders>");   
		sb.append("<Font ss:FontName=\"宋体\"/>");   
		sb.append("</Style>"); 
		sb.append(" </Styles>\n");
	}

	/**
	 * 结束
	 * @param sb
	 * @throws Exception
	 */
	public static void writeEnd(StringBuilder sb)  throws Exception{
		sb.append("</Workbook>\n");
	}
	
	/**
	 * 写一行，写表头
	 * @param cells
	 * @param sb
	 * @param rowStyle
	 * @param cellStyle
	 * @param height
	 * @throws Exception
	 */
	public static void writeRow(String[] cells,StringBuilder sb,String rowStyle,String cellStyle,String height) throws Exception{
		sb.append("<Row  ss:AutoFitHeight=\"0\" ss:Height=\"").append(height).append("\" ss:StyleID=\"").append(rowStyle).append("\">").append("\n");
		
		if (cells != null) {
			for (int j = 0; j < cells.length; j++) {
				writeCell(cells[j],cellStyle,sb);
			}
		}
		sb.append("</Row>").append("\n");
	}
	
	/**
	 * 写一个单元格
	 * @param text
	 * @param cellStyle
	 * @param sb
	 */
	public static void writeCell(String text,String cellStyle,StringBuilder sb){
		if (text == null)
			text = "";
		text = text.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");// 转义
		sb.append("<Cell ss:StyleID=\"").append(cellStyle).append("\"><Data ss:Type=\"String\">").append(text).append("</Data></Cell>\n");
	}

	/**
	 * 写表体行
	 * @param
	 * @param dataList
	 * @param sb
	 * @param out
	 */
	public void writeBodyRow( List dataList,
			StringBuilder sb, FileOutputStream out) throws Exception,
			IOException {
		for(int i =0 ;i<dataList.size();i++){//写 表体,每500行写一次
			Map rowMap =null;
			rowMap = (Map)dataList.get(i);
			String[] cells = new String[colName.length];
			for (int j = 0; j < colName.length; j++) {
				cells[j]=(String)rowMap.get(colName[j]);
			}
			writeRow(cells,sb,"rown","celln","25.5");
			if(i%500==0){
				out.write(sb.toString().getBytes());
				sb.delete(0, sb.length());
			}
		}
	}

	/**
	 * 设置列属性结束
	 * @param sb
	 * @param out
	 */
	public static void setColumnAttSuffix(StringBuilder sb) throws IOException {
		sb.append("</Table>\n");
		sb.append("</Worksheet>\n");
	}

	/**
	 * 设置列属性开始
	 * @param sheetName sheet名称
	 * @param rowCount 行记录数
	 * @param header 头信息 
	 * @param sb 字符串
	 */
	public void setColumnAttPrefix(StringBuilder sb) {
		sb.append("<Worksheet ss:Name=\"").append(sheetName).append("\">");
		sb.append("\n");
		sb.append("<Table ss:ExpandedColumnCount=\"" ).append(colName.length).append("\" ss:ExpandedRowCount=\"" ).append(EXPANDED_ROW_COUNT).append( "\" x:FullColumns=\"1\" x:FullRows=\"1\">");
		sb.append("\n");
		for (int j = 0; j < colName.length; j++) {//设置列属性
			sb.append("<Column ss:AutoFitWidth=\"1\" ss:Width=\"65\"/>\n");
		}
	}
	
	/**
	 * 生成一个文件
	 * @param filePath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static File getFile(String filePath, String fileName) throws Exception{
	    return new File(CommonUtil.append(filePath, fileName));
	}
	
	/**
	 * 获取文件流
	 * @param filePath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public FileOutputStream getFileOutputStream(String filePath, String fileName) throws Exception{
		File file = getFile(filePath, CommonUtil.append(fileName, FILE_TYPE_XLS));
		// 文件目录不存在时，重新创建
		File ff = file.getParentFile();
		if(!ff.exists()) {
			ff.mkdirs();
		}
		return new FileOutputStream(file);
	}
	
	/**
	 * 压缩文件
	 * 
	 * @param rootPathKey 根目录(配置文件中的KEY)
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public void zipFile(String rootPathKey, String fileName) throws Exception {
		String filePath = (String)CustomizedPropertyConfigurer.getContextProperty(rootPathKey);
//		Properties propertys = new Properties();
//		propertys.load(this.getClass().getResourceAsStream("/msg/reportConfig_zh_CN.properties"));
//		String filePath = propertys.getProperty(rootPathKey);
		
		String fileFullPath = CommonUtil.append(filePath, fileName, FILE_TYPE_ZIP);
		logger.info("filefullpath:" + fileFullPath);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileFullPath));
		File file = getFile(filePath, CommonUtil.append(fileName, FILE_TYPE_XLS));
		FileInputStream fis = new FileInputStream(file);
		out.putNextEntry(new ZipEntry(CommonUtil.append(fileName, FILE_TYPE_XLS)));

		int len = 0;
		byte[] buffer = new byte[1024*1024];//每次读1M

		while ((len = fis.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		fis.close();
		out.closeEntry();
		out.close();
		file.delete();
	}
	
	/**
	 * 创建文件
	 * 
	 * @param rootPathKey 根目录(配置文件中的KEY)
	 * @return
	 * @throws Exception
	 */
	public File createFile(String rootPathKey) throws Exception {
		String path = (String)CustomizedPropertyConfigurer.getContextProperty(rootPathKey);
		//设定生成的excel 保存地址
//		Properties propertys = new Properties();
//		propertys.load(this.getClass().getResourceAsStream("/msg/reportConfig_zh_CN.properties"));
//		String path = propertys.getProperty(rootPathKey);
		
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		file = new File(CommonUtil.append(path, Long.toString(new Date().getTime()), FILE_TYPE_XLS));
		if (file.exists()) {
			String tmpPath = file.getPath();
			int xls = tmpPath.indexOf(FILE_TYPE_XLS);
			int n = 1;
			File f = null;
			do {
				// 查找不存在的文件，防止多个用户同时建立文件，或者同个用户同时建立多个文件
				f = new File(CommonUtil.append(tmpPath.substring(0, xls), String.valueOf(n), FILE_TYPE_XLS));
				n++;
			} while (f.exists() && f.length() > 0);
			file = f;
		}
		file.createNewFile();
		return file;
	}
	
	/**
	 * 绘制数据
	 * 
	 * @param rootPathKey 根目录(配置文件中的KEY)
	 * @param list 数据
	 * @return
	 * @throws Exception
	 */
	public String writeFile(String rootPathKey, List list) throws Exception {
		File file =createFile(rootPathKey);
		FileOutputStream out =new FileOutputStream(file);
		StringBuilder sb = new StringBuilder();
		writeHeader(sb);
		writeStyle(sb);
		setColumnAttPrefix(sb);
		writeRow(colName_CN, sb, "rown","celln","15");
		writeBodyRow(list, sb, out);
		setColumnAttSuffix(sb);
		writeEnd(sb);
		out.write(sb.toString().getBytes());
		sb.delete(0, sb.length());
		out.flush();
		out.close();
		return file.getName();
	}
	
	public static void main(String[] args) {
		List list = new ArrayList();
		Map m = new HashMap();
		m.put("FORM_CODE", "adf");
		m.put("FORM_NAME", "addf");
		m.put("KPI_CODE", "ad3f");
		m.put("KPI_DIM_NAME", "adf4");
		m.put("DIM_CODE", "adf32");
		m.put("DIM_NAME", "adf23");
		m.put("DATA_TYPE", "adf43");
		m.put("AREA_CODE", "adf211");
		m.put("BALANCE", "adf232");
		m.put("BALANCE2", "adf3343");
		list.add(m);
		ExcelExport export = new ExcelExport("rep");
		try {
			String filename = export.writeFile(Constants.PATH_REPORT_KEY, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
