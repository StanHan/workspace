package demo.java.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.util.Region;

import com.sun.org.apache.bcel.internal.generic.NEW;

@SuppressWarnings("all")
public class ExcelExportTwo {

    private static Log logger = LogFactory.getLog(ExcelExportTwo.class);
    private String fileType = ".xls";

    // 表单名
    private String sheetName;
    
    // 表单名
    private String sheetName2;

    // 列名
    private String[] colName;

    // 列名描述
    private String[] colName_CN;
    
    // 列名
    private String[] colName2;
    // 列名描述
    private String[] colName_CN2;
    
    private String[] colT2_1 = new String[] { "ACCT_NO", "QUALE", "A01", "A02", "A03",
			"A04", "A05", "A06", "A07", "A08", "A09", "A10", "A11", "A12", "A13",
			"A14", "A15", "A16", "A17", "A18", "A19", "A11", "A21", "A22", "A23",
			"A04", "A25", "A26", "A07", "A28", "A29", "A30", "A31"};
    
	private String[] colT2_1_CN = new String[] { "类型", "账号", "账户性质", "1日", "2日", "3日",
			"4日", "5日", "6日", "7日", "8日", "9日", "10日", "11日", "12日", "13日", "14日",
			"15日", "16日", "17日", "18日", "19日", "10日", "21日", "22日", "23日", "24日", "25日",
			"26日", "27日", "28日", "29日", "30日", "31日" };
	
	private String[] colFix = new String[] {"ACCT_NO", "NAME", "ACCT_NO_TWO", "NATURE", "CLIENT_NO", "ORG_ID"};
	
	private String[] colFix_CN = new String[] {"备付金客户非活期账户号", "机构名称", "备付金来源活期账户号", "活期账户性质", "客户号", "组织机构代码"};

    public ExcelExportTwo(String modelId) {
    	// 工作表名字 （创建工作表时用，无模板导出时与导出文件名相同）
        sheetName = CommonUtil.isoToUTF8(CustomizedPropertyConfigurer.getContextProperty(modelId + "_SHEETNAME"));
        
        // 列名（从Map中取值时用）
        String value = CustomizedPropertyConfigurer.getContextProperty(modelId + "_COLNAME");
        if (value != null && value.length() > 0) {
            colName = value.split(",");
        }

        // 中文列名（无模板导出时显示用）
        value = CustomizedPropertyConfigurer.getContextProperty(modelId + "_COLNAME_CN");
        if (value != null && value.length() > 0) {
            // colName_CN = CommonUtil.isoToUTF8(value).split(",");
            colName_CN = value.split(",");
        }
        
        // 工作表名字 （创建工作表时用，无模板导出时与导出文件名相同）
        sheetName2 = CommonUtil.isoToUTF8(CustomizedPropertyConfigurer.getContextProperty(modelId + "2_SHEETNAME"));
        
        // 列名（从Map中取值时用）
        value = CustomizedPropertyConfigurer.getContextProperty(modelId + "2_COLNAME");
        if (value != null && value.length() > 0) {
            colName2 = value.split(",");
        }

        // 中文列名（无模板导出时显示用）
        value = CustomizedPropertyConfigurer.getContextProperty(modelId + "2_COLNAME_CN");
        if (value != null && value.length() > 0) {
            // colName_CN = CommonUtil.isoToUTF8(value).split(",");
            colName_CN2 = value.split(",");
        }
    }

    public File createFile() throws Exception {
        // String path = ConfigureFileReadFactory.getFilePath(BaseConstance.DL_PATH);
        // 设定生成的excel 保存地址
        String path = CustomizedPropertyConfigurer.getContextProperty("reportPath");

        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        file = new File(path + Long.toString(new Date().getTime()) + fileType);
        if (file.exists()) {
            String tmpPath = file.getPath();
            int xls = tmpPath.indexOf(".xls");
            int n = 1;
            File f = null;
            do {
                // 查找不存在的文件，防止多个用户同时建立文件，或者同个用户同时建立多个文件
                f = new File(tmpPath.substring(0, xls) + String.valueOf(n) + fileType);
                n++;
            } while (f.exists() && f.length() > 0);
            file = f;
        }
        file.createNewFile();
        return file;
    }
    
