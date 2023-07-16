package com.velikanovdev.recipes.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velikanovdev.recipes.entity.User;
import com.velikanovdev.recipes.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testRegisterUser_Success() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userService.registerUser(user)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk());

        verify(userService, times(1)).registerUser(user);
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        ResponseEntity<?> responseEntity = ResponseEntity.badRequest().body("Please check your credentials");
        doReturn(responseEntity).when(userService).registerUser(user);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).registerUser(user);
    }

    @Test
    public void testRegisterUser_InvalidPassword() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("short");

        ResponseEntity<?> responseEntity = ResponseEntity.badRequest().body("Password must contain at least 8 characters");
        doReturn(responseEntity).when(userService).registerUser(user);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).registerUser(user);
    }

    private static String asJsonString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
