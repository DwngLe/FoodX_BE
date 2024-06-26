package com.example.foodx_be.repository;

import com.example.foodx_be.enity.BusinessProof;
import com.example.foodx_be.enums.UpdateState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusinessProofRepository extends JpaRepository<BusinessProof, UUID> {
    List<BusinessProof> findAllByUpdateState(UpdateState updateState);
}
