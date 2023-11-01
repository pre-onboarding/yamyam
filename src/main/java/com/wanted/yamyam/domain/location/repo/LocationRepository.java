package com.wanted.yamyam.domain.location.repo;

import com.wanted.yamyam.domain.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location,Long> {
}
