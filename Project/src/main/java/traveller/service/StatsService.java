package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exception.AuthorizationException;
import traveller.model.dao.statistics.StatsDBDao;
import traveller.model.dto.statsDTO.StatsProfileDto;
import traveller.model.pojo.StatsProfile;
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

    //get most popular posts for today / the week / todo

    //get new users count for the past one month

    //get most popular people from database by period



    public List<StatsProfileDto> getFavouriteProfilesByAgeGroup(int minRange, int maxRange, long actorId) {
        //service might be available only for ADMINS
        if(userRep.getById(actorId).getRole() != Role.ADMIN){
            throw new AuthorizationException("restricted data");
        }
        List<StatsProfile> listFromDao = statsDao.getFavouriteProfilesByAgeGroup(minRange, maxRange);
        List<StatsProfileDto> popularProfiles = new ArrayList<>();
        for(StatsProfile i : listFromDao){
            popularProfiles.add(new StatsProfileDto(i));
        }
        System.out.println("We are good here in Service 33");
        return popularProfiles;
    }
}
