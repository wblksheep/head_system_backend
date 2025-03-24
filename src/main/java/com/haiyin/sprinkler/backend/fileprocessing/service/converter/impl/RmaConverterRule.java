package com.haiyin.sprinkler.backend.fileprocessing.service.converter.impl;

import com.haiyin.sprinkler.backend.fileprocessing.dao.HeadStatus;
import com.haiyin.sprinkler.backend.fileprocessing.dao.SprinklerDAO;
import com.haiyin.sprinkler.backend.fileprocessing.dto.RmaDTO;
import com.haiyin.sprinkler.backend.fileprocessing.service.converter.DAOConverterRule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("rmaConverterRule")
public class RmaConverterRule implements DAOConverterRule<RmaDTO, SprinklerDAO> {

    @Override
    public SprinklerDAO convert(RmaDTO dto) {
        SprinklerDAO s = new SprinklerDAO();
        s.setHeadSerial(dto.getHeadSerial());
        s.setRealType(dto.getRealType());
        s.setDamageDate(dto.getDamageDate());
        s.setRmaDate(dto.getRmaDate());
        s.setCustomer(dto.getCustomer());
        s.setHeadHistory(dto.getHeadSerial());
        s.setRmaLocation(dto.getRmaLocation());
        s.setStatus(HeadStatus.UNDER_MAINTENANCE);
        s.setVersion(0);
        return s;
    }

    @Override
    public String getSceneType() {
        return "RMA";
    }
}
