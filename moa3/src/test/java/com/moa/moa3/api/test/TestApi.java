package com.moa.moa3.api.test;

import com.moa.moa3.api.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestApi extends BaseControllerTest {

    @Test
    public void testApi() throws Exception {
        ResultActions result = mockMvc.perform(get("/test"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("randomNum").exists());
    }
}
