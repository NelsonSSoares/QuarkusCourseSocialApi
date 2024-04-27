package org.acme.domain.model.dto;

import lombok.Data;
import org.acme.domain.model.Post;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private String text;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post){
        PostResponse postResponse = new PostResponse();
        postResponse.setText(post.getText());
        postResponse.setDateTime(post.getDateTime());
        return postResponse;
    }
}
