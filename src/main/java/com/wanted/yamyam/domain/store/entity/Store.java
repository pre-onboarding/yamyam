package com.wanted.yamyam.domain.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(StoreId.class)
public class Store implements Serializable {

    @Id
    @Column(name = "store_name")
    private String name;
  
    @Id
    @Column(name = "store_address")
    private String address;

    @Column
    private double lat;

    @Column
    private double lon;

    @Column
    private String category;

    @Column
    private double rating;
}
