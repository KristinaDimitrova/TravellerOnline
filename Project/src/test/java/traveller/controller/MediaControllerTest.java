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
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.mediaService.getVideoById(anyLong())).thenReturn("AAAAAAAA".getBytes());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/video/{id}", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.mediaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("video/mp4"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("AAAAAAAA")));
    }

    @Test
    public void testGetImageById() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.mediaService.getImageById(anyLong())).thenReturn("mouse".getBytes());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/image/{id}", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.mediaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType("image/jpg"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("mouse")));
    }
}

