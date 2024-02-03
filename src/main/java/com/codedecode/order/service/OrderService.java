package com.codedecode.order.service;

import com.codedecode.order.dto.OrderDTO;
import com.codedecode.order.dto.OrderDTOFromFE;
import com.codedecode.order.dto.UserDTO;
import com.codedecode.order.entity.Order;
import com.codedecode.order.mapper.OrderMapper;
import com.codedecode.order.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

 @Autowired
 OrderRepo orderRepo;

 @Autowired
 SequenceGenerator sequenceGenerator;

 @Autowired
 RestTemplate restTemplate;

 public List<OrderDTO> getAllOrders() {
  List<Order> orders = orderRepo.findAll();
  return OrderMapper.INSTANCE.mapOrdersToOrderDTOs(orders);
 }

 public OrderDTO saveOrderInDb(OrderDTOFromFE orderDetails) {
  Integer newOrderID = sequenceGenerator.generateNextOrderId();
  UserDTO userDTO = fetchUserDetailsFromUserId(orderDetails.getUserId());
  Order orderToBeSaved = new Order(newOrderID, orderDetails.getFoodItemsList(),
          orderDetails.getRestaurant(), userDTO);
  orderRepo.save(orderToBeSaved);
  return OrderMapper.INSTANCE.mapOrderToOrderDTO(orderToBeSaved);
 }

 private UserDTO fetchUserDetailsFromUserId(Integer userId) {
  return restTemplate.getForObject("http://USER-SERVICE/user/fetchUserById/" + userId,
          UserDTO.class);
 }

 public void deleteOrder(int orderId) {
  try {
   orderRepo.deleteById(orderId);
   System.out.println(orderId);
  } catch (NumberFormatException e) {
   // Handle the case where orderId is not a valid integer
   throw new IllegalArgumentException("Invalid orderId format: " + orderId);
  } catch (Exception e) {
   // Handle other exceptions here (e.g., if the order doesn't exist)
   throw new RuntimeException("Error deleting order: " + e.getMessage()+ orderId);
  }
 }



}
