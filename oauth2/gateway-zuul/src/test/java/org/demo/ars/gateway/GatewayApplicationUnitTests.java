package org.demo.ars.gateway;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * MockUp tests
 *
 * @author arsen.ibragimov
 *
 */
public class GatewayApplicationUnitTests {

    MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup( new GatewayApplication()).build();
    }

    @Test
    public void getDate() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/user").accept( MediaType.APPLICATION_JSON)).andExpect( status().isMethodNotAllowed());
    }

    @Test
    public void postDate() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders.post( "/user").accept( MediaType.APPLICATION_JSON)).andExpect( status().isOk());
    }

}
