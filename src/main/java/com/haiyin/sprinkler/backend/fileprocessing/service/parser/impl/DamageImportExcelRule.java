package com.haiyin.sprinkler.backend.fileprocessing.service.parser.impl;

import com.haiyin.sprinkler.backend.fileprocessing.dto.DamageDTO;
import com.haiyin.sprinkler.backend.fileprocessing.service.parser.rule.ExcelParseRule;
import com.haiyin.sprinkler.backend.utils.LocalDateParseUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
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
@Qualifier("damageImportRule")
public class DamageImportExcelRule implements ExcelParseRule<DamageDTO> {
    @Override
    public List<DamageDTO> parse(InputStream stream) throws IOException {
        // 使用POI解析为导入DTO
        List<DamageDTO> list = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(stream);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);

            for (int j =0; j < sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                if (row == null||row.getRowNum() == 0) continue; // 跳过标题行
                String headSerial = row.getCell(2).getStringCellValue();
                if(headSerial == null || headSerial.isEmpty()) continue;
                DamageDTO dto = new DamageDTO();
                dto.setHeadSerial(headSerial);

                if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                    row.getCell(1).setCellType(CellType.STRING);
                }
                dto.setDamageDate(LocalDateParseUtil.parseDate(row.getCell(1).getStringCellValue()));

                dto.setHeadHistory(row.getCell(3).getStringCellValue());

                dto.setNote(row.getCell(4).getStringCellValue());

                dto.setDamageType(row.getCell(5).getStringCellValue());

                dto.setRealType(row.getCell(6).getStringCellValue());

                list.add(dto);
            }
        }

        return list;
    }

    @Override
    public String getSceneType() {
        return "DAMAGEIMPORT";
    }
}
