package io.github.mateusferian.quarkussocial.rests.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class FollowerPerUserResponseDTO {

    private Integer followersCount;

    private List<FollowerResponseDTO> content;
}
