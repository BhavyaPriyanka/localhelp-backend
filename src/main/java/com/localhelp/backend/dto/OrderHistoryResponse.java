package com.localhelp.backend.dto;


import java.util.List;


public class OrderHistoryResponse {


    private Long orderId;

    private Double totalAmount;

    private String orderDate;

    private List<OrderItemResponse> items;



    public OrderHistoryResponse(
            Long orderId,
            Double totalAmount,
            String orderDate,
            List<OrderItemResponse> items
    ){

        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.items = items;

    }



    public Long getOrderId(){
        return orderId;
    }


    public Double getTotalAmount(){
        return totalAmount;
    }


    public String getOrderDate(){
        return orderDate;
    }


    public List<OrderItemResponse> getItems(){
        return items;
    }

}