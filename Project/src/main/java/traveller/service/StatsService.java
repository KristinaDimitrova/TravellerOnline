package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exception.AuthorizationException;
import traveller.model.dao.statistics.StatsDBDao;
import traveller.model.dto.statsDTO.StatsProfileDTO;
import traveller.model.pojo.stats.StatsSignups;
import traveller.model.pojo.stats.StatsProfile;
import traveller.registration.Role;
import traveller.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatsService {
    @Autowired
    private StatsDBDao statsDao;
    @Autowired
    private UserRepository userRep;

    public StatsSignups getSignupsCountByAgeRange(int minAge, int maxAge, int periodDays, long actorId) {
        if(userRep.getById(actorId).getRole() != Role.ADMIN){
            throw new AuthorizationException("sensitive data");
        }
        return statsDao.getSignupsCountByAgeRange(minAge, maxAge, periodDays);
    }

    public List<StatsProfileDTO> getFavouriteProfilesByAgeGroup(int minRange, int maxRange, long actorId) {
        //service is available only for ADMINS
        if(userRep.getById(actorId).getRole() != Role.ADMIN){
            throw new AuthorizationException("sensitive data");
        }
        List<StatsProfile> listFromDao = statsDao.getFavouriteProfilesByAgeGroup(minRange, maxRange);
        List<StatsProfileDTO> popularProfiles = new ArrayList<>();
        for(StatsProfile i : listFromDao){
            popularProfiles.add(new StatsProfileDTO(i));
        }
        return popularProfiles;
    }
}
