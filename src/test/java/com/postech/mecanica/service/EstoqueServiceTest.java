package com.postech.mecanica.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.mecanica.entity.Estoque;
import com.postech.mecanica.mapper.EstoqueMapper;
import com.postech.mecanica.model.EstoqueDTO;
import com.postech.mecanica.repository.EstoqueRepository;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private EstoqueMapper estoqueMapper;

    @Mock
    private OrdemServicoService ordemServicoService;

    @InjectMocks
    private EstoqueService estoqueService;

    @Test
    void salvar_deveSalvarItemEstoque() {
        EstoqueDTO dto = new EstoqueDTO();
        dto.setNomeItem("Pastilha de Freio");
        dto.setQuantidadeItem(10);
        dto.setValorItem(45.0);

        Estoque estoque = new Estoque();
        estoque.setId(1L);
        estoque.setNomeItem("Pastilha de Freio");
        estoque.setQuantidadeItem(10);
        estoque.setValorItem(45.0);

        when(estoqueMapper.toEntity(dto)).thenReturn(estoque);
        when(estoqueRepository.save(estoque)).thenReturn(estoque);
        when(estoqueMapper.toDTO(estoque)).thenReturn(dto);

        EstoqueDTO resultado = estoqueService.salvar(dto);

        assertThat(resultado.getNomeItem()).isEqualTo("Pastilha de Freio");
        verify(estoqueRepository).save(estoque);
    }

    @Test
    void listarTodos_deveRetornarItens() {
        Estoque estoque = new Estoque();
        estoque.setId(2L);
        estoque.setNomeItem("Filtro de Ar");
        estoque.setQuantidadeItem(5);
        estoque.setValorItem(30.0);

        EstoqueDTO dto = new EstoqueDTO();
        dto.setId(2L);
        dto.setNomeItem("Filtro de Ar");
        dto.setQuantidadeItem(5);
        dto.setValorItem(30.0);

        when(estoqueRepository.findAll()).thenReturn(List.of(estoque));
        when(estoqueMapper.toDTO(estoque)).thenReturn(dto);

        List<EstoqueDTO> resultado = estoqueService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNomeItem()).isEqualTo("Filtro de Ar");
    }

    @Test
    void atualizar_deveAlterarDadosDoEstoque() {
        Estoque estoque = new Estoque();
        estoque.setId(4L);
        estoque.setNomeItem("Velas");
        estoque.setQuantidadeItem(8);
        estoque.setValorItem(12.0);

        EstoqueDTO dto = new EstoqueDTO();
        dto.setNomeItem("Jogo de Velas");
        dto.setQuantidadeItem(12);
        dto.setValorItem(15.5);

        when(estoqueRepository.findById(4L)).thenReturn(Optional.of(estoque));
        when(estoqueRepository.save(estoque)).thenReturn(estoque);

        estoqueService.atualizar(4L, dto);

        assertThat(estoque.getNomeItem()).isEqualTo("Jogo de Velas");
        assertThat(estoque.getQuantidadeItem()).isEqualTo(12);
        verify(estoqueRepository).save(estoque);
    }
}
