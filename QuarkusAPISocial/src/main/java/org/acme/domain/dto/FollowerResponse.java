package org.acme.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.acme.domain.entities.Follower;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowerResponse {

    private Long id;

    private String name;

    public FollowerResponse(Follower follower){
        this(follower.getId(), follower.getFollower().getName());
    }
}
