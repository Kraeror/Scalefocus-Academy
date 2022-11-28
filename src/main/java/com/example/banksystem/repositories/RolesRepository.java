package com.example.banksystem.repositories;

import com.example.banksystem.models.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<RoleEntity, Long> {
	/**
	 * Gets an optional object of {@link RoleEntity} that is assigned to the given role name.
	 * @param role the name of the role that the role is assigned to.
	 * @return a {@link RoleEntity} with the given role name from the repository.
	 */
	Optional<RoleEntity> findByRole(String role);
}
