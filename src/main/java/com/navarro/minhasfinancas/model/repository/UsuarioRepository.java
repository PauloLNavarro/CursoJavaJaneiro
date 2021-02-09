package com.navarro.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navarro.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	boolean existsByEmail (String email);
	
	Optional<Usuario> findByEmail(String email); //Em Spring Data essa declaração é uma query methods (não precisa especificar a query para consulta ao banco de dados)
}