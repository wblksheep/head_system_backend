package com.haiyin.sprinkler.backend.fileprocessing.service.parser.impl;

import com.haiyin.sprinkler.backend.fileprocessing.dto.AllocateDTO;
import com.haiyin.sprinkler.backend.fileprocessing.service.parser.rule.ExcelParseRule;
import com.haiyin.sprinkler.backend.utils.LocalDateParseUtil;
import org.apache.poi.ss.usermodel.Cell;
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
@Qualifier("allocateRule") // 可选限定符
public class AllocateExcelRule implements ExcelParseRule<AllocateDTO> {
    @Override
    public List<AllocateDTO> parse(InputStream stream) throws IOException {
        List<AllocateDTO> allocateList = new ArrayList<>();
        // 使用POI解析为领用DTO
        Workbook workbook = new XSSFWorkbook(stream);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);
            for (int j =0; j < sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                if (row == null||row.getRowNum() == 0) continue; // 跳过标题行
                AllocateDTO dto = new AllocateDTO();
                if (row.getCell(2) == null) continue;
                String sprinklerNo = row.getCell(2).getStringCellValue();
                dto.setHeadSerial(sprinklerNo);
                if(row.getCell(4)==null){
                    dto.setUsageDate(null);
                }else {
                    dto.setUsageDate(LocalDateParseUtil.parseDate(row.getCell(4).getStringCellValue()));
                }
                if(row.getCell(5)==null){
                    dto.setUser(null);
                }else {
                    dto.setUser(row.getCell(5).getStringCellValue());
                }
                if(row.getCell(6)==null){
                    dto.setUsagePurpose(null);
                }else {
                    dto.setUsagePurpose(row.getCell(6).getStringCellValue());
                }
                if(row.getCell(7)==null){
                    dto.setColor(null);
                    dto.setPosition(null);
                } else {
                    String color_position = row.getCell(7).getStringCellValue();
                    Matcher matcher = Pattern.compile("(\\d+)").matcher(color_position);
                    if (matcher.find()) {
                        String color = color_position.substring(0, matcher.start());
                        String position = matcher.group();
                        dto.setColor(color);
                        dto.setPosition(position);
                    }
                }
                if(row.getCell(8)==null){
                    dto.setHistory(null);
                }else {
                    dto.setHistory(row.getCell(8).getStringCellValue());
                }
                allocateList.add(dto);
            }
        }
        return allocateList;
    }

    @Override
    public String getSceneType() {
        return "ALLOCATE"; // 对应sceneType参数
    }
}
