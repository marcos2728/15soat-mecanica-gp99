package com.postech.mecanica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.mecanica.model.ClienteVeiculoRequestDTO;
import com.postech.mecanica.model.ClienteResponseDTO;
import com.postech.mecanica.service.ClienteService;

import jakarta.validation.Valid;

/**
 * Controller REST para gerenciar clientes.
 * 
 * Responsável por expor endpoints para criar, buscar, atualizar e deletar clientes.
 * Todos os clientes devem ter um veículo associado.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Cria um novo cliente com veículo associado.
     *
     * @param clienteDTO dados do cliente e veículo a serem cadastrados
     * @return ResponseEntity com status 201 e dados do cliente salvo
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteVeiculoRequestDTO clienteDTO) {
        ClienteResponseDTO clienteSalvo = clienteService.cadastrarComVeiculo(clienteDTO);
        return ResponseEntity.status(201).body(clienteSalvo);
    }

    /**
     * Busca um cliente pelo CPF ou CNPJ.
     *
     * @param cpfCnpj o CPF ou CNPJ do cliente a ser buscado
     * @return ResponseEntity com dados do cliente encontrado
     */
    @GetMapping("/{cpfCnpj}")
    public ResponseEntity<ClienteResponseDTO> buscarPorCpfCnpj(@PathVariable String cpfCnpj) {
        return ResponseEntity.ok(clienteService.buscarPorCpfCnpj(cpfCnpj));
    }

    /**
     * Atualiza um cliente existente.
     *
     * @param id identificador único do cliente a ser atualizado
     * @param clienteDTO novos dados do cliente e veículo
     * @return ResponseEntity com mensagem de sucesso
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteVeiculoRequestDTO clienteDTO) {
        clienteService.atualizar(id, clienteDTO);
        return ResponseEntity.ok("Cliente atualizado com sucesso");
    }

    /**
     * Deleta um cliente.
     *
     * @param id identificador único do cliente a ser deletado
     * @return ResponseEntity com status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