    public File createFile(String fileName) throws Exception {
        // String path = ConfigureFileReadFactory.getFilePath(BaseConstance.DL_PATH);
        // 设定生成的excel 保存地址
        String path = CustomizedPropertyConfigurer.getContextProperty("pathExcel");

        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        file = new File(path + fileName + fileType);
        if (file.exists()) {
            String tmpPath = file.getPath();
            int xls = tmpPath.indexOf(".xls");
            int n = 1;
            File f = null;
            do {
                // 查找不存在的文件，防止多个用户同时建立文件，或者同个用户同时建立多个文件
                f = new File(tmpPath.substring(0, xls) + String.valueOf(n) + fileType);
                n++;
            } while (f.exists() && f.length() > 0);
            file = f;
        }
        file.createNewFile();
        return file;
    }
    
    public File createUrlFile(String url, String fileName) throws Exception {
        String path = CustomizedPropertyConfigurer.getContextProperty("pathExcel");

        path += url + "/" ;
        
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        file = new File(path + fileName + fileType);
        file.createNewFile();
        return file;
    }

    /**
     * 
     * Description:设置字体 
     * @param font
     */
    public void setFont(HSSFFont font) {
        font.setFontName("宋体");
        // font.setBoldweight((short) 100);
        // font.setFontHeight((short) 300);
        // font.setColor(HSSFColor.BLUE.index);
    }

    /**
     * 
     * Description:设置样式 
     * @param style
     */
    public void setStyle(HSSFCellStyle style) {
        // style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        // style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        // 设置边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居中
    }

    /**
     * 
     * Description:写入标题 
     * @param titleField
     * @param sheet
     * @param style
     */
    public void writeTitle(String[] titleField, HSSFWorkbook wb, HSSFSheet sheet, HSSFCellStyle style) {
        HSSFRow title = sheet.createRow(0);
        title.setHeight((short) 500);
       
        for (int i = 0; i < titleField.length; i++) {
            HSSFCell cell = title.createCell(i);
            // 给Excel的单元格设置样式和赋值
            cell.setCellStyle(style);
            cell.setCellValue(titleField[i]);
        }
    }

    /**
     * 
     * Description:写入内容 
     * @param data
     * @param outputFields
     * @param index
     * @param sheet
     * @param style
     */
    public void writeBody(List<Map<String, String>> data, Map<String, Integer> m, String[] outputFields, int index, HSSFSheet sheet,
            HSSFCellStyle style) {

        for (Map<String, String> map : data) {
            // 创建Excel的sheet的一行
            HSSFRow row = sheet.createRow(index++);
            row.setHeight((short)500);
            String key = map.get(outputFields[0]);
            Integer v = m.get(key);
            for (int i = 0; i < outputFields.length; i++) {
                HSSFCell cell = row.createCell(i);
             // 给Excel的单元格设置样式和赋值
                cell.setCellStyle(style);
				if(i==2){
					cell.setCellValue(v != null ? "否" : "是");
				} else if(i==3){
					Hyperlink link = new HSSFHyperlink(Hyperlink.LINK_DOCUMENT);
					if(v!=null){
						link.setAddress("#详细!A"+(v+1));
						cell.setHyperlink(link);
						cell.setCellValue("不符合，详情请点击！");
					} else {
						cell.setCellValue("符合");
					}
					
				} else {
					Object value = map.get(outputFields[i]);
					if(value != null){
						cell.setCellValue(String.valueOf(value));
					}else{
						cell.setCellValue("N/A");
					}
				}
            }
        }
    }
    
