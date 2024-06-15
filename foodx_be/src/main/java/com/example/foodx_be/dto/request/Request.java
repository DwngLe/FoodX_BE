package com.example.foodx_be.dto.request;

import com.example.foodx_be.dto.response.PageRequestDTO;
import com.example.foodx_be.dto.response.SearchRequestDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
public class Request {
    private List<SearchRequestDTO> searchRequestDTO;
    private PageRequestDTO pageRequestDTO;
    private Sort.Direction sort = Sort.Direction.ASC;
    private String sortByColumn = "id";
}
