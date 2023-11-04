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
@IdClass(StoreId.class)
public class Store {

    @Id
    @Column(name = "store_name")
    private String name;

    @Id
    @Column(name = "store_address")
    private String address;

    @Column
    private String lat;

    @Column
    private String lon;

    @Column
    private String category;

    @Column
    private String rating;

}
