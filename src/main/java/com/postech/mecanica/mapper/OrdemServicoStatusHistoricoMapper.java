package com.postech.mecanica.mapper;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.postech.mecanica.entity.OrdemServicoStatusHistorico;
import com.postech.mecanica.entity.StatusOrdemServico;
import com.postech.mecanica.model.TempoMedioPorFaseResponseDTO;
import com.postech.mecanica.model.TempoPorFaseResponseDTO;

@Component
public class OrdemServicoStatusHistoricoMapper {

    public TempoPorFaseResponseDTO toResponseDTO(OrdemServicoStatusHistorico historico) {
        TempoPorFaseResponseDTO dto = new TempoPorFaseResponseDTO();
        dto.setStatus(historico.getStatus());
        dto.setDataHoraInicio(historico.getDataHoraInicio());
        dto.setDataHoraFim(historico.getDataHoraFim());

        boolean emAndamento = historico.getDataHoraFim() == null;
        LocalDateTime fimOuAgora = emAndamento ? LocalDateTime.now() : historico.getDataHoraFim();

        dto.setTempoMinutos(Duration.between(historico.getDataHoraInicio(), fimOuAgora).toMinutes());
        dto.setEmAndamento(emAndamento);

        return dto;
    }

    public TempoMedioPorFaseResponseDTO toMediaResponseDTO(StatusOrdemServico status, long tempoMedioMinutos,
            int quantidadeAmostras) {
        return new TempoMedioPorFaseResponseDTO(status, tempoMedioMinutos, quantidadeAmostras);
    }
}