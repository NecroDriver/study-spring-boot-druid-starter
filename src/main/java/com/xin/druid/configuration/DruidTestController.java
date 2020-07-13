package com.xin.druid.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author creator mafh 2019/11/20 10:30
 * @author updater
 * @version 1.0.0
 */
@RestController
@RequestMapping("/druidTest")
public class DruidTestController {

    /**
     * 数据源配置
     */
    @Autowired
    DataSource dataSource;

    /**
     * 测试数据源
     */
    @GetMapping("/dataSource")
    public void testDataSource() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connect = dataSource.getConnection();
        System.out.println(connect);
        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        System.out.println("数据源最大连接数：" + druidDataSource.getMaxActive());
        System.out.println("数据源初始化连接数：" + druidDataSource.getInitialSize());
        connect.close();
    }
}
