package com.wwh.homework0801.mapper;

import com.wwh.homework0801.model.OrderInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by diandian
 * @date 2021/8/15.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@MapperScan("com.wwh.homework0801.mapper")
public class OrderInfoMapperTest {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Test
    @Transactional
    public void test() throws SQLException {
        // 通过sharding插入数据，通过sharding自己的日志输出看出插入不同的数据库和表
        orderInfoMapper.insert(new OrderInfo(1L, 1L));
        orderInfoMapper.insert(new OrderInfo(2L, 2L));

        // 只传use_info_id，看到单库进行了所有表的查询
        Map<String, Object> condition = new HashMap<>(1);
        condition.put("userInfoId", 1L);

        List<OrderInfo> result = orderInfoMapper.queryList(condition);
        assert result.size() == 1;

        // 只传id，看到进行了多库单表的查询
        condition = new HashMap<>(1);
        condition.put("id", 1L);
        result = orderInfoMapper.queryList(condition);
        assert result.size() == 1;


    }
}
