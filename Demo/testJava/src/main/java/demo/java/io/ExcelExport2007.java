package demo.java.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pactera.pisa.common.core.CustomizedPropertyConfigurer;

@SuppressWarnings("all")
public class ExcelExport2007 {

    private static Log logger = LogFactory.getLog(ExcelExport2007.class);
    private String fileType = ".xlsx";
    /** key:title、unit、type */

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
			"A14", "A15", "A16", "A17", "A18", "A19", "A20", "A21", "A22", "A23",
			"A24", "A25", "A26", "A27", "A28", "A29", "A30", "A31"};
    
	private String[] colT2_1_CN = new String[] { "类型", "账号", "账户性质", "1日", "2日", "3日",
			"4日", "5日", "6日", "7日", "8日", "9日", "10日", "11日", "12日", "13日", "14日",
			"15日", "16日", "17日", "18日", "19日", "10日", "21日", "22日", "23日", "24日", "25日",
			"26日", "27日", "28日", "29日", "30日", "31日" };
	
	private String[] colFix = new String[] {"ACCT_NO", "NAME", "ACCT_NO_TWO", "NATURE", "CLIENT_NO", "ORG_ID"};
	
	private String[] colFix_CN = new String[] {"备付金定期账户", "机构名称", "备付金来源活期账户", "活期账户性质", "客户号", "组织机构代码"};

    public ExcelExport2007(String modelId) {
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
    public void setFont(XSSFFont font) {
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
    public void setStyle(XSSFCellStyle style) {
        // style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        // style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        // 设置边框
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        style.setAlignment(XSSFCellStyle.ALIGN_LEFT); // 居中
    }

    /**
     * 
     * Description:写入标题 
     * @param titleField
     * @param sheet
     * @param style
     */
    public void writeTitle(String[] titleField, XSSFWorkbook xb, XSSFSheet sheet, XSSFCellStyle style) {
//    	styleMap
        XSSFRow title = sheet.createRow(0);
        XSSFCell cell = title.createCell(0);
        cell.setCellValue(titleField[0]);
        title.setHeight((short) 500);
        cell.setCellStyle(style);
        for (int i = 1; i < titleField.length; i++) {
            cell = title.createCell(i);
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
    public void writeBody(List<Map<String, String>> data, Map<String, Integer> m, String[] outputFields, int index, XSSFSheet sheet,
            XSSFCellStyle style) {
        for (Map<String, String> map : data) {
            // 创建Excel的sheet的一行
            XSSFRow row = sheet.createRow(index++);
            row.setHeight((short)500);
            String key = map.get(outputFields[0]);
            Integer v = m.get(key);
            for (int i = 0; i < outputFields.length; i++) {
                XSSFCell cell = row.createCell(i);
             // 给Excel的单元格设置样式和赋值
                cell.setCellStyle(style);
				if(i==2){
					cell.setCellValue(v != null ? "否" : "是");
				} else if(i==3){
					Hyperlink link = new XSSF2007Link(XSSFHyperlink.LINK_DOCUMENT);
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
    
    public void writeBody(List<Map<String, String>> data, String[] outputFields, int index, XSSFSheet sheet,
            XSSFCellStyle style) {
        for (Map<String, String> map : data) {
            // 创建Excel的sheet的一行
            XSSFRow row = sheet.createRow(index++);
            for (int i = 0; i < outputFields.length; i++) {
                XSSFCell cell = row.createCell(i);
                row.setHeight((short)300);
				// 给Excel的单元格设置样式和赋值
				cell.setCellStyle(style);
				Object value = map.get(outputFields[i]);
				if(value != null){
					cell.setCellValue(String.valueOf(value));
				}else{
					if(i == 2)
						cell.setCellValue("合计");
					else
						cell.setCellValue("0");
				}
            }
        }
    }
    
    public int writeBodyT2_1(List<Map<String, String>> data, String[] outputFields, int index, XSSFSheet sheet,
            XSSFCellStyle[] style) {
    	XSSFRow row = null;
    	XSSFCell cell = null;
    	
    	double vals[] = new double[31];
    	double vals2[] = new double[31];
    	int number = index+1;
    	String type = null;
    	int count = 0;
    	
    	for (int i = 0; i < data.size(); i++) {
    		index++;
    		count++;
    		Map<String, String> map = data.get(i);
        	if(type == null){
        		type = map.get("TYPE");
        	}
        	
        	row = sheet.createRow(index);
        	cell = row.createCell(0);
			cell.setCellStyle(style[2]);
    		for (int j = 0; j < outputFields.length; j++) {
                cell = row.createCell(j+1);
				cell.setCellStyle(style[4]);
				Object value = map.get(outputFields[j]);
				
				if(value != null){
					Double doub = null;
					try {
						doub = Double.parseDouble(String.valueOf(value));
					} catch (NumberFormatException e) {}
					if(doub != null && j >= 2){
						double b = Double.parseDouble(String.valueOf(value));
						cell.setCellValue(b);
					} else {
						cell.setCellValue(String.valueOf(value));
					}
				}else{
					cell.setCellType(cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(0D);
				}
				double d=0;
				try {
					value = value == null ? 0 : value;
					d = Double.parseDouble(String.valueOf(value));
				} catch (NumberFormatException e) {
					
				}
				if(j >= 2){
					vals[j-2] += d;
					vals2[j-2] += d;
				}
            }//for end
    		
    		boolean flag = i == data.size() - 1;
    		if(i < data.size() - 1 || flag){
    			map = flag ? map : data.get(i+1);
    			
    			if(!type.equals(map.get("TYPE")) || flag){
    				sheet.addMergedRegion(new CellRangeAddress(number, index, 0, 0));
    				row = sheet.getRow(number);
    				cell = row.createCell(0);
    				cell.setCellValue("0".equals(type) ? "备付金专用存款账户("+(count)+")" : "备付金非活期存款账户("+(count)+")");
    				cell.setCellStyle(style[2]);
    				
    				index++;
    				createCell(sheet, style[1], index, index, 0, 2);
    				row = sheet.getRow(index);
    				sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
    				cell = row.createCell(0);
    				cell.setCellStyle(style[1]);
    				cell.setCellValue(("0".equals(type) ? "专用存款账户" : "非活期存款账户")+"合计");
    				
    				for (int k = 0; k < vals.length; k++) {
    					cell = row.createCell(k+3);
    					cell.setCellType(cell.CELL_TYPE_NUMERIC);
    					cell.setCellValue(vals[k]);
    					cell.setCellStyle(style[4]);
    				}
    				
    				vals = new double[31];
    				type = map.get("TYPE");
    				number = index + 1;
        			count = 0;
    			}
    		}
		}// data for end
    	
    	index++;
    	createCell(sheet, style[1], index, index, 0, 2);
    	row = sheet.getRow(index);
    	sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
    	cell = row.createCell(0);
		cell.setCellStyle(style[1]);
		cell.setCellValue("合计");
    	for (int k = 0; k < vals2.length; k++) {
			cell = row.createCell(k+3);
			cell.setCellType(cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(vals2[k]);
			cell.setCellStyle(style[4]);
		}
    	return index;
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
            XSSFWorkbook xb = new XSSFWorkbook();
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            XSSFSheet sheet = xb.createSheet("sheet1");
            sheet.setDefaultColumnWidth(10);
            sheet.setColumnWidth(0, 3600);
            sheet.setColumnWidth(1, 15000);
            sheet.setColumnWidth(2, 3000);
            sheet.setColumnWidth(3, 6000);

            // 创建字体样式
            XSSFFont font = xb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            
            // 创建单元格样式
            XSSFCellStyle style = xb.createCellStyle();
           
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            writeTitle(colName_CN, xb, sheet, style);

            // 创建字体样式
            font = xb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);//粗体显示
            
            // 创建单元格样式
            style = xb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);// 垂直居中
            String[] outputFields = colName;
            //主体
            writeBody(data, map, outputFields, 1, sheet, style);
            
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            XSSFSheet sheet2 = xb.createSheet("详细");
            sheet2.setDefaultColumnWidth(10);
            
            // 设置title样式
            font = xb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            
            // 创建单元格样式
            style = xb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            writeTitle(colName_CN2, xb, sheet2, style);
            outputFields = colName2;
            
            // 设置body样式
            font = xb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);//粗体显示
            
            // 创建单元格样式
            style = xb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);// 垂直居中
            
            writeBody(data2, outputFields, 1, sheet2, style);
            
            xb.write(out);
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
    public String test(List<Map<String, String>> data, String fileName)  throws Exception {
    	FileOutputStream out = null;
        File file = null;
        try {
            file = createUrlFile(fileName, fileName+"T2_1");
            out = new FileOutputStream(file);

            // 创建Excel的工作书册 Workbook,对应到一个excel文档
            XSSFWorkbook xb = new XSSFWorkbook();
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            XSSFSheet sheet = xb.createSheet("COUNT");
            sheet.setDefaultColumnWidth(10);
            sheet.setColumnWidth(0, 3500);
            sheet.setColumnWidth(1, 4500);
            sheet.setColumnWidth(2, 6500);
            
            // 创建字体样式
            XSSFFont font = xb.createFont();

            // 创建单元格样式
            XSSFCellStyle style = xb.createCellStyle();

            // 创建字体样式
            font = xb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            
            // 创建单元格样式
            style = xb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            writeTitle(colT2_1_CN, xb, sheet, style);

            // 创建字体样式
            font = xb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);//粗体显示
            
            // 创建单元格样式
            style = xb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            String[] outputFields = colT2_1;
//            writeBodyT2_1(data, outputFields, 1, sheet, style);
            
            xb.write(out);
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
            XSSFWorkbook xb = new XSSFWorkbook();
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            XSSFSheet sheet = xb.createSheet("非活期账户一览表");
            sheet.setDefaultColumnWidth(10);
            sheet.setColumnWidth(0, 4000);
            sheet.setColumnWidth(1, 4500);
            sheet.setColumnWidth(2, 6000);
            sheet.setColumnWidth(3, 3500);
            sheet.setColumnWidth(5, 3500);
            
            // 创建字体样式
            XSSFFont font = xb.createFont();

            // 创建单元格样式
            XSSFCellStyle style = xb.createCellStyle();

            // 创建字体样式
            font = xb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            
            // 创建单元格样式
            style = xb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            writeTitle(colFix_CN, xb, sheet, style);

            // 创建字体样式
            font = xb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//设置字体大小
            font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);//粗体显示
            
            // 创建单元格样式
            style = xb.createCellStyle();
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            
            String[] outputFields = colFix;
            writeBody(data, outputFields, 1, sheet, style);
            
            xb.write(out);
            return file.getName();
        }finally {
            out.close();
        }
    }
    
    public static XSSFCellStyle createStyle(XSSFWorkbook wb){
    	XSSFCellStyle style = wb.createCellStyle();
 		style.setWrapText(true);//设置自动换行
 		style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
 		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
 		style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
 		style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
 		style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
 		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
    	return style;
    }
    
    public static XSSFCellStyle[] createStyles(XSSFWorkbook xb){
	    XSSFFont font1 = xb.createFont();
	    font1.setFontHeightInPoints((short) 14);
	    font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	    
	    XSSFFont font2 = xb.createFont();
	    font2.setFontHeightInPoints((short) 12);
	    font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
	    
	    XSSFFont font3 = xb.createFont();
	    //设置字体颜色为(红色)
	    font3.setColor(HSSFColor.RED.index);
	    font3.setFontHeightInPoints((short) 12);
	    font3.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
	    
	    XSSFCellStyle style1 = createStyle(xb);
	    style1.setFont(font1);
	    
	    XSSFCellStyle style2 = createStyle(xb);
	    style2.setFont(font2);
	    
	    XSSFCellStyle style3 = createStyle(xb);
	    style3.setFont(font3);
	    
	    XSSFCellStyle style4 = xb.createCellStyle();
	    style4.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
	    style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
	    style4.setFont(font2);
	    
	    XSSFCellStyle style5 = createStyle(xb);
	    style5.setFont(font2);
	    
	    XSSFDataFormat format = xb.createDataFormat();
	    style5.setDataFormat(format.getFormat("0.000000"));
		
	    return new XSSFCellStyle[]{style1, style2, style3, style4, style5};
    }
    
    /**
     * 创建单元格 设定样式 （注意：里面都是用的createRow 外面请是用getRow 否则会出现样式错乱）
     * @param row
     * @param style
     * @param begin
     * @param end
     */
    public static void createCell(XSSFSheet sheet, XSSFCellStyle style, int firstRow, int lastRow, int firstCol, int lastCol){
    	for(int i=firstRow; i <= lastRow; i++){
    		XSSFRow row = sheet.createRow(i);
    		for(int j=firstCol; j <= lastCol;j++) {
        		XSSFCell cell = row.createCell(j);
        		cell.setCellStyle(style);
        	}
    	}
    }
    
    public String writeT2_1(List<Map<String, String>> data, String name, String fileName) throws Exception {
    	FileOutputStream out = null;
        File file = null;
        try {
            //创建Excel
        	int index = 0;
    	    XSSFWorkbook xb = new XSSFWorkbook();
    	    XSSFCellStyle style[] = createStyles(xb);
    	    //创建工作薄
    	    XSSFSheet sheet = xb.createSheet("COUNT");
    	    //创建行
    	    XSSFRow row = sheet.createRow(index);
    	    //创建单元格
    	    XSSFCell cell = row.createCell(0);
    	    
    	    for(int i=0; i <= 33; i++){
    	    	 sheet.setColumnWidth(i, 5500);
    	    }
    	    
    	    //设置单元格值
    	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 32));
    	    cell.setCellValue("表2-1 上海银行"+name+"机构备付金银行账户余额统计表");
    	    cell.setCellStyle(style[0]);
    	    cell = row.createCell(33);
    	    cell.setCellValue("单位:万元");
    	    cell.setCellStyle(style[1]);
    	    
    	    
    	    index++;
    	    createCell(sheet, style[1], index, index, 2, 33);
    	    row = sheet.getRow(index);
    	    sheet.addMergedRegion(new CellRangeAddress(index, index+1, 0, 0));
    	    sheet.addMergedRegion(new CellRangeAddress(index, index, 1, 33));
    	    cell = row.createCell(0);
    	    cell.setCellStyle(style[1]);
    	    cell = row.createCell(1);
    	    cell.setCellValue("日期");
    	    cell.setCellStyle(style[1]);
    	    
    	    index++;
    	    row = sheet.createRow(index);
    	    cell = row.createCell(0);
    	    cell.setCellStyle(style[1]);
    	    cell = row.createCell(1);
    	    cell.setCellValue("账号");
    	    cell.setCellStyle(style[1]);
    	    cell = row.createCell(2);
    	    cell.setCellValue("账户性质");
    	    cell.setCellStyle(style[1]);
    	    
    	    for(int i=1; i < 32; i++){
    	    	cell = row.createCell(i+2);
    		    cell.setCellValue(i+"日");
    		    cell.setCellStyle(style[1]);
    	    }
            
            String[] outputFields = colT2_1;
            index = writeBodyT2_1(data, outputFields, index, sheet, style);
    	    
            index++;
            row = sheet.createRow(index);
            cell = row.createCell(0);
    	    cell.setCellValue("填表人:");
    	    cell.setCellStyle(style[3]);
    	    
    	    cell = row.createCell(2);
    	    cell.setCellValue("复核人:");
    	    cell.setCellStyle(style[3]);
    	    
    	    cell = row.createCell(4);
    	    cell.setCellValue("填报日期:");
    	    cell.setCellStyle(style[3]);
    	    
            file = createUrlFile(fileName, fileName+"_T2-1");
            out = new FileOutputStream(file);
            xb.write(out);
            return file.getName();
        }finally {
			out.close();
        }
    }

    public static void main(String[] args) {
//    	readyExcel2007Temp();
    	//创建Excel
    	int index = 0;
	    XSSFWorkbook wb = new XSSFWorkbook();
	    XSSFCellStyle style[] = createStyles(wb);
	    //创建工作薄
	    XSSFSheet sheet = wb.createSheet("COUNT");
	    //创建行
	    XSSFRow row = sheet.createRow(index);
	    //创建单元格
	    XSSFCell cell = row.createCell(0);
	    
	    for(int i=0; i <= 33; i++){
	    	 sheet.setColumnWidth(i, 3500);
	    }
	    
	    //设置单元格值
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 32));
	    cell.setCellValue("表2-1 XX银行XX支付机构备付金银行账户余额统计表");
	    cell.setCellStyle(style[0]);
	    cell = row.createCell(33);
	    cell.setCellValue("单位:万元");
	    cell.setCellStyle(style[1]);
	    
	    
	    index++;
	    createCell(sheet, style[1], index, index, 2, 33);
	    row = sheet.getRow(index);
	    sheet.addMergedRegion(new CellRangeAddress(index, index+1, 0, 0));
	    sheet.addMergedRegion(new CellRangeAddress(index, index, 1, 33));
	    cell = row.createCell(0);
	    cell.setCellStyle(style[1]);
	    cell = row.createCell(1);
	    cell.setCellValue("日期");
	    cell.setCellStyle(style[1]);
	    
	    index++;
	    row = sheet.createRow(index);
	    cell = row.createCell(0);
	    cell.setCellStyle(style[1]);
	    cell = row.createCell(1);
	    cell.setCellValue("账号");
	    cell.setCellStyle(style[1]);
	    cell = row.createCell(2);
	    cell.setCellValue("账户性质");
	    cell.setCellStyle(style[1]);
	    
	    for(int i=1; i < 32; i++){
	    	cell = row.createCell(i+2);
		    cell.setCellValue(i+"日");
		    cell.setCellStyle(style[1]);
	    }
	    
	    index++;
	    createCell(sheet, style[1], index, index+5, 0, 0);
	    sheet.addMergedRegion(new CellRangeAddress(index, index+5, 0, 0));
	    row = sheet.getRow(index);
	    cell = row.createCell(0);
	    cell.setCellValue("备付金专用存款账户(5)");
	    cell.setCellStyle(style[2]);
	    
	    FileOutputStream out;
		try {
			out = new FileOutputStream("D:\\default_palette.xlsx");
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
    }
}
