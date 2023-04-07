package org.demo.ars.resource;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
public class ResourceApplicationUnitTests {

    MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup( new ResourceController()).build();
    }

    @Test
    public void getStatistic() throws Exception {
        //@formatter:off
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/hi").accept( MediaType.APPLICATION_JSON))
                .andExpect( status().isOk())
                .andExpect( content().string( equalTo( "Hello!")));
        //@formatter:on
    }

}
