package com.codedecode.order.controller;


import com.codedecode.order.dto.OrderDTO;
import com.codedecode.order.dto.OrderDTOFromFE;
import com.codedecode.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
 private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

 @Autowired
 OrderService orderService;

 @GetMapping("/getAllOrders")
 public ResponseEntity<List<OrderDTO>> getAllOrders() {
  List<OrderDTO> orderDTOs = orderService.getAllOrders();
  return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
 }

 @PostMapping("/saveOrder")
 public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTOFromFE orderDetails) {
  OrderDTO orderSavedInDB = orderService.saveOrderInDb(orderDetails);
  return new ResponseEntity<>(orderSavedInDB, HttpStatus.CREATED);
 }

 @DeleteMapping("/deleteOrder/{orderId}")
 public ResponseEntity<String> deleteOrder(@PathVariable Integer orderId) {
  try {
   logger.info("Attempting to delete order with ID: {}", orderId);
   orderService.deleteOrder(orderId);
   logger.info("Successfully deleted order with ID: {}", orderId);
   return new ResponseEntity<>("Order deleted successfully.", HttpStatus.OK);
  } catch (Exception e) {
   e.printStackTrace();
   return new ResponseEntity<>("Error deleting order " + orderId, HttpStatus.INTERNAL_SERVER_ERROR);
  }
 }

}
