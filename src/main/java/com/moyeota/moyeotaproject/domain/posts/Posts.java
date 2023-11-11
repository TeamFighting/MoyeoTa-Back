package com.moyeota.moyeotaproject.domain.posts;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String departure;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    private String content;

    @Enumerated(EnumType.STRING)
    private SameGender sameGenderStatus;

    @Enumerated(EnumType.STRING)
    private Vehicle vehicle;

    @Column(nullable = false)
    private int numberOfRecruitment;

    @Column(nullable = false)
    private int numberOfParticipants;

    @Enumerated(EnumType.STRING)
    private PostsStatus status;

    @Column(nullable = false)
    private int fare;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private double distance;

    @Column(nullable = false)
    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId") //FK
    private Users user;

    @OneToMany(mappedBy = "post")
    private List<ParticipationDetails> participationDetails = new ArrayList<>();

    //연관관계 메서드
    public void setUser(Users user) {
        this.user = user;
        user.getPosts().add(this);
    }

    @Builder
    public Posts(String title, Category category, String departure, String destination, LocalDateTime departureTime,
                 String content, SameGender sameGenderStatus, Vehicle vehicle, int numberOfRecruitment,
                 int numberOfParticipants, int fare, int duration, int distance, int view, Users user) {
        this.title = title;
        this.category = category;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.content = content;
        this.sameGenderStatus = sameGenderStatus;
        this.vehicle = vehicle;
        this.numberOfRecruitment = numberOfRecruitment;
        this.numberOfParticipants = numberOfParticipants;
        this.status = PostsStatus.RECRUITING;
        this.fare = fare;
        this.duration = duration;
        this.distance = distance;
        this.view = 0;
        this.setUser(user);
    }

    public void addUser() {
        this.numberOfParticipants++;
    }

    public void minusUser() {
        this.numberOfParticipants--;
        this.status = PostsStatus.RECRUITING;
    }

    public void update(String title, String content, Category category, String departure, String destination,
                       LocalDateTime departureTime, SameGender sameGenderStatus, Vehicle vehicle,
                       int numberOfRecruitment, int fare, int duration, double distance) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.sameGenderStatus = sameGenderStatus;
        this.vehicle = vehicle;
        this.numberOfRecruitment = numberOfRecruitment;
        this.fare = fare;
        this.duration = duration;
        this.distance = distance;
    }

    public void postsComplete() {
        if (this.status == PostsStatus.COMPLETE) {
            throw new IllegalStateException("이미 모집이 마감된 글입니다.");
        }
        this.status = PostsStatus.COMPLETE;
    }
}
