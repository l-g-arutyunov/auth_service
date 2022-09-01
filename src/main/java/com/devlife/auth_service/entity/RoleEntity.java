package com.devlife.auth_service.entity;

import com.devlife.auth_service.enums.RoleEnum;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "auth_role")
@Data
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
}
