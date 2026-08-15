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

import com.postech.mecanica.entity.Servico;
import com.postech.mecanica.mapper.ServicoMapper;
import com.postech.mecanica.model.ServicoDTO;
import com.postech.mecanica.repository.ServicoRepository;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

    @Mock
    private ServicoRepository servicoRepository;

    private ServicoService servicoService;

    @BeforeEach
    void setUp() {
        servicoService = new ServicoService(servicoRepository);
    }

    @Test
    void salvar_deveSalvarServico() {
        ServicoDTO dto = new ServicoDTO();
        dto.setNomeServico("Alinhamento");
        dto.setValorServico(120.0);

        Servico servico = new Servico();
        servico.setNomeServico("Alinhamento");
        servico.setValorServico(120.0);

        when(servicoRepository.save(any(Servico.class))).thenReturn(servico);

        ServicoDTO resultado = servicoService.salvar(dto);

        assertThat(resultado.getNomeServico()).isEqualTo("Alinhamento");
        assertThat(resultado.getValorServico()).isEqualTo(120.0);
        verify(servicoRepository).save(any(Servico.class));
    }

    @Test
    void listarTodos_deveRetornarServicos() {
        Servico servico = new Servico();
        servico.setId(2L);
        servico.setNomeServico("Balanceamento");
        servico.setValorServico(80.0);

        when(servicoRepository.findAll()).thenReturn(List.of(servico));

        List<ServicoDTO> resultado = servicoService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNomeServico()).isEqualTo("Balanceamento");
    }

    @Test
    void atualizar_deveAlterarServico() {
        Servico servico = new Servico();
        servico.setId(9L);
        servico.setNomeServico("Troca de óleo");
        servico.setValorServico(90.0);

        ServicoDTO dto = new ServicoDTO();
        dto.setNomeServico("Troca de filtro");
        dto.setValorServico(110.0);

        when(servicoRepository.findById(9L)).thenReturn(Optional.of(servico));
        when(servicoRepository.save(servico)).thenReturn(servico);

        servicoService.atualizar(9L, dto);

        assertThat(servico.getNomeServico()).isEqualTo("Troca de filtro");
        assertThat(servico.getValorServico()).isEqualTo(110.0);
        verify(servicoRepository).save(servico);
    }
}
