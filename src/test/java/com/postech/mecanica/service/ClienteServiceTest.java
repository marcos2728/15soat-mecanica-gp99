package com.postech.mecanica.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.mecanica.entity.Cliente;
import com.postech.mecanica.entity.Veiculo;
import com.postech.mecanica.mapper.ClienteMapper;
import com.postech.mecanica.mapper.VeiculoMapper;
import com.postech.mecanica.model.ClienteResponseDTO;
import com.postech.mecanica.model.ClienteVeiculoRequestDTO;
import com.postech.mecanica.model.VeiculoDTO;
import com.postech.mecanica.repository.ClienteRepository;
import com.postech.mecanica.repository.VeiculoRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private VeiculoMapper veiculoMapper;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void cadastrarComVeiculo_deveSalvarClienteEVeiculo() {
        ClienteVeiculoRequestDTO dto = new ClienteVeiculoRequestDTO();
        dto.setCpfCNpj("12345678909");
        dto.setNome("Maria Silva");
        dto.setEmail("maria@email.com");
        dto.setTelefone("11999999999");
        dto.setDataDeNascimento("1990-01-01");

        VeiculoDTO veiculoDTO = new VeiculoDTO();
        veiculoDTO.setPlaca("ABC1234");
        veiculoDTO.setModelo("Civic");
        veiculoDTO.setMarca("Honda");
        veiculoDTO.setAnoFabricacao(2020);
        dto.setVeiculo(veiculoDTO);

        Veiculo veiculo = new Veiculo();
        veiculo.setId(10L);
        veiculo.setPlaca("ABC1234");

        Cliente cliente = new Cliente();
        cliente.setId(8L);
        cliente.setCpfCnpj("12345678909");
        cliente.setNome("Maria Silva");
        cliente.setVeiculo(veiculo);

        ClienteResponseDTO esperado = new ClienteResponseDTO();
        esperado.setId(8L);
        esperado.setNome("Maria Silva");
        esperado.setCpfCnpj("12345678909");
        esperado.setVeiculo(veiculoDTO);

        when(veiculoMapper.toEntity(dto.getVeiculo())).thenReturn(veiculo);
        when(veiculoRepository.save(veiculo)).thenReturn(veiculo);
        when(clienteMapper.toEntity(dto)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toResponseDTO(cliente)).thenReturn(esperado);

        ClienteResponseDTO resultado = clienteService.cadastrarComVeiculo(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(veiculoRepository).save(veiculo);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void buscarPorCpfCnpj_deveRetornarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setCpfCnpj("98765432100");
        cliente.setNome("João");

        ClienteResponseDTO esperado = new ClienteResponseDTO();
        esperado.setId(2L);
        esperado.setNome("João");
        esperado.setCpfCnpj("98765432100");

        when(clienteRepository.findByCpfCnpjComVeiculo("98765432100")).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponseDTO(cliente)).thenReturn(esperado);

        ClienteResponseDTO resultado = clienteService.buscarPorCpfCnpj("98765432100");

        assertThat(resultado).isEqualTo(esperado);
        verify(clienteRepository).findByCpfCnpjComVeiculo("98765432100");
    }

    @Test
    void atualizar_deveAlterarDadosDoCliente() {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(5L);
        clienteExistente.setCpfCnpj("11111111111");
        clienteExistente.setNome("Velho");
        clienteExistente.setEmail("velho@email.com");

        ClienteVeiculoRequestDTO dto = new ClienteVeiculoRequestDTO();
        dto.setNome("Novo Nome");
        dto.setEmail("novo@email.com");

        when(clienteRepository.findById(5L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(clienteExistente)).thenReturn(clienteExistente);

        clienteService.atualizar(5L, dto);

        assertThat(clienteExistente.getNome()).isEqualTo("Novo Nome");
        assertThat(clienteExistente.getEmail()).isEqualTo("novo@email.com");
        verify(clienteRepository).save(clienteExistente);
    }
}
