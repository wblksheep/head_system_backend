package com.haiyin.sprinkler.backend.fileprocessing.dto;

import com.haiyin.sprinkler.backend.fileprocessing.dao.HeadStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class ImportDTO {
    private String purchasDateContrast;
    private String headModel;
    private String headSerial;
    private LocalDate warehouseDate;
    private Float voltage;
    private Integer jetsout;

    private HeadStatus status;

    public String getPurchasDateContrast() {
        return this.purchasDateContrast;
    }

    public String getHeadModel() {
        return this.headModel;
    }

    public String getHeadSerial() {
        return this.headSerial;
    }

    public LocalDate getWarehouseDate() {
        return this.warehouseDate;
    }

    public Float getVoltage() {
        return this.voltage;
    }

    public Integer getJetsout() {
        return this.jetsout;
    }

    public HeadStatus getStatus() {
        return this.status;
    }

    public void setPurchasDateContrast(String purchasDateContrast) {
        this.purchasDateContrast = purchasDateContrast;
    }

    public void setHeadModel(String headModel) {
        this.headModel = headModel;
    }

    public void setHeadSerial(String headSerial) {
        this.headSerial = headSerial;
    }

    public void setWarehouseDate(LocalDate warehouseDate) {
        this.warehouseDate = warehouseDate;
    }

    public void setVoltage(Float voltage) {
        this.voltage = voltage;
    }

    public void setJetsout(Integer jetsout) {
        this.jetsout = jetsout;
    }

    public void setStatus(HeadStatus status) {
        this.status = status;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ImportDTO)) return false;
        final ImportDTO other = (ImportDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$purchasDateContrast = this.getPurchasDateContrast();
        final Object other$purchasDateContrast = other.getPurchasDateContrast();
        if (this$purchasDateContrast == null ? other$purchasDateContrast != null : !this$purchasDateContrast.equals(other$purchasDateContrast))
            return false;
        final Object this$headModel = this.getHeadModel();
        final Object other$headModel = other.getHeadModel();
        if (this$headModel == null ? other$headModel != null : !this$headModel.equals(other$headModel)) return false;
        final Object this$headSerial = this.getHeadSerial();
        final Object other$headSerial = other.getHeadSerial();
        if (this$headSerial == null ? other$headSerial != null : !this$headSerial.equals(other$headSerial))
            return false;
        final Object this$warehouseDate = this.getWarehouseDate();
        final Object other$warehouseDate = other.getWarehouseDate();
        if (this$warehouseDate == null ? other$warehouseDate != null : !this$warehouseDate.equals(other$warehouseDate))
            return false;
        final Object this$voltage = this.getVoltage();
        final Object other$voltage = other.getVoltage();
        if (this$voltage == null ? other$voltage != null : !this$voltage.equals(other$voltage)) return false;
        final Object this$jetsout = this.getJetsout();
        final Object other$jetsout = other.getJetsout();
        if (this$jetsout == null ? other$jetsout != null : !this$jetsout.equals(other$jetsout)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ImportDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $purchasDateContrast = this.getPurchasDateContrast();
        result = result * PRIME + ($purchasDateContrast == null ? 43 : $purchasDateContrast.hashCode());
        final Object $headModel = this.getHeadModel();
        result = result * PRIME + ($headModel == null ? 43 : $headModel.hashCode());
        final Object $headSerial = this.getHeadSerial();
        result = result * PRIME + ($headSerial == null ? 43 : $headSerial.hashCode());
        final Object $warehouseDate = this.getWarehouseDate();
        result = result * PRIME + ($warehouseDate == null ? 43 : $warehouseDate.hashCode());
        final Object $voltage = this.getVoltage();
        result = result * PRIME + ($voltage == null ? 43 : $voltage.hashCode());
        final Object $jetsout = this.getJetsout();
        result = result * PRIME + ($jetsout == null ? 43 : $jetsout.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    public String toString() {
        return "ImportDTO(purchasDateContrast=" + this.getPurchasDateContrast() + ", headModel=" + this.getHeadModel() + ", headSerial=" + this.getHeadSerial() + ", warehouseDate=" + this.getWarehouseDate() + ", voltage=" + this.getVoltage() + ", jetsout=" + this.getJetsout() + ", status=" + this.getStatus() + ")";
    }
}
