package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.dal.ImageRepository;
import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.dto.ImageUploadRequest;
import ru.yandex.practicum.catsgram.exception.ImageFileException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.ImageMapper;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.ImageData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${image.upload.directory}")
    private String imageDirectory;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public ImageDto createImage(ImageUploadRequest request, long postId) {
        try {
            Image image = ImageMapper.mapToImage(request, postId, imageDirectory);
            MultipartFile file = request.getFiles().get(0);
            Path path = Paths.get(image.getFilePath());
            Files.write(path, file.getBytes());

            Image savedImage = imageRepository.save(image);
            return ImageMapper.mapToImageDto(savedImage);
        } catch (IOException e) {
            throw new ImageFileException("Ошибка при сохранении файла изображения");
        }
    }

    public ImageDto getImageById(long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Изображение не найдено"));
        return ImageMapper.mapToImageDto(image);
    }

    public List<ImageDto> getImagesByPostId(long postId) {
        Optional<Image> images = imageRepository.findByPostId(postId);
        if (images.isEmpty()) {
            throw new NotFoundException("Изображения для поста не найдены");
        }
        return images.stream()
                .map(ImageMapper::mapToImageDto)
                .collect(Collectors.toList());
    }

    public ImageData getImageData(long imageId)  throws IOException {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Изображение не найдено"));

        Path path = Paths.get(image.getFilePath());
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            byte[] data = Files.readAllBytes(path);
            return new ImageData(data, image.getOriginalFileName());
        } else {
            throw new IOException("Не удалось прочитать файл изображения");
        }
    }
}
