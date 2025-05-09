package com.yoger.productserviceorganization.product.application.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface ManageProductImagePort {
    String uploadImage(MultipartFile image);

    void deleteImage(String imageUrl);

    String updateImage(MultipartFile image, String originImageUrl);
}
