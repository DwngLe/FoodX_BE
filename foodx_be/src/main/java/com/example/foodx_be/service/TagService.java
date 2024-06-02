package com.example.foodx_be.service;

import com.example.foodx_be.dto.response.TagDTO;
import com.example.foodx_be.enity.Tag;

import java.util.List;
import java.util.UUID;

public interface TagService {
    List<TagDTO> getListTag();

    void addTag(List<TagDTO> tagDTOList);

    Tag getTagEity(UUID id);

    List<TagDTO> convertToListTagDTO(List<Tag> tagList);
}
