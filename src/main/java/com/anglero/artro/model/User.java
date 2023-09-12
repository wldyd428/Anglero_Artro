package com.anglero.artro.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String realname;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Role getRoleAuthority() {
        return role;
    }

}
