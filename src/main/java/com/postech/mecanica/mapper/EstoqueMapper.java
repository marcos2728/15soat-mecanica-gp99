package com.postech.mecanica.mapper;

import org.springframework.stereotype.Component;

import com.postech.mecanica.entity.Estoque;
import com.postech.mecanica.model.EstoqueDTO;

@Component
public class EstoqueMapper {


    public Estoque toEntity(EstoqueDTO dto) {
        Estoque entity = new Estoque();
        entity.setId(dto.getId());
        entity.setNomeItem(dto.getNomeItem());
        entity.setQuantidadeItem(dto.getQuantidadeItem());
        entity.setValorItem(dto.getValorItem());
        return entity;
    }
    
    public EstoqueDTO toDTO(Estoque entity) {
        EstoqueDTO dto = new EstoqueDTO();
        dto.setId(entity.getId());
        dto.setNomeItem(entity.getNomeItem());
        dto.setQuantidadeItem(entity.getQuantidadeItem());
        dto.setValorItem(entity.getValorItem());
        return dto;
    }

}
