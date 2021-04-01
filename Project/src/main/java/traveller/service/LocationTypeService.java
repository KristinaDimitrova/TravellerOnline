package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exception.BadRequestException;
import traveller.model.dto.locationTypeDTO.LocationTypeDTO;
import traveller.model.pojo.LocationType;
import traveller.repository.LocationTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LocationTypeService {

    @Autowired
    LocationTypeRepository locationTypeRepo;

    public LocationType addLocationType(LocationTypeDTO locationTypeDTO) {
        LocationType locationType = new LocationType(locationTypeDTO);
       return locationTypeRepo.save(locationType);
    }

    public List<LocationType> getAllLocationTypes(){
        return locationTypeRepo.findAll();
    }

    public LocationType getById(long id){
        Optional<LocationType> optionalLocationType = locationTypeRepo.findById(id);
        if(optionalLocationType.isPresent()){
            return optionalLocationType.get();
        }
        else {
            throw new BadRequestException("There is no location type with this id!");
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
