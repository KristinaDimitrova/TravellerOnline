package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import traveller.model.dao.statistics.StatsDBDao;

import java.time.LocalDateTime;

@Service
public class ScheduledService {
    @Autowired
    StatsDBDao dao;

    //@Scheduled(fixedDelay = 1000 * 60 * 3, initialDelay = 1000 * 60 * 1)
    //@Scheduled(cron = "0 0 1 1 * ?", zone = "Europe/Sofia") //on every first date at 1 am
    @Scheduled(cron = "10 * * * * ?", zone = "Europe/Sofia")
    public void ScheduledAnalysisTopInfluencers(){
        System.out.println("Heey  " + LocalDateTime.now());
        dao.saveRecordOfMostPopularUsers();
    }

    //Cron : second, minute, hour, day of month, month, day(s) of week
    //@Scheduled(cron = "0 0 1 * * ?", zone = "Europe/Sofia") //every day at 1:00 AM
    @Scheduled(cron = "10 * * * * ?", zone = "Europe/Sofia")
    public void ScheduledAnalysisTopPosts(){
        System.out.println("Heey  " + LocalDateTime.now());
        dao.saveRecordOfMostPopularPosts();
    }

    //запази данни за нови потребители за седмицата по възрастова група ->
    //@Scheduled(cron = "0 0 1 * * MON", zone = "Europe/Sofia") every Monday
    @Scheduled(cron = "10 * * * * ?", zone = "Europe/Sofia") //every minute
    public void whatIsWrong(){
        System.out.println("Things seem to be running smoothly  " + LocalDateTime.now());
        //todo must perform the stats
        dao.saveRecordSignups();
    }
}
