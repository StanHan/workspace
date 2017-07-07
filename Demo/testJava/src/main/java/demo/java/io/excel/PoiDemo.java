package demo.java.io.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlObject;

public class PoiDemo {

	public static void main(String[] args) throws Exception {
		testRewriting();
		XmlObject a = null;
	}
	
	public static void testRewriting() throws IOException {
		File file = new File("d:\\cache\\workbook1.xls");
		InputStream inputStream = new FileInputStream(file);
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(inputStream);
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = wb.getSheetAt(0);
		int rowNumber = sheet.getPhysicalNumberOfRows();
		System.out.println(rowNumber);
		int firstRowNum = sheet.getFirstRowNum();
		System.out.println(firstRowNum);
		for (Row row : sheet) {
			for (Cell cell : row) {
				CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
				System.out.print(cellRef.formatAsString());
				System.out.print(" - ");

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					System.out.println(cell.getRichStringCellValue().getString());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						System.out.println(cell.getDateCellValue());
					} else {
						System.out.println(cell.getNumericCellValue());
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.println(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					System.out.println(cell.getCellFormula());
					break;
				default:
					System.out.println();
				}
			}
		}
		
		for (int i = 0; i < 20; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				row = sheet.createRow(i);
			}
			Cell cell = row.getCell(7);
			if (cell == null) {
				cell = row.createCell(7);
			}

			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue("a test");
		}
		

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		fileOut.close();
	}

	public static void demo1() throws IOException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");
		Sheet sheet2 = wb.createSheet("second sheet");
		// returns " O'Brien's sales "
		String safeName = WorkbookUtil.createSafeSheetName("[O'Brien's sales*?]");
		Sheet sheet3 = wb.createSheet(safeName);
		CreationHelper createHelper = wb.getCreationHelper();
		// Create a row and put some cells in it. Rows are 0 based.
		Row row_0 = sheet.createRow(0);
		// Create a cell and put a value in it.
		Cell cell_0 = row_0.createCell(0);
		cell_0.setCellValue(1);

		// Or do it on one line.
		row_0.createCell(1).setCellValue(1.2);
		row_0.createCell(2).setCellValue(createHelper.createRichTextString("This is a string"));
		row_0.createCell(3).setCellValue(true);
		row_0.createCell(4).setCellValue(new Date());

		// we style the second cell as a date (and time). It is important to
		// create a new cell style from the workbook otherwise you can end up
		// modifying the built in style and effecting not only this cell but
		// other cells.
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
		// Style the cell with borders all around.设置单元格格式
		CellStyle style = wb.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREEN.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLUE.getIndex());
		style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());

		// Aqua background，单元格背景色
		CellStyle style2 = wb.createCellStyle();
		style2.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		style2.setFillPattern(CellStyle.BIG_SPOTS);

		// Orange "foreground", foreground being the fill foreground not the
		// font color.
		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		// Create a new font and alter it.设置字体
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 24);
		font.setFontName("Courier New");
		font.setItalic(true);
		font.setStrikeout(true);
		style.setFont(font);

		Cell cell_5 = row_0.createCell(5);
		cell_5.setCellValue(new Date());
		cell_5.setCellStyle(cellStyle);

		// you can also set date as java.util.Calendar
		Cell cell_6 = row_0.createCell(6);
		cell_6.setCellValue(Calendar.getInstance());
		cell_6.setCellStyle(cellStyle);

		Row row_2 = sheet.createRow((short) 2);
		row_2.createCell(0).setCellValue(1.1);
		row_2.createCell(1).setCellValue(new Date());
		row_2.createCell(2).setCellValue(Calendar.getInstance());
		row_2.createCell(3).setCellValue("a string");
		row_2.createCell(4).setCellValue(true);
		row_2.createCell(5).setCellType(Cell.CELL_TYPE_ERROR);

		Row row_4 = sheet.createRow((short) 4);
		row_4.setHeightInPoints(30);

		createCell(wb, row_4, (short) 0, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_BOTTOM);
		createCell(wb, row_4, (short) 1, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.VERTICAL_BOTTOM);
		createCell(wb, row_4, (short) 2, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
		createCell(wb, row_4, (short) 3, CellStyle.ALIGN_GENERAL, CellStyle.VERTICAL_CENTER);
		createCell(wb, row_4, (short) 4, CellStyle.ALIGN_JUSTIFY, CellStyle.VERTICAL_JUSTIFY);
		createCell(wb, row_4, (short) 5, CellStyle.ALIGN_LEFT, CellStyle.VERTICAL_TOP);
		createCell(wb, row_4, (short) 6, CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_TOP);

		// Merging cells,合并单元格
		Row row_6 = sheet.createRow((short) 6);
		Cell cell = row_6.createCell((short) 1);
		cell.setCellValue("This is a test of merging");

		sheet.addMergedRegion(new CellRangeAddress(1, // first row (0-based)
				1, // last row (0-based)
				1, // first column (0-based)
				2 // last column (0-based)
		));

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("d:\\cache\\workbook1.xls");
		wb.write(fileOut);
		fileOut.close();

		Workbook xssfWb = new XSSFWorkbook();
		FileOutputStream fileOutStream = new FileOutputStream("d:\\cache\\workbook2.xlsx");
		xssfWb.write(fileOutStream);
		fileOutStream.close();
	}

	/**
	 * Using a File object allows for lower memory consumption, while an
	 * InputStream requires more memory as it has to buffer the whole file.
	 */
	public static void demo2() throws EncryptedDocumentException, InvalidFormatException, IOException {

		// Use a file
		Workbook wb = WorkbookFactory.create(new File("MyExcel.xls"));

		Sheet sheet1 = wb.getSheetAt(0);
		for (Row row : sheet1) {
			for (Cell cell : row) {
				CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
				System.out.print(cellRef.formatAsString());
				System.out.print(" - ");

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					System.out.println(cell.getRichStringCellValue().getString());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						System.out.println(cell.getDateCellValue());
					} else {
						System.out.println(cell.getNumericCellValue());
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.println(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					System.out.println(cell.getCellFormula());
					break;
				default:
					System.out.println();
				}
			}
		}

		// Use an InputStream, needs more memory
		Workbook wb2 = WorkbookFactory.create(new FileInputStream("MyExcel.xlsx"));
	}

	/**
	 * If using HSSFWorkbook or XSSFWorkbook directly, you should generally go
	 * through NPOIFSFileSystem or OPCPackage, to have full control of the
	 * lifecycle (including closing the file when done):
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public static void demo3() throws IOException, Exception {

		// HSSFWorkbook, File
		NPOIFSFileSystem fs = new NPOIFSFileSystem(new File("file.xls"));
		HSSFWorkbook wb = new HSSFWorkbook(fs.getRoot(), true);
		fs.close();

		// HSSFWorkbook, InputStream, needs more memory
		NPOIFSFileSystem fs2 = new NPOIFSFileSystem(new FileInputStream("MyExcel.xlsx"));
		HSSFWorkbook wb2 = new HSSFWorkbook(fs2.getRoot(), true);

		// XSSFWorkbook, File
		OPCPackage pkg = OPCPackage.open(new File("file.xlsx"));
		XSSFWorkbook wb3 = new XSSFWorkbook(pkg);
		pkg.close();

		// XSSFWorkbook, InputStream, needs more memory
		OPCPackage pkg2 = OPCPackage.open(new FileInputStream("MyExcel.xlsx"));
		XSSFWorkbook wb4 = new XSSFWorkbook(pkg);
		pkg.close();
	}

	public static void reading_Rewriting() throws IOException, EncryptedDocumentException, InvalidFormatException {
		InputStream inp = new FileInputStream("workbook.xls");
		// InputStream inp = new FileInputStream("workbook.xlsx");

		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheetAt(0);
		Row row = sheet.getRow(2);
		Cell cell = row.getCell(3);
		if (cell == null) {
			cell = row.createCell(3);
		}

		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue("a test");

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("workbook.xls");
		wb.write(fileOut);
		fileOut.close();
	}

	/**
	 * Creates a cell and aligns it a certain way.
	 *
	 * @param wb
	 *            the workbook
	 * @param row
	 *            the row to create the cell in
	 * @param column
	 *            the column number to create the cell in
	 * @param halign
	 *            the horizontal alignment for the cell.
	 */
	private static void createCell(Workbook wb, Row row, short column, short halign, short valign) {
		Cell cell = row.createCell(column);
		cell.setCellValue("Align It");
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(halign);
		cellStyle.setVerticalAlignment(valign);
		cell.setCellStyle(cellStyle);
	}
}
