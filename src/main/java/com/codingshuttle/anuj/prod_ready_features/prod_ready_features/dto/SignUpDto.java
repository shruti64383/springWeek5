package com.codingshuttle.anuj.prod_ready_features.prod_ready_features.dto;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.enums.Roles;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {
    private String name;
    private String email;
    private String password;
    private Set<Roles> roles;
}
