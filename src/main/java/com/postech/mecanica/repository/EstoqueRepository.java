package com.postech.mecanica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postech.mecanica.entity.Estoque;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    
}
