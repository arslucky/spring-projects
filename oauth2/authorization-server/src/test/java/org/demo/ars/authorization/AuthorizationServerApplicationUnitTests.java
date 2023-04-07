package org.demo.ars.authorization;

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
public class AuthorizationServerApplicationUnitTests {

    MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup( new AuthorizationServerApplication()).build();
    }

    @Test
    public void getUser() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/user").accept( MediaType.APPLICATION_JSON)).andExpect( status().isOk());
    }

}
