package com.william;

import com.william.annotation.ExcelColumn;
import com.william.annotation.ExcelExport;
import com.william.contants.Version;
import com.william.exception.ConfigException;
import com.william.exception.ParseException;
import mx4j.log.Log;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;



/**
 * @Auther: williamdream
 * @Date: 2019/7/29 19:37
 * @Description: 导出工具类
 */
public class ExcelUtil {
    public final static float BASE_WIDTH_1_PX = 35.7f;
    public final static float BASE_HEIGHT_1_PX = 15.625f;
    public final static float BASE_CHINESE = 2 * 256;

    private final static Log logger = LogFactory.getLog(ExcelUtil.class);

    private Workbook workbook = null;
    private Version version;
    public ExcelUtil(Version version){
        if(version == Version.EXCEL2003){
            workbook = new HSSFWorkbook();
        }else if(version == Version.EXCEL2007){
            workbook = new XSSFWorkbook();
        }
        this.version = version;
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-8-12下午1:53:58<br/>
     * 功能: 导出 excel <br/>
     * @param title 标题
     * @param data 数据列表
     * @param titleStyle 标题样式
     * @param headStyle 头部样式
     * @param bodyStyle 主体数据样式
     * @return
     */
    public  Workbook export(String title, List<T> data, CellStyle titleStyle, CellStyle headStyle, CellStyle bodyStyle){
        if(data == null || data.size() == 0){
            //无数据直接反回空
            return null;
        }
        //取得当前需要导出的类型
        T dataType = data.get(0);
        Class<? extends Object> clazz = dataType.getClass();
        ExcelExport excelExport = clazz.getAnnotation(ExcelExport.class);
        if(excelExport == null){
            throw new ConfigException("配置错误,需要在目标类加注解 ExcelExport 才可导出");
        }
        int sheetMaxRow = -1;
        if(excelExport.sheetMaxRow() == -1 && version == Version.EXCEL2003){
            //设置配置为最大行数
            sheetMaxRow = 60000;
        }
        try {
            List<ColumnConfig> columnConfigs = parseColumnConfig(clazz,true);
            //计算数据是否超量,是否需要创建多个 sheet
            List<Sheet> sheets  = new ArrayList<Sheet>();
            if(sheetMaxRow == -1 || data.size() <= sheetMaxRow){
                //只会创建一个 sheet
                //delete by sanri at 2017/08/30 使用标题做为 sheet 名称会有问题如有特殊字符
//				String sheetName = title;
//				if(StringUtils.isBlank(sheetName)){
//					sheetName = "全部数据";
//				}
                Sheet createSheet = workbook.createSheet("全部数据");
                sheets.add(createSheet);
            }else{
                int sheetCount = (data.size() -1 ) / sheetMaxRow  + 1;
                for (int i = 0; i < sheetCount; i++) {
//					String sheetName = title+"_part"+i;
//					if(StringUtils.isBlank(title)){
//						sheetName = "部分数据_part"+i;
//					}
                    Sheet createSheet = workbook.createSheet("部分数据_part"+i);
                    sheets.add(createSheet);
                }
            }
            //正式添加数据
            if(sheets.size() == 1){		//添加全部数据到一张 sheet 页中,如果只有一张 sheet 页的话
                Sheet sheet = sheets.get(0);
                int startRow = createSheetTitle(title,titleStyle,excelExport,columnConfigs,sheet);
                insertDataToSheet(sheet,data,columnConfigs,startRow,headStyle,bodyStyle,excelExport);
            }else{
                for (int i=0;i<sheets.size();i++) {
                    Sheet sheet = sheets.get(i);
                    //如果有标题,添加标题
                    int startRow = createSheetTitle(title, titleStyle, excelExport, columnConfigs, sheet);
                    //复制截断的数据,到数据表 sheet 页
                    int startDataIndex = i * sheetMaxRow;
                    int endDataIndex = (i + 1) * sheetMaxRow;
                    if(endDataIndex > data.size()){
                        endDataIndex = data.size();
                    }
                    List<T> partData = new ArrayList<T>();
                    for (int j = startDataIndex; j < endDataIndex; j++) {
                        partData.add(data.get(j));
                    }
                    insertDataToSheet(sheet,partData,columnConfigs,startRow,headStyle,bodyStyle,excelExport);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     *
     * 功能:下面都是各种需要的导出方法重载<br/>
     * 创建时间:2017-8-13上午8:57:04<br/>
     * 作者：sanri<br/>
     */
    public Workbook export(List<T> data,CellStyle headStyle,CellStyle bodyStyle){
        return export("", data, null, headStyle, bodyStyle);
    }
    public Workbook export(List<T> data){
        return export(data, null, null);
    }
    public Workbook export(String title,List<T> data){
        return export(title, data, null);
    }
    public Workbook export(String title,List<T> data,CellStyle titleStyle){
        return export(title, data, titleStyle,null,null);
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-9-19下午5:06:41<br/>
     * 功能:以默认样式导出 <br/>
     * @param title
     * @param data
     * @return
     */
    public Workbook exportDefaultStyle(String title,List<T> data){
//		createCellFont("宋体", IndexedColors.BLACK, 15, b, i, u)
        CellStyle titleStyle = createCellStyle(null, IndexedColors.WHITE, true, false);
        CellStyle headStyle = createCellStyle(null, IndexedColors.LIGHT_GREEN, true, false);
        CellStyle bodyStyle = createCellStyle(null, IndexedColors.LIGHT_YELLOW, true, true);
        return export(title, data, titleStyle, headStyle, bodyStyle);
    }
    public Workbook exportDefaultStyle(List<T> data){
        return exportDefaultStyle("", data);
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-9-1下午2:31:33<br/>
     * 功能:写到输入流 <br/>
     * 此方法只能用于导出<br/>
     * @param outputStream
     * @throws IOException
     */
    public void writeTo(OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-9-1下午2:27:29<br/>
     * 功能:将当前的 workbook 转为输入流,此方法只能用于导出 <br/>
     * @return
     * @throws IOException
     */
    public InputStream toInputStream() throws IOException{
        return toInputStream(workbook);
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-9-1下午2:30:33<br/>
     * 功能:将任何一个 workbook 转为输入流 <br/>
     * @param workbook
     * @return
     * @throws IOException
     */
    public static InputStream toInputStream(Workbook workbook) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            workbook.write(byteArrayOutputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return byteArrayInputStream;
        } finally{
            if(byteArrayOutputStream != null){
                byteArrayOutputStream.close();
            }
        }
    }

    /**
     * @作者: sanri
     * @时间: 2017/8/12 21:32
     * @功能: 创建空的工作薄
     * @param version
     * @param tips 提示文本,调用导出为空时可使用此方法返回一个带提示文本的工作薄
     */
    public static Workbook createEmptyWorkbook(Version version,String tips){
        Workbook workbook = null;
        if(version == Version.EXCEL2003){
            workbook = new HSSFWorkbook();
        }else{
            workbook = new XSSFWorkbook();
        }
        if(StringUtils.isNotBlank(tips)){
            Sheet sheet = workbook.createSheet();
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue(tips);
            //设置列宽为 tips中文列宽
            sheet.setColumnWidth(0, (int)(tips.length() * BASE_CHINESE));
        }
        return workbook;
    }

    /**
     *
     * @param font 字体设置
     * @param background 背景色
     * @param center 是否居中
     * @param wrapText 是否自动换行
     * @return<br/>
     */
    public CellStyle createCellStyle(Font font,IndexedColors background,boolean center,boolean wrapText){
        CellStyle createCellStyle = workbook.createCellStyle();
        if(font != null){
            createCellStyle.setFont(font);
        }
        createCellStyle.setFillForegroundColor(background.getIndex());
        createCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        createCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        createCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        createCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        createCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        if(center){//水平居中,垂直居中
            createCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            createCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        }
        createCellStyle.setWrapText(wrapText);
        return createCellStyle;
    }

    /**
     *
     * @param family 字体种类
     * @param color 字体颜色
     * @param size 字体大小
     * @param b 是否加粗
     * @return<br/>
     */
    public Font createCellFont(String family,IndexedColors color,short size,boolean b){
        Font createFont = workbook.createFont();
        createFont.setCharSet(Font.DEFAULT_CHARSET);
        createFont.setColor(color.getIndex());
        createFont.setFontName(family);
//		createFont.setFontHeight(size);
        //modify by sanri at 2017/09/19 字体使用点数
        createFont.setFontHeightInPoints(size);
        if(b){
            createFont.setBoldweight((short)700);
        }
        return createFont;
    }

    /**
     *
     * 功能:创建字体  <br/>
     * @param family 字体种类
     * @param color 字体颜色
     * @param size 字体大小
     * @param b 是否加粗
     * @param i 是否斜体
     * @param u 是否加下划线
     * @return
     */
    public Font createCellFont(String family,IndexedColors color,short size,boolean b,boolean i,boolean u){
        Font createFont = createCellFont(family, color, size, b);
        createFont.setItalic(i);
        if(u){
            createFont.setUnderline((byte)1);
        }
        return createFont;
    }

    /**
     *
     * 功能:简单数据导入 <br/>
     * @param in 输入流
     * @param clazz 导的数据是什么类型
     * @param whichCol 导哪一列数据 从 0 开始
     * @param startRow 从 0 开始
     * @return
     * 注:只支持第一个 sheet 页
     */
    public static <T> List<T> importListData(InputStream in,Class<T> clazz,int whichCol,int startRow){
        List<T> data = new ArrayList<T>();

        Workbook workbook = getWorkbookFromInputStream(in);
        Sheet sheet = workbook.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

        //起始行大于总行数,返回空数据
        if(startRow >= physicalNumberOfRows){
            return data;
        }

        CreationHelper creationHelper = workbook.getCreationHelper();
        //开始处理每一行数据
        for(int i=startRow;i<physicalNumberOfRows;i++){
            Row row = sheet.getRow(i);
            if(row == null){
                //处理行是绝对空的问题 add by sanri at 2017/12/12
                continue;
            }
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();
            if(whichCol >= physicalNumberOfCells){
                data.add(null);
                continue;
            }
            Cell cell = row.getCell(whichCol);
            //转换 cell 数据,并加入
            if(cell == null){
                continue;
            }
            T cellData = transferCellData(cell.getCellType(),cell,clazz, DateFormatUtils.ISO_DATE_FORMAT,decimalFormat,creationHelper);
            data.add(cellData);
        }
        return data;
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-11-15下午12:31:59<br/>
     * 功能:导入  map 类型数据 <br/>
     * @param in
     * @param clazz
     * @param keyCol 键列 从 0 开始
     * @param valueCol 值列 从 0 开始
     * @param startRow 起始行 从 0 开始
     * @return
     */
    public static <V> Map<String,V> importMapData(InputStream in, Class<V> clazz, int keyCol, int valueCol, int startRow){
        Map<String,V> mapData = new HashMap<String, V>();

        Workbook workbook = getWorkbookFromInputStream(in);
        Sheet sheet = workbook.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

        //起始行大于总行数,返回空数据
        if(startRow >= physicalNumberOfRows){
            return mapData;
        }

        CreationHelper creationHelper = workbook.getCreationHelper();

        //开始处理每一行数据
        for(int i=startRow;i<physicalNumberOfRows;i++){
            Row row = sheet.getRow(i);
            if(row == null){
                //处理行是绝对空的问题 add by sanri at 2017/12/12
                continue;
            }
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();
            if(keyCol >= physicalNumberOfCells){
                throw new IllegalArgumentException("键列不能为空 [row:"+i+",col:"+keyCol+"]");
            }

            //获取键数据
            Cell keyCell = row.getCell(keyCol);
            if(keyCell == null){
                continue;
            }
            String key = transferCellData(keyCell.getCellType(),keyCell, String.class, DateFormatUtils.ISO_DATE_FORMAT, decimalFormat,creationHelper);

            //获取值数据
            V value = null;
            if(valueCol < physicalNumberOfCells){
                Cell valueCell = row.getCell(valueCol);
                if(valueCell == null){
                    continue;
                }
                value = transferCellData(valueCell.getCellType(),valueCell, clazz, DateFormatUtils.ISO_DATE_FORMAT, decimalFormat,creationHelper);
            }

            mapData.put(key, value);
        }

        return mapData;
    }


    /**
     * @作者: sanri
     * @时间: 2017/8/12 21:37
     * @功能: 从输入流导入 excel 数据,只支持一个 sheet 页
     * @param in
     */
    public static <T> List<T> importData(InputStream in,Class<T> clazz){
        ExcelImport excelImport = clazz.getAnnotation(ExcelImport.class);
        if(excelImport == null){
            throw new ConfigException("需要在目标类加注解 ExcelImport 才可实现导入");
        }
        List<T> data = new ArrayList<T>();
        try{
            Workbook workbook = getWorkbookFromInputStream(in);

            //真正解析 excel 流,只解析第一个 sheet 页
            List<ColumnConfig> columnConfigs = parseColumnConfig(clazz,false);
            int startRow = excelImport.startRow();
            CreationHelper creationHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.getSheetAt(0);
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            for(int i = startRow;i<physicalNumberOfRows;i++){
                Row row = sheet.getRow(i);
                if(row == null){
                    //add by sanri at 2017/12/12 解决绝对空行问题
                    continue;
                }
                //判断每行的数据都为 null 跳过 at 2018/3/13
                boolean emptyRow = isEmptyRow(row);
                if(emptyRow){
                    continue;
                }
                T dataItem = clazz.newInstance();
                data.add(dataItem);
                for (ColumnConfig columnConfig : columnConfigs) {
                    try{
                        int index = columnConfig.getIndex();
                        Method writeMethod = columnConfig.getWriteMethod();
                        Cell cell = row.getCell(index);
                        if(cell != null){		//add by sanri at 2017/09/08 解决单元格为空单元格的问题
                            invokeData(cell.getCellType(), cell, writeMethod, columnConfig, dataItem, creationHelper);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (ConfigException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2018-3-13下午3:18:12<br/>
     * 功能:判断当前行是否全为空,如果全为空,返回 true <br/>
     * @param row
     * @return
     */
    private static boolean isEmptyRow(Row row) {
        if (row == null) {
            return true;
        }
        short firstCellNum = row.getFirstCellNum();
        short lastCellNum = row.getLastCellNum();

        for (int i = firstCellNum; i < lastCellNum; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-12-2下午4:11:26<br/>
     * 功能:从输入流获取工作薄 <br/>
     * @param in
     * @return
     */
    public static Workbook getWorkbookFromInputStream(InputStream in)throws ParseException {
//		Workbook workbook = null;
//		if (!in.markSupported()) {
//			in = new PushbackInputStream(in, 8);
//		}
//		try {
//			if (POIFSFileSystem.hasPOIFSHeader(in)) {
//				workbook =  new HSSFWorkbook(in);
//			}
//			if (POIXMLDocument.hasOOXMLHeader(in)) {
//				workbook =  new XSSFWorkbook(OPCPackage.open(in));
//			}
//		}  catch (Exception e) {
//			throw new ParseException("excel 流解析失败", e);
//		}
//		return workbook;
        try {
            return WorkbookFactory.create(in);
        } catch (Exception e) {
            throw new ParseException("excel 流解析失败 ",e);
        }
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-12-2下午4:21:40<br/>
     * 功能:将单元格数据转换成指定类型 <br/>
     * @param cell
     * @param dataType
     * @param creationHelper CreationHelper creationHelper = workbook.getCreationHelper();
     * @return
     */
    public static <T> T transferCellData(int cellType, Cell cell, Class<T> dataType, FastDateFormat dateFormat, DecimalFormat numberFormat, CreationHelper creationHelper ){
        if(cell == null){
            // 处理单元格为绝对空的问题 add by sanri at 2017/12/12
            return null;
        }
        switch (cellType) {
            case Cell.CELL_TYPE_BOOLEAN:
                boolean booleanCellValue = cell.getBooleanCellValue();
                if(dataType == Boolean.class || dataType == boolean.class){
                    return dataType.cast(booleanCellValue);
                }
                //TODO 需要将 bool 值转 dataType 类型
                return null;
            case Cell.CELL_TYPE_BLANK:
                //null 值无需写入
                return null;
            case Cell.CELL_TYPE_FORMULA:
                FormulaEvaluator formulaEvaluator = creationHelper.createFormulaEvaluator();
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                int newCellType = cellValue.getCellType();
                return transferCellData(newCellType, cell, dataType, dateFormat, numberFormat, creationHelper);
            case Cell.CELL_TYPE_NUMERIC:
                //add by sanri at 2017/09/08 判断是否为日期单元格,使用 excel 日期进行转换
                double doubleValue = cell.getNumericCellValue();
                //根据目标类型来判断需要设置的值
                if(dataType == Date.class){
                    //目标类型为 Date
                    Date javaDate = DateUtil.getJavaDate(doubleValue);
                    return dataType.cast(javaDate);
                }

                if(dataType == String.class){
                    if( DateUtil.isCellDateFormatted(cell)){
                        //如果单元格是 date 类型,则转为 date 字符串
                        Date javaDate = DateUtil.getJavaDate(doubleValue);
                        return dataType.cast(dateFormat.format(javaDate));
                    }

                    String realValue = decimalFormat.format(doubleValue);
                    return dataType.cast(realValue);
                }

                if(dataType== int.class || dataType== Integer.class){
                    return dataType.cast(new Double(doubleValue).intValue());
                }

                if(dataType == Float.class || dataType == Float.class){
                    return dataType.cast(new Double(doubleValue).floatValue());
                }

                if(dataType == Double.class || dataType == double.class){
                    return dataType.cast(doubleValue);
                }

                if(dataType == Long.class || dataType == long.class){
                    return dataType.cast(new Double(doubleValue).longValue());
                }
                logger.error("不支持的数字类型转换,只支持[int,float,long,double]中的一种,在:"+cell.getRowIndex()+" 行,"+cell.getColumnIndex()+" 列");
                break;
            case Cell.CELL_TYPE_STRING:
                String stringCellValue = cell.getStringCellValue();
                if(dataType.isPrimitive() || dataType == Integer.class || dataType == Short.class
                        || dataType == Long.class || dataType == Float.class
                        || dataType == Double.class || dataType == Character.class
                        || dataType == Boolean.class || dataType == Byte.class ){
                    return dataType.cast(Double.parseDouble(stringCellValue));		//TODO 需要转换
                }

                if(dataType == Date.class){
                    String pattern = dateFormat.getPattern();
                    try {
                        Date parseDate = DateUtils.parseDate(stringCellValue, new String []{pattern});
                        return dataType.cast(parseDate);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                        logger.error("单元格日期解析错误,给定的日期值为 :"+stringCellValue+",要求的日期格式为:"+pattern+"; 在:"+cell.getRowIndex()+" 行,"+cell.getColumnIndex()+" 列");
                    }
                }

                return dataType.cast(stringCellValue);
            case Cell.CELL_TYPE_ERROR:
                //作为 null 值写入
                return null;
            default:
                break;
        }
        return null;
    }

    /**
     *
     * 功能:注入数据,这里可能会有很多问题,还有数据小数点问题<br/>
     * 创建时间:2017-8-12下午10:52:05<br/>
     * 作者：sanri<br/>
     * @param cellType
     * @param cell
     * @param writeMethod
     * @param columnConfig
     * @param dataItem
     * @param creationHelper
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException<br/>
     */
    final static DecimalFormat decimalFormat = new DecimalFormat("0");
    private static void invokeData(int cellType, Cell cell, Method writeMethod, ColumnConfig columnConfig, Object dataItem, CreationHelper creationHelper) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> dataType = columnConfig.getDataType();
        switch (cellType) {
            case Cell.CELL_TYPE_BOOLEAN:
                boolean booleanCellValue = cell.getBooleanCellValue();
                if(dataType == Boolean.class || dataType == boolean.class){
                    writeMethod.invoke(dataItem, booleanCellValue);
                }else{
                    writeMethod.invoke(dataItem, String.valueOf(booleanCellValue));
                }
                break;
            case Cell.CELL_TYPE_BLANK:
                //null 值无需写入
                break;
            case Cell.CELL_TYPE_FORMULA:
                FormulaEvaluator formulaEvaluator = creationHelper.createFormulaEvaluator();
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                int newCellType = cellValue.getCellType();
                invokeData(newCellType, cell, writeMethod, columnConfig, dataItem, creationHelper);
                break;
            case Cell.CELL_TYPE_NUMERIC:
                //add by sanri at 2017/09/08 判断是否为日期单元格,使用 excel 日期进行转换
                double doubleValue = cell.getNumericCellValue();
                //根据目标类型来判断需要设置的值
                if(dataType == Date.class){
                    //目标类型为 Date
                    Date javaDate = DateUtil.getJavaDate(doubleValue);
                    writeMethod.invoke(dataItem, javaDate);
                }else if(dataType == String.class){
                    if( DateUtil.isCellDateFormatted(cell)){
                        //如果单元格是 date 类型,则转为 date 字符串
                        Date javaDate = DateUtil.getJavaDate(doubleValue);
                        writeMethod.invoke(dataItem, DateFormatUtils.format(javaDate,columnConfig.getPattern()));
                    }else{
                        //add by sanri at 2017/09/08 解决读出科学计数法的字符串问题
                        String realValue = decimalFormat.format(doubleValue);
                        writeMethod.invoke(dataItem, realValue);
                    }
                }else if(dataType== int.class || dataType== Integer.class){
                    writeMethod.invoke(dataItem, (int)doubleValue);
                }else if(dataType == Float.class || dataType == Float.class){
                    writeMethod.invoke(dataItem, (float)doubleValue);
                }else if(dataType == Double.class || dataType == double.class){
                    writeMethod.invoke(dataItem, doubleValue);
                }else if(dataType == Long.class || dataType == long.class){
                    writeMethod.invoke(dataItem, (long)doubleValue);
                }else{
                    logger.error("不支持的数字类型转换,只支持[int,float,long,double]中的一种,在:"+columnConfig.getChinese());
                }
                break;
            case Cell.CELL_TYPE_STRING:
                String stringCellValue = cell.getStringCellValue();
                if(dataType.isPrimitive() || dataType == Integer.class || dataType == Short.class
                        || dataType == Long.class || dataType == Float.class
                        || dataType == Double.class || dataType == Character.class
                        || dataType == Boolean.class || dataType == Byte.class ){
                    writeMethod.invoke(dataItem, Double.parseDouble(stringCellValue));//TODO 每种都需要转换
                }else if(dataType == Date.class){
                    try {
                        writeMethod.invoke(dataItem, DateUtils.parseDate(stringCellValue, new String[]{columnConfig.getPattern()}));
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                        logger.error("单元格日期解析错误,给定的日期值为 :"+stringCellValue+",要求的日期格式为:"+columnConfig.getPattern());
                    }
                }else{
                    writeMethod.invoke(dataItem, stringCellValue);
                }
                break;
            case Cell.CELL_TYPE_ERROR:
                //作为 null 值写入
                break;
            default:
                break;
        }
    }

    /**
     * 功能:获取类的列配置<br/>
     * 创建时间:2017-8-12下午10:08:21<br/>
     * 作者：sanri<br/>
     * @param clazz
     * @param readWrite 解析读时为真,解析写时为假
     * @return
     * @throws IntrospectionException
     * @throws ConfigException
     * @throws NoSuchFieldException
     * @throws SecurityException<br/>
     */
    private static List<ColumnConfig> parseColumnConfig(Class<? extends Object> clazz,boolean readWrite) throws IntrospectionException,
            ConfigException, NoSuchFieldException, SecurityException {
        //获取列配置,所有需要导出的类,最后应该都是从 Object 继承
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        if(propertyDescriptors == null || propertyDescriptors.length == 0 ){
            //必须要有属性配置
            throw new ConfigException("bean 和其父类, 必须至少包含一个属性");
        }
        //获取 bean 上所有的列配置
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            String propertyName = propertyDescriptor.getName();
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            if(!typeSupport(propertyType)){
                throw new ConfigException("不支持的类型:"+propertyType);
            }
            //只导出属性可读的属性,没有 get 方法的属性不进行导出
            if((readMethod != null && readWrite) || (writeMethod != null && !readWrite)){
                //先从属性列上获取配置,如果属性列上没有,就从读方法上获取,并覆盖属性列上的配置
                ColumnConfig columnConfig = new ColumnConfig(propertyName, readMethod, writeMethod);
                columnConfig.setDataType(propertyType);
                Field propertyField = null;
                Class<?> currentClass = clazz;
                while(currentClass != Object.class && propertyField == null){
                    try{
                        propertyField = currentClass.getDeclaredField(propertyName);
                    }catch(NoSuchFieldException e){
                        currentClass = currentClass.getSuperclass();
                    }
                }
                if(propertyField == null){
                    throw new NoSuchFieldException("没有此属性:"+propertyName);
                }
                ExcelColumn excelColumn = propertyField.getAnnotation(ExcelColumn.class);
                if(excelColumn != null){
                    columnConfig.config(excelColumn.value(), excelColumn.width(),excelColumn.charWidth(),excelColumn.pxWidth(), excelColumn.index(), excelColumn.hidden(), excelColumn.pattern(),excelColumn.chineseWidth());
                }
                //使用方法上的配置,覆盖属性上的配置
                ExcelColumn methodExcelColumn = null;
                if(readWrite){
                    //从读方法上覆盖配置
                    methodExcelColumn = readMethod.getAnnotation(ExcelColumn.class);
                }else{
                    //从写方法上覆盖配置
                    methodExcelColumn = writeMethod.getAnnotation(ExcelColumn.class);
                }
                if(methodExcelColumn != null){
                    columnConfig.config(methodExcelColumn.value(), methodExcelColumn.width(),excelColumn.charWidth(),excelColumn.pxWidth(), methodExcelColumn.index(), methodExcelColumn.hidden(), methodExcelColumn.pattern(),excelColumn.chineseWidth());
                }
                //add by sanri at 2017/08/30 只有配置了 ExcelColumn 的才可进行导入导出
                if(excelColumn != null || methodExcelColumn != null){
                    columnConfigs.add(columnConfig);
                }
            }
        }
        //对导出的属性配置进行排序
        Collections.sort(columnConfigs);
        return columnConfigs;
    }

    /**
     * 作者: sanri
     * 时间 : 2017/08/12
     * 功能 : 创建 sheet 标题,如果存在的话
     * @param title
     * @param titleStyle
     * @param excelExport
     * @param columnConfigs
     * @param sheet
     * @return 返回当前 sheet 起始行
     */
    private static int createSheetTitle(String title, CellStyle titleStyle, ExcelExport excelExport, List<ColumnConfig> columnConfigs, Sheet sheet) {
        int startRow = 0;
        if(StringUtils.isNotBlank(title)){
            Row titleRow = sheet.createRow(startRow++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title);
            if(titleStyle != null){
                titleCell.setCellStyle(titleStyle);
            }
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnConfigs.size() - 1));
            short titleRowHeight = excelExport.titleRowHeight();
            titleRowHeight = (short) (titleRowHeight * BASE_HEIGHT_1_PX);
            titleRow.setHeight(titleRowHeight);
        }
        return startRow;
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-8-12下午6:35:29<br/>
     * 功能:添加数据到 sheet 页 <br/>
     * @param <T>
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static <T> void insertDataToSheet(Sheet sheet,List<T> partData, List<ColumnConfig> columnConfigs,int startRow,CellStyle headStyle,CellStyle bodyStyle,ExcelExport excelExport) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Row headRow = sheet.createRow(startRow++);
        headRow.setHeight((short)(excelExport.headRowHeight() * BASE_HEIGHT_1_PX));
        //创建标题列
        for (int i=0;i<columnConfigs.size();i++) {
            ColumnConfig columnConfig = columnConfigs.get(i);
            String chinese = columnConfig.getChinese();
            Cell headCell = headRow.createCell(i);
            headCell.setCellValue(chinese);
            if(headStyle != null){
                headCell.setCellStyle(headStyle);
            }
        }
        //创建数据列
        for (int i = 0; i < partData.size(); i++) {
            Row bodyRow = sheet.createRow(startRow ++);
            bodyRow.setHeight((short)(excelExport.bodyRowHeight() * BASE_HEIGHT_1_PX));
            T dataItem = partData.get(i);
            for (int j=0;j<columnConfigs.size();j++) {
                ColumnConfig columnConfig = columnConfigs.get(j);
                Method readMethod = columnConfig.getReadMethod();
                Cell bodyCell = bodyRow.createCell(j);
                if(bodyStyle != null){
                    bodyCell.setCellStyle(bodyStyle);
                }
                Object cellData = readMethod.invoke(dataItem);
                Class<?> dataType = columnConfig.getDataType();
                if(dataType == Date.class){
                    //获取日期对象数据
                    Date cellDataReal = null;
                    if(cellData != null){
                        cellDataReal = (Date)cellData;
                    }

                    //如果是日期类型,则调用转换规则进行转换
                    String pattern = columnConfig.getPattern();
                    if(StringUtils.isBlank(pattern)){
                        //如果是空格式,直接设置日期数据
                        bodyCell.setCellValue(cellDataReal);
                    }else{
                        if(cellDataReal != null){
                            bodyCell.setCellValue(DateFormatUtils.format(cellDataReal,pattern));
                        }
                    }
                }else if(dataType == Boolean.class || dataType == boolean.class){
                    //必须有值
                    boolean cellBooleanReal = Boolean.parseBoolean(ObjectUtils.toString(cellData));
                    if(cellBooleanReal){
                        bodyCell.setCellValue("是");
                    }else{
                        bodyCell.setCellValue("否");
                    }
                }else{
                    bodyCell.setCellValue(ObjectUtils.toString(cellData));
                }
            }
        }
        //设置列宽
        boolean autoWidth = excelExport.autoWidth();
        if(autoWidth){
            //自动列宽后使用两倍自动列宽
            for (int i=0;i<columnConfigs.size();i++) {
                sheet.autoSizeColumn(i);
                ColumnConfig columnConfig = columnConfigs.get(i);
                if(columnConfig.isChineseWidth()){
                    int width = sheet.getColumnWidth(i);
                    // 宽度设置为原来两倍,并且加一个中文字宽度
                    int width_2 = (int) (width * 2 + 1 * BASE_CHINESE);
                    //add by sanri at 2017/12/02 解决最大宽度超出限制问题
                    if(width > 65280){
                        width = 65280;
                    }
                    sheet.setColumnWidth(i, width_2);
                }
            }
        }else{
            //宽度配置策略 如果没有配置任何宽度,则取标题中文字宽度,如果有配置,则使用配置
            for (int i = 0; i < columnConfigs.size(); i++) {
                ColumnConfig columnConfig = columnConfigs.get(i);
                //begin modify by sanri at 2017/09/19 增加列宽配置策略
                int width = columnConfig.getWidth();
                int charWidth = columnConfig.getCharWidth();
                int pxWidth = columnConfig.getPxWidth();
                int finalWidth = -1;
                if(width == -1 && charWidth == -1 && pxWidth == -1){
                    //没有配置任何宽度,使用标题中文字宽度
                    finalWidth = (int) (columnConfig.getChinese().length() * BASE_CHINESE);
                }else{
                    if(width != -1){
                        finalWidth  = width;
                    }else if(charWidth != -1){
                        finalWidth = (int) (charWidth * BASE_CHINESE);
                    }else{
                        finalWidth = (int) (pxWidth * BASE_WIDTH_1_PX);
                    }
                }
//				if(width < columnConfig.getChinese().length()){
//					//如果默认宽度是小于了中文字的宽度,则取中文字的宽度
//					width = (int) (columnConfig.getChinese().length() * BASE_CHINESE);
//				}
                //add by sanri at 2017/12/02 解决最大宽度超出限制问题
                if(finalWidth > 65280){
                    finalWidth = 65280;
                }
                sheet.setColumnWidth(i,finalWidth);
                //end modify by sanri at 2017/09/19 增加列宽配置策略
            }
        }
        //隐藏列配置
        for (int i = 0; i < columnConfigs.size(); i++) {
            ColumnConfig columnConfig = columnConfigs.get(i);
            boolean hidden = columnConfig.isHidden();
            sheet.setColumnHidden(i,hidden);
        }
    }

    /**
     *
     * 作者:sanri <br/>
     * 时间:2017-8-12下午2:29:59<br/>
     * 功能: 是否支持给定的类型<br/>
     * @param clazz
     * @return
     */
    private static boolean typeSupport(Class<?> clazz){
        return clazz.isPrimitive() || clazz == String.class
                || clazz == Integer.class || clazz == Short.class
                || clazz == Long.class || clazz == Float.class
                || clazz == Double.class || clazz == Character.class
                || clazz == Boolean.class || clazz == Byte.class
                || clazz == Date.class;
    }

}
