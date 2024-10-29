package Winspire.service.implementation;

import Winspire.exception.BadApiRequestException;
import Winspire.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImplementation implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImplementation.class);
    @Override
    public String upload(MultipartFile file, String path) throws IOException {

        String originalFilename=file.getOriginalFilename();
        String fileName= UUID.randomUUID().toString().substring(0,8);
        String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
        logger.info(extension);
        String fileNameWithExtension=fileName+extension;
        String fullPathWithFileName= Paths.get(path,fileNameWithExtension).toString();
        if (extension.equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")
        )
        {
         // file save

            File folder = new File(path);
            if (!folder.exists()){
                folder.mkdirs();
            }

            Files.copy(file.getInputStream(),Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }
        else{
            throw  new BadApiRequestException("FileName with this "+extension+" not allowed");
        }


    }

    @Override
    public InputStream getResource(String path, String ImageName) throws FileNotFoundException {
        String fullPath=path+File.separator+ImageName;
        logger.info(path);
        logger.info(fullPath);
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }

    @Override
    public List<String> uploadMultiple(MultipartFile[] files, String path) throws IOException {
        List<String> fileNamesWithExtensions = new ArrayList<>();

        for (MultipartFile file:files){
            String originalFilename=file.getOriginalFilename();
            String fileName= UUID.randomUUID().toString().substring(0,8);
            String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
            logger.info(extension);
            String fileNameWithExtension=fileName+extension;
            String fullPathWithFileName= Paths.get(path,fileNameWithExtension).toString();
            if (extension.equalsIgnoreCase(".png") ||
                    extension.equalsIgnoreCase(".jpg")
                    || extension.equalsIgnoreCase(".jpeg")
            )
            {
                // file save

                File folder = new File(path);
                if (!folder.exists()){
                    folder.mkdirs();
                }

                Files.copy(file.getInputStream(),Paths.get(fullPathWithFileName));
                fileNamesWithExtensions.add(fileNameWithExtension);
            }
            else{
                throw  new BadApiRequestException("FileName with this "+extension+" not allowed");
            }
        }

        return fileNamesWithExtensions;
    }

    @Override
    public List<InputStream> getRareUsercollection(String path,List<String> imageName) throws FileNotFoundException {
        List<InputStream> allCollectionImages=new ArrayList<>();
        for (String name:imageName){
            String fullPath=path+File.separator+name;
            logger.info(path);
            logger.info(fullPath);
            InputStream inputStream=new FileInputStream(fullPath);
            allCollectionImages.add(inputStream);
        }
        return allCollectionImages;
    }
}
