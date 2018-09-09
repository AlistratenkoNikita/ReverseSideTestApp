package ua.com.alistratenko.ReverseSideTestApp.entity;

/**
 * Represents Like from vk
 *
 * @author Nikita
 * @since 08.09.2018
 */
public class VkCustomLike {
    private int postOwnerId;
    private int postId;
    private int userId;

    public int getPostOwnerId() {
        return postOwnerId;
    }

    public VkCustomLike setPostOwnerId(int postOwnerId) {
        this.postOwnerId = postOwnerId;
        return this;
    }

    public int getPostId() {
        return postId;
    }

    public VkCustomLike setPostId(int postId) {
        this.postId = postId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public VkCustomLike setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "VkCustomLike{" +
                "postOwnerId=" + postOwnerId +
                ", postId=" + postId +
                ", userId=" + userId +
                '}';
    }
}
