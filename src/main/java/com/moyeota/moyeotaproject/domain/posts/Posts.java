package com.moyeota.moyeotaproject.domain.posts;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String recruiterName;

    @Column(nullable = false)
    private String recruiterGender;

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

    //연관관계 메서드
    public void setUser(Users user) {
        this.user = user;
        user.getPosts().add(this);
    }
}
