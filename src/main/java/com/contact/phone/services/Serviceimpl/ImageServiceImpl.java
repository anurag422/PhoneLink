package com.contact.phone.services.Serviceimpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.contact.phone.Helper.AppConstant;
import com.contact.phone.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile contactImage,String filename) {

        try {
            byte[] data = contactImage.getBytes();
            cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id",filename));
            return this.getUrlFromPublicId(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary.url().transformation(new Transformation<>().width(AppConstant.Width).height(AppConstant.Height).crop(AppConstant.Crop)).generate(publicId);
    }
}
