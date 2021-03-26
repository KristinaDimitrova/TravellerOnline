package traveller.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.exceptions.UnauthorizedException;
import traveller.model.POJOs.LocationType;
import traveller.model.services.LocationTypeService;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
public class LocationTypeController extends MotherController{
    @Autowired
    LocationTypeService locationTypeService;

    @PostMapping("/locationType")
    public LocationType addLocationType(@RequestBody LocationType locationType, HttpSession session) throws UnauthorizedException {
        if(SessionManager.isUserLoggedIn(session)){
            return locationTypeService.addLocationType(locationType);
        }
        else{
            throw new UnauthorizedException("You need to be logged in!");
        }
    }

    @GetMapping("/locationType/all")
    public List<LocationType> getAllLocationTypes(HttpSession session) throws UnauthorizedException {
        if(SessionManager.isUserLoggedIn(session)){
            return locationTypeService.getAllLocationTypes();
        }
        else{
            throw new UnauthorizedException("You need to be logged in!");
        }
    }

    @GetMapping("/locationType/{id}")
    public LocationType getById (@PathVariable int id,HttpSession session ) throws UnauthorizedException {
        if(SessionManager.isUserLoggedIn(session)){
            return locationTypeService.getById(id);
        }
        else{
            throw new UnauthorizedException("You need to be logged in!");
        }
    }
}





