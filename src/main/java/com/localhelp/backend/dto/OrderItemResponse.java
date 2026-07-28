package com.localhelp.backend.dto;


public class OrderItemResponse {


    private Long medicineId;

    private String medicineName;

    private Integer quantity;

    private Double price;



    public OrderItemResponse(
            Long medicineId,
            String medicineName,
            Integer quantity,
            Double price
    ) {

        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.price = price;

    }




    public Long getMedicineId() {
        return medicineId;
    }



    public String getMedicineName() {
        return medicineName;
    }



    public Integer getQuantity() {
        return quantity;
    }



    public Double getPrice() {
        return price;
    }

}