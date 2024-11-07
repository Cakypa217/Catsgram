package ru.yandex.practicum.catsgram.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImageUploadRequest {
    private List<MultipartFile> files;
}
