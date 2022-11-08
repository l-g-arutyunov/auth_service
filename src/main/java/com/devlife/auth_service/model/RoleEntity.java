package com.devlife.auth_service.model;

import com.devlife.auth_service.enums.RoleEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "auth_role")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class RoleEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_role_id_seq")
    @SequenceGenerator(name = "auth_user_id_seq", sequenceName = "auth_user_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
