package com.example.foodx_be.dto;

import com.example.foodx_be.ulti.GlobalOperator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
public class RequestDTO {
    private List<SearchRequestDTO> searchRequestDTO;
    private GlobalOperator globalOperator;
    private PageRequestDTO pageRequestDTO;
    private Sort.Direction sort = Sort.Direction.ASC;
    private String sortByColumn = "id";
}
