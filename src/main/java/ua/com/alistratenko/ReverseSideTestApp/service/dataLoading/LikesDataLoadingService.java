package ua.com.alistratenko.ReverseSideTestApp.service.dataLoading;

import com.vk.api.sdk.objects.wall.WallPostFull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;
import ua.com.alistratenko.ReverseSideTestApp.entity.VkCustomLike;
import ua.com.alistratenko.ReverseSideTestApp.entity.VkCustomPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that provides loading service for likes info from vk
 *
 * @author Nikita
 * @since 08.09.2018
 */
@Service
public class LikesDataLoadingService extends DataLoadingService {

    @Value("${max-likes-per-request}")
    private int MAX_LIKES_COUNT_PER_REQUEST;

    @Value("${max-posts-per-request}")
    private int MAX_POSTS_COUNT_PER_REQUEST;

    /**
     * Loads all necessary likes data from vk (all posts from group and all likes for every post in it)
     */
    public void loadData() {
        loadPostsData();
        loadLikesData();
    }

    /**
     * Loads all post from specific vk group
     */
    private void loadPostsData() {
        int numberOfWallPosts = requestSender.getGroupWallPostsCount(GROUP_ID);

        for (int i = 0; i <= numberOfWallPosts / MAX_POSTS_COUNT_PER_REQUEST; i++) {

            List<WallPostFull> wallPosts = requestSender.getGroupWallPosts(GROUP_ID, MAX_POSTS_COUNT_PER_REQUEST, i * MAX_POSTS_COUNT_PER_REQUEST);
            generalDAL.addPosts(wallPosts, GROUP_ID);

            try {
                Thread.sleep(PAUSE_FOR_EVERY_REQUEST);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("-- Wallposts loaded. Amount: " + wallPosts.size() + ", with offset = " + (i * MAX_POSTS_COUNT_PER_REQUEST));
        }

        logger.info("------ LOADING OF WALLPOSTS IS DONE ------");
    }

    /**
     * Generates List of @{@link VkCustomLike}
     * @param postOwnerId owner id of post
     * @param wallPostId post id
     * @param idsWhoLiked ids of people who liked given post
     * @return generated list of Likes
     */
    private List<VkCustomLike> generateLikes(int postOwnerId, int wallPostId, List<Integer> idsWhoLiked) {
        List<VkCustomLike> likes = new ArrayList<>();

        idsWhoLiked.forEach(s -> likes.add(new VkCustomLike().setPostId(wallPostId).setUserId(s).setPostOwnerId(postOwnerId)));

        return likes;
    }

    /**
     * Loads all likes of specific post
     */
    private void loadLikesData() {
        CloseableIterator<VkCustomPost> postStream = generalDAL.getPostStream(GROUP_ID);

        while (postStream.hasNext()) {
            VkCustomPost post = postStream.next();

            int wallPostLikesAmount = requestSender.getWallPostsLikesCount(GROUP_ID, post.getId());

            for (int i = 0; i <= wallPostLikesAmount / MAX_LIKES_COUNT_PER_REQUEST; i++) {

                List<Integer> listOfIdsWhoLikedWallPost = requestSender.getListOfIdsWhoLikedWallPost(
                        GROUP_ID,
                        post.getId(),
                        MAX_LIKES_COUNT_PER_REQUEST,
                        (i * MAX_LIKES_COUNT_PER_REQUEST)
                );

                generalDAL.addLikes(generateLikes(
                        GROUP_ID,
                        post.getId(),
                        listOfIdsWhoLikedWallPost));

                try {
                    Thread.sleep(PAUSE_FOR_EVERY_REQUEST);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                logger.info("-- Likes loaded. Post id: "+ post.getId() + ", likes amount: " + listOfIdsWhoLikedWallPost.size() + ", with offset = " + (i * MAX_LIKES_COUNT_PER_REQUEST));
            }
        }

        logger.info("------ LOADING OF LIKES IS DONE ------");
    }
}
