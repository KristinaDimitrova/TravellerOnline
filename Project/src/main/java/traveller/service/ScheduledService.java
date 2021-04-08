package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import traveller.model.dao.statistics.StatsDBDao;
import traveller.model.pojo.Image;
import traveller.model.pojo.Video;
import traveller.repository.ImageRepository;
import traveller.repository.VideoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduledService {
    @Autowired
    private StatsDBDao dao;
    @Autowired
    private VideoRepository videoRepo;
    @Autowired
    private ImageRepository imageRepo;
    @Autowired
    private MediaService mediaService;

    @Scheduled(cron = "0 1 1 * * ?", zone = "Europe/Sofia") //daily at 1:01 am
    public void ScheduledAnalysisTopInfluencers(){
        dao.saveRecordOfMostPopularUsers();
    }

    //Cron : second, minute, hour, day of month, month, day(s) of week
    @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Sofia")
    public void ScheduledAnalysisTopPosts(){
        dao.saveRecordOfMostPopularPosts();
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Sofia") //every day at 1 am
    public void ScheduledAnalysisNewSignups(){
        dao.saveRecordSignups();
    }

    //@Scheduled(fixedDelay = 1000 * 60 * 999999999, initialDelay = 1000 * 60 * 999999999)
    public void deleteDemonFilesFromDbAndCloud(){
        List<Image> imagesToDelete = imageRepo.getAllByPostIdIsNull();
        for(Image image : imagesToDelete ){
            mediaService.deleteFileFromAmazonS3(image.getFileName());
            imageRepo.delete(image);
        }

        List<Video> videosToDelete = videoRepo.getAllByPostIdIsNull();
        for(Video video : videosToDelete){
            mediaService.deleteFileFromAmazonS3(video.getFileName());
            videoRepo.delete(video);
        }
    }
}
