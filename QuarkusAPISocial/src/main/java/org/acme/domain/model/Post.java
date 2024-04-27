package org.acme.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist //executado antes de persistir no banco
    public void prePersist() {
        setDateTime(LocalDateTime.now());
    }
}
