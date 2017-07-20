package com.java.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {

	/**
	 * 生成Excel保存到瀏覽器
	 * 
	 * @param filename
	 *            excel文件名
	 * @param excelTitle
	 *            表頭
	 * @param sheetdata
	 *            數據(String[][])
	 * @param sheetname
	 *            sheet名稱
	 * @param out
	 * @return
	 */
	public static boolean exportExcel(String filename, String[] excelTitle,
			String[][] sheetdata, String sheetname, OutputStream out) {
		WritableWorkbook wbook = null;
		WritableSheet sheet = null;

		try {
			wbook = Workbook.createWorkbook(out);
			sheet = wbook.createSheet(sheetname, 0);
			int row = 0;
			// 填写表头
			setSheetTitle(sheet, excelTitle, row);
			// 遍历map，填充数据
			setSheetData(sheet, sheetdata, row);
			wbook.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {// 切记，此处一定要关闭流，否则你会下载一个空文件
				if (wbook != null)
					wbook.close();
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 生成Excel，保存到服務器
	 * 
	 * @param excelTitle
	 *            表頭
	 * @param sheetdata
	 *            數據(String[][])
	 * @param sheetname
	 *            sheet名稱
	 * @param dir
	 *            存放目錄
	 * @param filetitle
	 *            文件名前綴
	 * 
	 * @return
	 */
	public static boolean exportExcel(String[] excelTitle,
			String[][] sheetdata, String sheetname, String dir, String filetitle) {
		WritableWorkbook wbook = null;
		WritableSheet sheet = null;

		// 自动生成日期
		SimpleDateFormat autoDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 文件名为：当前名+日期时间
		filetitle = filetitle + autoDate.format(new Date());
		File dirFile = new File(dir + filetitle + ".xls");

		try {
			// 生成文件名
			wbook = Workbook.createWorkbook(dirFile);
			sheet = wbook.createSheet(sheetname, 0);
			int row = 0;
			// 填写表头
			setSheetTitle(sheet, excelTitle, row);
			// 遍历map，填充数据
			setSheetData(sheet, sheetdata, row);
			wbook.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {// 切记，此处一定要关闭流，否则你会下载一个空文件
				if (wbook != null)
					wbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 設置表頭
	 * 
	 * @param sheet
	 * @param excelTitle
	 * @param row
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private static void setSheetTitle(WritableSheet sheet, String[] excelTitle,
			int row) throws RowsExceededException, WriteException {
		// 设置excel标题格式，字體
		WritableFont wfont = new WritableFont(WritableFont.ARIAL, 12,
				WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
				Colour.BLACK);
		WritableCellFormat wcfFC = new WritableCellFormat(wfont);
		// 设置每一列宽度, excel表頭
		for (int i = 0; i < excelTitle.length; i++) {
			sheet.setColumnView(i, 13);
			sheet.addCell(new Label(i, row, excelTitle[i], wcfFC));
		}
	}

	/**
	 * 填充Excel數據
	 * 
	 * @param sheet
	 * @param map
	 * @param row
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private static void setSheetData(WritableSheet sheet, String[][] sheetdata,
			int row) throws RowsExceededException, WriteException {
		// 遍历并填充
		for (int i = 0; i < sheetdata.length; i++) {
			row++;
			for (int j = 0; j < sheetdata[0].length; j++) {
				sheet.addCell(new Label(j, row, sheetdata[i][j]));
			}
		}
	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，可自定义工作表大小）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系 如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式
	 *            如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写
	 *            fieldMap.put("college.collegeName","学院名称")
	 * @param sheetName
	 *            工作表的名称
	 * @param sheetSize
	 *            每个工作表中记录的最大个数
	 * @param out
	 *            导出流
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list,
			LinkedHashMap<String, String> fieldMap, String sheetName,
			int sheetSize, OutputStream out) throws Exception {

		if (list.size() == 0 || list == null) {
			throw new Exception("数据源中没有任何数据");
		}

		if (sheetSize > 65535 || sheetSize < 1) {
			sheetSize = 65535;
		}

		// 创建工作簿并发送到OutputStream指定的地方
		WritableWorkbook wwb;
		try {
			wwb = Workbook.createWorkbook(out);

			// 因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
			// 所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
			// 1.计算一共有多少个工作表
			double sheetNum = Math.ceil(list.size()
					/ new Integer(sheetSize).doubleValue());

			// 2.创建相应的工作表，并向其中填充数据
			for (int i = 0; i < sheetNum; i++) {
				// 如果只有一个工作表的情况
				if (1 == sheetNum) {
					WritableSheet sheet = wwb.createSheet(sheetName, i);
					fillSheet(sheet, list, fieldMap, 0, list.size() - 1);

					// 有多个工作表的情况
				} else {
					WritableSheet sheet = wwb.createSheet(sheetName + (i + 1),
							i);

					// 获取开始索引和结束索引
					int firstIndex = i * sheetSize;
					int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list
							.size() - 1 : (i + 1) * sheetSize - 1;
					// 填充工作表
					fillSheet(sheet, list, fieldMap, firstIndex, lastIndex);
				}
			}

			wwb.write();
			wwb.close();

		} catch (Exception e) {
			e.printStackTrace();
			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				throw new ExcelException("導出Excel失敗");
			}
		}

	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，工作表大小为2003支持的最大值）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系
	 * @param out
	 *            导出流
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list,
			LinkedHashMap<String, String> fieldMap, String sheetName,
			OutputStream out) throws Exception {

		listToExcel(list, fieldMap, sheetName, 65535, out);

	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（导出到浏览器，可以自定义工作表的大小）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系
	 * @param sheetSize
	 *            每个工作表中记录的最大个数
	 * @param response
	 *            使用response可以导出到浏览器
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list,
			LinkedHashMap<String, String> fieldMap, String sheetName,
			int sheetSize, HttpServletResponse response) throws Exception {

		// 设置默认文件名为当前时间：年月日时分秒
		String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(
				new Date()).toString();

		// 设置response头信息
		response.reset();
		response.setContentType("application/vnd.ms-excel"); // 改成输出excel文件
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName + ".xls");
		// 创建工作簿并发送到浏览器
		try {

			OutputStream out = response.getOutputStream();
			listToExcel(list, fieldMap, sheetName, sheetSize, out);

		} catch (Exception e) {
			e.printStackTrace();

			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				throw new ExcelException("導出Excel失敗");
			}
		}
	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（导出到浏览器，工作表的大小是2003支持的最大值）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系
	 * @param response
	 *            使用response可以导出到浏览器
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list,
			LinkedHashMap<String, String> fieldMap, String sheetName,
			HttpServletResponse response) throws Exception {

		listToExcel(list, fieldMap, sheetName, 65535, response);
	}

	/**
	 * @MethodName : excelToList
	 * @param in
	 *            承载着Excel的输入流
	 * @param sheetName
	 *            要导入的sheet名稱，傳空表示第一個個sheet
	 * @param entityClass
	 *            ：List中对象的类型（Excel中的每一行都要转化为该类型的对象）
	 * @param fieldMap
	 *            Excel中的中文列头和类的英文属性的对应关系Map
	 * @param uniqueFields
	 *            指定业务主键组合（即复合主键），这些列的组合不能重复
	 * @return List
	 * @throws Exception
	 */
	public static <T> List<T> excelToList(InputStream in, String sheetName,
			Class<T> entityClass, LinkedHashMap<String, String> fieldMap,
			String[] uniqueFields) throws Exception {

		// 定义要返回的list
		List<T> resultList = new ArrayList<T>();
		
		try {

			// 根据Excel数据源创建WorkBook
			Workbook wb = Workbook.getWorkbook(in);
			// 获取工作表
			Sheet sheet = null;
			if (sheetName.trim() == "") {
				sheet = wb.getSheet(0);
			} else
				sheet = wb.getSheet(sheetName);

			// 获取工作表的有效行数
			int realRows = 0;
			for (int i = 0; i < sheet.getRows(); i++) {

				int nullCols = 0;
				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell currentCell = sheet.getCell(j, i);
					if (currentCell == null
							|| "".equals(currentCell.getContents().toString())) {
						nullCols++;
					}
				}

				if (nullCols == sheet.getColumns()) {
					break;
				} else {
					realRows++;
				}
			}

			// 如果Excel中没有数据则提示错误
			if (realRows <= 1) {
				throw new Exception("Excel文件中沒有任何數據");
			}

			Cell[] firstRow = sheet.getRow(0);
			String[] excelFieldNames = new String[firstRow.length];

			// 获取Excel中的列名
			for (int i = 0; i < firstRow.length; i++) {
				excelFieldNames[i] = firstRow[i].getContents().toString()
						.trim();
			}

			// 判断需要的字段在Excel中是否都存在
			boolean isExist = true;
			List<String> excelFieldList = Arrays.asList(excelFieldNames);
			for (String cnName : fieldMap.keySet()) {
				if (!excelFieldList.contains(cnName)) {
					isExist = false;
					break;
				}
			}

			// 如果有列名不存在，则抛出异常，提示错误
			if (!isExist) {
				throw new ExcelException("Excel中缺少必要欄位，或欄位名稱有誤");
			}

			// 将列名和列号放入Map中,这样通过列名就可以拿到列号
			LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();
			for (int i = 0; i < excelFieldNames.length; i++) {
				colMap.put(excelFieldNames[i], firstRow[i].getColumn());
			}

			// 判断是否有重复行
			// 1.获取uniqueFields指定的列
			Cell[][] uniqueCells = new Cell[uniqueFields.length][];
			for (int i = 0; i < uniqueFields.length; i++) {
				int col = colMap.get(uniqueFields[i]);
				uniqueCells[i] = sheet.getColumn(col);
			}

			// 2.从指定列中寻找重复行

			for (int i = 1; i < realRows; i++) {
				int nullCols = 0;
				for (int j = 0; j < uniqueFields.length; j++) {
					String currentContent = uniqueCells[j][i].getContents();
					// Cell sameCell = sheet.findCell(currentContent);
					Cell sameCell = sheet.findCell(currentContent,
							uniqueCells[j][i].getColumn(),
							uniqueCells[j][i].getRow() + 1,
							uniqueCells[j][i].getColumn(),
							uniqueCells[j][realRows - 1].getRow(), true);
					if (sameCell != null) {
						nullCols++;
					}
				}

				if (nullCols == uniqueFields.length) {
					throw new ExcelException("Excel中有重複行，請檢查");
				}
			}
			// 将sheet转换为list
			for (int i = 1; i < realRows; i++) {
				// 新建要转换的对象
				T entity = entityClass.newInstance();

				// 给对象中的字段赋值
				for (Entry<String, String> entry : fieldMap.entrySet()) {
					// 获取中文字段名
					String cnNormalName = entry.getKey();
					// 获取英文字段名
					String enNormalName = entry.getValue();
					// 根据中文字段名获取列号
					int col = colMap.get(cnNormalName);

					// 获取当前单元格中的内容
					String content = sheet.getCell(col, i).getContents()
							.toString().trim();
					// 给对象赋值
					setFieldValueByName(enNormalName, content, entity);
				}

				resultList.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				e.printStackTrace();
				throw new ExcelException("導入Excel失敗");
			}
		}
		return resultList;
	}

	/*
	 * <-------------------------辅助的私有方法------------------------------------------
	 * ----->
	 */
	/**
	 * @MethodName : getFieldValueByName
	 * @Description : 根据字段名获取字段值
	 * @param fieldName
	 *            字段名
	 * @param o
	 *            对象
	 * @return 字段值
	 */
	private static Object getFieldValueByName(String fieldName, Object o)
			throws Exception {

		Object value = null;
		Field field = getFieldByName(fieldName, o.getClass());

		if (field != null) {
			field.setAccessible(true);
			value = field.get(o);
		} else {
			throw new ExcelException(o.getClass().getSimpleName() + "類不存在欄位名 "
					+ fieldName);
		}

		return value;
	}

	/**
	 * @MethodName : fillSheet
	 * @Description : 向工作表中填充数据
	 * @param sheet
	 *            工作表
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            中英文字段对应关系的Map
	 * @param firstIndex
	 *            开始索引
	 * @param lastIndex
	 *            结束索引
	 */
	private static <T> void fillSheet(WritableSheet sheet, List<T> list,
			LinkedHashMap<String, String> fieldMap, int firstIndex,
			int lastIndex) throws Exception {

		CellView cellView = new CellView();
		cellView.setAutosize(true); // 设置自动大小
		sheet.setColumnView(1, cellView);// 根据内容自动设置列宽

		// 设置表头格式
		jxl.write.WritableFont wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
		
		WritableCellFormat wc1 = new WritableCellFormat(wfont);
		wc1.setAlignment(Alignment.CENTRE); // 设置居中
		wc1.setBorder(Border.ALL, BorderLineStyle.THIN); // 设置边框线
		wc1.setBackground(jxl.format.Colour.PALE_BLUE); // 设置单元格的背景颜色
		// wc.setWrap(true);// 设置自动换行
		
		// 设置内容格式
		jxl.write.WritableFont wfont2 = new jxl.write.WritableFont(WritableFont.ARIAL, 11);
		
		WritableCellFormat wc2 = new WritableCellFormat(wfont2);
		wc2.setAlignment(Alignment.CENTRE);
		wc2.setVerticalAlignment(VerticalAlignment.CENTRE);
		wc2.setBorder(Border.ALL, BorderLineStyle.THIN);
		wc2.setWrap(true);
		
		WritableCellFormat wcf_2 = new WritableCellFormat(wfont);
		wcf_2.setAlignment(Alignment.LEFT);
		wcf_2.setBorder(Border.ALL, BorderLineStyle.THIN);
		wcf_2.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		// 定义存放英文字段名和中文字段名的数组
		String[] enFields = new String[fieldMap.size()];
		String[] cnFields = new String[fieldMap.size()];
		
		//如果是課程提報名單報表，單獨合併單元格操作
		if(sheet.getName() == "課程提報名單"){
			
			sheet.mergeCells(0, 0, 10, 0);// 合并单元格
			String courseTitleValue="";
			T t=list.get(0);
			courseTitleValue=getFieldValueByName("courseTitle",t).toString();
			sheet.setRowView(1, 500, false);
			Label label = new Label(0, 0, "課程名稱:"+courseTitleValue, wcf_2);
			sheet.addCell(label);
			
			// 填充数组
			int count = 0;
			for (Entry<String, String> entry : fieldMap.entrySet()) {
				enFields[count] = entry.getKey();
				cnFields[count] = entry.getValue();
				count++;
			}
			// 填充表头
			sheet.setRowView(1, 400, false);
			Label label2 = new Label(0, 1, "序號", wc1);
			sheet.addCell(label2);
			for (int i = 0; i < cnFields.length; i++) {
				Label label1 = new Label(i+1, 1, cnFields[i], wc1);
				sheet.addCell(label1);
			}

			// 填充内容
			int rowNo = 2;
			for (int index = firstIndex; index <= lastIndex; index++) {
				// 获取单个对象
				Label label3 = new Label(0, index+2, String.valueOf(index+1), wc2);
				sheet.addCell(label3);
				T item = list.get(index);
				for (int i = 0; i < enFields.length; i++) {
					Object objValue = getFieldValueByNameSequence(enFields[i], item);
					String fieldValue = objValue == null ? "" : objValue.toString();
					Label label4 = new Label(i+1, rowNo, fieldValue, wc2);
					sheet.addCell(label4);
				}
				rowNo++;
			}
			for(int i=2;i<rowNo;i++)
			{
				sheet.setRowView(i, 400, false);
			}
		}else{
			// 填充数组
			int count = 0;
			for (Entry<String, String> entry : fieldMap.entrySet()) {
				enFields[count] = entry.getKey();
				cnFields[count] = entry.getValue();
				count++;
			}
			// 填充表头
			for (int i = 0; i < cnFields.length; i++) {
				Label label = new Label(i, 0, cnFields[i], wc1);
				sheet.addCell(label);
			}

			// 填充内容
			int rowNo = 1;
			for (int index = firstIndex; index <= lastIndex; index++) {
				// 获取单个对象
				T item = list.get(index);
				for (int i = 0; i < enFields.length; i++) {
					Object objValue = getFieldValueByNameSequence(enFields[i], item);
					String fieldValue = objValue == null ? "" : objValue.toString();
					Label label = new Label(i, rowNo, fieldValue, wc2);
					sheet.addCell(label);
				}

				rowNo++;
			}
		}
		
		// 设置自动列宽
		setColumnAutoSize(sheet, 5);
	}

	/**
	 * @MethodName : setFieldValueByName
	 * @Description : 根据字段名给对象的字段赋值
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            字段值
	 * @param o
	 *            对象
	 */
	public static void setFieldValueByName(String fieldName,
			Object fieldValue, Object o) throws Exception {

		Field field = getFieldByName(fieldName, o.getClass());
		if (field != null) {
			field.setAccessible(true);
			// 获取字段类型
			Class<?> fieldType = field.getType();

			// 根据字段类型给字段赋值
			if (String.class == fieldType) {
				field.set(o, String.valueOf(fieldValue));
			} else if ((Integer.TYPE == fieldType)
					|| (Integer.class == fieldType)) {
				field.set(o, Integer.parseInt(fieldValue.toString()));
			} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
				field.set(o, Long.valueOf(fieldValue.toString()));
			} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
				if (fieldValue.toString().trim() != "") {
					field.set(o, Float.valueOf(fieldValue.toString()));
				}
			} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
				field.set(o, Short.valueOf(fieldValue.toString()));
			} else if ((Double.TYPE == fieldType)
					|| (Double.class == fieldType)) {
				if (fieldValue.toString().trim() != "") {
					field.set(o, Double.valueOf(fieldValue.toString()));
				}
			} else if (Character.TYPE == fieldType) {
				if ((fieldValue != null)
						&& (fieldValue.toString().length() > 0)) {
					field.set(o,
							Character.valueOf(fieldValue.toString().charAt(0)));
				}
			} else if (Date.class == fieldType) {
				if (fieldValue.toString().trim() != "") {
					field.set(o, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(fieldValue.toString()));
				}
			} else if (Timestamp.class == fieldType) {
				if (fieldValue.toString().trim() != "") {
					field.set(
							o,
							Timestamp.valueOf(fieldValue.toString().replace(
									'/', '-')));
				}
			} else {
				field.set(o, fieldValue);
			}
		} else {
			throw new ExcelException(o.getClass().getSimpleName() + "類不存在欄位名"
					+ fieldName);
		}
	}

	/**
	 * @MethodName : setColumnAutoSize
	 * @Description : 设置工作表自动列宽和首行加粗
	 * @param ws
	 */
	private static void setColumnAutoSize(WritableSheet ws, int extraWith) {
		// 获取本列的最宽单元格的宽度
		for (int i = 0; i < ws.getColumns(); i++) {
			int colWith = 0;
			for (int j = 0; j < ws.getRows(); j++) {
				String content = ws.getCell(i, j).getContents().toString();
				// int cellWith = content.length();
				byte[] bstrLength = content.getBytes();
				int cellWith = bstrLength.length;
				if (colWith < cellWith) {
					colWith = cellWith;
				}
			}
			// 设置单元格的宽度为最宽宽度+额外宽度
			ws.setColumnView(i, colWith + extraWith);
		}

	}

	/**
	 * @MethodName : getFieldByName
	 * @Description : 根据字段名获取字段
	 * @param fieldName
	 *            字段名
	 * @param clazz
	 *            包含该字段的类
	 * @return 字段
	 */
	private static Field getFieldByName(String fieldName, Class<?> clazz) {
		// 拿到本类的所有字段
		Field[] selfFields = clazz.getDeclaredFields();

		// 如果本类中存在该字段，则返回
		for (Field field : selfFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		// 否则，查看父类中是否存在此字段，如果有则返回
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class) {
			return getFieldByName(fieldName, superClazz);
		}

		// 如果本类和父类都没有，则返回空
		return null;
	}

	/**
	 * @MethodName : getFieldValueByNameSequence
	 * @Description : 根据带路径或不带路径的属性名获取属性值
	 *              即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
	 * 
	 * @param fieldNameSequence
	 *            带路径的属性名或简单属性名
	 * @param o
	 *            对象
	 * @return 属性值
	 * @throws Exception
	 */
	private static Object getFieldValueByNameSequence(String fieldNameSequence,
			Object o) throws Exception {

		Object value = null;

		// 将fieldNameSequence进行拆分
		String[] attributes = fieldNameSequence.split("\\.");
		if (attributes.length == 1) {
			value = getFieldValueByName(fieldNameSequence, o);
		} else {
			// 根据属性名获取属性对象
			Object fieldObj = getFieldValueByName(attributes[0], o);
			String subFieldNameSequence = fieldNameSequence
					.substring(fieldNameSequence.indexOf(".") + 1);
			value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
		}
		return value;

	}

}
