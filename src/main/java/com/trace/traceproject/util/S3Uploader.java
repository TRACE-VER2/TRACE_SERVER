package com.trace.traceproject.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    /**
     * Spring Boot Cloud AWS 사용하면 S3관련 Bean 자동 생성
     * AmazonS3, AmazonS3Client, ResourceLoader 직접 설정할 필요 X
     */
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //dirName은 S3에 생성된 디렉터리를 나타냄
    public String upload(MultipartFile multipartFile, String dirName) throws IOException,
            UnsupportedEncodingException, NoSuchAlgorithmException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //파일 이름 중복 방지 (MD5Generator로 파일이름 인코딩)
        String fileName = dirName + "/" + new MD5Generator(uploadFile.getName());
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile); //Multipartfile -> File로 전환되면서 로컬에 파일 생성된 것을 삭제
        return uploadImageUrl;
    }

    //S3 bucket에 파일 넣는 메소드
    private String putS3(File uploadFile, String fileName) {
        //파일을 S3에 "public 읽기 권한"으로 put
        //이미지 파일 주소만 있으면 누구나 읽어올 수 있음
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //로컬에 생성된 File 삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다");
        }
    }
            
    //MultipartFile -> File로 변환
    //S3버킷에는 MultipartFile 타입은 전송이 안됨
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
