package com.moyeota.moyeotaproject.domain.users;


import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@RequiredArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;
    private String profileImage;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean gender;

    @Column(nullable = false)
    private Float averageStarRate;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private Boolean isAuthenticated;

    @OneToMany(mappedBy = "user")
    private List<Posts> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ParticipationDetails> participationDetails = new ArrayList<>();

    public List<Posts> getPosts() {
        return this.posts;
    }

    public void addPost(Posts post) {
        posts.add(post);
        post.setUser(this);
    }

    @Builder
    public Users(String loginId, String password, String name, String profileImage, String phoneNumber, String email, String status, Boolean gender, Float averageStarRate, String school, Boolean isAuthenticated) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = status;
        this.gender = gender;
        this.averageStarRate = averageStarRate;
        this.school = school;
        this.isAuthenticated = isAuthenticated;
    }

}
