package com.moontech.archetype.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moontech.archetype.commons.enums.Genre;
import com.moontech.archetype.commons.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidad para usuario.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Getter
@Setter
@ToString
@Entity(name = "users")
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends AuditableEntity {
  /** Identificador del usuario. */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /** Identificador del usuario. */
  @Column(name = "id_user", nullable = false, length = 20)
  private String idUser;

  /** Propiedad para el nombre del usuario. */
  @NaturalId
  @Column(name = "username", nullable = false, length = 20, unique = true)
  private String username;

  /** Propiedad para la contraseña. */
  @Column(name = "password", nullable = false, length = 200)
  private String password;

  /** Propiedad primer nombre. */
  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  /** Propiedad segundo nombre. */
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  /** Propiedad para el correo. */
  @Column(name = "email", nullable = false, length = 100)
  private String email;

  /** Propiedad para el phone. */
  @Column(name = "phone", nullable = false, length = 10)
  private String phone;

  /** Propiedad para el género. */
  @Enumerated(EnumType.STRING)
  private Genre genre;

  /** Propiedad para el status del usuario. */
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, columnDefinition = "varchar")
  private Status status;

  /** Propiedad para el rol del usuario. */
  @ManyToMany
  @ToString.Exclude
  @EqualsAndHashCode.Include
  @JoinTable(
      name = "user_roles",
      joinColumns = {@JoinColumn(name = "id_user")},
      inverseJoinColumns = {@JoinColumn(name = "id_role")})
  @Column(name = "id_role", nullable = false)
  private Set<RoleEntity> roles;

  /** Empresa del empleado. */
  @JsonIgnore
  @ManyToOne
  @JoinColumn(
      name = "id_enterprise",
      referencedColumnName = "id")
  private EnterpriseEntity enterprise;
}
