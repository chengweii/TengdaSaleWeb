package com.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 描述信息
 *
 * @author chengwei11
 * @date 2019/7/8
 */
@Entity
@Table(name = "sale_report", schema = "tengda_sale", catalog = "")
public class SaleReportEntity {
    private int id;
    private String name;
    private String querySql;
    private Timestamp createTime;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "query_sql")
    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleReportEntity that = (SaleReportEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(querySql, that.querySql) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, querySql, createTime);
    }
}
