package com.wwh.homework0801.mapper;

import com.wwh.homework0801.model.OrderInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Created by diandian
 * @date 2021/8/15.
 */
@Repository
public interface OrderInfoMapper {

    void insert(OrderInfo order);

    void delete(Long id);

    void update(OrderInfo order);

    List<OrderInfo> queryList(Map<String, Object> condition);

}
