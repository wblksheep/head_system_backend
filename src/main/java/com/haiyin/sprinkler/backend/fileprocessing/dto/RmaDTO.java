package com.haiyin.sprinkler.backend.fileprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class RmaDTO {
    private String rmaDate;//做RMA日期
    private String headSerial;//喷头序列号，唯一
    private String realType;//返修原因，和DamageDTO共用字段
    private LocalDate damageDate;//返仓日期，DamageDTO共用字段

    private String customer;//返修客户
    private String headHistory;//喷头历史
    private String rmaLocation;//RMA地点

    public String getHeadSerial() {
        return this.headSerial;
    }

    public String getRealType() {
        return this.realType;
    }

    public LocalDate getDamageDate() {
        return this.damageDate;
    }

    public LocalDate getRmaDate() {
        return this.rmaDate;
    }

    public String getCustomer() {
        return this.customer;
    }

    public String getHeadHistory() {
        return this.headHistory;
    }

    public String getRmaLocation() {
        return this.rmaLocation;
    }

    public void setHeadSerial(String headSerial) {
        this.headSerial = headSerial;
    }

    public void setRealType(String realType) {
        this.realType = realType;
    }

    public void setDamageDate(LocalDate damageDate) {
        this.damageDate = damageDate;
    }

    public void setRmaDate(LocalDate rmaDate) {
        this.rmaDate = rmaDate;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setHeadHistory(String headHistory) {
        this.headHistory = headHistory;
    }

    public void setRmaLocation(String rmaLocation) {
        this.rmaLocation = rmaLocation;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RmaDTO)) return false;
        final RmaDTO other = (RmaDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$headSerial = this.getHeadSerial();
        final Object other$headSerial = other.getHeadSerial();
        if (this$headSerial == null ? other$headSerial != null : !this$headSerial.equals(other$headSerial))
            return false;
        final Object this$realType = this.getRealType();
        final Object other$realType = other.getRealType();
        if (this$realType == null ? other$realType != null : !this$realType.equals(other$realType)) return false;
        final Object this$damageDate = this.getDamageDate();
        final Object other$damageDate = other.getDamageDate();
        if (this$damageDate == null ? other$damageDate != null : !this$damageDate.equals(other$damageDate))
            return false;
        final Object this$rmaDate = this.getRmaDate();
        final Object other$rmaDate = other.getRmaDate();
        if (this$rmaDate == null ? other$rmaDate != null : !this$rmaDate.equals(other$rmaDate)) return false;
        final Object this$customer = this.getCustomer();
        final Object other$customer = other.getCustomer();
        if (this$customer == null ? other$customer != null : !this$customer.equals(other$customer)) return false;
        final Object this$headHistory = this.getHeadHistory();
        final Object other$headHistory = other.getHeadHistory();
        if (this$headHistory == null ? other$headHistory != null : !this$headHistory.equals(other$headHistory))
            return false;
        final Object this$rmaLocation = this.getRmaLocation();
        final Object other$rmaLocation = other.getRmaLocation();
        if (this$rmaLocation == null ? other$rmaLocation != null : !this$rmaLocation.equals(other$rmaLocation))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RmaDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $headSerial = this.getHeadSerial();
        result = result * PRIME + ($headSerial == null ? 43 : $headSerial.hashCode());
        final Object $realType = this.getRealType();
        result = result * PRIME + ($realType == null ? 43 : $realType.hashCode());
        final Object $damageDate = this.getDamageDate();
        result = result * PRIME + ($damageDate == null ? 43 : $damageDate.hashCode());
        final Object $rmaDate = this.getRmaDate();
        result = result * PRIME + ($rmaDate == null ? 43 : $rmaDate.hashCode());
        final Object $customer = this.getCustomer();
        result = result * PRIME + ($customer == null ? 43 : $customer.hashCode());
        final Object $headHistory = this.getHeadHistory();
        result = result * PRIME + ($headHistory == null ? 43 : $headHistory.hashCode());
        final Object $rmaLocation = this.getRmaLocation();
        result = result * PRIME + ($rmaLocation == null ? 43 : $rmaLocation.hashCode());
        return result;
    }

    public String toString() {
        return "RmaDTO(headSerial=" + this.getHeadSerial() + ", realType=" + this.getRealType() + ", damageDate=" + this.getDamageDate() + ", rmaDate=" + this.getRmaDate() + ", customer=" + this.getCustomer() + ", headHistory=" + this.getHeadHistory() + ", rmaLocation=" + this.getRmaLocation() + ")";
    }
}
