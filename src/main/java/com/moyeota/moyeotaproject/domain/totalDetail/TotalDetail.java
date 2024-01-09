package com.moyeota.moyeotaproject.domain.totalDetail;

import com.moyeota.moyeotaproject.domain.posts.Posts;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class TotalDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double totalDistance;

    @Column(nullable = false)
    private double totalPayment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Posts post;

    @Builder
    public TotalDetail(double totalDistance, double totalPayment, Posts post) {
        this.totalDistance = totalDistance;
        this.totalPayment = totalPayment;
        this.post = post;
    }
}
