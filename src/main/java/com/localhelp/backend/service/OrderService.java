package com.localhelp.backend.service;

import com.localhelp.backend.dto.OrderItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.localhelp.backend.dto.OrderRequest;
import com.localhelp.backend.dto.OrderResponse;
import com.localhelp.backend.dto.OrderHistoryResponse;

import com.localhelp.backend.model.Medicine;
import com.localhelp.backend.model.OrderItem;
import com.localhelp.backend.model.Orders;
import com.localhelp.backend.model.User;

import com.localhelp.backend.repository.MedicineRepository;
import com.localhelp.backend.repository.OrderItemRepository;
import com.localhelp.backend.repository.OrdersRepository;
import com.localhelp.backend.repository.UserRepository;

import java.util.List;


@Service
@Transactional
public class OrderService {


    private final MedicineRepository medicineRepo;

    private final OrdersRepository ordersRepo;

    private final OrderItemRepository itemRepo;

    private final UserRepository userRepo;



    public OrderService(
            MedicineRepository medicineRepo,
            OrdersRepository ordersRepo,
            OrderItemRepository itemRepo,
            UserRepository userRepo
    ) {
        this.medicineRepo = medicineRepo;
        this.ordersRepo = ordersRepo;
        this.itemRepo = itemRepo;
        this.userRepo = userRepo;
    }




    public OrderResponse createOrder(OrderRequest request) {


        // GET LOGGED-IN USER FROM JWT

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if(authentication == null){

            throw new RuntimeException(
                    "User not authenticated"
            );
        }


        String username = authentication.getName();


        System.out.println(
                "JWT USERNAME = " + username
        );



        User user =
                userRepo.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );




       Orders order = new Orders();

order.setUser(user);

order.setTotalAmount(0.0);

order.setStatus("PLACED");



        Orders savedOrder =
                ordersRepo.save(order);



        double total = 0.0;



        for(OrderRequest.Item item : request.getItems()) {


            Medicine medicine =
                    medicineRepo.findById(
                            item.getMedicineId()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Medicine not found"
                            )
                    );



            int quantity =
                    item.getQuantity();



            if(medicine.getStock() < quantity){

                throw new RuntimeException(
                        "Insufficient stock for "
                        + medicine.getName()
                );
            }




            // REDUCE STOCK

            medicine.setStock(
                    medicine.getStock() - quantity
            );


            medicineRepo.save(medicine);




            double itemTotal =
                    medicine.getPrice() * quantity;



            total += itemTotal;





            // SAVE ORDER ITEM

            OrderItem orderItem =
                    new OrderItem();


            orderItem.setOrder(
                    savedOrder
            );


            orderItem.setMedicineId(
                    medicine.getId()
            );


            orderItem.setQuantity(
                    quantity
            );


            orderItem.setPrice(
                    medicine.getPrice()
            );



            itemRepo.save(orderItem);

        }





        // UPDATE ORDER TOTAL

        savedOrder.setTotalAmount(
                total
        );


        ordersRepo.save(savedOrder);




        return new OrderResponse(
                savedOrder.getId(),
                total,
                "ORDER PLACED SUCCESSFULLY"
        );

    }





    public List<OrderHistoryResponse> getMyOrders() {


    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();



    String username =
            authentication.getName();



    User user =
            userRepo.findByUsername(username)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found"
                            )
                    );



    return ordersRepo.findByUser(user)
            .stream()
            .map(order -> {


                List<OrderItemResponse> items =
                        itemRepo.findByOrder(order)
                        .stream()
                        .map(item -> {


                            Medicine medicine =
                                    medicineRepo.findById(
                                            item.getMedicineId()
                                    )
                                    .orElseThrow(() ->
                                            new RuntimeException(
                                                    "Medicine not found"
                                            )
                                    );



                            return new OrderItemResponse(
                                    medicine.getId(),
                                    medicine.getName(),
                                    item.getQuantity(),
                                    item.getPrice()
                            );


                        })
                        .toList();




                return new OrderHistoryResponse(
                        order.getId(),
                        order.getTotalAmount(),
                        null,
                        items
                );


            })
            .toList();

}
}
