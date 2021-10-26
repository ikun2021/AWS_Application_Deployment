package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository =mock(UserRepository.class);
    private OrderRepository orderRepository =mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        //inject 3 fields into userController to call
        TestUtils.injectObjects(orderController,"userRepository",userRepository);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepository);

    }

    @Test
    public void submit(){
        User user = new User(1L,"username");
        Cart cart = new Cart();
        cart.addItem(new Item(1L,"name1", BigDecimal.valueOf(10.5)));
        cart.addItem(new Item(2L,"name2", BigDecimal.valueOf(20.5)));
        cart.setId(1L);
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername("username")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("username");
        UserOrder userOrder =response.getBody();
        Assert.assertEquals(BigDecimal.valueOf(31.0),userOrder.getTotal());
    }

    @Test
    public void getOrdersForUser(){
        User user = new User(1L,"username");
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setId(5L);
        userOrder.setTotal(BigDecimal.valueOf(30.5));
        List<UserOrder> list = new ArrayList<>();
        list.add(userOrder);
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(list);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("username");
        Assert.assertEquals(200,response.getStatusCodeValue());
        Assert.assertEquals(1,response.getBody().size());
    }
}
