package com.postech.mecanica.mapper;

import org.springframework.stereotype.Component;

import com.postech.mecanica.entity.Cliente;
import com.postech.mecanica.model.ClienteVeiculoRequestDTO;
import com.postech.mecanica.model.ClienteResponseDTO;


@Component
public class ClienteMapper {

    private final VeiculoMapper veiculoMapper;

    public ClienteMapper(VeiculoMapper veiculoMapper) {
        this.veiculoMapper = veiculoMapper;
    }

    
    public Cliente toEntity(ClienteVeiculoRequestDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setCpfCnpj(clienteDTO.getCpfCNpj());
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());
        cliente.setDataDeNascimento(clienteDTO.getDataDeNascimento());
        return cliente;
    }

    
    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getCpfCnpj(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.getDataDeNascimento(),
            veiculoMapper.toDTO(cliente.getVeiculo())  
        );
    }
}