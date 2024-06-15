package com.example.foodx_be.mapper;

import com.example.foodx_be.dto.response.TagDTO;
import com.example.foodx_be.enity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(source = "tagName", target = "tagName")
    @Mapping(source = "tagDescription", target = "tagDescription")
    TagDTO toTagDTO(Tag restaurantTag);

    List<TagDTO> toTagDTOList(List<Tag> restaurantTags);
}
