package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import traveller.model.dao.location.LocationDBDao;

@RestController
public class LocationController extends MotherController{
    @Autowired
    private LocationDBDao locationDBDao ;
}
