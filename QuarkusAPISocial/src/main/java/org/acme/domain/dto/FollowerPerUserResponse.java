package org.acme.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
public class FollowerPerUserResponse {

    private Integer followersCount;
    private List<FollowerResponse> content;


}
