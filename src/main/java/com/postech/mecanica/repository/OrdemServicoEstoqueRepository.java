package com.postech.mecanica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.postech.mecanica.entity.OrdemServicoEstoque;

public interface OrdemServicoEstoqueRepository extends JpaRepository<OrdemServicoEstoque, Long> {

    @Query("SELECT ose FROM OrdemServicoEstoque ose " +
           "WHERE ose.estoque.id = :estoqueId AND ose.disponivel = false " +
           "ORDER BY ose.ordemServico.dataHoraCriacao ASC")
    List<OrdemServicoEstoque> findPendentesPorEstoqueOrdenadoPorAntiguidade(@Param("estoqueId") Long estoqueId);
}