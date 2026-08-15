package com.postech.mecanica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postech.mecanica.entity.OrdemServico;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
}