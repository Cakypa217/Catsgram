package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.dto.ImageUploadRequest;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.ImageData;
import ru.yandex.practicum.catsgram.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/posts/{postId}/images")
    public ResponseEntity<List<ImageDto>> getPostImages(@PathVariable("postId") long postId) {
        List<ImageDto> images = imageService.getImagesByPostId(postId);
        return ResponseEntity.ok(images);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts/{postId}/images")
    public ResponseEntity<List<ImageDto>> addPostImages(@PathVariable("postId") long postId,
                                                        @RequestParam("image") List<MultipartFile> files) {
        List<ImageDto> createdImages = files.stream()
                .map(file -> {
                    ImageUploadRequest singleFileRequest = new ImageUploadRequest();
                    singleFileRequest.setFiles(List.of(file));
                    return imageService.createImage(singleFileRequest, postId);
                })
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdImages);
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<ImageDto> getImage(@PathVariable("imageId") long imageId) {
        ImageDto image = imageService.getImageById(imageId);
        return ResponseEntity.ok(image);
    }

    @GetMapping(value = "/images/{imageId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable long imageId) {
        try {
            ImageData imageData = imageService.getImageData(imageId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename(imageData.getName())
                            .build()
            );
            return new ResponseEntity<>(imageData.getData(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}