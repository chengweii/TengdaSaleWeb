package com.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 描述信息
 *
 * @author chengwei11
 * @date 2019/7/1
 */
@Entity
@Table(name = "sale_record", schema = "tengda_sale", catalog = "")
public class SaleRecordEntity {
    private long id;
    private byte type;
    private String partsCode;
    private String partsName;
    private int partsNum;
    private BigDecimal partsPrice;
    private BigDecimal orderAmount;
    private String saleObject;
    private Timestamp createTime;
    private Timestamp updateTime;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type")
    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Basic
    @Column(name = "parts_code")
    public String getPartsCode() {
        return partsCode;
    }

    public void setPartsCode(String partsCode) {
        this.partsCode = partsCode;
    }

    @Basic
    @Column(name = "parts_name")
    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    @Basic
    @Column(name = "parts_num")
    public int getPartsNum() {
        return partsNum;
    }

    public void setPartsNum(int partsNum) {
        this.partsNum = partsNum;
    }

    @Basic
    @Column(name = "parts_price")
    public BigDecimal getPartsPrice() {
        return partsPrice;
    }

    public void setPartsPrice(BigDecimal partsPrice) {
        this.partsPrice = partsPrice;
    }

    @Basic
    @Column(name = "order_amount")
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Basic
    @Column(name = "sale_object")
    public String getSaleObject() {
        return saleObject;
    }

    public void setSaleObject(String saleObject) {
        this.saleObject = saleObject;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleRecordEntity that = (SaleRecordEntity) o;
        return id == that.id &&
                type == that.type &&
                partsNum == that.partsNum &&
                Objects.equals(partsCode, that.partsCode) &&
                Objects.equals(partsName, that.partsName) &&
                Objects.equals(partsPrice, that.partsPrice) &&
                Objects.equals(orderAmount, that.orderAmount) &&
                Objects.equals(saleObject, that.saleObject) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, partsCode, partsName, partsNum, partsPrice, orderAmount, saleObject, createTime, updateTime);
    }
}
