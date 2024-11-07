package ru.yandex.practicum.catsgram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PostImageResponse {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long postId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ImageDto> images;
}
