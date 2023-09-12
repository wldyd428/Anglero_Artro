package com.anglero.artro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String username;

    private Role role;

    public UserDTO (User user) {
        this.username = user.getUsername();
        this.role = user.getRoleAuthority();
    }

}
