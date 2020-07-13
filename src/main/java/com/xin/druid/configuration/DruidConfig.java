package com.xin.druid.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DruidConfig druid连接池配置
 *
 * @author mfh 2020/7/13 17:37
 * @version 1.0.0
 **/
@Configuration
public class DruidConfig {

    private final Logger logger = LoggerFactory.getLogger(DruidConfig.class);

    @Autowired
    private DruidProperties druidProperties;

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        logger.info("==== init druid dataSource ====");
        return new DruidDataSource();
    }

    /**
     * druid监控页面配置
     * 展示Druid的统计信息,StatViewServlet的用途包括：1.提供监控信息展示的html页面2.提供监控信息的JSON API
     *
     * @return bean
     */
    @Bean
    public ServletRegistrationBean<Servlet> druidServlet() {
        logger.info("==== init Druid Servlet Config =====");
        ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>();
        // 配置一个拦截器
        servletRegistrationBean.setServlet(new StatViewServlet());
        // 指定拦截器只拦截druid管理页面的请求
        servletRegistrationBean.addUrlMappings(druidProperties.getStatViewServlet().getUrlPattern());
        HashMap<String, String> initParam = new HashMap<>();
        // 用户名
        initParam.put("loginUsername", druidProperties.getStatViewServlet().getLoginUsername());
        // 密码
        initParam.put("loginPassword", druidProperties.getStatViewServlet().getLoginPassword());
        // 是否允许重置druid的统计信息
        initParam.put("resetEnable", druidProperties.getStatViewServlet().getResetEnable());
        // 配置的格式 <IP>或者<IP>/<SUB_NET_MASK_size>其中128.242.127.1/24 24表示，前面24位是子网掩码，比对的时候，前面24位相同就匹配,不支持IPV6。
        // IP白名单 (没有配置或者为空，则允许所有访问)
        initParam.put("allow", druidProperties.getStatViewServlet().getAllow());
        // IP黑名单 (存在共同时，deny优先于allow)
        initParam.put("deny", druidProperties.getStatViewServlet().getDeny());
        //设置初始化参数
        servletRegistrationBean.setInitParameters(initParam);
        return servletRegistrationBean;
    }

    /**
     * 配置druid过滤规则
     *
     * @return bean
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidFilter() {
        logger.info("==== init filter registration ====");
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.setUrlPatterns(Collections.singletonList(druidProperties.getWebStatFilter().getUrlPattern()));
        Map<String, String> initParam = new HashMap<>();
        initParam.put("exclusions", druidProperties.getWebStatFilter().getExclusions());
        filterRegistrationBean.setInitParameters(initParam);
        return filterRegistrationBean;
    }
}
