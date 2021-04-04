package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import traveller.exception.BadRequestException;
import traveller.model.dto.stats.StatsProfileDTO;
import traveller.model.pojo.stats.StatsSignups;
import traveller.service.StatsService;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class StatsController extends AbstractController{

    @Autowired
    StatsService statsService;
    @Autowired
    SessionManager sessManager;

    @GetMapping(value = "/signups")
    public StatsSignups getSignupsByAgeRangeAndInterval(@RequestParam(value = "min") int minAge,
      @RequestParam(value = "max") int maxAge, @RequestParam(value = "interval") int intervalDays,
        HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        if(minAge > maxAge){
            throw new BadRequestException("bottom of a given range must be smaller than the top");
        }
        if(intervalDays < 1){
            throw new BadRequestException("interval cannot be smaller than 1");
        }
        return statsService.getSignupsCountByAgeRange(minAge,maxAge,intervalDays, actorId);
    }

    @GetMapping(value = "/influencers")
    public List<StatsProfileDTO> getMostFollowedProfilesByAgeGroup(@RequestParam(defaultValue = "20", value = "min") int minAge,
         @RequestParam(defaultValue = "25", value = "max") int maxAge, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        if(minAge > maxAge){
            throw new BadRequestException("min age can't be larger than max age.");
        }
        if(minAge < 5 || maxAge > 99){
            throw new BadRequestException("invalid input");
        }
        return statsService.getFavouriteProfilesByAgeGroup(minAge, maxAge, actorId);
    }
}
