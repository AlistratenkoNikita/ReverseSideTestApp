package ua.com.alistratenko.ReverseSideTestApp.service;

import com.vk.api.sdk.objects.base.Sex;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.com.alistratenko.ReverseSideTestApp.dal.GeneralDAL;
import ua.com.alistratenko.ReverseSideTestApp.entity.LikesCountHolder;
import ua.com.alistratenko.ReverseSideTestApp.entity.VkCustomUser;
import ua.com.alistratenko.ReverseSideTestApp.requestSender.RequestSender;
import ua.com.alistratenko.ReverseSideTestApp.service.dataLoading.DataLoadingService;

import java.util.List;

/**
 * Provides methods to perform set action for a test project
 *
 * @author Nikita
 * @since 09.09.2018
 */
@Service
public class ActionService {

    @Value("${application.vk.groupid}")
    protected int GROUP_ID;

    private DataLoadingService likesLoadingService;
    private DataLoadingService userLoadingService;
    private RequestSender requestSender;
    private GeneralDAL dal;
    private Logger logger;

    @Autowired
    public ActionService(@Qualifier("likesDataLoadingService") DataLoadingService likesLoadingService,
                         @Qualifier("userDataLoadingService") DataLoadingService userLoadingService,
                         GeneralDAL dal, RequestSender requestSender, Logger logger) {
        this.likesLoadingService = likesLoadingService;
        this.userLoadingService = userLoadingService;
        this.requestSender = requestSender;
        this.dal = dal;
        this.logger = logger;
    }

    /**
     * Prints out to console top 5 people who liked the greatest amount of post in group
     */
    public void showTopFiveLikersOfSpecificGroup(){
        likesLoadingService.loadData();

        List<LikesCountHolder> likes = dal.getTopFiveUsersByLikes(GROUP_ID);

        logger.info("");
        logger.info("------------------ LIKES COUNT ------------------");
        for (int i = 0; i < likes.size(); i++) {
            LikesCountHolder like = likes.get(i);
            VkCustomUser user = requestSender.getUserById(like.getUserId());

            logger.info("--- Top " + (i+1) + ". First name = " + user.getFirsName() + ", last name = " + user.getLastName() + ", likes amount = " + like.getLikesCount() + " ---");
        }
        logger.info("-------------------------------------------------");
        logger.info("");
    }

    /**
     * Prints out to console amount of MALE and FEMALE persons in group
     */
    public void showSexCountOfGroup(){
        userLoadingService.loadData();

        logger.info("");
        logger.info("------------------ SEX COUNT ------------------");
        logger.info("--- Number of men in group = " + dal.getSpecificSexCountInGroup(GROUP_ID, Sex.MALE) + "\t\t ------");
        logger.info("--- Number of women in group = " + dal.getSpecificSexCountInGroup(GROUP_ID, Sex.FEMALE) + "\t\t ------");
        logger.info("-----------------------------------------------");
        logger.info("");
    }
}
