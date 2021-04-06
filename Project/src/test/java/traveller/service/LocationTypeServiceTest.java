package traveller.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import traveller.exception.BadRequestException;
import traveller.model.dto.locationType.LocationTypeDTO;
import traveller.model.pojo.LocationType;
import traveller.model.pojo.Post;
import traveller.repository.LocationTypeRepository;

@ContextConfiguration(classes = {LocationTypeDTO.class, LocationTypeService.class})
@ExtendWith(SpringExtension.class)
public class LocationTypeServiceTest {
    @Autowired
    private LocationTypeDTO locationTypeDTO;

    @MockBean
    private LocationTypeRepository locationTypeRepository;

    @Autowired
    private LocationTypeService locationTypeService;

    @Test
    public void testAddLocationType() {
        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());
        when(this.locationTypeRepository.save((LocationType) any())).thenReturn(locationType);
        assertSame(locationType, this.locationTypeService.addLocationType(this.locationTypeDTO));
        verify(this.locationTypeRepository).save((LocationType) any());
    }

    @Test
    public void testGetAllLocationTypes() {
        ArrayList<LocationType> locationTypeList = new ArrayList<LocationType>();
        when(this.locationTypeRepository.findAll()).thenReturn(locationTypeList);
        List<LocationType> actualAllLocationTypes = this.locationTypeService.getAllLocationTypes();
        assertSame(locationTypeList, actualAllLocationTypes);
        assertTrue(actualAllLocationTypes.isEmpty());
        verify(this.locationTypeRepository).findAll();
    }

    @Test
    public void testGetById() {
        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());
        Optional<LocationType> ofResult = Optional.<LocationType>of(locationType);
        when(this.locationTypeRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(locationType, this.locationTypeService.getById(123L));
        verify(this.locationTypeRepository).findById((Long) any());
    }

    @Test
    public void testGetById2() {
        when(this.locationTypeRepository.findById((Long) any())).thenReturn(Optional.<LocationType>empty());
        assertThrows(BadRequestException.class, () -> this.locationTypeService.getById(123L));
        verify(this.locationTypeRepository).findById((Long) any());
    }

    @Test
    public void testGetByName() {
        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());
        Optional<LocationType> ofResult = Optional.<LocationType>of(locationType);
        when(this.locationTypeRepository.findByName(anyString())).thenReturn(ofResult);
        assertSame(locationType, this.locationTypeService.getByName("Name"));
        verify(this.locationTypeRepository).findByName(anyString());
    }

    @Test
    public void testGetByName2() {
        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());
        when(this.locationTypeRepository.save((LocationType) any())).thenReturn(locationType);
        when(this.locationTypeRepository.findByName(anyString())).thenReturn(Optional.<LocationType>empty());
        assertSame(locationType, this.locationTypeService.getByName("Name"));
        verify(this.locationTypeRepository).save((LocationType) any());
        verify(this.locationTypeRepository).findByName(anyString());
    }
}

