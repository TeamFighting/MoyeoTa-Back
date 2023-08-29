package com.moyeota.moyeotaproject.domain.review;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double starRate;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    public void setUser(Users user){
        this.user = user;
        user.getReviews().add(this);
    }

    @Builder
    public Review(double starRate, String content, Users user) {
        this.starRate = starRate;
        this.content = content;
        this.setUser(user);
    }
}
