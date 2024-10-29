package Winspire.controller;

import Winspire.dto.ApiResponse;
import Winspire.dto.UserDto;
import Winspire.service.FileService;
import Winspire.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${users.profileImage.active}")
    private String imagePath;
    @Value("${users.rareCollections.active}")
    private String MultipleUserImageUploadPath;

    private Logger logger= LoggerFactory.getLogger(UserController.class);


    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto){
        UserDto userDto1 = userService.create(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser(){
        List<UserDto> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable ("userId") String userId){
        UserDto userDto = userService.getUser(userId);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> Update(@RequestBody UserDto userDto,@PathVariable("userId") String id){
        UserDto updatedDto = userService.update(userDto, id);
        return new ResponseEntity<>( updatedDto,HttpStatus.OK);
    }



    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> Delete(@PathVariable("userId") String id){

        userService.delete(id);
        ApiResponse response = ApiResponse.builder().message("User Delete Successfully")
                .status(HttpStatus.OK)
                .success(true).build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/uploadRareCollectionImages/{userId}")
    public ResponseEntity<ApiResponse> upload(
            @PathVariable("userId") String userId,
            @RequestParam("images")MultipartFile [] files) throws IOException {
        List<String> rareCollections = fileService.uploadMultiple(files, MultipleUserImageUploadPath);
        UserDto user = userService.getUser(userId);
        user.setRareCollectionImages(rareCollections);
        userService.update(user,userId);
        ApiResponse response = ApiResponse.builder().message(rareCollections + "Uploaded").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/image/profile/{userId}")
    public ResponseEntity<ApiResponse> saveProfile(
            @PathVariable String userId,
            @RequestParam("profileImage") MultipartFile file
    ) throws IOException {

        String imageName = fileService.upload(file, imagePath);
        UserDto user = userService.getUser(userId);
        user.setProfileImage(imageName);
        userService.update(user,userId);
        ApiResponse response = ApiResponse.builder().message(imageName + "Uploaded").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/image/downloadProfile/{userId}")
    public void serveImage(
            @PathVariable("userId") String userId,
            HttpServletResponse response
    ) throws IOException {
        UserDto user = userService.getUser(userId);
        logger.info(imagePath);
        logger.info(user.getProfileImage());
        InputStream resource = fileService.getResource(imagePath, user.getProfileImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }



    @GetMapping("/downloadRareCollectionImages/{userId}")
    public void getCollection(@PathVariable("userId") String userId,
       HttpServletResponse response
                              ) throws IOException {
        UserDto user = userService.getUser(userId);
        List<String> rareCollectionImages = user.getRareCollectionImages();
        logger.info(rareCollectionImages.toString());
        List<InputStream> rareUserCollection = fileService.getRareUsercollection(MultipleUserImageUploadPath, rareCollectionImages);
        // Set response headers
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"rare_collection_images.zip\"");

        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            for (int i = 0; i < rareUserCollection.size(); i++) {
                InputStream inputStream = rareUserCollection.get(i);
                String imageName = rareCollectionImages.get(i);

                // Create a new zip entry for each image
                zipOut.putNextEntry(new ZipEntry(imageName));

                // Write image data to the zip entry
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, bytesRead);
                }
                zipOut.closeEntry();
                inputStream.close();
            }
            zipOut.finish();
        } catch (IOException e) {
            logger.error("Error sending images: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not send images");
        }

    }

}
