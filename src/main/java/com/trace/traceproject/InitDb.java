package com.trace.traceproject;

import com.trace.traceproject.domain.*;
import com.trace.traceproject.domain.enums.*;
import com.trace.traceproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Profile("local")
@Service
@RequiredArgsConstructor
public class InitDb implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final BuildingRepository buildingRepository;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final LocationRepository locationRepository;
    private final PreferenceRepository preferenceRepository;

    @Override
    public void run(String... args) throws Exception {

        Preference p1 = new Preference(Tag.CHEAP);
        Preference p2 = new Preference(Tag.LARGE);
        Preference p3 = new Preference(Tag.NO_BUG);
        Preference p4 = new Preference(Tag.QUIET);
        Preference p5 = new Preference(Tag.SUNNY);
        preferenceRepository.save(p1);
        preferenceRepository.save(p2);
        preferenceRepository.save(p3);
        preferenceRepository.save(p4);
        preferenceRepository.save(p5);


        Member member1 = new Member("minso", passwordEncoder.encode("1234"), "김민수", "minso@naver.com", "01012344567");
        memberRepository.save(member1);
        Member member2 = new Member("sooyoung", passwordEncoder.encode("1234"), "이수영", "sooyoung@naver.com", "01032454567");
        memberRepository.save(member2);

        Location location1 = new Location(LocationStatus.JJOKMOON);
        Location location2 = new Location(LocationStatus.CHULMOON);
        Location location3 = new Location(LocationStatus.HANSUNGSHIN);
        locationRepository.save(location1);
        locationRepository.save(location2);
        locationRepository.save(location3);

        Building building1 = new Building(location1, "서울시 성북구 동선동", "11-2",8, "1960");
        buildingRepository.save(building1);
        Building building2 = new Building(location2, "서울시 노원구 중계동","71-3",10, "1987");
        buildingRepository.save(building2);



        for (int i = 0; i < 50; i++) {
            Image image = new Image(null, "orig" + i, "filename" + i, "filePath/"+ i, null);
            Review review = Review.builder()
                    .member(member1)
                    .building(building1)
                    .roomNumber(Integer.toString(i))
                    .rentType(RentType.MONTHLY)
                    .deposit(1000)
                    .monthlyRent(100)
                    .score(5)
                    .area(6)
                    .livingStart(LocalDate.now())
                    .livingEnd(LocalDate.now().plusDays(1))
                    .remodeled(true)
                    .waterPressure(GoodBadStatus.BAD)
                    .lighting(GoodBadStatus.GOOD)
                    .frozen(GoodBadStatus.BAD)
                    .bug(BugStatus.ALWAYS)
                    .noise(NoiseStatus.NOISY)
                    .options("안녕하세요" + i)
                    .build();

            review.addImage(image);
            reviewRepository.save(review);
        }

        for (int i = 50; i < 100; i++) {
            Image image = new Image(null, "orig" + i, "filename" + i, "filePath/"+ i, null);
            Review review = Review.builder()
                    .member(member2)
                    .building(building2)
                    .roomNumber(String.valueOf(i))
                    .rentType(RentType.MONTHLY)
                    .deposit(1000)
                    .monthlyRent(100)
                    .score(5)
                    .area(6)
                    .livingStart(LocalDate.now())
                    .livingEnd(LocalDate.now().plusDays(1))
                    .remodeled(true)
                    .waterPressure(GoodBadStatus.BAD)
                    .lighting(GoodBadStatus.GOOD)
                    .frozen(GoodBadStatus.BAD)
                    .bug(BugStatus.ALWAYS)
                    .noise(NoiseStatus.NOISY)
                    .options("안녕하세요" + i)
                    .build();

            review.addImage(image);
            reviewRepository.save(review);
        }

        for (int i = 0; i < 30; i++) {
            Building building = Building.builder()
                    .address("서울시 노원구 중계" + i + "동")
                    .lotNumber(String.valueOf(i))
                    .completionDate("1987")
                    .location(location1)
                    .build();

            buildingRepository.save(building);
        }

        for (int i = 0; i < 30; i++) {
            Building building = Building.builder()
                    .address("서울시 종로구 명륜" + i + "가")
                    .lotNumber(String.valueOf(i))
                    .completionDate("1994")
                    .location(location2)
                    .build();

            buildingRepository.save(building);
        }

        for (int i = 0; i < 30; i++) {
            Building building = Building.builder()
                    .address("서울시 강북구 도봉" + i + "동")
                    .lotNumber(String.valueOf(i))
                    .completionDate("1960")
                    .location(location3)
                    .build();

            buildingRepository.save(building);
        }
    }
}
