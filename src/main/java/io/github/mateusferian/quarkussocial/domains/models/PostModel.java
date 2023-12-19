package io.github.mateusferian.quarkussocial.domains.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="post_text")
    private String text;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @PrePersist
    public void prePersist(){
        setDateTime(LocalDateTime.now());
    }
}
