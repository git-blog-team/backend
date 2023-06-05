package com.blog.blograss.apis.banner;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.blog.blograss.apis.banner.object.BannerDto;
import com.blog.blograss.apis.banner.object.BannerIdsDto;
import com.blog.blograss.apis.banner.object.BannerListQueryDto;
import com.blog.blograss.commons.response.Message;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public ResponseEntity<Message> createBanner(BannerDto bannerDto) {

    try {
        
        String bannerId = UUID.randomUUID().toString();
        
        bannerDto.setBannerId(bannerId);

        bannerMapper.createBanner(bannerDto);

        BannerDto createdBannerDto = bannerMapper.getBanner(bannerId);

        return ResponseEntity.ok().body(Message.write("SUCCESS", createdBannerDto));

    } catch(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR",e));
    }

    }

    @Override
    public ResponseEntity<Message> getBanner(String bannerId) {
        try {

            BannerDto getBannerDto = bannerMapper.getBanner(bannerId);

            if (getBannerDto != null) {

                return ResponseEntity.ok().body(Message.write("SUCCESS", getBannerDto));

            } else {

                return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));

            }
    
        } catch(Exception e) {
    
            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR",e));
    
        }
    }

    @Override
    public ResponseEntity<Message> deleteBanner(BannerIdsDto bannerIds) {
        try {

            List<String> imageIdsToDelete = bannerMapper.getBannerImageIdsToDelete(bannerIds);

            for (String bannerImageId : imageIdsToDelete) {

                amazonS3Client.deleteObject(bucket, "images/" + bannerImageId);

            }
        
            bannerMapper.deleteBanner(bannerIds);

            return ResponseEntity.ok().body(Message.write("SUCCESS", bannerIds));
    
        } catch(Exception e) {
    
            e.printStackTrace();

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR",e));
    
        }
    }

    @Override
    public ResponseEntity<Message> getBannerList(int rowCount, int page, String bannerType, String search, String sortField, String sortOrder) {

        try {

            int offset = (page - 1) * rowCount;

            BannerListQueryDto bannerListQueryDto = BannerListQueryDto.builder()
                    .offset(offset)
                    .rowCount(rowCount)
                    .bannerType(bannerType)
                    .sortField(sortField)
                    .sortOrder(sortOrder)
                    .search(search)
                    .build();
            
            List<BannerDto> getBannerList = bannerMapper.getBannerList(bannerListQueryDto);

            int totalCount = bannerMapper.getTotalCount(bannerType,search);

            if (getBannerList != null) {
                
                return ResponseEntity.ok().body(Message.write("SUCCESS", getBannerList, totalCount));

            } else {

                return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));
            }
            
        } catch(Exception e) {
    
            e.printStackTrace();

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR",e));
    
        }
    }

    @Override
    public ResponseEntity<Message> updateBanner(BannerDto banner, String bannerId) {

        try {

            bannerMapper.updateBanner(banner);

            BannerDto updatedBannerDto = bannerMapper.getBanner(bannerId);

            return ResponseEntity.ok().body(Message.write("SUCCESS", updatedBannerDto));

        } catch (Exception e) {
            
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR"));
        }
    }
}
