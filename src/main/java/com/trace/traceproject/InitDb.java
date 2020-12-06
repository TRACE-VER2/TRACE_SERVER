package com.trace.traceproject;

import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.repository.BuildingRepository;
import com.trace.traceproject.repository.MemberRepository;
import com.trace.traceproject.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Profile("local")
@Service
@RequiredArgsConstructor
public class InitDb implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final BuildingRepository buildingRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public void run(String... args) throws Exception {
        Member member = new Member("syleemk", "1234", "이글루", "syleemk@naver.com", "01012341234");
        memberRepository.save(member);
        Building building = new Building(null, "성북구", LocalDateTime.now());
        buildingRepository.save(building);
    }
}
