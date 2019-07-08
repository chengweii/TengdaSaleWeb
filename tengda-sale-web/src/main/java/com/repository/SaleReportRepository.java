package com.repository;

import com.model.SaleRecordEntity;
import com.model.SaleReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 描述信息
 *
 * @author chengwei11
 * @date 2019/7/1
 */
@Repository
public interface SaleReportRepository extends JpaRepository<SaleReportEntity, Integer> {
}