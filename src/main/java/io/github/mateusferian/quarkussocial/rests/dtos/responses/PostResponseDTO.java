package io.github.mateusferian.quarkussocial.rests.dtos.responses;

import io.github.mateusferian.quarkussocial.domains.models.PostModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDTO {

    private String text;

    private LocalDateTime dateTime;

    public static PostResponseDTO fromEntity(PostModel post){
        var response = new PostResponseDTO();
        response.setText(post.getText());
        response.setDateTime(post.getDateTime());

        return response;
    }
}
