package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class); //create a mock UserRepository

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        //inject 3 fields into userController to call
        TestUtils.injectObjects(userController,"userRepository",userRepository);
        TestUtils.injectObjects(userController,"cartRepository",cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",bCryptPasswordEncoder);

    }

    @Test
    //primary,sanity,basic test for create user
    public void create_user_happy_path(){
        when(bCryptPasswordEncoder.encode("password")).thenReturn("hashedPassword");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("password");
        r.setConfirmPassword("password");

        ResponseEntity<User> response = userController.createUser(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        User user = response.getBody();
        Assert.assertEquals("hashedPassword",user.getPassword());
    }

    @Test
    public void findById(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1,"username")));
        ResponseEntity<User> response = userController.findById(1L);
        Assert.assertEquals("username",response.getBody().getUsername());
    }

    @Test
    public void findByName(){
        when(userRepository.findByUsername("username")).thenReturn(new User(2L,"username"));
        ResponseEntity<User> response = userController.findByUserName("username");
        Assert.assertEquals(2L,response.getBody().getId());
    }
}
