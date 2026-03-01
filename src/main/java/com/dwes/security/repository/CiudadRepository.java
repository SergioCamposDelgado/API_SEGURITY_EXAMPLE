package com.dwes.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dwes.security.entities.Ciudad;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    // Aquí ya tienes métodos como save(), findAll(), deleteById(), etc.
}
