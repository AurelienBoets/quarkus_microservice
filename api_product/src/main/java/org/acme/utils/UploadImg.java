package org.acme.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UploadImg {
    
    @ConfigProperty(name = "file.upload-dir")
    String uploadDir;

 public String upload(String imgBase64, String name) {
        try {
            byte[] imgData = Base64.getDecoder().decode(imgBase64);
            String salt = UUID.randomUUID().toString();
            String fileName = salt + "_" + name;
            Files.createDirectories(Paths.get(uploadDir));
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, imgData);
            return fileName;
        } catch (Exception e) {
            Logger logger=Logger.getLogger(getClass().getName());
            logger.warning("Error uploading image: " + e.getMessage());
            return null;
        }
    }

    public String getImg(String fileName){
        try{
            Path filePath = Paths.get(uploadDir, fileName);
            File file=new File(filePath.toUri());
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            return Base64.getEncoder().encodeToString(fileContent);
        }catch(Exception e){
            Logger logger=Logger.getLogger(getClass().getName());
            logger.warning(e.getMessage());
        }
        return "";
    }
}
