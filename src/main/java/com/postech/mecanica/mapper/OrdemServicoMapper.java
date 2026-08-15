package com.postech.mecanica.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.postech.mecanica.entity.OrdemServico;
import com.postech.mecanica.model.ItemEstoqueResponseDTO;
import com.postech.mecanica.model.ItemServicoResponseDTO;
import com.postech.mecanica.model.OrdemServicoResponseDTO;

@Component
public class OrdemServicoMapper {


    public OrdemServicoResponseDTO toResponseDTO(OrdemServico ordemServico) {
        OrdemServicoResponseDTO dto = new OrdemServicoResponseDTO();
        dto.setId(ordemServico.getId());
        dto.setVeiculoId(ordemServico.getVeiculo().getId());
        dto.setPlacaVeiculo(ordemServico.getVeiculo().getPlaca());
        dto.setStatus(ordemServico.getStatus());
        dto.setValorTotal(ordemServico.getValorTotal());
        dto.setDataHoraCriacao(ordemServico.getDataHoraCriacao());
        dto.setDataHoraAtualizacao(ordemServico.getDataHoraAtualizacao());

        List<ItemEstoqueResponseDTO> pecas = ordemServico.getPecas().stream()
                .map(item -> {
                    ItemEstoqueResponseDTO i = new ItemEstoqueResponseDTO();
                    i.setEstoqueId(item.getEstoque().getId());
                    i.setNomePeca(item.getEstoque().getNomeItem());
                    i.setQuantidade(item.getQuantidade());
                    i.setValorUnitario(item.getValorUnitario());
                    i.setDisponivel(item.getDisponivel());
                    return i;
                }).collect(Collectors.toList());

        List<ItemServicoResponseDTO> servicos = ordemServico.getServicos().stream()
                .map(item -> {
                    ItemServicoResponseDTO s = new ItemServicoResponseDTO();
                    s.setServicoId(item.getServico().getId());
                    s.setNomeServico(item.getServico().getNomeServico());
                    s.setValorAplicado(item.getValorAplicado());
                    return s;
                }).collect(Collectors.toList());

        dto.setPecas(pecas);
        dto.setServicos(servicos);

        return dto;
    }
}