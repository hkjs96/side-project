package com.sideproject.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String token;
    private String name;
    private String email;
    private String password;
    private String id;
    private Boolean terms;
}