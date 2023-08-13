package com.moyeota.moyeotaproject.domain.posts;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

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

    @Column(nullable = false)
    private String departure;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    private String content;

    @Enumerated(EnumType.STRING)
    private SameGender sameGenderStatus;

    @Column(nullable = false)
    private int numberOfRecruitment;

    @Column(nullable = false)
    private int numberOfParticipants;

    @Enumerated(EnumType.STRING)
    private PostsStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId") //FK
    private Users user;

    @OneToMany(mappedBy = "post")
    public List<ParticipationDetails> participationDetails = new ArrayList<>();

    //연관관계 메서드
    public void setUser(Users user) {
        this.user = user;
        user.getPosts().add(this);
    }

    @Builder
    public Posts(String title, String departure, String destination, LocalDateTime departureTime, String content, SameGender sameGenderStatus, int numberOfRecruitment, int numberOfParticipants, Users user) {
        this.title = title;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.content = content;
        this.sameGenderStatus = sameGenderStatus;
        this.numberOfRecruitment = numberOfRecruitment;
        this.numberOfParticipants = numberOfParticipants;
        this.status = PostsStatus.RECRUITING;
        this.setUser(user);
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void postsComplete() {
        if(this.status == PostsStatus.COMPLETE)
            throw new IllegalStateException("이미 모집이 마감된 글입니다.");
        this.status = PostsStatus.COMPLETE;
    }
}
