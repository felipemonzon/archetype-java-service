package com.moontech.archetype.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moontech.archetype.commons.enums.Status;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidad para empresas.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Getter
@Setter
@Entity
@ToString
@Table(name = "enterprises")
@EntityListeners(AuditingEntityListener.class)
public class EnterpriseEntity extends AuditableEntity implements Serializable {
  /** Serial. */
  @Serial private static final long serialVersionUID = -8089684905670011173L;

  /** Identificador de la empresa. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Identificador alfanumérico de la empresa. */
  @Column(name = "id_enterprise", length = 20, nullable = false)
  private String idEnterprise;

  /** Nombre de la empresa. */
  @Column(name = "name", nullable = false, length = 200)
  private String name;

  /** Dirección de la empresa. */
  @Column(name = "address", nullable = false, length = 100)
  private String address;

  /** Teléfono de la empresa. */
  @Column(name = "phone", nullable = false, length = 10)
  private String phone;

  /** Status de la empresa. */
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, columnDefinition = "varchar")
  private Status active;

  /** Encargado de la empresa. */
  @Column(name = "manager", nullable = false, length = 100)
  private String manager;

  /** RFC de la empresa. */
  @Column(name = "rfc", nullable = false, length = 20)
  private String rfc;

  /** Usuarios pertenecientes a la empresa. */
  @JsonIgnore
  @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL)
  @ToString.Exclude
  private Set<UserEntity> users;
}
