package com.wanted.yamyam.domain.store.entity;

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
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double lat;

    @Column
    private double lon;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String category;

    @Column
    private double rating;
}
