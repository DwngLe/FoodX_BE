package com.example.foodx_be.enity;

import com.example.foodx_be.ulti.OwnerRole;
import com.example.foodx_be.ulti.UpdateState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BusinessProof {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(name = "owner_role")
    @Enumerated(EnumType.STRING)
    private OwnerRole ownerRole;
    @Column(name = "business_proof_url")
    private String businessProofUrl;
    @Column(name = "update_state")
    @Enumerated(EnumType.STRING)
    private UpdateState updateState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_owner", referencedColumnName = "id")
    private User userOwner;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_restaurant", referencedColumnName = "id")
    private Restaurant restaurant;

}
