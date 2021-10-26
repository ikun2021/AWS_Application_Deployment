package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp () {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addAndRemove(){
        User user = new User(1L,"username");
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setId(1L);
        user.setCart(cart);
        Item item = new Item(1L,"item1", BigDecimal.valueOf(30));
        ModifyCartRequest modifyCartRequest1 = new ModifyCartRequest("username",1L,3);
        ModifyCartRequest modifyCartRequest2 = new ModifyCartRequest("username",1L,2);
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response1 = cartController.addTocart(modifyCartRequest1);
        Assert.assertEquals(3,response1.getBody().getItems().size());

        ResponseEntity<Cart> response2 = cartController.removeFromcart(modifyCartRequest2);
        Assert.assertEquals(1,response2.getBody().getItems().size());
        Assert.assertEquals(BigDecimal.valueOf(30),response2.getBody().getTotal());
    }
}
