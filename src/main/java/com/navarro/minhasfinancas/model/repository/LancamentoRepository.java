package com.navarro.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navarro.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
