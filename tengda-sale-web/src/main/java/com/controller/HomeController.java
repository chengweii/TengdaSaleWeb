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
public class HomeController {
    @RequestMapping("/home")
    public String home() {
        return "home";
    }
}
