package com.controller;

import com.common.JsonResult;
import com.model.SaleRecordEntity;
import com.repository.SaleRecordRepository;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述信息
 *
 * @author chengwei11
 * @date 2019/7/1
 */
@Controller
public class SaleRecordController {
    @Autowired
    private SaleRecordRepository saleRecordRepository;

    @RequestMapping("/sale_record/list")
    @ResponseBody
    public JsonResult list() {
        List<SaleRecordEntity> list = saleRecordRepository.findAll();
        List<SaleRecordEntityVo> listVo = new ArrayList<SaleRecordEntityVo>();
        if (!CollectionUtils.isEmpty(list)) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            list.forEach(item -> {
                SaleRecordEntityVo vo = new SaleRecordEntityVo();
                BeanUtils.copyProperties(item, vo);
                vo.setCreateTimeText(dateFormat.format(item.getCreateTime()));
                if (item.getUpdateTime() != null) {
                    vo.setUpdateTimeText(dateFormat.format(item.getUpdateTime()));
                }
                listVo.add(vo);
            });
            return JsonResult.success(listVo);
        }

        return JsonResult.success(null);
    }

    @Data
    public static class SaleRecordEntityVo extends SaleRecordEntity {
        private String createTimeText;
        private String updateTimeText;
    }

    @RequestMapping("/sale_record/add")
    @ResponseBody
    public JsonResult add(@RequestBody SaleRecordEntity saleRecordEntity) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        saleRecordEntity.setCreateTime(now);
        saleRecordEntity.setUpdateTime(now);
        saleRecordEntity.setOrderAmount(saleRecordEntity.getPartsPrice().multiply(BigDecimal.valueOf(saleRecordEntity.getPartsNum())));
        SaleRecordEntity result = saleRecordRepository.saveAndFlush(saleRecordEntity);
        return JsonResult.success(result);
    }
}
