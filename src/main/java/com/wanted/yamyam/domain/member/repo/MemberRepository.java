package com.wanted.yamyam.domain.member.repo;

import com.wanted.yamyam.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

}
