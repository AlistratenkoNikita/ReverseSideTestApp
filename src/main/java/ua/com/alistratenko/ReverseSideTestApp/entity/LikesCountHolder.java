package ua.com.alistratenko.ReverseSideTestApp.entity;

/**
 * Represents number of post likes from one person for one group
 *
 * @author Nikita
 * @since 09.09.2018
 */
public class LikesCountHolder {
    private int userId;
    private int likesCount;

    public int getUserId() {
        return userId;
    }

    public LikesCountHolder setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public LikesCountHolder setLikesCount(int likesCount) {
        this.likesCount = likesCount;
        return this;
    }

    @Override
    public String toString() {
        return "LikesCountHolder{" +
                "userId=" + userId +
                ", likesCount=" + likesCount +
                '}';
    }
}
