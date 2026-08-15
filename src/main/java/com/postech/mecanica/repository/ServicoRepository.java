package com.postech.mecanica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postech.mecanica.entity.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    
}
