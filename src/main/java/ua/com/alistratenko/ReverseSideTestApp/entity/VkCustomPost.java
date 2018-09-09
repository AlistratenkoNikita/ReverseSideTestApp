package ua.com.alistratenko.ReverseSideTestApp.entity;

/**
 * Custom Post class that stores only needed info about wallpost from vk
 *
 * @author Nikita
 * @since 08.09.2018
 */
public class VkCustomPost {

    private int id;
    private int ownerId;
    private int likesAmount;

    public VkCustomPost() {
    }

    public VkCustomPost(int id, int ownerId, int likesAmount) {
        this.id = id;
        this.ownerId = ownerId;
        this.likesAmount = likesAmount;
    }

    public int getId() {
        return id;
    }

    public int getLikesAmount() {
        return likesAmount;
    }

    public VkCustomPost setId(int id) {
        this.id = id;
        return this;
    }

    public VkCustomPost setLikesAmount(int likesAmount) {
        this.likesAmount = likesAmount;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public VkCustomPost setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    @Override
    public String toString() {
        return "VkCustomPost{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", likesAmount=" + likesAmount +
                '}';
    }
}
