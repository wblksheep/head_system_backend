package com.haiyin.sprinkler.backend.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileParseUtil {
    // 样式缓存
    private static class StyleCacheKey {
        final String fontName;
        final short fontSize;
        final boolean bold;
        final HorizontalAlignment alignment;
        // 可添加其他样式属性...

        StyleCacheKey(CellStyle style, Font font) {
            this.fontName = font.getFontName();
            this.fontSize = font.getFontHeightInPoints();
            this.bold = font.getBold();
            this.alignment = style.getAlignment();
        }

        @Override
        public boolean equals(Object o) {
            // 实现equals和hashCode用于缓存比较
            return this == o;
        }
    }

    private static Map<StyleCacheKey, CellStyle> styleCache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String inputFile = "input.xlsx";
        String outputFile = "output.xlsx";
        String currentDir = System.getProperty("user.dir");
        System.out.println(currentDir);

        try (Workbook workbook = WorkbookFactory.create(new File(inputFile))) {
            Sheet sheet = workbook.getSheetAt(0);

            // 读取所有行数据（包含样式）
            List<List<CellData>> rows = new ArrayList<>();
            for (Row row : sheet) {
                List<CellData> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(new CellData(cell));
                }
                rows.add(rowData);
            }

            // 按第二列排序
            rows.sort(Comparator.comparing(row -> {
                if (row.size() > 1) {
                    return parseSerialNumber(row.get(1).getValue());
                }
                return new SerialNumber(Long.MAX_VALUE, Long.MAX_VALUE);
            }));

            // 创建新工作簿
            try (Workbook newWorkbook = new XSSFWorkbook()) {
                Sheet newSheet = newWorkbook.createSheet();

                // 写入排序后的数据
                for (int rowNum = 0; rowNum < rows.size(); rowNum++) {
                    Row newRow = newSheet.createRow(rowNum);
                    List<CellData> rowData = rows.get(rowNum);

                    for (int colNum = 0; colNum < rowData.size(); colNum++) {
                        CellData cellData = rowData.get(colNum);
                        Cell newCell = newRow.createCell(colNum);

                        // 设置值
                        setCellValue(newCell, cellData);

                        // 复制样式
                        copyCellStyle(newWorkbook, newCell, cellData);
                    }
                }

                // 复制列宽
                for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                    newSheet.setColumnWidth(i, sheet.getColumnWidth(i));
                }

                // 保存文件
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    newWorkbook.write(fos);
                }
            }
        }
    }

    private static SerialNumber parseSerialNumber(String value) {
        if (value == null) return new SerialNumber(Long.MAX_VALUE, Long.MAX_VALUE);

        String[] parts = value.split("-");
        if (parts.length != 2) return new SerialNumber(Long.MAX_VALUE, Long.MAX_VALUE);

        try {
            long prefix = Long.parseLong(parts[0]);
            long suffix = Long.parseLong(parts[1]);
            return new SerialNumber(prefix, suffix);
        } catch (NumberFormatException e) {
            return new SerialNumber(Long.MAX_VALUE, Long.MAX_VALUE);
        }
    }

    private static void setCellValue(Cell cell, CellData cellData) {
        switch (cellData.getCellType()) {
            case STRING:
                cell.setCellValue(cellData.getValue());
                break;
            case NUMERIC:
                cell.setCellValue(Double.parseDouble(cellData.getValue()));
                break;
            case BOOLEAN:
                cell.setCellValue(Boolean.parseBoolean(cellData.getValue()));
                break;
            default:
                cell.setCellValue(cellData.getValue());
        }
    }

    private static void copyCellStyle(Workbook newWorkbook, Cell newCell, CellData cellData) {
        CellStyle newStyle = newWorkbook.createCellStyle();
        CellStyle srcStyle = cellData.getCellStyle();

        // 复制字体
        Font srcFont = cellData.getFont();
        Font newFont = newWorkbook.createFont();
        newFont.setFontName(srcFont.getFontName());
        newFont.setFontHeightInPoints(srcFont.getFontHeightInPoints());
        newFont.setBold(srcFont.getBold());
        newStyle.setFont(newFont);

        // 复制对齐方式
        newStyle.setAlignment(srcStyle.getAlignment());

        // 复制其他样式属性（边框、颜色等）...

        newCell.setCellStyle(newStyle);
    }

    static class SerialNumber implements Comparable<SerialNumber> {
        final long prefix;
        final long suffix;

        SerialNumber(long prefix, long suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override
        public int compareTo(SerialNumber o) {
            int prefixCompare = Long.compare(this.prefix, o.prefix);
            return prefixCompare != 0 ? prefixCompare : Long.compare(this.suffix, o.suffix);
        }
    }

    static class CellData {
        private final String value;
        private final CellType cellType;
        private final CellStyle cellStyle;
        private final Font font;

        public CellData(Cell cell) {
            this.value = getCellValueAsString(cell);
            this.cellType = cell.getCellType();
            this.cellStyle = cell.getCellStyle();
            this.font = cell.getSheet().getWorkbook().getFontAt(cellStyle.getFontIndex());
        }

        private String getCellValueAsString(Cell cell) {
            if (cell == null) {
                return "";
            }

            CellType cellType = cell.getCellType();
            if (cellType == CellType.FORMULA) {
                cellType = cell.getCachedFormulaResultType();
            }

            try {
                switch (cellType) {
                    case STRING:
                        return cell.getRichStringCellValue().getString();

                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            return sdf.format(cell.getDateCellValue());
                        }
                        double numValue = cell.getNumericCellValue();
                        if (numValue == Math.floor(numValue)) {
                            return String.valueOf((long) numValue);
                        }
                        return String.valueOf(numValue);

                    case BOOLEAN:
                        return cell.getBooleanCellValue() ? "TRUE" : "FALSE";

                    case FORMULA:
                        // 已在上方处理过FORMULA类型
                        return "[Formula]";

                    case BLANK:
                        return "";

                    case ERROR:
                        return "ERROR:" + cell.getErrorCellValue();

                    default:
                        return cell.toString();
                }
            } catch (Exception e) {
                return "[Error Reading Cell]";
            }
        }

        public String getValue() {
            return this.value;
        }

        public CellType getCellType() {
            return this.cellType;
        }

        public CellStyle getCellStyle() {
            return this.cellStyle;
        }

        public Font getFont() {
            return this.font;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof CellData)) return false;
            final CellData other = (CellData) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$value = this.getValue();
            final Object other$value = other.getValue();
            if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
            final Object this$cellType = this.getCellType();
            final Object other$cellType = other.getCellType();
            if (this$cellType == null ? other$cellType != null : !this$cellType.equals(other$cellType)) return false;
            final Object this$cellStyle = this.getCellStyle();
            final Object other$cellStyle = other.getCellStyle();
            if (this$cellStyle == null ? other$cellStyle != null : !this$cellStyle.equals(other$cellStyle))
                return false;
            final Object this$font = this.getFont();
            final Object other$font = other.getFont();
            if (this$font == null ? other$font != null : !this$font.equals(other$font)) return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof CellData;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $value = this.getValue();
            result = result * PRIME + ($value == null ? 43 : $value.hashCode());
            final Object $cellType = this.getCellType();
            result = result * PRIME + ($cellType == null ? 43 : $cellType.hashCode());
            final Object $cellStyle = this.getCellStyle();
            result = result * PRIME + ($cellStyle == null ? 43 : $cellStyle.hashCode());
            final Object $font = this.getFont();
            result = result * PRIME + ($font == null ? 43 : $font.hashCode());
            return result;
        }

        public String toString() {
            return "FileParseUtil.CellData(value=" + this.getValue() + ", cellType=" + this.getCellType() + ", cellStyle=" + this.getCellStyle() + ", font=" + this.getFont() + ")";
        }

        // Getters...
    }
}
