package com.haiyin.sprinkler.backend.fileprocessing.service.parser.impl;

import com.haiyin.sprinkler.backend.fileprocessing.dto.AllocateDTO;
import com.haiyin.sprinkler.backend.fileprocessing.dto.ImportDTO;
import com.haiyin.sprinkler.backend.fileprocessing.service.parser.rule.ExcelParseRule;
import com.haiyin.sprinkler.backend.utils.LocalDateParseUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Qualifier("importRule")
public class ImportExcelRule implements ExcelParseRule<ImportDTO> {
    @Override
    public List<ImportDTO> parse(InputStream stream) throws IOException {
        // 使用POI解析为导入DTO
        Workbook workbook = new XSSFWorkbook(stream);
        List<ImportDTO> list = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);
            for (int j =0; j < sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                if (row == null||row.getRowNum() == 0) continue; // 跳过标题行
                ImportDTO dto = new ImportDTO();
                if (row.getCell(2) == null) continue;
                String headSerial = row.getCell(2).getStringCellValue();
                dto.setHeadSerial(headSerial);
                if(row.getCell(0) == null) {
                    dto.setPurchasDateContrast(null);
                }else {
                    if (row.getCell(0).getCellType() == CellType.NUMERIC) {
                        row.getCell(0).setCellType(CellType.STRING);
                    }
                    dto.setPurchasDateContrast(row.getCell(0).getStringCellValue());
                }



                if(row.getCell(1) == null) {
                    dto.setHeadModel(null);
                }else {
                    if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                        row.getCell(1).setCellType(CellType.STRING);
                    }
                    dto.setHeadModel(row.getCell(1).getStringCellValue());
                }
                if(row.getCell(3) == null) {
                    dto.setWarehouseDate(null);
                }else {
                    if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                        row.getCell(3).setCellType(CellType.STRING);
                    }
                    dto.setWarehouseDate(LocalDateParseUtil.parseDate(row.getCell(3).getStringCellValue()));
                }

                if (headSerial != "") {
                    list.add(dto);
                }
            }
        }
        return list;
    }

    @Override
    public String getSceneType() {
        return "IMPORT";
    }
}
