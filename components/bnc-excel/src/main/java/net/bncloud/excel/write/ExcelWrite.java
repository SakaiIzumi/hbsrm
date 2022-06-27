package net.bncloud.excel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.property.OnceAbsoluteMergeProperty;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import net.bncloud.excel.style.CellStyleFontWriteHandler;
import net.bncloud.excel.style.CellStyleWidthWriteHandler;
import net.bncloud.excel.style.data.ComplexHeadStyles;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public  class ExcelWrite {

//    public static final String filePath="E:\\";

    /**
     * 单行表单头
     * @param response
     * @param fileName
     * @param sheetName
     * @param data
     * @param headColumnList
     */
    public static void simpleWrite(HttpServletResponse response, String fileName, String sheetName, List<List<String>> data,
                                   List<String> headColumnList) {
        List<List<String>> targetHeaderColumn = new ArrayList<>();
        for (String headColumn : headColumnList) {
            targetHeaderColumn.add(Collections.singletonList(headColumn));
        }
        ExcelWriter excelWriter = null;
        try {
            wrapperHttpAPIServletResponse(response, fileName);
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).head(targetHeaderColumn).build();
            excelWriter.write(data, writeSheet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }



    public static void simpleWrite(HttpServletResponse response, String fileName, String sheetName, List<List<String>> data,
                                     List<List<String>> column, Integer mergeOffSet) {

//        String fileName = fName + System.currentTimeMillis() + ".xlsx";
//        String fileFullName = filePath+fileName;
        ExcelWriter excelWriter = null;
        try {
            OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy01 = null;
            int firstRowIndex=1;
            int lastRowIndex=data.size()-mergeOffSet;
            if(firstRowIndex!=lastRowIndex&& firstRowIndex<lastRowIndex){//相等说明只有一个单元格合并，一个单元格合并会报错
                OnceAbsoluteMergeProperty onceAbsoluteMergeProperty01 =//第一列进行操作，第一行到mergeoffset进行合并  1-7  合并
                        new OnceAbsoluteMergeProperty(firstRowIndex,lastRowIndex,0,0);
                onceAbsoluteMergeStrategy01 = new OnceAbsoluteMergeStrategy(onceAbsoluteMergeProperty01);
            }

           /* OnceAbsoluteMergeProperty onceAbsoluteMergeProperty01 =//第一列进行操作，第一行到mergeoffset进行合并  1-7  合并
                    new OnceAbsoluteMergeProperty(1,data.size()-mergeOffSet,0,0);
            OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy01 = new OnceAbsoluteMergeStrategy(onceAbsoluteMergeProperty01);*/

            //公式合并，公式有可能只有一个，一个单元格合并会报错，需要判断

            OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy02 = null;
            int totalFirst = data.size() - mergeOffSet + 1;
            int totalLast=data.size()-1;
            if(totalFirst!=totalLast&&totalFirst<totalLast){
                OnceAbsoluteMergeProperty onceAbsoluteMergeProperty02 =//第一列进行操作，第mergeOffSet+1行到data.size()-1行的格子合并
                        new OnceAbsoluteMergeProperty(totalFirst,totalLast,0,0);//todo************
               onceAbsoluteMergeStrategy02 = new OnceAbsoluteMergeStrategy(onceAbsoluteMergeProperty02);
            }

            /*OnceAbsoluteMergeProperty onceAbsoluteMergeProperty02 =//第一列进行操作，第mergeOffSet+1行到data.size()-1行的格子合并
                    new OnceAbsoluteMergeProperty(data.size()-mergeOffSet+1,data.size()-1,0,0);//todo************
            OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy02 = new OnceAbsoluteMergeStrategy(onceAbsoluteMergeProperty02);*/



            OnceAbsoluteMergeProperty onceAbsoluteMergeProperty03 =//最后一行把第一列到第二列的格子横向合并
                    new OnceAbsoluteMergeProperty(data.size(),data.size(),0,1);
            OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy03 = new OnceAbsoluteMergeStrategy(onceAbsoluteMergeProperty03);

            // 设置表头样式队列FIFO
            ArrayBlockingQueue<ComplexHeadStyles> complexHeadStylesArrayBlockingQueue=new ArrayBlockingQueue<>(4);
            //设置头样式
            CellStyleFontWriteHandler cellStyleFontWriteHandler =new CellStyleFontWriteHandler(complexHeadStylesArrayBlockingQueue);

            wrapperHttpServletResponse(response,fileName);
            excelWriter = EasyExcel.write(response.getOutputStream())
                    .registerWriteHandler(onceAbsoluteMergeStrategy01)
                    .registerWriteHandler(onceAbsoluteMergeStrategy02)
                    .registerWriteHandler(onceAbsoluteMergeStrategy03)
                    .registerWriteHandler(cellStyleFontWriteHandler)
                    .registerWriteHandler(new CellStyleWidthWriteHandler())
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).head(column).build();
            excelWriter.write(data, writeSheet);


        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
//        return fileFullName;
    }


    /**
     * 普通下载
     * @param response
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    public static void wrapperHttpServletResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20")+ ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename="+fileName);
    }
    /**
     * wrapper API下载的 前端解析不了filename*=
     * @param response
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    public static void wrapperHttpAPIServletResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20") + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename="+fileName);
    }

    public static void main(String[] args) {
        WriteSheet writeSheet = new WriteSheet();
        writeSheet.setSheetName("xxx");
        OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategyColumnNormal = new OnceAbsoluteMergeStrategy(1,2,0,0);
        ExcelWriter excelWriter = EasyExcel.write(new File("D:\\test.xlsx"))
                .registerWriteHandler(onceAbsoluteMergeStrategyColumnNormal).build();
        List<List<String>> list = new ArrayList<>();

        List<String> normalCol1Strings = new ArrayList<>();
        normalCol1Strings.add("1");
        normalCol1Strings.add("2");
        normalCol1Strings.add("3");
        normalCol1Strings.add("4");
        list.add(normalCol1Strings);
        list.add(normalCol1Strings);
        list.add(normalCol1Strings);
        excelWriter.write(list,writeSheet);
        excelWriter.finish();
    }
}
