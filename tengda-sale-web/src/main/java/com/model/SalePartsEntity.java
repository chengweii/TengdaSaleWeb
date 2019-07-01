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
@Table(name = "sale_parts", schema = "tengda_sale", catalog = "")
public class SalePartsEntity {
    private int id;
    private String partsCode;
    private String partsName;
    private String partsImage;
    private int totalNum;
    private BigDecimal currentPrice;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private Timestamp createTime;
    private Timestamp updateTime;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "parts_image")
    public String getPartsImage() {
        return partsImage;
    }

    public void setPartsImage(String partsImage) {
        this.partsImage = partsImage;
    }

    @Basic
    @Column(name = "total_num")
    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    @Basic
    @Column(name = "current_price")
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Basic
    @Column(name = "max_price")
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Basic
    @Column(name = "min_price")
    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
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
        SalePartsEntity that = (SalePartsEntity) o;
        return id == that.id &&
                totalNum == that.totalNum &&
                Objects.equals(partsCode, that.partsCode) &&
                Objects.equals(partsName, that.partsName) &&
                Objects.equals(partsImage, that.partsImage) &&
                Objects.equals(currentPrice, that.currentPrice) &&
                Objects.equals(maxPrice, that.maxPrice) &&
                Objects.equals(minPrice, that.minPrice) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, partsCode, partsName, partsImage, totalNum, currentPrice, maxPrice, minPrice, createTime, updateTime);
    }
}
