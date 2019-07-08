package com.controller;

import com.domain.JsonResult;
import com.domain.Pager;
import com.model.SaleReportEntity;
import com.repository.SaleReportRepository;
import lombok.Data;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述信息
 *
 * @author chengwei11
 * @date 2019/7/1
 */
@Controller
public class SaleReportController {
    @Autowired
    private SaleReportRepository saleReportRepository;

    @Resource
    private EntityManager entityManager;

    @RequestMapping("/sale_report/list")
    @ResponseBody
    public JsonResult list(@RequestBody Pager pager) {
        PageRequest pageable = new PageRequest(pager.getPageNo() - 1, pager.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Page<SaleReportEntity> page = saleReportRepository.findAll(pageable);
        return JsonResult.success(page);
    }

    @RequestMapping("/sale_report/add")
    @ResponseBody
    public JsonResult add(@RequestBody SaleReportEntity saleReportEntity) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        saleReportEntity.setCreateTime(now);
        SaleReportEntity result = saleReportRepository.saveAndFlush(saleReportEntity);
        return JsonResult.success(result);
    }

    @RequestMapping("/sale_report/modify")
    @ResponseBody
    public JsonResult modify(@RequestBody SaleReportEntity saleReportEntity) {
        SaleReportEntity oldSaleReportEntity = saleReportRepository.findOne(saleReportEntity.getId());
        oldSaleReportEntity.setName(saleReportEntity.getName());
        oldSaleReportEntity.setQuerySql(saleReportEntity.getQuerySql());
        SaleReportEntity result = saleReportRepository.saveAndFlush(oldSaleReportEntity);
        return JsonResult.success(result);
    }

    @RequestMapping("/sale_report/delete/{id}")
    @ResponseBody
    public JsonResult delete(@PathVariable int id) {
        saleReportRepository.delete(id);
        saleReportRepository.flush();
        return JsonResult.success(true);
    }

    @RequestMapping("/sale_report/execute/{id}")
    @ResponseBody
    public JsonResult execute(@PathVariable int id) {
        SaleReportEntity saleReportEntity = saleReportRepository.findOne(id);
        Query query = entityManager.createNativeQuery(dealSQL(saleReportEntity));
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List rows = query.getResultList();

        if (!CollectionUtils.isEmpty(rows)) {
            List sortRows = (List) rows.stream().map(item -> {
                Map m = new TreeMap();
                m.putAll((Map) item);

                Map<String, Object> row = new LinkedHashMap<String, Object>();
                m.keySet().forEach(key -> {
                    row.put(key.toString().replaceFirst("\\[.*?\\]", ""), m.get(key));
                });

                return row;
            }).collect(Collectors.toList());

            ExecuteResult executeResult = new ExecuteResult();
            executeResult.setContent(sortRows);
            executeResult.setAttrNames(new ArrayList());
            executeResult.setHeaders(new ArrayList());
            Map map = (Map) sortRows.get(0);
            map.keySet().forEach(item -> {
                executeResult.getHeaders().add(item);
                executeResult.getAttrNames().add(item);
            });
            return JsonResult.success(executeResult);
        }

        return JsonResult.success(null);
    }

    @Data
    public static class ExecuteResult<T> {
        private List<T> content;
        private List<String> headers;
        private List<String> attrNames;
    }

    private String dealSQL(SaleReportEntity oldSaleReportEntity) {
        String sql = oldSaleReportEntity.getQuerySql();
        for (TimeFunction value : TimeFunction.values()) {
            sql = sql.replaceAll(value.getFunctionName(), "'" + value.getFunction().apply(null) + "'");
        }
        return sql;
    }

    public enum TimeFunction {

        /**
         * 当月
         */
        MONTH_CURRENT("@@MONTH_CURRENT", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(cale.getTime());
        });
        private String functionName;
        private Function<String, String> function;

        public String getFunctionName() {
            return functionName;
        }

        public void setFunctionName(String functionName) {
            this.functionName = functionName;
        }

        public Function<String, String> getFunction() {
            return function;
        }

        public void setFunction(Function<String, String> function) {
            this.function = function;
        }

        TimeFunction(String functionName, Function<String, String> function) {
            this.function = function;
            this.functionName = functionName;
        }

        public static TimeFunction fromName(String name) {
            TimeFunction[] values = TimeFunction.values();
            for (TimeFunction value : values) {
                if (value.functionName.equals(name)) {
                    return value;
                }
            }

            return null;
        }
    }
}
