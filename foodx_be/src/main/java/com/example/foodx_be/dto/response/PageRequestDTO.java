package com.example.foodx_be.dto.response;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

@Getter
@Setter
public class PageRequestDTO {
    @Size(min = 0, message = "INVALID_PAGE_NUMBER")
    private Integer pageNo = 0;
    @Size(min = 5, message = "INVALID_PAGE_SIZE")
    private Integer pageSize = 5;

    public Pageable getPageable(PageRequestDTO dto) {
        Integer page = Objects.nonNull(dto.getPageNo()) && dto.getPageNo() > -1 ? dto.getPageNo() : this.pageNo;
        Integer size = Objects.nonNull(dto.getPageSize()) && dto.getPageSize() > 0 ? dto.getPageSize() : this.pageSize;
        PageRequest request = PageRequest.of(page, size);

        return request;
    }
}
