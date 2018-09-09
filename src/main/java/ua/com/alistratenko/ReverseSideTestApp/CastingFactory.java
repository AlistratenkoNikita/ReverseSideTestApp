package ua.com.alistratenko.ReverseSideTestApp;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import org.springframework.stereotype.Component;
import ua.com.alistratenko.ReverseSideTestApp.entity.VkCustomPost;
import ua.com.alistratenko.ReverseSideTestApp.entity.VkCustomUser;

/**
 * Provides methods to cast vk Java sdk objects to custom objects
 *
 * @author Nikita
 * @since 09.09.2018
 */
@Component
public class CastingFactory {

    public static VkCustomUser UserXtrCountersToVkCustomUser(UserXtrCounters user, int groupId){
        return new VkCustomUser(user.getId(), user.getFirstName(), user.getLastName(), user.getSex()).addSubscription(groupId);
    }

    public static VkCustomUser UserXtrCountersToVkCustomUser(UserXtrCounters user){
        return new VkCustomUser(user.getId(), user.getFirstName(), user.getLastName(), user.getSex());
    }

    public static VkCustomPost WallPostFullToVkCustomPost(WallPostFull post, int groupId){
        return new VkCustomPost(post.getId(), groupId, post.getLikes().getCount());
    }
}
