package com.localhelp.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.localhelp.backend.model.OrderItem;
import com.localhelp.backend.model.Orders;

import java.util.List;



public interface OrderItemRepository 
        extends JpaRepository<OrderItem, Long> {


    List<OrderItem> findByOrder(Orders order);


}