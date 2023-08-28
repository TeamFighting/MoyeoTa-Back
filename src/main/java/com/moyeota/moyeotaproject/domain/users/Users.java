package com.moyeota.moyeotaproject.domain.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessage;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String profileImage;

    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String loginId;
    @Column(nullable = false)
    private String password;
    private String status;
    private Boolean gender;
    private Float averageStarRate;
    private String school;
    private Boolean isAuthenticated;
    private String socialLogin;

    @OneToMany(mappedBy = "user")
    private List<Posts> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ParticipationDetails> participationDetails = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public List<Posts> getPosts() {
        return this.posts;
    }

    public void addPost(Posts post) {
        posts.add(post);
        post.setUser(this);
    }

//    @Builder
//    public Users(String name, String profileImage, String password, Boolean gender, Float averageStarRate, String school, Boolean isAuthenticated) {
//        this.name = name;
//        this.profileImage = profileImage;
//        this.password = password;
//        this.gender = gender;
//        this.averageStarRate = averageStarRate;
//        this.school = school;
//        this.isAuthenticated = isAuthenticated;
//    }

    //테스트용으로 잠깐 만들어 두었습니다. 나중에 제거하겠습니다.
    @Builder
    public Users(String name, String profileImage, String phoneNumber, String email, String loginId, String password, String status, Boolean gender, Float averageStarRate, String school, Boolean isAuthenticated, String socialLogin) {
        this.name = name;
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
        this.socialLogin = socialLogin;
    }
    public Users(String name, String profileImage, String phoneNumber, String email, String loginId, String password, String status, Boolean gender, Float averageStarRate, String school, Boolean isAuthenticated) {
        this.name = name;
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
    }

}
