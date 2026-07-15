package com.moontech.archetype.application.mapper;

import com.moontech.archetype.domain.entity.RoleEntity;
import com.moontech.archetype.domain.entity.UserEntity;
import com.moontech.archetype.infrastructure.model.response.LoginResponse;
import com.moontech.archetype.infrastructure.model.response.SecurityResponse;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Mapper for user.
 *
 * @author Felipe Monzón
 * @since 22 jun., 2023
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "id", source = "principal.idUser")
  LoginResponse mapToLoginResponse(SecurityResponse principal);

  @Mapping(target = "id", source = "user.id")
  @Mapping(target = "idUser", source = "user.idUser")
  @Mapping(target = "phone", source = "user.phone")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "firstName", source = "user.firstName")
  @Mapping(target = "lastName", source = "user.lastName")
  @Mapping(target = "genre", source = "user.genre")
  @Mapping(target = "status", source = "user.status")
  @Mapping(target = "enterpriseId", source = "user.enterprise.id")
  @Mapping(target = "enterpriseName", source = "user.enterprise.name")
  @Mapping(target = "displayName", source = "user", qualifiedByName = "joinFullName")
  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "password", source = "user.password")
  SecurityResponse mapToUserResponse(UserDetails userDetails, UserEntity user);

  default SecurityResponse mapToSecurityResponse(UserEntity entity) {
    var user = mapToUser(entity);
    return mapToUserResponse(user, entity);
  }

  default UserDetails mapToUser(UserEntity user) {
    return new User(user.getUsername(), user.getPassword(), getAuthorities(user));
  }

  default Set<? extends GrantedAuthority> getAuthorities(UserEntity retrievedUser) {
    Set<RoleEntity> roles = retrievedUser.getRoles();
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
    return authorities;
  }

  @Named("joinFullName")
  default String joinFullName(UserEntity user) {
    return Optional.ofNullable(user)
        .map(
            us ->
                Stream.of(us.getFirstName(), us.getLastName())
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.trim().isEmpty())
                    .collect(Collectors.joining(StringUtils.SPACE)))
        .orElse(StringUtils.EMPTY);
  }
}
