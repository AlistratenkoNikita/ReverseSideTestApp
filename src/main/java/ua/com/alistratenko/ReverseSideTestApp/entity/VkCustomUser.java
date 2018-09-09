package ua.com.alistratenko.ReverseSideTestApp.entity;

import com.vk.api.sdk.objects.base.Sex;

import java.util.HashSet;
import java.util.Set;

/**
 * Custom User class that stores only needed info about user from vk
 *
 * @author Nikita
 * @since 08.09.2018
 */
public class VkCustomUser {

    private int id;
    private Sex sex;
    private String firsName;
    private String lastName;
    private Set<Integer> groupsIds;

    public int getId() {
        return id;
    }

    public VkCustomUser() {
         groupsIds = new HashSet<>();
    }

    public VkCustomUser(int id, String firsName, String lastName, Sex sex) {
        this.id = id;
        this.sex = sex;
        this.firsName = firsName;
        this.lastName = lastName;
        groupsIds = new HashSet<>();
    }

    public VkCustomUser setId(int id) {
        this.id = id;
        return this;
    }

    public Sex getSex() {
        return sex;
    }

    public VkCustomUser setSex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public String getFirsName() {
        return firsName;
    }

    public VkCustomUser setFirsName(String firsName) {
        this.firsName = firsName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public VkCustomUser setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Set<Integer> getGroupsIds() {
        return groupsIds;
    }

    public VkCustomUser addSubscription(Integer groupId){
        groupsIds.add(groupId);
        return this;
    }

    @Override
    public String toString() {
        return "VkCustomUserWithSubcriptions{" +
                "id=" + id +
                ", sex='" + sex + '\'' +
                ", firsName='" + firsName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", groupsIds=" + groupsIds +
                '}';
    }
}
