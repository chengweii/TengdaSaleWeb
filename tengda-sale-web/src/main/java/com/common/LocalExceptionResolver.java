package com.common;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 描述信息
 *
 * @author chengwei11
 * @date 2019/7/5
 */
public class LocalExceptionResolver implements HandlerExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        LOGGER.error("服务端异常", e);

        httpServletResponse.setContentType("text/html; charset=utf-8");
        PrintWriter out = null;
        try {

            out = httpServletResponse.getWriter();
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage(), e1);
        }
        JsonResult jsonResult = new JsonResult(500, "服务端异常", null);
        if (e instanceof HttpRequestMethodNotSupportedException) {
            jsonResult.setCode(405);
            jsonResult.setMessage("不支持的请求类型");
        } else {
            jsonResult.setCode(500);
            jsonResult.setMessage("系统内部错误");
        }

        out.println(JSON.toJSON(jsonResult));
        out.flush();
        out.close();
        return new ModelAndView();
    }
}
