package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import traveller.exception.BadRequestException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.statsDTO.StatsProfileDto;
import traveller.service.StatsService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class StatsController extends AbstractController{

    @Autowired
    StatsService statsService;
    @Autowired
    SessionManager sessManager;

//here we have endpoints that can be used to get data
//from top posts, most influential users, ...
    @GetMapping(value = "/influencers/top/{periodMonths}")
    public void whichAreTheMostInfluentialUsers(){
        //todo
        //return a dto with an array list of the users
    }

    @GetMapping(value = "/influencers/{minAge}/{maxAge}")
    public List<StatsProfileDto> getMostFollowedProfilesByAgeGroup(@PathVariable int minAge, @PathVariable int maxAge, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        if(minAge > maxAge){
            throw new BadRequestException("min age can't be larger than max age.");
        }
        return statsService.getFavouriteProfilesByAgeGroup(minAge, maxAge, actorId);
    }
}
