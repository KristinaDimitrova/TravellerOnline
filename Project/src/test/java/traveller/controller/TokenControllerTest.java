package traveller.controller;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import traveller.model.dto.MessageDTO;
import traveller.service.TokenService;
import traveller.service.UserService;

@ContextConfiguration(classes = {TokenController.class})
@ExtendWith(SpringExtension.class)
public class TokenControllerTest {
    @Autowired
    private TokenController tokenController;

    @MockBean
    private TokenService tokenService;

    @Test
    public void testConfirm() throws Exception {
        when(this.tokenService.confrimToken(anyString())).thenReturn(new MessageDTO("Email verified. You're good to go!"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tokens/{token}", "value");
        MockMvcBuilders.standaloneSetup(this.tokenController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"Email verified. You're good to go!\"}")));
    }

}

