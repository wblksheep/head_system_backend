package com.haiyin.sprinkler.backend.fileprocessing.service.saver.impl;

import com.haiyin.sprinkler.backend.fileprocessing.dao.SprinklerDAO;
import com.haiyin.sprinkler.backend.fileprocessing.service.saver.rule.AbstractSaverTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Qualifier("rmaSaverRule")
public class RmaSaverRule extends AbstractSaverTemplate{
    
    @Override
    protected String getInsertSQL() {
        return "INSERT INTO tb_sprinklers (id, head_serial, real_type, damage_date, rma_date, customer, head_history, rma_location, update_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "head_serial = VALUES(head_serial), real_type=VALUES(real_type), damage_date=VALUES(damage_date), rma_date=VALUES(rma_date), customer=VALUES(customer), head_history=VALUES(head_history), rma_location=VALUES(rma_location), update_time=VALUES(update_time)";
    }

    @Override
    protected Object[] mapDAOToParams(SprinklerDAO dao) {
        return new Object[]{
                dao.getId(),
                dao.getHeadSerial(),
                dao.getRealType(),
                dao.getDamageDate(),
                dao.getRmaDate(),
                dao.getCustomer(),
                dao.getHeadHistory(),
                dao.getRmaLocation(),
                LocalDateTime.now()
        };
    }

    @Override
    public String getSceneType() {
        return "RMA";
    }
}
