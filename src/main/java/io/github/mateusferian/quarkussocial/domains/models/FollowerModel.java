package io.github.mateusferian.quarkussocial.domains.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_follower")
@Data
public class FollowerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "id_follower")
    private UserModel follower;
}
