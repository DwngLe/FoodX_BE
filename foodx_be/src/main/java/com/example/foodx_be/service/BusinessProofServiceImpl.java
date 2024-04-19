package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddBusinessProofCommand;
import com.example.foodx_be.dto.BusinessProofDTO;
import com.example.foodx_be.enity.BusinessProof;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.NoResultsFoundException;
import com.example.foodx_be.repository.BusinessProofRepository;
import com.example.foodx_be.ulti.UpdateState;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class BusinessProofServiceImpl implements BusinessProofService {
    private UserService userService;
    private RestaurantService restaurantService;
    private CloudiaryService cloudiaryService;

    private BusinessProofRepository businessProofRepository;

    private final String FOLDER_UPLOAD = "Business Proof";

    @Override
    public void addBusinessProof(AddBusinessProofCommand addBusinessProofCommand, MultipartFile multipartFile) throws IOException {
        User userOwner = userService.getUser(addBusinessProofCommand.getIdUser());
        Restaurant restaurant = restaurantService.getRestaurantEnity(addBusinessProofCommand.getIdRestaurant());

        Map result = cloudiaryService.uploadFile(multipartFile, FOLDER_UPLOAD);

        BusinessProof businessProof = new BusinessProof();
        businessProof.setUserOwner(userOwner);
        businessProof.setRestaurant(restaurant);
        businessProof.setUpdateState(UpdateState.PENDING);
        businessProof.setOwnerRole(addBusinessProofCommand.getOwnerRole());
        businessProof.setBusinessProofUrl((String) result.get("url"));

        businessProofRepository.save(businessProof);

    }

    @Override
    public void reviewBusinessProof(UUID idBusinessProof, UpdateState updateState) {
        Optional<BusinessProof> businessProofOptional = businessProofRepository.findById(idBusinessProof);
        BusinessProof businessProof = unwrapBusinessProof(businessProofOptional);
        if(updateState.equals(UpdateState.ACCEPTED)){
            Restaurant restaurant = restaurantService.getRestaurantEnity(businessProof.getRestaurant().getId());
            restaurant.setHasAnOwner(true);
            restaurantService.saveRestaurantEnity(restaurant);
        }
        businessProof.setUpdateState(updateState);
        businessProofRepository.save(businessProof);
    }

    @Override
    public BusinessProofDTO getBusinessProof(UUID idBusinessProof) {
        Optional<BusinessProof> businessProofOptional = businessProofRepository.findById(idBusinessProof);
        BusinessProof businessProof = unwrapBusinessProof(businessProofOptional);
        return convertToBusinessProofDTO(businessProof);
    }

    @Override
    public Page<BusinessProofDTO> getListBusinessProofByState(int pageNo, int limit, UpdateState state) {
        List<BusinessProof> businessProofList = businessProofRepository.findAllByUpdateState(state);
        if (businessProofList.isEmpty()) {
            throw new NoResultsFoundException();
        }
        return convertListBusinessProofToPage(businessProofList, pageNo, limit);
    }


    public Page<BusinessProofDTO> convertListBusinessProofToPage(List<BusinessProof> businessProofList, int pageNo, int limit) {
        List<BusinessProofDTO> businessProofDTOList = new ArrayList<>();
        for (BusinessProof businessProof : businessProofList) {
            businessProofDTOList.add(convertToBusinessProofDTO(businessProof));
        }
        Pageable pageable = PageRequest.of(pageNo, limit);

        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) Math.min(pageable.getOffset() + pageable.getPageSize(), businessProofDTOList.size());
        List<BusinessProofDTO> subList = businessProofDTOList.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, businessProofDTOList.size());
    }

    static BusinessProof unwrapBusinessProof(Optional<BusinessProof> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new NoResultsFoundException();
    }

    @Override
    public BusinessProof convertToBusinessProofEnity(BusinessProofDTO businessProofDTO) {
        return null;
    }

    public BusinessProofDTO convertToBusinessProofDTO(BusinessProof businessProof) {
        BusinessProofDTO.BusinessProofDTOBuilder builder = BusinessProofDTO.builder()
                .id(businessProof.getId())
                .ownerRole(businessProof.getOwnerRole())
                .businessProofUrl(businessProof.getBusinessProofUrl())
                .updateState(businessProof.getUpdateState())
                .idRestaurant(businessProof.getRestaurant().getId())
                .restaurantName(businessProof.getRestaurant().getRestaurantName());

        if (businessProof.getUserOwner() != null) {
            builder.userOwner(userService.convertToDTO(businessProof.getUserOwner()));
        }
        return builder.build();
    }

}
