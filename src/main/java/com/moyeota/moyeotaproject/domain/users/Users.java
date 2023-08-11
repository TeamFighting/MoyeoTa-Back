package com.moyeota.moyeotaproject.domain.users;


import com.moyeota.moyeotaproject.domain.posts.Posts;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String profileImage;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private boolean gender;

    @Column(nullable = false)
    private float averageStarRate;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private boolean isAuthenticated;

    @OneToMany(mappedBy = "user")
    private List<Posts> posts = new ArrayList<>();

    public List<Posts> getPosts() {
        return this.posts;
    }

    public void addPost(Posts post) {
        posts.add(post);
        post.setUser(this);
    }

}
