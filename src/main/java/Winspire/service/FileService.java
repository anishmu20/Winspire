package Winspire.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    String upload(MultipartFile file, String id) throws IOException;
    InputStream getResource(String path,String ImageName) throws FileNotFoundException;

    List<String> uploadMultiple(MultipartFile[] files , String path) throws IOException;

    List<InputStream> getRareUsercollection(String path,List<String> imageName)throws FileNotFoundException;

}
