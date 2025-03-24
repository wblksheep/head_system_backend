package com.haiyin.sprinkler.backend.fileprocessing.service.parser.impl;

import com.haiyin.sprinkler.backend.fileprocessing.dto.DamageDTO;
import com.haiyin.sprinkler.backend.fileprocessing.dto.RmaDTO;
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
@Qualifier("rmaRule")
public class RmaExcelRule implements ExcelParseRule<RmaDTO> {
    @Override
    public List<RmaDTO> parse(InputStream stream) throws IOException {
        // 使用POI解析为导入DTO
        Workbook workbook = new XSSFWorkbook(stream);
        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
        List<RmaDTO> list = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() < 1) continue; // 跳过标题行
            String headSerial = row.getCell(1).getStringCellValue();
            if(headSerial == null || headSerial.isEmpty()) continue;
            RmaDTO dto = new RmaDTO();
            dto.setHeadSerial(headSerial);

            dto.setRealType(row.getCell(2).getStringCellValue());

            if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                row.getCell(3).setCellType(CellType.STRING);
            }

            dto.setDamageDate(LocalDateParseUtil.parseDate(row.getCell(3).getStringCellValue()));

            if (row.getCell(4).getCellType() == CellType.NUMERIC) {
                row.getCell(4).setCellType(CellType.STRING);
            }
            dto.setRmaDate(LocalDateParseUtil.parseDate(row.getCell(4).getStringCellValue()));

            dto.setCustomer(row.getCell(5).getStringCellValue());

            dto.setHeadHistory(row.getCell(6).getStringCellValue());
            dto.setRmaLocation(row.getCell(7).getStringCellValue());

            list.add(dto);
        }
        return list;
    }

    @Override
    public String getSceneType() {
        return "RMA";
    }
}
