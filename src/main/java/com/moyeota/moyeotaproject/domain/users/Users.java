package com.moyeota.moyeotaproject.domain.users;


import com.moyeota.moyeotaproject.controller.dto.UserDto;
import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.chatRoomAndUsers.ChatRoomAndUsers;
import com.moyeota.moyeotaproject.domain.oAuth.OAuth;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.review.Review;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String profileImage;

    private String nickName;

    private String phoneNumber;

    private String email;

    @Column(nullable = false)
    private String loginId; // OAuthProvider + email
    @Column(nullable = false)
    private String password;
    private String status;
    private String gender;
    private String age;
    private Float averageStarRate;
    private String school;
    private Boolean isAuthenticated;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<OAuth> oAuths = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Posts> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<ParticipationDetails> participationDetails = new ArrayList<>();

//    @OneToMany(mappedBy = "user", orphanRemoval = true)
//    private List<ChatMessage> chatMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChatRoomAndUsers> chatRoomAndUsersList = new ArrayList<>();

    public List<Posts> getPosts() {
        return this.posts;
    }

    public void addPost(Posts post) {
        posts.add(post);
        post.setUser(this);
    }

    public void updateOAuth(OAuth oAuth) {
        oAuths.add(oAuth);
    }

    // 프로필 정보 전체 업데이트
    public void updateUsers(UserDto.updateDto usersDto) {
        this.name = Optional.ofNullable(usersDto.getName()).orElse(this.name);
        this.profileImage = Optional.ofNullable(usersDto.getProfileImage()).orElse(this.profileImage);
        this.phoneNumber = Optional.ofNullable(usersDto.getPhoneNumber()).orElse(this.phoneNumber);
        this.email = Optional.ofNullable(usersDto.getEmail()).orElse(this.email);
        this.gender = Optional.ofNullable(usersDto.getGender()).orElse(this.gender);
    }

    @Builder
    public Users(String name, String nickName, String profileImage, String phoneNumber, String email, String loginId, String password, String status, String gender, Float averageStarRate, String school, Boolean isAuthenticated, String age) {
        this.name = name;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.loginId = loginId;
        this.password = password;
        this.status = status;
        this.gender = gender;
        this.averageStarRate = averageStarRate;
        this.school = school;
        this.isAuthenticated = isAuthenticated;
        this.age = age;
    }

    @Builder
    public Users(String loginId, String password, String email) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }

    public void defaultProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void createNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateSchoolAuthenticate(String univName) {
        this.school = univName;
        this.isAuthenticated = Boolean.TRUE;
    }
}
