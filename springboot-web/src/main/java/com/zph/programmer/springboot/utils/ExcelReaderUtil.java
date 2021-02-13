package com.zph.programmer.springboot.utils;

import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 读取xlsx大文件
 **/
@Slf4j
public class ExcelReaderUtil {
    //excel2003扩展名
    public static final String EXCEL03_EXTENSION = ".xls";
    //excel2007扩展名
    public static final String EXCEL07_EXTENSION = ".xlsx";

    public static List<List<Object>> readExcelSheet(String fileName, int sheetIndex) throws Exception {
        if (fileName.endsWith(EXCEL07_EXTENSION)) {
            FileInputStream in = new FileInputStream(fileName);
            Workbook wk = StreamingReader.builder()
                    .rowCacheSize(100)  //缓存到内存中的行数，默认是10 ->不支持获取行数等操作
                    .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(in);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            Sheet sheet = wk.getSheetAt(sheetIndex);
            if (sheet == null) {
                return Collections.emptyList();
            }
            List<List<Object>> rowDataList = new ArrayList<>();
            //遍历所有的行
            int columnNum = 0;
            for (Row row : sheet) {
                if (row == null) continue;
                int rowNum = row.getRowNum();
                log.info("开始遍历第" + rowNum + "行数据：" + row.getLastCellNum());
                //遍历所有的列
                if (rowNum == 0) {
                    columnNum = row.getLastCellNum();
                }
                // 存放每一行的数据集合
                List<Object> cellValueList = new ArrayList<>(columnNum);
                for (Cell cell : row) {
                    cellValueList.add(ExcelReaderUtil.getCellValue(cell));
                }
                rowDataList.add(cellValueList);
            }
            return rowDataList;
        } else {
            throw new Exception("只能打开XLSX格式的文件");
        }
    }

    public static List<List<List<Object>>> readFullExcel(String fileName) throws Exception {
        FileInputStream in = new FileInputStream(fileName);
        Workbook wk = StreamingReader.builder()
                .rowCacheSize(100)  //缓存到内存中的行数，默认是10 ->不支持获取行数等操作
                .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
                .open(in);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
        List<List<List<Object>>> dataList = new ArrayList<>();
        int sheetNum = wk.getNumberOfSheets();
        log.info("解析excel,共" + sheetNum + "页数据");
        for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {
            log.info("开始遍历sheet" + sheetIndex + "页数据");
            Sheet sheet = wk.getSheetAt(sheetIndex);
            if (sheet == null) {
                return Collections.emptyList();
            }
            List<List<Object>> rowDataList = new ArrayList<>();
            //遍历所有的行
            int columnNum = 0;
            for (Row row : sheet) {
                if (row == null) continue;
                int rowNum = row.getRowNum();

                //遍历所有的列
                if (rowNum == 0) {
                    columnNum = row.getLastCellNum();
                }
                // 存放每一行的数据集合
                List<Object> cellValueList = new ArrayList<>(columnNum);
                for (Cell cell : row) {
                    cellValueList.add(ExcelReaderUtil.getCellValue(cell));
                }
                rowDataList.add(cellValueList);
            }
            log.info("遍历sheet" + sheetIndex + "页数据读取成功");
            dataList.add(rowDataList);
        }
        log.info("解析excel" + fileName + "成功");
        return dataList;
    }

    /**
     * 获取单元格内容
     */
    public static Object getCellValue(Cell cell) {
        if (cell == null) return null;
        if (CellType.STRING.equals(cell.getCellType())) {
            return cell.getRichStringCellValue().getString();
        } else if (CellType.NUMERIC.equals(cell.getCellType())) {
            return cell.getNumericCellValue();
        } else if (CellType.BOOLEAN.equals(cell.getCellType())) {
            return cell.getBooleanCellValue();
        } else if (CellType.FORMULA.equals(cell.getCellType())) {
            return cell.getCellFormula();
        } else {
            return null;
        }
    }


    public static void main(String[] args) throws Exception {
        String path = "d:/output.xlsx";
        List<List<Object>> list = ExcelReaderUtil.readExcelSheet(path, 1);
        System.out.println("1");
    }
}