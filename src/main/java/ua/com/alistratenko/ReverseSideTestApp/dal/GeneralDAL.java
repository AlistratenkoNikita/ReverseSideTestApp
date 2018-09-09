package ua.com.alistratenko.ReverseSideTestApp.dal;

import com.vk.api.sdk.objects.base.Sex;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Repository;
import ua.com.alistratenko.ReverseSideTestApp.CastingFactory;
import ua.com.alistratenko.ReverseSideTestApp.entity.LikesCountHolder;
import ua.com.alistratenko.ReverseSideTestApp.entity.VkCustomLike;
import ua.com.alistratenko.ReverseSideTestApp.entity.VkCustomPost;
import ua.com.alistratenko.ReverseSideTestApp.entity.VkCustomUser;

import java.util.List;

/**
 * Provides methods to work with MongoDB
 *
 * @author Nikita
 * @since 08.09.2018
 */
@Repository
public class GeneralDAL {

    private final MongoTemplate db;

    @Autowired
    public GeneralDAL(MongoTemplate db) {
        this.db = db;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void deleteInfoFromDB(){
        db.dropCollection(VkCustomLike.class);
        db.dropCollection(VkCustomPost.class);
        db.dropCollection(VkCustomUser.class);
    }

    /**
     * Adds users that are subscribed to specific vk group to db
     * @param users users to add to db
     * @param groupId id of group users are subscribed to
     */
    public void addUsers(List<UserXtrCounters> users, int groupId){
        users.forEach(s -> db.save(CastingFactory.UserXtrCountersToVkCustomUser(s, groupId)));
    }

    /**
     * Adds posts of specific vk group to db
     * @param posts posts to add to db
     * @param groupId id of group that owns posts
     */
    public void addPosts(List<WallPostFull> posts, int groupId){
        posts.forEach(s -> db.save(CastingFactory.WallPostFullToVkCustomPost(s, groupId)));
    }

    /**
     * Generates stream from db with posts from specific group
     * @param groupId id of group that owns posts
     * @return stream
     */
    public CloseableIterator<VkCustomPost> getPostStream(int groupId){
        Query q = new Query();
        q.addCriteria(Criteria.where("ownerId").in(groupId));
        return db.stream(q, VkCustomPost.class);
    }

    /**
     * Saves like from specific person that liked specific post
     * @param likes likes to add to db
     */
    public void addLikes(List<VkCustomLike> likes){
        for (VkCustomLike like : likes) {
            db.save(like);
        }
    }

    /**
     * Retrieves top 5 people who have liked the greates amount of posts in specific group
     * @param groupId group id to get users from
     * @return list of <=5 people
     */
    public List<LikesCountHolder> getTopFiveUsersByLikes(int groupId){
        Aggregation a = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("postOwnerId").in(groupId)),
                Aggregation.group("userId").count().as("likesCount"),
                Aggregation.project("likesCount").and("userId").previousOperation(),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "likesCount")),
                Aggregation.limit(5)
        );

        return db.aggregate(a, VkCustomLike.class, LikesCountHolder.class).getMappedResults();
    }

    /**
     * Counts amount of people of same sex in specific group
     * @param groupId group id to get users from
     * @param sex specific sex
     * @return number of people with of same sex
     */
    public int getSpecificSexCountInGroup(int groupId, Sex sex){
        Aggregation a = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("groupsIds").in(groupId).and("sex").is(getSexString(sex))),
                Aggregation.group("sex").count().as("count"),
                Aggregation.project("count").and("sex").previousOperation(),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "count"))
        );

        return db.aggregate(a, VkCustomUser.class, SexCountHolder.class).getMappedResults().get(0).getCount();
    }

    /**
     * Casts @{@link Sex} to String
     * @param sex @{@link Sex} object
     * @return string representation of @{@link Sex}
     */
    private String getSexString(Sex sex){
        return sex.getValue() == 1 ? "FEMALE" : sex.getValue() == 2 ? "MALE" : "UNKOWN";
    }

    /**
     * Is used to hold number of people of same sex
     */
    private class SexCountHolder{
        private int count;

        private int getCount() {
            return count;
        }

        private SexCountHolder setCount(int count) {
            this.count = count;
            return this;
        }
    }
}
