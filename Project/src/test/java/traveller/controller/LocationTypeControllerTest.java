package traveller.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import traveller.model.dto.locationType.LocationTypeDTO;
import traveller.model.pojo.LocationType;
import traveller.model.pojo.Post;
import traveller.service.LocationTypeService;

@ContextConfiguration(classes = {LocationTypeController.class})
@ExtendWith(SpringExtension.class)
public class LocationTypeControllerTest {
    @Autowired
    private LocationTypeController locationTypeController;

    @MockBean
    private LocationTypeService locationTypeService;

    @MockBean
    private SessionManager sessionManager;

    @Test
    public void testAddLocationType() throws Exception {
        when(this.sessionManager.authorizeLogin(any())).thenReturn(1L);

        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Mountain");
        locationType.setPosts(new ArrayList<>());

        when(this.locationTypeService.addLocationType(any())).thenReturn(locationType);

        LocationTypeDTO locationTypeDTO = new LocationTypeDTO();
        locationTypeDTO.setName("Mountain");
        String content = (new ObjectMapper()).writeValueAsString(locationTypeDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/locationTypes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.locationTypeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"id\":123,\"name\":\"Mountain\"}")));
    }

    @Test
    public void testGetAllLocationTypes() throws Exception {
        when(this.sessionManager.authorizeLogin(any())).thenReturn(1L);

        LocationType locationType = new LocationType();
        locationType.setName("Mountain");
        locationType.setId(123L);
        ArrayList<LocationType> list = new ArrayList<>();
        list.add(locationType);

        when(this.locationTypeService.getAllLocationTypes()).thenReturn(list);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/locationTypes/all");
        MockMvcBuilders.standaloneSetup(this.locationTypeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"id\":123,\"name\":\"Mountain\"}")));
    }

    @Test
    public void testGetById() throws Exception {
        when(this.sessionManager.authorizeLogin(any())).thenReturn(1L);

        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());
        when(this.locationTypeService.getById(anyLong())).thenReturn(locationType);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/locationTypes/{id}", 123456789);
        MockMvcBuilders.standaloneSetup(this.locationTypeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"id\":123,\"name\":\"Name\"}")));
    }

}

