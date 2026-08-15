package com.postech.mecanica.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.postech.mecanica.entity.Estoque;
import com.postech.mecanica.exception.ResourceNotFoundException;
import com.postech.mecanica.mapper.EstoqueMapper;
import com.postech.mecanica.model.EstoqueDTO;
import com.postech.mecanica.repository.EstoqueRepository;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final EstoqueMapper estoqueMapper;
    private final OrdemServicoService ordemServicoService;

    public EstoqueService(EstoqueRepository estoqueRepository,
            EstoqueMapper estoqueMapper,
            OrdemServicoService ordemServicoService) {
        this.estoqueRepository = estoqueRepository;
        this.estoqueMapper = estoqueMapper;
        this.ordemServicoService = ordemServicoService;
    }

    public EstoqueDTO salvar(EstoqueDTO estoqueDTO) {
        Estoque estoque = estoqueMapper.toEntity(estoqueDTO);
        return estoqueMapper.toDTO(estoqueRepository.save(estoque));
    }

    public List<EstoqueDTO> listarTodos() {
        return estoqueRepository.findAll().stream()
                .map(estoqueMapper::toDTO)
                .toList();
    }

    public EstoqueDTO consultarItemId(Long id) {
        return estoqueRepository.findById(id)
                .map(estoqueMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o ID: " + id));
    }

    public void atualizar(Long id, EstoqueDTO estoqueDTO) {
        Estoque itemEstoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o ID: " + id));

        itemEstoque.setNomeItem(estoqueDTO.getNomeItem());
        itemEstoque.setValorItem(estoqueDTO.getValorItem());
        itemEstoque.setQuantidadeItem(estoqueDTO.getQuantidadeItem());

        // reconcilia ordens de serviço pendentes ANTES de salvar, já que isso
        // pode reduzir a quantidade de novo (reservando peça pras OS pendentes)
        ordemServicoService.reconciliarPecasPendentes(itemEstoque);

        estoqueRepository.save(itemEstoque);
    }

    public void deletar(Long id) {
        if (!estoqueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item não encontrado com o ID: " + id);
        }
        estoqueRepository.deleteById(id);
    }
}