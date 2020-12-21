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
import com.trace.traceproject.util.MD5Generator;
import com.trace.traceproject.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BuildingRepository buildingRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public Long save(List<MultipartFile> files, ReviewSaveDto reviewSaveDto) {
        if(files == null || files.isEmpty()) files = new ArrayList<>();

        Member member = memberRepository.findByUserId(reviewSaveDto.getUserId())
                .orElseThrow(()-> new NoSuchEntityException("유효하지 않은 회원id 입니다."));
        Building building = buildingRepository.findById(reviewSaveDto.getBuildingId())
                .orElseThrow(() -> new NoSuchEntityException("유효하지 않은 빌딩입니다."));

        List<Image> images = uploadImages(files);

        //개인이 한 건물당 남길 수 있는 리뷰 하나로 제한?

        Review review = Review.createReview(reviewSaveDto.getReviewInfo(), member, building, images);

        return reviewRepository.save(review).getId();
    }

    private List<Image> uploadImages(List<MultipartFile> files) {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String origFilename = file.getOriginalFilename();
                String filename = new MD5Generator(origFilename).toString();
                //s3버킷의 images 폴더에 이미지 저장
                String filePath = s3Uploader.upload(file, "images");

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

                imageRepository.save(image);
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
    public void update(ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(reviewUpdateDto.getReviewId())
                .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 리뷰입니다."));

        //변경감지
        review.changeReview(reviewUpdateDto);
    }

    //회원이 쓴 리뷰 목록 조회(페이징 처리)
    //컨트롤러단에서 PageRequest 생성해서 전달할필요없음 (알아서 매핑해줌) default설정만 해주기
    public Slice<Review> findMemberReview(String userId, Pageable pageable){
        return reviewRepository.findByUserId(userId, pageable);
    }

    //건물에 달린 리뷰 목록 조회(페이징 처리)
    public Slice<Review> findBuildingReview(Long buildingId, Pageable pageable) {
        return reviewRepository.findByBuildingId(buildingId, pageable);
    }
}