    public void writeBody(List<Map<String, String>> data, String[] outputFields, int index, HSSFSheet sheet,
            HSSFCellStyle style) {
        for (Map<String, String> map : data) {
            // 创建Excel的sheet的一行
            HSSFRow row = sheet.createRow(index++);
            for (int i = 0; i < outputFields.length; i++) {
                HSSFCell cell = row.createCell(i);
                row.setHeight((short)300);
				// 给Excel的单元格设置样式和赋值
				cell.setCellStyle(style);
				Object value = map.get(outputFields[i]);
				if(value != null){
					cell.setCellValue(String.valueOf(value));
				}else{
					cell.setCellValue("0");
				}
            }
        }
    }
    
    public void writeBodyT2_1(List<Map<String, String>> data, String[] outputFields, int index, HSSFSheet sheet,
            HSSFCellStyle style) {
    	String quale = null;
    	long vals[] = new long[31];
    	int number = index;
    	String type = null;
        for (Map<String, String> map : data) {
        	if(quale == null){
        		quale = map.get("QUALE");
        	}
        	if(type == null){
        		type = map.get("TYPE");
        	}
        	
        	HSSFRow row = null;
			if(!quale.equals(map.get("QUALE"))){
				row = sheet.createRow(index++);
				row.setHeight((short)300);
				sheet.addMergedRegion(new Region(index-1, (short)1, index-1, (short)2));
				HSSFCell cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue(quale+"合计");
				for (int i = 1; i < vals.length; i++) {
					cell = row.createCell(i+2);
					cell.setCellValue(vals[i]);
					cell.setCellStyle(style);
				}
				quale = map.get("QUALE");
				vals = new long[31];
				if(!type.equals(map.get("TYPE"))){
					sheet.addMergedRegion(new Region(number, (short)0, index-1, (short)0));
					cell = sheet.getRow(number).createCell(0);
					
					cell.setCellValue("0".equals(type) ? "备付金专用存款账户(5)" : "备付金非活期存款账户(5)");
					cell.setCellStyle(style);
					number = index;
					type = map.get("TYPE");
				}
			}
        	
        	
            // 创建Excel的sheet的一行
            row = sheet.createRow(index++);
            HSSFCell cell = row.createCell(0);
            cell.setCellStyle(style);
            for (int i = 1; i <= outputFields.length; i++) {
                cell = row.createCell(i);
				// 给Excel的单元格设置样式和赋值
				cell.setCellStyle(style);
				Object value = map.get(outputFields[i-1]);
				if(value != null){
					cell.setCellValue(String.valueOf(value));
				}else{
					cell.setCellValue("0");
				}
				Long v=null;
				try {
					value = value == null ? 0 : value;
					v = Long.parseLong(String.valueOf(value));
				} catch (NumberFormatException e) {
					
				}
				if(i >= 2 && v != null){
					vals[i-2] += v; 
				}
            }
        }
        
        sheet.addMergedRegion(new Region(number, (short)0, index, (short)0));
        HSSFCell cell = sheet.getRow(number).createCell(0);
		cell.setCellValue("备付金非活期存款账户(5)");
		cell.setCellStyle(style);
		
		HSSFRow row = sheet.createRow(index);
		sheet.addMergedRegion(new Region(index, (short) 1, index, (short) 2));
		cell = row.createCell(0);
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(quale + "合计");
		cell = row.createCell(2);
		cell.setCellStyle(style);
		for (int i = 0; i < vals.length; i++) {
			cell = row.createCell(i + 3);
			cell.setCellValue(vals[i]);
			cell.setCellStyle(style);
		}
		
		
    }

