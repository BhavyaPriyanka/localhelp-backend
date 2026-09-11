package com.localhelp.backend.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.localhelp.backend.dto.OrderRequest;
import com.localhelp.backend.dto.OrderResponse;
import com.localhelp.backend.dto.OrderHistoryResponse;

import com.localhelp.backend.service.OrderService;


import java.util.List;



@RestController
@RequestMapping("/orders")

public class OrderController {



    private final OrderService orderService;



    public OrderController(
            OrderService orderService
    ){

        this.orderService = orderService;

    }





    /*
     * CREATE ORDER
     *
     * POST /orders
     *
     * Header:
     * Authorization: Bearer <JWT_TOKEN>
     *
     * Body:
     * {
     *   "items":[
     *      {
     *        "medicineId":1,
     *        "quantity":2
     *      }
     *   ]
     * }
     *
     */


    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request
    ){


        OrderResponse response =
                orderService.createOrder(request);



        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );

    }






    /*
     * GET USER ORDER HISTORY
     *
     * GET /orders/my-orders
     *
     * Header:
     * Authorization: Bearer <JWT_TOKEN>
     *
     */


    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderHistoryResponse>> getMyOrders(){


        List<OrderHistoryResponse> orders =
                orderService.getMyOrders();



        return ResponseEntity.ok(
                orders
        );

    }

    @GetMapping
public ResponseEntity<List<OrderHistoryResponse>> getOrders(){

    return ResponseEntity.ok(
        orderService.getMyOrders()
    );

}



}