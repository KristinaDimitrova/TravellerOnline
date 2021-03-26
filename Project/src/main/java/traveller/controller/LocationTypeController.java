package traveller.controller;


import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import traveller.model.POJOs.LocationType;
import traveller.model.services.LocationTypeService;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
public class LocationTypeController extends AbstractController{
    @Autowired
    LocationTypeService locationTypeService;

    @PostMapping("/locationType")
    public LocationType addLocationType(@RequestBody LocationType locationType, HttpSession session)  {
        if(SessionManager.isUserLoggedIn(session)){
            return locationTypeService.addLocationType(locationType);
        }
        else{
            throw new traveller.exceptions.AuthenticationException("You need to be logged in!");
        }
    }

    @GetMapping("/locationType/all")
    public List<LocationType> getAllLocationTypes(HttpSession session) {
        if(SessionManager.isUserLoggedIn(session)){
            return locationTypeService.getAllLocationTypes();
        }
        else{
            throw new traveller.exceptions.AuthenticationException("You need to be logged in!");
        }
    }

    @GetMapping("/locationType/{id}")
    public LocationType getById (@PathVariable int id,HttpSession session )  {
        if(SessionManager.isUserLoggedIn(session)){
            return locationTypeService.getById(id);
        }
        else{
            throw new traveller.exceptions.AuthenticationException("You need to be logged in!");
        }
    }
}





