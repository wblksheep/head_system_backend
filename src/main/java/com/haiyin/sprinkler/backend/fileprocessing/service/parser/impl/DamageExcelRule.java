package com.haiyin.sprinkler.backend.fileprocessing.service.parser.impl;

import com.haiyin.sprinkler.backend.fileprocessing.dto.DamageDTO;
import com.haiyin.sprinkler.backend.fileprocessing.dto.ImportDTO;
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
@Qualifier("damageRule")
public class DamageExcelRule implements ExcelParseRule<DamageDTO> {
    @Override
    public List<DamageDTO> parse(InputStream stream) throws IOException {
        // 使用POI解析为导入DTO
        Workbook workbook = new XSSFWorkbook(stream);
        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
        List<DamageDTO> list = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() < 2) continue; // 跳过标题行
            String headSerial = row.getCell(2).getStringCellValue();
            if(headSerial == null || headSerial.isEmpty()) continue;
            DamageDTO dto = new DamageDTO();
            dto.setHeadSerial(headSerial);

            if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                row.getCell(1).setCellType(CellType.STRING);
            }
            String purchaseContract = row.getCell(1).getStringCellValue();
            Matcher matcher = Pattern.compile("\\d+").matcher(purchaseContract);
            int matcher_start = 0;
            if (matcher.find(matcher_start)) {
                LocalDate purchaseDate = LocalDateParseUtil.parseDate(matcher.group(0));

                // 提取数字
                dto.setDamageDate(purchaseDate);
                matcher_start = matcher.end();
            }

            dto.setHistory(row.getCell(3).getStringCellValue());

            dto.setNote(row.getCell(4).getStringCellValue());

            dto.setDamageType(row.getCell(5).getStringCellValue());

            dto.setRealType(row.getCell(6).getStringCellValue());

            list.add(dto);
        }
        return list;
    }

    @Override
    public String getSceneType() {
        return "DAMAGE";
    }
}
