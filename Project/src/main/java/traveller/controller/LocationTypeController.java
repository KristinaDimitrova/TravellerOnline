package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.exceptions.AuthenticationException;
import traveller.model.DTO.locationTypeDTO.LocationTypeDTO;
import traveller.model.POJOs.LocationType;
import traveller.model.services.LocationTypeService;
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
        if(sessionManager.isUserLoggedIn(session)){
            return locationTypeService.addLocationType(locationTypeDTO);
        }
        else{
            throw new AuthenticationException("You need to be logged in!");
        }
    }

    @GetMapping("/locationType/all")
    public List<LocationType> getAllLocationTypes(HttpSession session) {
        if(sessionManager.isUserLoggedIn(session)){
            return locationTypeService.getAllLocationTypes();
        }
        else{
            throw new AuthenticationException("You need to be logged in!");
        }
    }

    @GetMapping("/locationType/{id}")
    public LocationType getById (@PathVariable int id,HttpSession session )  {
        if(sessionManager.isUserLoggedIn(session)){
            return locationTypeService.getById(id);
        }
        else{
            throw new AuthenticationException("You need to be logged in!");
        }
    }
}





