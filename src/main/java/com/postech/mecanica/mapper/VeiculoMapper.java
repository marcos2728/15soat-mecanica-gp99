package com.postech.mecanica.mapper;

import com.postech.mecanica.entity.Veiculo;
import com.postech.mecanica.model.VeiculoDTO;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    public Veiculo toEntity(VeiculoDTO dto) {
        Veiculo entity = new Veiculo();
        entity.setId(dto.getId());
        entity.setPlaca(dto.getPlaca());
        entity.setModelo(dto.getModelo());
        entity.setMarca(dto.getMarca());
        entity.setAnoFabricacao(dto.getAnoFabricacao());
        return entity;
    }
    public VeiculoDTO toDTO(Veiculo veiculo) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(veiculo.getId());
        dto.setPlaca(veiculo.getPlaca());
        dto.setModelo(veiculo.getModelo());
        dto.setMarca(veiculo.getMarca());
        dto.setAnoFabricacao(veiculo.getAnoFabricacao());
        return dto;
    }

}