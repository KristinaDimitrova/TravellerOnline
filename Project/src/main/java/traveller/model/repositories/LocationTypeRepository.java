package traveller.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traveller.model.POJOs.LocationType;

import java.util.Optional;

@Repository
public interface LocationTypeRepository extends JpaRepository<LocationType, Integer> {

     Optional<LocationType> findByName(String name);
}
