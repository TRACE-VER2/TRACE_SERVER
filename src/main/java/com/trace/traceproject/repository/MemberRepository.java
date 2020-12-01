package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByNameAndPhoneNum(String name, String phoneNum);

    Member findByUserId(String userId);
}
