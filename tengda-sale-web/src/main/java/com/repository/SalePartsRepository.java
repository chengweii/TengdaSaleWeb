package com.repository;

import com.model.SalePartsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 描述信息
 *
 * @author chengwei11
 * @date 2019/7/1
 */
@Repository
public interface SalePartsRepository extends JpaRepository<SalePartsEntity, Integer> {
}