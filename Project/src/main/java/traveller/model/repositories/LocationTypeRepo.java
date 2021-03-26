package traveller.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traveller.model.POJOs.LocationType;

@Repository
public interface LocationTypeRepo extends JpaRepository<LocationType, Integer> {
}
