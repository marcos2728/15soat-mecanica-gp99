package com.postech.mecanica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postech.mecanica.entity.OrdemServicoStatusHistorico;
import org.springframework.data.jpa.repository.Query;

public interface OrdemServicoStatusHistoricoRepository extends JpaRepository<OrdemServicoStatusHistorico, Long> {

    @Query("SELECT o FROM OrdemServicoStatusHistorico o WHERE o.ordemServico.id = :ordemServicoId AND o.dataHoraFim IS NULL")
    Optional<OrdemServicoStatusHistorico> findByOrdemServicoIdAndDataHoraFimIsNull(Long ordemServicoId);

    @Query("SELECT o FROM OrdemServicoStatusHistorico o WHERE o.ordemServico.id = :ordemServicoId ORDER BY o.dataHoraInicio asc")
    List<OrdemServicoStatusHistorico> findByOrdemServicoIdOrderByDataHoraInicioAsc(Long ordemServicoId);

    @Query("SELECT o FROM OrdemServicoStatusHistorico o WHERE o.dataHoraFim IS NOT NULL")
    List<OrdemServicoStatusHistorico> findByDataHoraFimIsNotNull();
}