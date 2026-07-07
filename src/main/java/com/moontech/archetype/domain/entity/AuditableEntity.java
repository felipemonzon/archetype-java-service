package com.moontech.archetype.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Audit entity auditora.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity implements Serializable {
  /** serial. */
  @Serial private static final long serialVersionUID = -7937775898702198567L;

  /** Usuario que crea el registro. */
  @CreatedBy
  @Column(name = "created_user", updatable = false, nullable = false)
  protected String createdBy;

  /** Fecha de creación. */
  @CreatedDate
  @Column(name = "created_date", updatable = false, nullable = false)
  protected LocalDateTime createdDate;

  /** Usuario que modifica el registro. */
  @LastModifiedBy
  @Column(name = "last_modified_user", nullable = false)
  protected String lastModifiedBy;

  /** Fecha que modifica. */
  @LastModifiedDate
  @Column(name = "last_modified_date", nullable = false)
  protected LocalDateTime lastModifiedDate;
}
