package com.controller;

import com.domain.JsonResult;
import com.domain.Pager;
import com.model.SalePartsEntity;
import com.repository.SalePartsRepository;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class SalePartsController {
    @Autowired
    private SalePartsRepository salePartsRepository;

    @RequestMapping("/sale_parts/list")
    @ResponseBody
    public JsonResult list(@RequestBody Pager pager) {
        PageRequest pageable = new PageRequest(pager.getPageNo() - 1, pager.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Page<SalePartsEntity> page = salePartsRepository.findAll(pageable);
        List<SalePartsEntityVo> listVo = new ArrayList<SalePartsEntityVo>();
        if (page != null && !CollectionUtils.isEmpty(page.getContent())) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            page.getContent().forEach(item -> {
                SalePartsEntityVo vo = new SalePartsEntityVo();
                BeanUtils.copyProperties(item, vo);
                vo.setCreateTimeText(dateFormat.format(item.getCreateTime()));
                if (item.getUpdateTime() != null) {
                    vo.setUpdateTimeText(dateFormat.format(item.getUpdateTime()));
                }
                listVo.add(vo);
            });
            return JsonResult.success(new PageImpl<SalePartsEntityVo>(listVo, pageable, page.getTotalElements()));
        }

        return JsonResult.success(null);
    }

    @Data
    public static class SalePartsEntityVo extends SalePartsEntity {
        private String createTimeText;
        private String updateTimeText;
    }

    @RequestMapping("/sale_parts/add")
    @ResponseBody
    public JsonResult add(@RequestBody SalePartsEntity salePartsEntity) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        salePartsEntity.setCreateTime(now);
        salePartsEntity.setUpdateTime(now);
        salePartsEntity.setPartsImage("详见纸质二维码");
        salePartsEntity.setMaxPrice(salePartsEntity.getCurrentPrice());
        salePartsEntity.setMinPrice(salePartsEntity.getCurrentPrice());
        SalePartsEntity result = salePartsRepository.saveAndFlush(salePartsEntity);
        return JsonResult.success(result);
    }

    @RequestMapping("/sale_parts/modify")
    @ResponseBody
    public JsonResult modify(@RequestBody SalePartsEntity salePartsEntity) {
        SalePartsEntity oldSalePartsEntity = salePartsRepository.findOne(salePartsEntity.getId());

        Timestamp now = new Timestamp(System.currentTimeMillis());
        oldSalePartsEntity.setUpdateTime(now);
        oldSalePartsEntity.setPartsName(salePartsEntity.getPartsName());
        oldSalePartsEntity.setCurrentPrice(salePartsEntity.getCurrentPrice());
        oldSalePartsEntity.setMaxPrice(salePartsEntity.getCurrentPrice().compareTo(oldSalePartsEntity.getMaxPrice()) > 0 ? salePartsEntity.getCurrentPrice() : oldSalePartsEntity.getMaxPrice());
        oldSalePartsEntity.setMinPrice(salePartsEntity.getCurrentPrice().compareTo(oldSalePartsEntity.getMinPrice()) < 0 ? salePartsEntity.getCurrentPrice() : oldSalePartsEntity.getMinPrice());
        oldSalePartsEntity.setTotalNum(salePartsEntity.getTotalNum());
        SalePartsEntity result = salePartsRepository.saveAndFlush(oldSalePartsEntity);
        return JsonResult.success(result);
    }

    @RequestMapping("/sale_parts/delete/{id}")
    @ResponseBody
    public JsonResult delete(@PathVariable int id) {
        salePartsRepository.delete(id);
        salePartsRepository.flush();
        return JsonResult.success(true);
    }
}
