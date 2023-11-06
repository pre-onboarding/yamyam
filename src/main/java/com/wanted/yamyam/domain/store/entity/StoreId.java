package com.wanted.yamyam.domain.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreId implements Serializable {
    @Column(name = "store_name")
    private String name;
    @Column(name = "store_address")
    private String address;
}
