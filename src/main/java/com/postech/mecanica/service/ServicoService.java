package com.postech.mecanica.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postech.mecanica.entity.Servico;
import com.postech.mecanica.exception.ResourceNotFoundException;
import com.postech.mecanica.mapper.ServicoMapper;
import com.postech.mecanica.model.ServicoDTO;
import com.postech.mecanica.repository.ServicoRepository;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ServicoMapper servicoMapper;

    public ServicoService(ServicoRepository servicoRepository){
        this.servicoRepository = servicoRepository;
        this.servicoMapper = new ServicoMapper();
    }

    public ServicoDTO salvar(ServicoDTO ServicoDTO) {
        Servico Servico = servicoMapper.toEntity(ServicoDTO);
        return servicoMapper.toDTO(servicoRepository.save(Servico));
    }

    public List<ServicoDTO > listarTodos() {
        return servicoRepository.findAll().stream()
                .map(servicoMapper::toDTO)
                .toList();
    }

    public ServicoDTO ServicoPorId(Long id) {
        return servicoRepository.findById(id)
                .map(servicoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com o ID: " + id));
    }

    public void atualizar(Long id, ServicoDTO ServicoDTO) {
        Servico ServicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com o ID: " + id));
     
        if (StringUtils.hasText(ServicoDTO.getNomeServico())) {
            ServicoExistente.setNomeServico(ServicoDTO.getNomeServico());
        }

        if (ServicoDTO.getValorServico() != null) {
            ServicoExistente.setValorServico(ServicoDTO.getValorServico());
        }

        servicoRepository.save(ServicoExistente);
    }

    public void deletar(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo não encontrado com o ID: " + id);
        }
        servicoRepository.deleteById(id);
    }
}
