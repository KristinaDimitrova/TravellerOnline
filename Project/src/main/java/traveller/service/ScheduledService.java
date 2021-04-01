package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import traveller.model.dao.statistics.StatsDBDao;
import traveller.model.dao.statistics.StatsDao;

import java.time.LocalDateTime;

@Service
public class ScheduledService {
    @Autowired
    StatsDBDao dao;

    //запази данни за най-популярни постове -> на всеки 3 минути, след 1 мин първоначално
    @Scheduled(fixedDelay = 1000 * 60 * 3, initialDelay = 1000 * 60 * 1) //every minute
    public void ScheduledAnalysisTopInfluencers(){
        System.out.println("Heey" + LocalDateTime.now());
        //todo must perform the stats
    }
    //every day at 1:00 АМ
    //Cron : second, minute, hour, day of month, month, day(s) of week
    //запази данни за най-популярни профили
    @Scheduled(cron = "0 0 1 * * ?", zone = "Europe/Sofia") //every minute
    public void ScheduledAnalysisTopPosts(){
        System.out.println("Heey" + LocalDateTime.now());
        //todo must perform the stats
        //calls methods in dto
        dao.saveRecordMostPopularUsers();
    }

    //запази данни за нови потребители за седмицата по възрастова група -> всеки ден, всеки час, всяка минута, на 10-тата секунда
    @Scheduled(cron = "10 * * * * ?", zone = "Europe/Sofia") //every minute
    public void whatIsWrong(){
        System.out.println("Things seem to run smoothly  " + LocalDateTime.now());
        //todo must perform the stats
        dao.saveRecordOfNewUsersByAgeGroup();
    }
}
