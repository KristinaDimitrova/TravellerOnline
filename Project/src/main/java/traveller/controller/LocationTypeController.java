package traveller.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import traveller.model.POJOs.LocationType;
import traveller.model.services.LocationTypeService;


@RestController
public class LocationTypeController extends MotherController{
    @Autowired
    LocationTypeService locationTypeService;

    @PostMapping("/locationType")
    public LocationType addLocationType(@RequestBody LocationType locationType){
        return locationTypeService.addLocationType(locationType);
    }

}
