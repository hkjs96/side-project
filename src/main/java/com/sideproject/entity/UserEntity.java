package com.sideproject.entity;

import com.sideproject.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USERS")
public class UserEntity {

    @Id
    private String id;
    private String name;
    private String password;
    private String email;
    private Boolean terms;
}
