package com.example.sweater;

import com.example.sweater.controller.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционный тест логина
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController controller;

    /**
     * Тест на загрузку содержимого страницы приветствия
     *
     * @throws Exception
     */
    @Test
    public void contextLoads() throws Exception{

        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, guest")))
                .andExpect(content().string(containsString("Please log in")));
    }

    /**
     * Тест на отказ в доступе, пока пользователь не залогинится
     *
     * @throws Exception
     */
    @Test
    public void accessDeniedTest() throws Exception{
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    /**
     * Тест на удачный логин
     *
     * @throws Exception
     */
    @Test
    @Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void correctLoginTest() throws Exception{
        this.mockMvc.perform(formLogin().user("admin").password("password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Тест на неудачный логин с несуществующим пользователем
     *
     * @throws Exception
     */
    @Test
    public void badCredentials() throws Exception{
        this.mockMvc.perform(post("/login").param("user", "arkasha"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


}
