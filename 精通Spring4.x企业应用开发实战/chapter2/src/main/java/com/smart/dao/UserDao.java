package com.smart.dao;

import com.smart.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 11981 on 2017/3/12.
 */

//通过Spring注解定义一个DAO
@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    //自动注入JdbcTemplate的Bean
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    //根据用户名和密码获取匹配的用户数
    public int getMatchCount(String userName, String password){
        return jdbcTemplate.queryForObject(MATCH_COUNT_SQL, new Object[] {userName, password}, Integer.class);
    }

    //根据用户名查询的SQL语句
    private final static String MATCH_COUNT_SQL = " SELECT count(*) FROM " +
            " t_user WHERE user_name =? and password=? ";
    //tips:在每行SQL语句的前后添加空格，可以避免一些错误
    private final static String UPDATE_LOGIN_INFO_SQL = " UPDATE t_user SET " +
            " last_visit=?,last_ip=?,credits=? WHERE user_id =?";

    //根据用户名获取User对象
    public User findUserByUserName(final String userName){
		String sqlStr = " SELECT user_id,user_name,credits "
				+ " FROM t_user WHERE user_name =? ";
        final User user = new User();
        jdbcTemplate.query(sqlStr, new Object[]{userName},
                //匿名类方式实现回调函数
                new RowCallbackHandler() {
                    //负责将查询结果从ResultSet装载到类似于领域对象的对象实例中。
                    public void processRow(ResultSet resultSet) throws SQLException {
                        user.setUserId(resultSet.getInt("user_id"));
                        user.setUserName(userName);
                        user.setCredits(resultSet.getInt("credits"));
                    }
                });
        return user;
    }

    //更新用户积分、最后登录IP以及最后登陆时间
    public void updateLoginInfo(User user){
        jdbcTemplate.update(UPDATE_LOGIN_INFO_SQL, new Object[] {user.getLastVisit(),
        user.getLastIp(), user.getCredits(), user.getUserId()});
    }

}
