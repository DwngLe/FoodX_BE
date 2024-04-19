package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddBusinessProofCommand;
import com.example.foodx_be.dto.BusinessProofDTO;
import com.example.foodx_be.dto.ReviewUpdate;
import com.example.foodx_be.enity.BusinessProof;
import com.example.foodx_be.ulti.UpdateState;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BusinessProofService {
    void addBusinessProof(AddBusinessProofCommand addBusinessProofCommand, MultipartFile multipartFile) throws IOException;
    Page<BusinessProofDTO> getListBusinessProofByState(int pageNo, int limit, UpdateState state);

    BusinessProof convertToBusinessProofEnity(BusinessProofDTO businessProofDTO);
}
