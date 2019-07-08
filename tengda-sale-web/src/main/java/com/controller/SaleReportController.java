package com.controller;

import com.domain.JsonResult;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * 描述信息
 *
 * @author chengwei11
 * @date 2019/7/1
 */
@Controller
public class SaleReportController {
    @Resource
    private EntityManager entityManager;

    @RequestMapping("/sale_report/execute/{id}")
    @ResponseBody
    public JsonResult execute(@PathVariable int id) {
        Query query = entityManager.createNativeQuery("SELECT a.parts_code as '[1]编码',a.parts_name  as '[2]名称',b.type  as '[3]类型',b.create_time  as '[4]时间' from sale_parts a inner join sale_record b on a.parts_code = b.parts_code");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List rows = query.getResultList();
        return JsonResult.success(rows);
    }
}
