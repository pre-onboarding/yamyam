package com.wanted.yamyam.domain.member.repo;

import com.wanted.yamyam.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
  
    List<Member> findAllByUseRecommendLunch(boolean useRecommendLunch);
}
