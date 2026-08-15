package com.postech.mecanica.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postech.mecanica.entity.Veiculo;
import com.postech.mecanica.exception.ResourceNotFoundException;
import com.postech.mecanica.mapper.VeiculoMapper;
import com.postech.mecanica.model.VeiculoDTO;
import com.postech.mecanica.repository.VeiculoRepository;

@Service
public class VeiculoService {
    
    private final VeiculoRepository veiculoRepository;
    private final VeiculoMapper veiculoMapper;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = new VeiculoMapper();
    }

        public VeiculoDTO salvar(VeiculoDTO veiculoDTO) {
            Veiculo veiculo = veiculoMapper.toEntity(veiculoDTO);
            return veiculoMapper.toDTO(veiculoRepository.save(veiculo));
    }

    public List<VeiculoDTO > listarTodos() {
        return veiculoRepository.findAll().stream()
                .map(veiculoMapper::toDTO)
                .toList();
    }

    public VeiculoDTO veiculoPorId(Long id) {
        return veiculoRepository.findById(id)
                .map(veiculoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com o ID: " + id));
    }

    public void atualizar(Long id, VeiculoDTO veiculoDTO) {
        Veiculo veiculoExistente = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com o ID: " + id));

        if (StringUtils.hasText(veiculoDTO.getMarca())) {
            veiculoExistente.setMarca(veiculoDTO.getMarca());
        }
        if (StringUtils.hasText(veiculoDTO.getModelo())) {
            veiculoExistente.setModelo(veiculoDTO.getModelo());
        }
        if (veiculoDTO.getAnoFabricacao() != null) {
            veiculoExistente.setAnoFabricacao(veiculoDTO.getAnoFabricacao());
        }
        if (StringUtils.hasText(veiculoDTO.getPlaca())) {
            veiculoExistente.setPlaca(veiculoDTO.getPlaca());
        }

        veiculoRepository.save(veiculoExistente);
    }

    public void deletar(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo não encontrado com o ID: " + id);
        }
        veiculoRepository.deleteById(id);
    }
}