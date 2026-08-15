package com.postech.mecanica.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import com.postech.mecanica.entity.Veiculo;
import com.postech.mecanica.mapper.VeiculoMapper;
import com.postech.mecanica.model.VeiculoDTO;
import com.postech.mecanica.repository.VeiculoRepository;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    private VeiculoService veiculoService;

    @BeforeEach
    void setUp() {
        veiculoService = new VeiculoService(veiculoRepository);
    }

    @Test
    void salvar_deveSalvarVeiculo() {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setPlaca("ABC1234");
        dto.setModelo("Civic");
        dto.setMarca("Honda");
        dto.setAnoFabricacao(2022);

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca("ABC1234");
        veiculo.setModelo("Civic");
        veiculo.setMarca("Honda");
        veiculo.setAnoFabricacao(2022);

        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        VeiculoDTO resultado = veiculoService.salvar(dto);

        assertThat(resultado.getPlaca()).isEqualTo("ABC1234");
        assertThat(resultado.getModelo()).isEqualTo("Civic");
        verify(veiculoRepository).save(any(Veiculo.class));
    }

    @Test
    void listarTodos_deveRetornarLista() {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(2L);
        veiculo.setPlaca("XYZ9876");
        veiculo.setModelo("Corolla");
        veiculo.setMarca("Toyota");
        veiculo.setAnoFabricacao(2021);

        when(veiculoRepository.findAll()).thenReturn(List.of(veiculo));

        List<VeiculoDTO> resultado = veiculoService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPlaca()).isEqualTo("XYZ9876");
    }

    @Test
    void atualizar_deveAlterarDadosVeiculo() {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(5L);
        veiculo.setPlaca("OLD1234");
        veiculo.setModelo("Gol");
        veiculo.setMarca("VW");
        veiculo.setAnoFabricacao(2010);

        VeiculoDTO dto = new VeiculoDTO();
        dto.setPlaca("NEW5678");
        dto.setModelo("Onix");
        dto.setMarca("Chevrolet");
        dto.setAnoFabricacao(2023);

        when(veiculoRepository.findById(5L)).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(veiculo)).thenReturn(veiculo);

        veiculoService.atualizar(5L, dto);

        assertThat(veiculo.getPlaca()).isEqualTo("NEW5678");
        assertThat(veiculo.getModelo()).isEqualTo("Onix");
        verify(veiculoRepository).save(veiculo);
    }
}
