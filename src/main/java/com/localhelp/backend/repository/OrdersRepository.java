package com.localhelp.backend.repository;

import com.localhelp.backend.model.Orders;
import com.localhelp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository 
        extends JpaRepository<Orders, Long> {

    List<Orders> findByUser(User user);

}