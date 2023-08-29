package com.moyeota.moyeotaproject.domain.participationDetails;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ParticipationDetails extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ParticipationDetailsStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Posts post;

    //연관관계 메서드
    public void setUser(Users user) {
        this.user = user;
        user.getParticipationDetails().add(this);
    }

    public void setPost(Posts post) {
        this.post = post;
        post.getParticipationDetails().add(this);
    }

    @Builder
    public ParticipationDetails(Users user, Posts post) {
        this.status = ParticipationDetailsStatus.JOIN;
        this.setUser(user);
        this.setPost(post);
    }

    public void cancel() {
        if(this.status == ParticipationDetailsStatus.CANCEL)
            throw new IllegalStateException("이미 취소되었습니다.");
        this.status = ParticipationDetailsStatus.CANCEL;
    }
}
