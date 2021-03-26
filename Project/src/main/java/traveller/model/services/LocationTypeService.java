package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exceptions.BadRequestException;
import traveller.model.POJOs.LocationType;
import traveller.model.repositories.LocationTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LocationTypeService {

    @Autowired
    LocationTypeRepository locationTypeRepo;

    public LocationType addLocationType(LocationType locationType) {
       return locationTypeRepo.save(locationType);
    }

    public List<LocationType> getAllLocationTypes(){
        return locationTypeRepo.findAll();
    }

    public LocationType getById(long id){
        Optional<LocationType> optionalLocationType = locationTypeRepo.findById((int) id);
        if(optionalLocationType.isPresent()){
            return optionalLocationType.get();
        }
        else {
            throw new BadRequestException("There is not location type with this id!");
        }
    }

    public LocationType getByName(String name){
        Optional<LocationType> optionalLocationType = locationTypeRepo.findByName(name);
        if(optionalLocationType.isPresent()){
            return optionalLocationType.get();
        }
        else {
            LocationType locationType = new LocationType(name);
            return locationTypeRepo.save(locationType);
        }
    }
}
