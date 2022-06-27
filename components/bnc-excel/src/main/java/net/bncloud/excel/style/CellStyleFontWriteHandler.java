package net.bncloud.excel.style;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import net.bncloud.excel.style.data.ComplexHeadStyles;
import org.apache.poi.ss.usermodel.*;

import java.util.concurrent.ArrayBlockingQueue;



public class CellStyleFontWriteHandler extends AbstractCellStyleStrategy {

    private ArrayBlockingQueue<ComplexHeadStyles> headStylesQueue;

    private Workbook workbook;

    public CellStyleFontWriteHandler(ArrayBlockingQueue<ComplexHeadStyles> headStylesQueue){
        this.headStylesQueue=headStylesQueue;
    }


    @Override
    protected void initCellStyle(Workbook workbook) {
        this.workbook=workbook;
    }

    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

        WriteCellStyle writeCellStyle=new WriteCellStyle();
        WriteFont writeFont = new WriteFont();
        writeFont.setColor(IndexedColors.BLACK1.getIndex());
        writeFont.setFontHeightInPoints((short)12);
        writeFont.setBold(true);
        writeCellStyle.setWriteFont(writeFont);
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.
        writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        writeCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        writeCellStyle.setBorderLeft(BorderStyle.NONE);
        writeCellStyle.setBorderTop(BorderStyle.NONE);
        writeCellStyle.setBorderRight(BorderStyle.NONE);
        writeCellStyle.setBorderBottom(BorderStyle.NONE);

        if(headStylesQueue !=null && ! headStylesQueue.isEmpty()){

            ComplexHeadStyles complexHeadStyle=headStylesQueue.peek();
            // 取出队列中的自定义表头信息，与当前坐标比较，判断是否相符
            if(cell.getColumnIndex() == complexHeadStyle.getY() && relativeRowIndex.equals(complexHeadStyle.getX())){
                // 设置自定义的表头样式
                writeCellStyle.setFillForegroundColor(complexHeadStyle.getIndexColor());
                // 样式出队
                headStylesQueue.poll();
            }
        }

        // WriteCellStyle转换为CellStyle
        CellStyle headCellStyle = StyleUtil.buildHeadCellStyle(workbook, writeCellStyle);
        // 设置表头样式
        cell.setCellStyle(headCellStyle);

    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

    }

}