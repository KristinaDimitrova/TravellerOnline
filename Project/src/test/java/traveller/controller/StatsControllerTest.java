package traveller.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import traveller.model.dto.statsDTO.StatsProfileDTO;
import traveller.service.StatsService;

@ContextConfiguration(classes = {StatsController.class})
@ExtendWith(SpringExtension.class)
public class StatsControllerTest {
    @MockBean
    private SessionManager sessionManager;

    @Autowired
    private StatsController statsController;

    @MockBean
    private StatsService statsService;

    @Test
    public void testGetMostFollowedProfilesByAgeGroup() throws Exception {
        when(this.statsService.getFavouriteProfilesByAgeGroup(anyInt(), anyInt(), anyLong()))
                .thenReturn(new ArrayList<>());
        when(this.sessionManager.authorizeLogin(any())).thenReturn(1L);

        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/influencers");
        MockHttpServletRequestBuilder paramResult = getResult.param("maxAge", String.valueOf(20));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("minAge", String.valueOf(60));
        /*
         * alternative :
         *
         * initResultAction(requestBuilder)
         *
         * */
        MockMvcBuilders.standaloneSetup(this.statsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("[]")));
    }

    @Test
    public void testGetMostFollowedProfilesByAgeGroup2() throws Exception {
        when(this.statsService.getFavouriteProfilesByAgeGroup(anyInt(), anyInt(), anyLong()))
                .thenReturn(new ArrayList<StatsProfileDTO>());
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/influencers");
        MockHttpServletRequestBuilder paramResult = getResult.param("maxAge", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("minAge", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(this.statsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("[]")));
    }

    @Test
    public void testGetSignupsByAgeRangeAndInterval() throws Exception { //FIXME
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/signups");
        MockHttpServletRequestBuilder paramResult = getResult.param("intervalDays", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("maxAge", String.valueOf(10));
        MockHttpServletRequestBuilder requestBuilder = paramResult1.param("minAge", String.valueOf(30));
        initResultAction(requestBuilder);
    }

    private void initResultAction(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.statsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}

