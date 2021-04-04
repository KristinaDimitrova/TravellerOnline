package traveller.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

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
import traveller.service.MediaService;

@ContextConfiguration(classes = {MediaController.class})
@ExtendWith(SpringExtension.class)
public class MediaControllerTest {
    @Autowired
    private MediaController mediaController;

    @MockBean
    private MediaService mediaService;

    @MockBean
    private SessionManager sessionManager;

    @Test
    public void testGetVideoById() throws Exception {
        when(this.sessionManager.authorizeLogin(any())).thenReturn(1L);
        when(this.mediaService.getVideoById(anyLong())).thenReturn("FunnyCats.mp4".getBytes());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/video/{id}", 12345678987654321L);
        initResultAction(requestBuilder);
    }

    @Test
    public void testGetImageById() throws Exception {   // FIXME: 4/3/2021
        when(this.sessionManager.authorizeLogin(any())).thenReturn(1L);
        when(this.mediaService.getImageById(anyLong())).thenReturn("FunnyCats.jpg".getBytes());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/image/{id}", 12345678987654321L);
        initResultAction(requestBuilder);
    }

    private void initResultAction(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.mediaController)
                .build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

