package traveller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traveller.model.pojo.LocationType;

import java.util.Optional;

@Repository
public interface LocationTypeRepository extends JpaRepository<LocationType, Long> {

     Optional<LocationType> findByName(String name);
}
