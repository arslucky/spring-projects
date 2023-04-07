/**
 *
 */
package org.demo.ars.ui.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * MockUp tests
 *
 * @author arsen.ibragimov
 *
 */
public class AppRestControllerUnitTests {

    MockMvc mockMvc;

    MockEnvironment mockEnvironment = new MockEnvironment();

    @Before
    public void setup() {
        mockEnvironment.setProperty( "log.level", "INFO");
        this.mockMvc = MockMvcBuilders.standaloneSetup( new AppRestController( mockEnvironment)).build();
    }

    @Test
    public void getDate() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/getDate").accept( MediaType.APPLICATION_JSON)).andExpect( status().isOk());
    }


    @Test
    public void getLogLevel() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/getLogLevel").accept( MediaType.APPLICATION_JSON)).andExpect( status().isOk());
    }

}
