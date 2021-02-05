package com.trace.traceproject.service;

import com.trace.traceproject.advice.exception.NoSuchEntityException;
import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Image;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.Review;
import com.trace.traceproject.dto.request.ReviewSaveDto;
import com.trace.traceproject.dto.request.ReviewUpdateDto;
import com.trace.traceproject.repository.BuildingRepository;
import com.trace.traceproject.repository.ImageRepository;
import com.trace.traceproject.repository.MemberRepository;
import com.trace.traceproject.repository.ReviewRepository;
import com.trace.traceproject.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BuildingRepository buildingRepository;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;

    @Transactional
    public Long save(List<MultipartFile> files, String userId, ReviewSaveDto reviewSaveDto) {
        if(files == null || files.isEmpty()) files = new ArrayList<>();

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new NoSuchEntityException("유효하지 않은 회원id 입니다."));
        Building building = buildingRepository.findById(reviewSaveDto.getBuildingId())
                .orElseThrow(() -> new NoSuchEntityException("유효하지 않은 빌딩입니다."));

        List<Image> images = uploadImages(files);

        //개인이 한 건물당 남길 수 있는 리뷰 하나로 제한?

        Review review = Review.createReview(reviewSaveDto.getReviewInfo(), member, building, images);

        //영속성 전이로 인해 review save되는 순간 image들도 save됨
        return reviewRepository.save(review).getId();
    }

    private List<Image> uploadImages(List<MultipartFile> files) {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String origFilename = file.getOriginalFilename();
                //파일 이름 중복 방지
                //UUID (Universal Unique IDentifier, 범용 고유 식별자) 생성
                UUID uuid = UUID.randomUUID();
                String filename = uuid.toString() + "_" + origFilename;
                //s3버킷의 images 폴더에 이미지 저장
                String filePath = s3Util.upload(file, filename, "images");

/*
                //로컬 저장소에 저장하는 방식
                //실행되는 위치의 files 폴더에 파일이 저장
                String savePath = System.getProperty("user.dir") + "\\files"; //escape코드라서 역슬래시 두번해줌
                //파일이 저장되는 폴더가 없으면 폴더 생성
                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String filePath = savePath + "\\" + filename;
                file.transferTo(new File(filePath));
*/

                Image image = Image.builder()
                        .origFilename(origFilename)
                        .filename(filename)
                        .filePath(filePath)
                        .build();

                //영속성 전이 사용함으로 인해 해줄필요 없어짐
                //imageRepository.save(image);
                images.add(image);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return images;
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new NoSuchEntityException("해당 게시물이 존재하지 않습니다."));
    }

    @Transactional
    public void update(Long id, List<MultipartFile> files, ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 리뷰입니다."));

        /**
         * 이미지 업데이트는 기존 이미지 삭제하고 추가로 업로드 하는 방식!
         * 삭제 업로드 수정 모두 cascade와 고아객체, 변경감지 이용
         */

        List<Long> deletedImages = reviewUpdateDto.getDeletedImages();
        //삭제되는 이미지가 있다면
        if (deletedImages != null) {
            log.info("이미지 삭제 시작");
            //리뷰의 이미지 리스트에서 해당하는 아이디와 일치하는 이미지를 제거한다.
            //s3에서 먼저 파일 제거
            review.getImages().forEach(image -> {
                if (deletedImages.contains(image.getId())) {
                    s3Util.delete(image.getFilename(),"images");
                }
            });
            log.info("s3 이미지 삭제 완료");
            //db에서 이미지 엔티티 제거
            review.getImages().removeIf(image -> deletedImages.contains(image.getId()));
            log.info("db 이미지 엔티티 삭제 완료");
        }

        //새로 업로드 되는 이미지 처리
        List<Image> images = uploadImages(files);
        for (Image image : images) {
            review.addImage(image); //연관관계 편의 메서드 사용
        }

        //변경감지
        review.changeReviewInfo(reviewUpdateDto);
    }

    //회원이 쓴 리뷰 목록 조회(페이징 처리)
    //컨트롤러단에서 PageRequest 생성해서 전달할필요없음 (알아서 매핑해줌) default설정만 해주기
    public Page<Review> findMemberReview(String userId, Pageable pageable){
        return reviewRepository.findByUserId(userId, pageable);
    }

    //건물에 달린 리뷰 목록 조회(페이징 처리)
    public Page<Review> findBuildingReview(Long buildingId, Pageable pageable) {
        return reviewRepository.findByBuildingId(buildingId, pageable);
    }

    @Transactional
    public void delete(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("유효하지 않은 리뷰 id입니다."));

        //리뷰에 딸린 s3업로드 이미지 제거
        review.getImages().forEach(img -> s3Util.delete(img.getFilename(), "images"));

        //리뷰 삭제 (영속성 전이(cascade)로 인해 image 엔티티들까지 같이 삭제)
        reviewRepository.deleteById(id);
    }
}
