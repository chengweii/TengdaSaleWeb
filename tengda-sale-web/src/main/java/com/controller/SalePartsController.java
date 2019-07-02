package com.controller;

import com.common.JsonResult;
import com.model.SalePartsEntity;
import com.repository.SalePartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
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
    public String list(ModelMap modelMap) {
        List<SalePartsEntity> list = salePartsRepository.findAll();
        modelMap.addAttribute("list", list);
        return "sale_parts/list";
    }

    @RequestMapping("/sale_parts/add")
    @ResponseBody
    public JsonResult add(@RequestBody SalePartsEntity salePartsEntity) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        salePartsEntity.setCreateTime(now);
        salePartsEntity.setUpdateTime(now);
        SalePartsEntity result = salePartsRepository.saveAndFlush(salePartsEntity);
        return JsonResult.success(result);
    }

    @RequestMapping("/sale_parts/delete/{id}")
    public String delete(int id) {
        salePartsRepository.delete(id);
        salePartsRepository.flush();
        return "redirect:/sale_parts/list";
    }
}
