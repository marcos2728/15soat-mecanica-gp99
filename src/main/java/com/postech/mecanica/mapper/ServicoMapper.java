package com.postech.mecanica.mapper;

import com.postech.mecanica.entity.Servico;
import com.postech.mecanica.model.ServicoDTO;

public class ServicoMapper {

    public Servico toEntity(ServicoDTO servicoDTO) {
        Servico servico = new Servico();
        servico.setId(servicoDTO.getId());
        servico.setNomeServico(servicoDTO.getNomeServico());
        servico.setValorServico(servicoDTO.getValorServico());
        return servico;
    }

    public ServicoDTO toDTO(Servico servico) {
        ServicoDTO servicoDTO = new ServicoDTO();
        servicoDTO.setId(servico.getId());
        servicoDTO.setNomeServico(servico.getNomeServico());
        servicoDTO.setValorServico(servico.getValorServico());
        return servicoDTO;
    }
    
}
