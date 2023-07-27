package vip.penint.cloud.common.core.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel工具类
 * 基于alibaba easyExcel
 *
 * @author Penint
 */
public class ExcelUtils<T> {

    /**
     *
     * @param response
     * @param t 导出的实体对象
     * @param list 导出的数据list
     * @param excelName excel名称
     * @param sheetName 工作表名称
     */
    public void exportExcel(HttpServletResponse response, T t, List<T> list,String excelName,String sheetName) {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode(excelName, "UTF-8");

            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 样式设置
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //设置背景颜色
            headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            //设置头字体
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontHeightInPoints((short) 13);
            headWriteFont.setBold(true);
            headWriteCellStyle.setWriteFont(headWriteFont);
            //设置头居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //内容策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置 水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            EasyExcel
                    .write(response.getOutputStream(), t.getClass())
                    .sheet(sheetName)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .registerWriteHandler(new SheetWriteHandler() {
                        @Override
                        public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

                        }

                        @Override
                        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                            // 冻结首行
                            writeSheetHolder.getSheet().createFreezePane(0, 1, 0, 1);
                        }
                    }).doWrite(list);
//        EasyExcel.write(response.getOutputStream(), Company.class).sheet(1,"企业").doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
