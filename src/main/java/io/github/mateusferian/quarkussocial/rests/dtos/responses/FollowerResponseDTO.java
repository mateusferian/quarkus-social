package io.github.mateusferian.quarkussocial.rests.dtos.responses;

import io.github.mateusferian.quarkussocial.domains.models.FollowerModel;
import lombok.Data;

@Data
public class FollowerResponseDTO {

    private Long id;

    private String name;

    public FollowerResponseDTO() {
    }

    public FollowerResponseDTO(FollowerModel follower) {
        this(follower.getId(), follower.getFollower().getName());
    }

    public FollowerResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
