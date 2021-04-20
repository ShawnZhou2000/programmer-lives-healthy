package com.abo.programmerliveshealthy.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author abo
 * @date 2020/7/7 9:12
 * @remarks
 **/
@Component
public class MyFileUpload {

    @Value("${file.upload.path}")
    private String filePath;
    @Value("${file.upload.path.relative}")
    private String fileRelativePath;

    public String uploadImg(MultipartFile img) throws IOException {
        String path = filePath;
        String myPath = fileRelativePath.substring(0, 5);
        Long mills = + System.currentTimeMillis();
        String originFileName = img.getOriginalFilename();
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));
        String filename = mills.toString() + suffix;
        File filepath = new File(path, filename);
        if(!filepath.getParentFile().exists()){
            filepath.getParentFile().mkdirs();
        }
        img.transferTo(new File(path + File.separator + filename));
        return myPath + filename;
    }
}
