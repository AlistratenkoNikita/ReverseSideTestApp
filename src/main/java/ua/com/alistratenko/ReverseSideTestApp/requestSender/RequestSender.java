package ua.com.alistratenko.ReverseSideTestApp.requestSender;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.queries.groups.GroupsGetMembersQuery;
import com.vk.api.sdk.queries.likes.LikesGetListFilter;
import com.vk.api.sdk.queries.likes.LikesGetListQuery;
import com.vk.api.sdk.queries.likes.LikesType;
import com.vk.api.sdk.queries.users.UserField;
import com.vk.api.sdk.queries.users.UsersGetQuery;
import com.vk.api.sdk.queries.wall.WallGetFilter;
import com.vk.api.sdk.queries.wall.WallGetQuery;
import org.springframework.stereotype.Component;
import ua.com.alistratenko.ReverseSideTestApp.CastingFactory;
import ua.com.alistratenko.ReverseSideTestApp.entity.VkCustomUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides methods to work with vk api using its Java SDK
 *
 * @author Nikita
 * @since 08.09.2018
 */
@Component
public class RequestSender {

    private static final String TOKEN = "8409b02a4b645adce9824519c3fc6bd7d7428ab5d23a551f9b46b1d4c7407ed5c46530dc4c1544a24de62";
    private static final int CLIENT_ID = 6681472;

    private static final VkApiClient VK;

    private static final ServiceActor SERVICE_ACTOR;
    private static final GroupActor GROUP_ACTOR;
    private static final UserActor USER_ACTOR;

    static {
        VK = new VkApiClient(HttpTransportClient.getInstance());
        SERVICE_ACTOR = new ServiceActor(CLIENT_ID, TOKEN);
        GROUP_ACTOR = new GroupActor(CLIENT_ID, TOKEN);
        USER_ACTOR = new UserActor(CLIENT_ID, TOKEN);
    }

    /**
     * Generates a query for retrieving users ids of specific group
     * @param groupId group id to get members of
     * @param count number of ids to retrieve (max 1_000)
     * @param offset offset of ids
     * @return query to execute
     */
    private GroupsGetMembersQuery getQueryForGroupMembersId(String groupId, int count, int offset){
        return VK.groups().getMembers(GROUP_ACTOR).count(count).offset(offset).groupId(groupId);
    }

    /**
     * Generates query for retrieving users profile by their ids
     * @param usersIds list of users ids
     * @return query to execute
     */
    private UsersGetQuery getQueryForGroupMembersProfile(List<String> usersIds){
        return VK.users().get(USER_ACTOR).userIds(usersIds).fields(UserField.SEX);
    }

    /**
     * Generates query for retrieving likes from specific post
     * @param groupId post owner id
     * @param wallPostId post id
     * @param count number of likes to get
     * @param offset offset of likes
     * @return query to execute
     */
    private LikesGetListQuery getQueryForLikes(int groupId, int wallPostId, int count, int offset){
        // Minus is placed in front of groupId due to vk specification of likes.getList method
        return VK.likes().getList(SERVICE_ACTOR, LikesType.POST).ownerId(-groupId)
                .itemId(wallPostId).friendsOnly(false)
                .filter(LikesGetListFilter.LIKES).skipOwn(true)
                .count(count).offset(offset);
    }

    /**
     * Generates query for retrieving posts from specific group
     * @param groupId group id
     * @param count number of posts to get
     * @param offset offset of posts
     * @return query to execute
     */
    private WallGetQuery getQueryForWall(int groupId, int count, int offset){
        // Minus is placed in front of groupId due to vk specification of wall.get method
        return VK.wall().get(USER_ACTOR).ownerId(-groupId)
                .filter(WallGetFilter.OWNER)
                .count(count).offset(offset);
    }

    /**
     * Retrives users profiles that are subscribed for a specific group
     * @param groupId group id to get members profiles of
     * @param count number of profiles to retrive (max 1_000)
     * @param offset offset of profiles
     * @return list of users profiles
     */
    public List<UserXtrCounters> getGroupMembers(int groupId, int count, int offset){
        List<UserXtrCounters> result = new ArrayList<>();

        try {
            List<Integer> ids = getQueryForGroupMembersId(
                    String.valueOf(groupId),
                    count,
                    offset
            ).execute().getItems();

            result = getQueryForGroupMembersProfile(
                    ids.stream().map(String::valueOf).collect(Collectors.toList())
            ).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Retrieves number of groups members
     * @param groupId group id to get members of
     * @return number of groups members
     */
    public Integer getGroupMembersCount(int groupId) {
        Integer count = 0;

        try {
            count = VK.groups().getMembers(GROUP_ACTOR).count(1).groupId(String.valueOf(groupId)).execute().getCount();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Retrieves wallposts info from a specific group
     * @param groupId group id to get wallposts info of
     * @param count number of wallposts to retrive (max 1_00)
     * @param offset offset of wallposts
     * @return list of wallposts info
     */
    public List<WallPostFull> getGroupWallPosts(int groupId, int count, int offset){
        List<WallPostFull> wallPosts =  new ArrayList<>();

        try {
            wallPosts = getQueryForWall(groupId, count, offset).execute().getItems();

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        return wallPosts;
    }

    /**
     * Retrieves number of groups wallposts
     * @param groupId group id to get wallposts of
     * @return number of groups wallposts
     */
    public Integer getGroupWallPostsCount(int groupId) {
        Integer count = 0;

        try {
             // Minus is placed in front of groupId due to vk specification of wall.get method
            count = getQueryForWall(groupId, 1, 0).execute().getCount();

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Retrieves list of users ids who liked specific wallpost
     * @param groupId group id that owns a wallpost
     * @param wallPostId wallpost id to get likes of
     * @param count number of users ids to retrive (max 1_000)
     * @param offset offset of ids
     * @return list of users ids
     */
    public List<Integer> getListOfIdsWhoLikedWallPost(int groupId, int wallPostId, int count, int offset){
        List<Integer> result = new ArrayList<>();

        try{
            result = getQueryForLikes(groupId, wallPostId, count, offset).execute().getItems();

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Retrieves number of likes of specific wallposts
     * @param groupId group id that owns a wallpost
     * @param wallPostId wallpost id to get likes of
     * @return number of wallpost likes
     */
    public Integer getWallPostsLikesCount(int groupId, int wallPostId) {
        Integer count = 0;

        try {
            count = getQueryForLikes(groupId, wallPostId, 1, 0).execute().getCount();

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Retrieves user profile from vk by its id
     *
     * @param userId user id to load
     * @return user profile
     */
    public VkCustomUser getUserById(int userId){
        VkCustomUser user = new VkCustomUser();

        try {
            List<UserXtrCounters> responseList = VK.users().get(USER_ACTOR)
                    .userIds(String.valueOf(userId))
                    .fields(UserField.SEX).execute();

            if (responseList != null || responseList.size() != 0){
                user = CastingFactory.UserXtrCountersToVkCustomUser(responseList.get(0));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
