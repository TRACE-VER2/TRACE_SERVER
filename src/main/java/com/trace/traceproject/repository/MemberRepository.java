package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNameAndPhoneNum(String name, String phoneNum);

    Optional<Member> findByUserId(String userId);
}
