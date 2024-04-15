package com.example.foodx_be.enity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private String content;
    @NotBlank
    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_sent", referencedColumnName = "id")
    private User userSent;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_recive", referencedColumnName = "id")
    private User userRecvie;
}
