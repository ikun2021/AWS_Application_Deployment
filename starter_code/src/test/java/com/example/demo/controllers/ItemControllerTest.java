package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository =mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepository);
    }

    @Test
    public void getItems(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertEquals(200,response.getStatusCodeValue());
    }

    @Test
    public void getItemById(){
        Item item = new Item(1L,"item1", BigDecimal.valueOf(30));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertEquals("item1",response.getBody().getName());
    }

    @Test
    public void getItemsByName(){
        Item item = new Item(1L,"item1", BigDecimal.valueOf(30));
        List<Item> list = new ArrayList<>();
        list.add(item);
        list.add(item);
        when(itemRepository.findByName("item1")).thenReturn(list);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item1");
        Assert.assertEquals(200,response.getStatusCodeValue());
        Assert.assertEquals(2,response.getBody().size());

    }
}