    /**
     * 
     * Description:写出excel 
     * @param data
     * @param data2
     * @param map
     * @param modleId
     * @return 返回生成的文件名
     * @throws Exception
     */
    public String writeFile(List<Map<String, String>> data, List<Map<String, String>> data2, Map<String, Integer> map, String fileName) throws Exception {
        FileOutputStream out = null;
        File file = null;
        try {
            file = createUrlFile(fileName, fileName);
            out = new FileOutputStream(file);

            // 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = wb.createSheet("sheet1");
            sheet.setDefaultColumnWidth(10);
            sheet.setColumnWidth(0, 3600);
            sheet.setColumnWidth(1, 15000);
            sheet.setColumnWidth(2, 3000);
            sheet.setColumnWidth(3, 6000);

            // 创建字体样式
            HSSFFont font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            
            // 创建单元格样式
            HSSFCellStyle style = wb.createCellStyle();
           
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            writeTitle(colName_CN, wb, sheet, style);

            // 创建字体样式
            font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//粗体显示
            
            // 创建单元格样式
            style = wb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 垂直居中
            String[] outputFields = colName;
            //主体
            writeBody(data, map, outputFields, 1, sheet, style);
            
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet2 = wb.createSheet("详细");
            sheet2.setDefaultColumnWidth(10);
            
            // 设置title样式
            font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            
            // 创建单元格样式
            style = wb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            writeTitle(colName_CN2, wb, sheet2, style);
            outputFields = colName2;
            
            // 设置body样式
            font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//粗体显示
            
            // 创建单元格样式
            style = wb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 垂直居中
            
            writeBody(data2, outputFields, 1, sheet2, style);
            
            wb.write(out);
            return file.getName();
        }finally {
            out.close();
        }
    }
    
    /**
     * 
     * Description:写出excel 
     * @param data
     * @param data2
     * @param map
     * @param modleId
     * @return 返回生成的文件名
     * @throws Exception
     */
    public String writeT2_1(List<Map<String, String>> data, String fileName)  throws Exception {
    	FileOutputStream out = null;
        File file = null;
        try {
            file = createUrlFile(fileName, fileName+"_T2-1");
            out = new FileOutputStream(file);

            // 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = wb.createSheet("余额统计");
            sheet.setDefaultColumnWidth(10);
            sheet.setColumnWidth(0, 3500);
            sheet.setColumnWidth(1, 4500);
            sheet.setColumnWidth(2, 6500);
            
            // 创建字体样式
            HSSFFont font = wb.createFont();

            // 创建单元格样式
            HSSFCellStyle style = wb.createCellStyle();

            // 创建字体样式
            font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            
            // 创建单元格样式
            style = wb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            writeTitle(colT2_1_CN, wb, sheet, style);

            // 创建字体样式
            font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//粗体显示
            
            // 创建单元格样式
            style = wb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            String[] outputFields = colT2_1;
            writeBodyT2_1(data, outputFields, 1, sheet, style);
            
            wb.write(out);
            return file.getName();
        }finally {
            out.close();
        }
    }
    
    /**
     * 
     * Description:写出excel 
     * @param data
     * @param data2
     * @param map
     * @param modleId
     * @return 返回生成的文件名
     * @throws Exception
     */
    public String writeFixAcctInfo(List<Map<String, String>> data, String fileName)  throws Exception {
    	FileOutputStream out = null;
        File file = null;
        try {
            file = createUrlFile(fileName, fileName+"非活期账户一览表");
            out = new FileOutputStream(file);

            // 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = wb.createSheet("违规账号");
            sheet.setDefaultColumnWidth(10);
            sheet.setColumnWidth(0, 3500);
            sheet.setColumnWidth(1, 4500);
            sheet.setColumnWidth(2, 6500);
            
            // 创建字体样式
            HSSFFont font = wb.createFont();

            // 创建单元格样式
            HSSFCellStyle style = wb.createCellStyle();

            // 创建字体样式
            font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            
            // 创建单元格样式
            style = wb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            writeTitle(colFix_CN, wb, sheet, style);

            // 创建字体样式
            font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//粗体显示
            
            // 创建单元格样式
            style = wb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            String[] outputFields = colFix;
            writeBody(data, outputFields, 1, sheet, style);
            
            wb.write(out);
            return file.getName();
        }finally {
            out.close();
        }
    }

    public static void main(String[] args) {
    	
    }
}
