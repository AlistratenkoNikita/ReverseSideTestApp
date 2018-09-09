package ua.com.alistratenko.ReverseSideTestApp.service.dataLoading;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class that provides loading service for users info from vk
 *
 * @author Nikita
 * @since 08.09.2018
 */
@Service
public class UserDataLoadingService extends DataLoadingService{

    @Value("${max-users-per-request}")
    private int MAX_USERS_COUNT_PER_REQUEST;

    /**
     * Loads all necessary user data from vk (users that are subscribed to specific group)
     */
    public void loadData(){

        int numberOfUsers = requestSender.getGroupMembersCount(GROUP_ID);

        for (int i = 0; i <= numberOfUsers / MAX_USERS_COUNT_PER_REQUEST; i++) {
            List<UserXtrCounters> members = requestSender.getGroupMembers(GROUP_ID, MAX_USERS_COUNT_PER_REQUEST, i * MAX_USERS_COUNT_PER_REQUEST);
            generalDAL.addUsers(members, GROUP_ID);

            try {
                Thread.sleep(PAUSE_FOR_EVERY_REQUEST);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("-- Users loaded. Amount: " + members.size() + ", with offset = " + (i * MAX_USERS_COUNT_PER_REQUEST));
        }

        logger.info("------ LOADING OF USERS IS DONE ------");
    }
}
