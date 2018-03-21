package demo.excel;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class JxlDemo {

	public static void main(String[] args) {
	    
	}

	
	
	public static void testJXL2() {
		String path = "C:/Users/Stan/Desktop/分期_大数据接口汇总模板.xls";
		File file = new File(path);
		try {
			WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
			WritableSheet writableSheet = writableWorkbook.getSheet("aa");
			int rows = writableSheet.getRows();
			for (int i = 0; i < rows; i++) {
				WritableCell writableCell_M = writableSheet.getWritableCell(i, 13);
				String content_M = writableCell_M.getContents();
				System.out.println(content_M);
				
				WritableCell writableCell_D = writableSheet.getWritableCell(i, 4);
				String content_D = writableCell_D.getContents();
				System.out.println(content_D);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testJXL1() {
		String path = "C:/Users/Stan/Desktop/分期_大数据接口汇总模板.xls";
		File file = new File(path);
		try {
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet("人行征信");
			Cell[] cells = sheet.getColumn(1);

			for (int i = 38; i < cells.length; i++) {
				Cell cell = cells[i];
				String name = cell.getContents();
				String comment = sheet.getCell(3, i).getContents().replace("\r\n", "").replace("\n", "");
				String type = sheet.getCell(4, i).getContents();
				String process = sheet.getCell(8, i).getContents().replace("\r\n", "").replace("\n", "");
				if (process != null && !"".equals(process.trim())) {
					System.out.println("private " + parseType(type) + " " + name + ";//" + comment + "#" + process);
				}
			}

		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String parseType(String type) {
		String type2 = type.toLowerCase();
		if (type2.contains("decimal")) {
			return "Double";
		}
		if (type2.contains("numeric")) {
			return "Integer";
		}
		if (type2.contains("string")) {
			return "String";
		}
		return type;
	}
}
