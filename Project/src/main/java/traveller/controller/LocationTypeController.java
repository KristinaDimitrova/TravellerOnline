package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.dto.locationTypeDTO.LocationTypeDTO;
import traveller.model.pojo.LocationType;
import traveller.model.service.LocationTypeService;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class LocationTypeController extends AbstractController{
    @Autowired
    LocationTypeService locationTypeService;
    @Autowired
    SessionManager sessionManager;

    @PostMapping("/locationType")
    public LocationType addLocationType(@RequestBody LocationTypeDTO locationTypeDTO, HttpSession session)  {
        sessionManager.authorizeLogin(session);
        return locationTypeService.addLocationType(locationTypeDTO);
    }

    @GetMapping("/locationType/all")
    public List<LocationType> getAllLocationTypes(HttpSession session) {
        sessionManager.authorizeLogin(session);
        return locationTypeService.getAllLocationTypes();
    }

    @GetMapping("/locationType/{id}")
    public LocationType getById (@PathVariable int id,HttpSession session )  {
        sessionManager.authorizeLogin(session);
        return locationTypeService.getById(id);
    }
}





