package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.model.POJOs.LocationType;
import traveller.model.repositories.LocationTypeRepo;

@Service
public class LocationTypeService {

    @Autowired
    LocationTypeRepo locationTypeRepo;

    public LocationType addLocationType(LocationType locationType) {
       return locationTypeRepo.save(locationType);
    }
}
