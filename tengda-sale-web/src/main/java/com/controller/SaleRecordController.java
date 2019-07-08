package com.controller;

import com.domain.JsonResult;
import com.domain.Pager;
import com.model.SaleRecordEntity;
import com.repository.SaleRecordRepository;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
    public JsonResult list(@RequestBody Pager pager) {
        PageRequest pageable = new PageRequest(pager.getPageNo() - 1, pager.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Page<SaleRecordEntity> page = saleRecordRepository.findAll(pageable);
        List<SaleRecordEntityVo> listVo = new ArrayList<SaleRecordEntityVo>();
        if (page != null && !CollectionUtils.isEmpty(page.getContent())) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            page.getContent().forEach(item -> {
                SaleRecordEntityVo vo = new SaleRecordEntityVo();
                BeanUtils.copyProperties(item, vo);
                vo.setCreateTimeText(dateFormat.format(item.getCreateTime()));
                if (item.getUpdateTime() != null) {
                    vo.setUpdateTimeText(dateFormat.format(item.getUpdateTime()));
                }
                vo.setTypeText(item.getType() == (byte) 1 ? "进货" : "出货");
                vo.setSaleObject(item.getSaleObject() == null ? "" : item.getSaleObject());
                listVo.add(vo);
            });
            return JsonResult.success(new PageImpl<SaleRecordEntityVo>(listVo, pageable, page.getTotalElements()));
        }

        return JsonResult.success(null);
    }

    @Data
    public static class SaleRecordEntityVo extends SaleRecordEntity {
        private String createTimeText;
        private String updateTimeText;
        private String typeText;
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

    @RequestMapping("/sale_record/modify")
    @ResponseBody
    public JsonResult modify(@RequestBody SaleRecordEntity saleRecordEntity) {
        SaleRecordEntity oldSaleRecordEntity = saleRecordRepository.findOne(saleRecordEntity.getId());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        oldSaleRecordEntity.setUpdateTime(now);
        oldSaleRecordEntity.setOrderAmount(saleRecordEntity.getOrderAmount());
        oldSaleRecordEntity.setPartsNum(saleRecordEntity.getPartsNum());
        oldSaleRecordEntity.setPartsPrice(saleRecordEntity.getPartsPrice());
        oldSaleRecordEntity.setSaleObject(saleRecordEntity.getSaleObject());
        SaleRecordEntity result = saleRecordRepository.saveAndFlush(oldSaleRecordEntity);
        return JsonResult.success(result);
    }

    @RequestMapping("/sale_record/delete/{id}")
    @ResponseBody
    public JsonResult delete(@PathVariable long id) {
        SaleRecordEntity saleRecordEntity = new SaleRecordEntity();
        saleRecordEntity.setId(id);
        saleRecordRepository.delete(saleRecordEntity);
        saleRecordRepository.flush();
        return JsonResult.success(true);
    }
}
