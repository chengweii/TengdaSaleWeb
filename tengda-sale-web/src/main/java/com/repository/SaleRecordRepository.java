package com.repository;

import com.model.SalePartsEntity;
import com.model.SaleRecordEntity;
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
public interface SaleRecordRepository extends JpaRepository<SaleRecordEntity, Integer> {
    @Query(value = "select * from sale_record where id=:id", nativeQuery = true)
    SaleRecordEntity findOne(@Param(value = "id") Long id);
}