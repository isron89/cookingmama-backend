package com.cookingmama.cookingmamabackend.controllers;


//import com.cookingmama.cookingmamabackend.models.ImageModel;
//import com.cookingmama.cookingmamabackend.repository.ImageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Log4j2
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping(path = "/image")
public class ImageController {
    @PostMapping("/upload-file")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String uploadImage(@RequestParam("image")MultipartFile file) throws Exception{
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getName());
        System.out.println(file.getContentType());
        System.out.println(file.getSize());

        String Path_Directory = "D:\\Tugas Besar ML\\cookingmama-backend\\src\\main\\resources\\static\\image";
//        String Path_Directory = "'${resource.path}'";
//        String Path_Directory = new ClassPathResource("static/image/").getFile().getAbsolutePath();
        Files.copy(file.getInputStream(), Paths.get(Path_Directory+ File.separator+file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

        return "Successfully Image is Uploaded";
    }

//    @Autowired
//    private ImageRepository imageRepository;
//
//    @PostMapping("/upload")
//    public ResponseEntity.BodyBuilder uploadImage(@RequestParam("imageFile")MultipartFile file) throws IOException{
//
//        log.info("Original Image Byte Size - " + file.getBytes().length);
//        ImageModel img = new ImageModel(file.getOriginalFilename(), file.getContentType(),
//                compressBytes(file.getBytes()));
//        imageRepository.save(img);
//        return ResponseEntity.status(HttpStatus.OK);
//    }
//
//    @GetMapping(path = {"/get/{imageName}"})
//    public ImageModel getImage(@PathVariable("imageName")String imageName)throws IOException{
//        final Optional<ImageModel> retrievedImage = imageRepository.findByName(imageName);
//        ImageModel img = new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType(),
//                decompressBytes(retrievedImage.get().getPicByte()));
//        return img;
//    }
//
//    //compress image before storing to the database
//    public static byte[] compressBytes(byte[] data){
//        Deflater deflater = new Deflater();
//        deflater.setInput(data);
//        deflater.finish();
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        while (!deflater.finished()){
//            int count = deflater.deflate(buffer);
//            outputStream.write(buffer,0, count);
//        }
//        try {
//            outputStream.close();
//        }catch (IOException e){
//        }
//        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
//        return outputStream.toByteArray();
//    }
//
//    //uncompressed the image bytes before returning it to the angular application
//    public static byte[] decompressBytes(byte[] data){
//        Inflater inflater = new Inflater();
//        inflater.setInput(data);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        try {
//            while (!inflater.finished()){
//                int count = inflater.inflate(buffer);
//                outputStream.write(buffer,0, count);
//            }
//            outputStream.close();
//        } catch (IOException e){
//        } catch (DataFormatException e){
//        }
//        return outputStream.toByteArray();
//    }
}
