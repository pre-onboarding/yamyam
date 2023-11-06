package com.wanted.yamyam.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String refreshToken;

    @Column
    private String username;

    @Column
    private double lat;

    @Column
    private double lon;

    @Column
    private boolean recommend;
 
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
}
