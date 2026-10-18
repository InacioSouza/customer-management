package br.com.system.i5.customer_management.model;

import br.com.system.i5.customer_management.model.api.AuditInformation;
import br.com.system.i5.customer_management.model.enuns.UserProfileEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private UserProfileEnum profile;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Version
    private Long version;

}
