package com.example.foodx_be.service;

import com.example.foodx_be.dto.response.TagDTO;
import com.example.foodx_be.enity.Tag;
import com.example.foodx_be.exception.AppException;
import com.example.foodx_be.exception.ErrorCode;
import com.example.foodx_be.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService{
    private TagRepository tagRepository;
    @Override
    public List<TagDTO> getListTag() {
        return convertToListTagDTO(tagRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void addTag(List<TagDTO> tagDTOList) {
        for(TagDTO tagDTO : tagDTOList){
            tagRepository.save(convertToTagEnity(tagDTO));
        }
    }

    @Override
    public Tag getTagEity(UUID id) {
        return unwrapUser(tagRepository.findById(id));
    }

    @Override
    public List<TagDTO> convertToListTagDTO(List<Tag> tagList) {
        List<TagDTO> tagDTOList = new ArrayList<>();
        for(Tag tag : tagList){
            tagDTOList.add(convertToTagDTO(tag));
        }
        return tagDTOList;
    }



    public TagDTO convertToTagDTO(Tag tag){
        return  TagDTO.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .tagDescription(tag.getTagDescription())
                .build();
    }

    public Tag convertToTagEnity(TagDTO tagDTO){
        return Tag.builder()
                .id(tagDTO.getId())
                .tagName(tagDTO.getTagName())
                .tagDescription(tagDTO.getTagDescription())
                .build();
    }

    static Tag unwrapUser(Optional<Tag> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new AppException(ErrorCode.TAG_NOT_EXISTED);
    }}
