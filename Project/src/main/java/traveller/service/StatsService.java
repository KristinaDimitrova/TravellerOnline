package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.model.dao.statistics.StatsDBDao;
@Service
public class StatsService {
    @Autowired
    private StatsDBDao statsDao;

    //get most popular posts for today / the week / todo

    //get most popular people from database by period

    //get new users count for the past one month
}
