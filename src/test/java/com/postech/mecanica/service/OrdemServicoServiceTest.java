package com.postech.mecanica.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.postech.mecanica.entity.OrdemServico;
import com.postech.mecanica.entity.Servico;
import com.postech.mecanica.entity.StatusOrdemServico;
import com.postech.mecanica.entity.Veiculo;
import com.postech.mecanica.mapper.OrdemServicoMapper;
import com.postech.mecanica.mapper.OrdemServicoStatusHistoricoMapper;
import com.postech.mecanica.model.DiagnosticoConcluidoRequestDTO;
import com.postech.mecanica.model.ItemEstoqueRequestDTO;
import com.postech.mecanica.model.ItemServicoRequestDTO;
import com.postech.mecanica.model.OrdemServicoRequestDTO;
import com.postech.mecanica.model.OrdemServicoResponseDTO;
import com.postech.mecanica.repository.EstoqueRepository;
import com.postech.mecanica.repository.OrdemServicoEstoqueRepository;
import com.postech.mecanica.repository.OrdemServicoRepository;
import com.postech.mecanica.repository.OrdemServicoStatusHistoricoRepository;
import com.postech.mecanica.repository.ServicoRepository;
import com.postech.mecanica.repository.VeiculoRepository;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

    @Mock
    private OrdemServicoRepository ordemServicoRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private OrdemServicoEstoqueRepository ordemServicoEstoqueRepository;

    @Mock
    private OrdemServicoMapper ordemServicoMapper;

    @Mock
    private OrdemServicoStatusHistoricoRepository ordemServicoStatusHistoricoRepository;

    @Mock
    private OrdemServicoStatusHistoricoMapper ordemServicoStatusHistoricoMapper;

    @InjectMocks
    private OrdemServicoService ordemServicoService;

    @Test
    void criar_deveSalvarOrdemComStatusCriada() {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setPlaca("ABC1234");

        OrdemServicoRequestDTO request = new OrdemServicoRequestDTO();
        request.setVeiculoId(1L);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(ordemServicoRepository.save(any(OrdemServico.class))).thenAnswer(invocation -> {
            OrdemServico ordemServico = invocation.getArgument(0);
            ordemServico.setId(99L);
            return ordemServico;
        });
        when(ordemServicoStatusHistoricoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(ordemServicoStatusHistoricoRepository.findByOrdemServicoIdAndDataHoraFimIsNull(anyLong())).thenReturn(Optional.empty());

        OrdemServicoResponseDTO response = new OrdemServicoResponseDTO();
        response.setId(99L);
        response.setStatus(StatusOrdemServico.CRIADA);
        when(ordemServicoMapper.toResponseDTO(any(OrdemServico.class))).thenReturn(response);

        OrdemServicoResponseDTO resultado = ordemServicoService.criar(request);

        assertThat(resultado.getStatus()).isEqualTo(StatusOrdemServico.CRIADA);
        assertThat(resultado.getId()).isEqualTo(99L);
        verify(ordemServicoRepository, org.mockito.Mockito.times(2)).save(any(OrdemServico.class));
    }

    @Test
    void concluirDiagnostico_comPecasEservicos_deveCalcularValorTotalEAtualizarStatus() {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(3L);
        veiculo.setPlaca("XYZ9876");

        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setId(10L);
        ordemServico.setVeiculo(veiculo);
        ordemServico.setStatus(StatusOrdemServico.EM_DIAGNOSTICO);

        Estoque estoque = new Estoque();
        estoque.setId(7L);
        estoque.setNomeItem("Filtro de Óleo");
        estoque.setValorItem(25.0);
        estoque.setQuantidadeItem(10);

        Servico servico = new Servico();
        servico.setId(5L);
        servico.setNomeServico("Balanceamento");
        servico.setValorServico(80.0);

        ItemEstoqueRequestDTO itemPeca = new ItemEstoqueRequestDTO();
        itemPeca.setEstoqueId(7L);
        itemPeca.setQuantidade(2);

        ItemServicoRequestDTO itemServico = new ItemServicoRequestDTO();
        itemServico.setServicoId(5L);

        DiagnosticoConcluidoRequestDTO request = new DiagnosticoConcluidoRequestDTO();
        request.setPecas(List.of(itemPeca));
        request.setServicos(List.of(itemServico));

        when(ordemServicoRepository.findById(10L)).thenReturn(Optional.of(ordemServico));
        when(estoqueRepository.findById(7L)).thenReturn(Optional.of(estoque));
        when(servicoRepository.findById(5L)).thenReturn(Optional.of(servico));
        when(ordemServicoRepository.save(ordemServico)).thenReturn(ordemServico);
        when(ordemServicoStatusHistoricoRepository.findByOrdemServicoIdAndDataHoraFimIsNull(10L)).thenReturn(Optional.empty());
        when(ordemServicoStatusHistoricoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoResponseDTO response = new OrdemServicoResponseDTO();
        response.setId(10L);
        response.setStatus(StatusOrdemServico.DIAGNOSTICO_CONCLUIDO);
        when(ordemServicoMapper.toResponseDTO(ordemServico)).thenReturn(response);

        OrdemServicoResponseDTO resultado = ordemServicoService.concluirDiagnostico(10L, request);

        assertThat(resultado.getStatus()).isEqualTo(StatusOrdemServico.DIAGNOSTICO_CONCLUIDO);
        assertThat(ordemServico.getValorTotal()).isEqualTo(130.0);
        assertThat(estoque.getQuantidadeItem()).isEqualTo(8);
        verify(estoqueRepository).save(estoque);
    }
}
