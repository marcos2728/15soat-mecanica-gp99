package com.postech.mecanica.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postech.mecanica.entity.Cliente;
import com.postech.mecanica.entity.Veiculo;
import com.postech.mecanica.exception.ResourceNotFoundException;
import com.postech.mecanica.mapper.ClienteMapper;
import com.postech.mecanica.mapper.VeiculoMapper;
import com.postech.mecanica.model.ClienteVeiculoRequestDTO;
import com.postech.mecanica.model.ClienteResponseDTO;
import com.postech.mecanica.repository.ClienteRepository;
import com.postech.mecanica.repository.VeiculoRepository;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final VeiculoRepository veiculoRepository;
    private final VeiculoMapper veiculoMapper;

    public ClienteService(ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository,
            ClienteMapper clienteMapper,
            VeiculoMapper veiculoMapper) {
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.clienteMapper = clienteMapper;
        this.veiculoMapper = veiculoMapper;
    }

    
    public ClienteResponseDTO cadastrarComVeiculo(ClienteVeiculoRequestDTO dto) {
        Veiculo veiculo = veiculoMapper.toEntity(dto.getVeiculo());
        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);

        Cliente cliente = clienteMapper.toEntity(dto);
        cliente.setVeiculo(veiculoSalvo);

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(clienteSalvo);
    }

    
    public ClienteResponseDTO buscarPorCpfCnpj(String cpfCnpj) {
        Cliente cliente = clienteRepository.findByCpfCnpjComVeiculo(cpfCnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + cpfCnpj));
        return clienteMapper.toResponseDTO(cliente);
    }

    
    public void atualizar(Long id, ClienteVeiculoRequestDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + id));

        // Atualiza apenas os campos que foram fornecidos
        if (StringUtils.hasText(clienteDTO.getCpfCNpj())) {
            clienteExistente.setCpfCnpj(clienteDTO.getCpfCNpj());
        }
        if (StringUtils.hasText(clienteDTO.getNome())) {
            clienteExistente.setNome(clienteDTO.getNome());
        }
        if (StringUtils.hasText(clienteDTO.getEmail())) {
            clienteExistente.setEmail(clienteDTO.getEmail());
        }
        if (StringUtils.hasText(clienteDTO.getTelefone())) {
            clienteExistente.setTelefone(clienteDTO.getTelefone());
        }
        if (StringUtils.hasText(clienteDTO.getDataDeNascimento())) {
            clienteExistente.setDataDeNascimento(clienteDTO.getDataDeNascimento());
        }

        clienteRepository.save(clienteExistente);
    }

    
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com o ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
