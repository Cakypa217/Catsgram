package ru.yandex.practicum.catsgram.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.dto.ImageUploadRequest;
import ru.yandex.practicum.catsgram.dto.PostImageResponse;
import ru.yandex.practicum.catsgram.model.Image;

import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageMapper {
    public static Image mapToImage(ImageUploadRequest request, Long postId, String imageDirectory) {
        Image image = new Image();
        image.setPostId(postId);
        image.setOriginalFileName(request.getFiles().get(0).getOriginalFilename());
        MultipartFile file = request.getFiles().get(0);
        String originalFilename = file.getOriginalFilename();
        image.setOriginalFileName(originalFilename);

        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;
        String filePath = Paths.get(imageDirectory, uniqueFileName).toString();
        image.setFilePath(filePath);
        return image;
    }

    public static PostImageResponse mapToImageForPost(Long postId, List<Image> images) {
        PostImageResponse response = new PostImageResponse();
        response.setPostId(postId);
        response.setImages(images.stream().map(ImageMapper::mapToImageDto).collect(Collectors.toList()));
        return response;
    }

    public static ImageDto mapToImageDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setPostId(image.getPostId());
        dto.setOriginalFileName(image.getOriginalFileName());
        dto.setFilePath(image.getFilePath());
        return dto;
    }
}
