package com.controller;

import com.alibaba.fastjson.JSON;
import com.domain.JsonResult;
import com.domain.Pager;
import com.google.common.base.MoreObjects;
import com.model.SaleReportEntity;
import com.repository.SaleReportRepository;
import lombok.Data;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleReportController.class);

    @Autowired
    private SaleReportRepository saleReportRepository;

    @Resource
    private EntityManager entityManager;

    @RequestMapping("/sale_report/list")
    @ResponseBody
    public JsonResult list(@RequestBody Pager pager) {
        PageRequest pageable = new PageRequest(pager.getPageNo() - 1, pager.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Page<SaleReportEntity> page = saleReportRepository.findAll(pageable);
        ReportResult reportResult = new ReportResult(200, "", page);
        reportResult.setFunctionInfo(JSON.toJSONString(TimeFunction.description()));
        return reportResult;
    }

    @Data
    public class ReportResult<T> extends JsonResult<T> {
        public ReportResult(int code, String message, T result) {
            super(code, message, result);
        }

        private String functionInfo;
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
                    row.put(key.toString().replaceFirst("\\[.*?\\]", ""), MoreObjects.firstNonNull(m.get(key), "--"));
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
        LOGGER.info("执行SQL={}", sql);
        return sql;
    }

    public static void main(String[] args) {
        for (TimeFunction value : TimeFunction.values()) {
            System.out.println(value.functionDesc + " " + value.getFunction().apply(null));
        }
    }

    public enum TimeFunction {
        /**
         * 本月月初
         */
        MONTH_CURRENT_BEGIN("@@MONTH_CURRENT_BEGIN", "本月月初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 本月月末
         */
        MONTH_CURRENT_END("@@MONTH_CURRENT_END", "本月月末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年本月月初
         */
        LAST_MONTH_CURRENT_BEGIN("@@LAST_MONTH_CURRENT_BEGIN", "去年本月月初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年本月月末
         */
        LAST_MONTH_CURRENT_END("@@LAST_MONTH_CURRENT_END", "去年本月月末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 上月月初
         */
        MONTH_LAST_BEGIN("@@MONTH_LAST_BEGIN", "上月月初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, -1);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 上月月末
         */
        MONTH_LAST_END("@@MONTH_LAST_END", "上月月末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年一季度初
         */
        QUARTER1_BEGIN("@@QUARTER1_BEGIN", "当年一季度初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年一季度末
         */
        QUARTER1_END("@@QUARTER1_END", "当年一季度末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 3);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年二季度初
         */
        QUARTER2_BEGIN("@@QUARTER2_BEGIN", "当年二季度初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 3);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年二季度末
         */
        QUARTER2_END("@@QUARTER2_END", "当年二季度末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 6);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年三季度初
         */
        QUARTER3_BEGIN("@@QUARTER3_BEGIN", "当年三季度初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 6);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年三季度末
         */
        QUARTER3_END("@@QUARTER3_END", "当年三季度末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 9);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年四季度初
         */
        QUARTER4_BEGIN("@@QUARTER4_BEGIN", "当年四季度初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 9);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年四季度末
         */
        QUARTER4_END("@@QUARTER4_END", "当年四季度末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 12);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年年初
         */
        YEAR_BEGIN("@@YEAR_BEGIN", "当年年初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 当年年末
         */
        YEAR_END("@@YEAR_END", "当年年末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.MONTH, 12);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年一季度初
         */
        LAST_QUARTER1_BEGIN("@@LAST_QUARTER1_BEGIN", "去年一季度初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年一季度末
         */
        LAST_QUARTER1_END("@@LAST_QUARTER1_END", "去年一季度末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 3);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年二季度初
         */
        LAST_QUARTER2_BEGIN("@@LAST_QUARTER2_BEGIN", "去年二季度初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 3);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年二季度末
         */
        LAST_QUARTER2_END("@@LAST_QUARTER2_END", "去年二季度末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 6);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年三季度初
         */
        LAST_QUARTER3_BEGIN("@@LAST_QUARTER3_BEGIN", "去年三季度初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 6);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年三季度末
         */
        LAST_QUARTER3_END("@@LAST_QUARTER3_END", "去年三季度末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 9);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年四季度初
         */
        LAST_QUARTER4_BEGIN("@@LAST_QUARTER4_BEGIN", "去年四季度初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 9);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年四季度末
         */
        LAST_QUARTER4_END("@@LAST_QUARTER4_END", "去年四季度末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 12);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年年初
         */
        LAST_YEAR_BEGIN("@@LAST_YEAR_BEGIN", "去年年初", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.HOUR_OF_DAY, 0);
            cale.set(Calendar.MINUTE, 0);
            cale.set(Calendar.SECOND, 0);
            return Formator.format(cale.getTime());
        }),
        /**
         * 去年年末
         */
        LAST_YEAR_END("@@LAST_YEAR_END", "去年年末", (input) -> {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.YEAR, -1);
            cale.set(Calendar.MONTH, 12);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
            return Formator.format(cale.getTime());
        });

        private String functionName;
        private String functionDesc;
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

        public String getFunctionDesc() {
            return functionDesc;
        }

        public void setFunctionDesc(String functionDesc) {
            this.functionDesc = functionDesc;
        }

        TimeFunction(String functionName, String functionDesc, Function<String, String> function) {
            this.function = function;
            this.functionName = functionName;
            this.functionDesc = functionDesc;
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

        public static List<String> description() {
            List<String> list = new ArrayList<String>();
            for (TimeFunction value : TimeFunction.values()) {
                list.add(String.format("@@%s:%s[%s]", value, value.functionDesc, value.getFunction().apply(null)));
            }
            return list;
        }

        private static class Formator {
            private static ThreadLocal<SimpleDateFormat> DATEFORMATE = new ThreadLocal<SimpleDateFormat>();

            private static String format(Date date) {
                if (DATEFORMATE.get() == null) {
                    DATEFORMATE.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                }
                return DATEFORMATE.get().format(date);
            }
        }
    }
}
