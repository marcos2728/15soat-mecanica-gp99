package com.postech.mecanica.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.postech.mecanica.entity.Estoque;
import com.postech.mecanica.entity.OrdemServico;
import com.postech.mecanica.entity.OrdemServicoEstoque;
import com.postech.mecanica.entity.OrdemServicoServico;
import com.postech.mecanica.entity.OrdemServicoStatusHistorico;
import com.postech.mecanica.entity.Servico;
import com.postech.mecanica.entity.StatusOrdemServico;
import com.postech.mecanica.entity.Veiculo;
import com.postech.mecanica.exception.RegraNegocioException;
import com.postech.mecanica.exception.ResourceNotFoundException;
import com.postech.mecanica.mapper.OrdemServicoMapper;
import com.postech.mecanica.mapper.OrdemServicoStatusHistoricoMapper;
import com.postech.mecanica.model.DiagnosticoConcluidoRequestDTO;
import com.postech.mecanica.model.ItemEstoqueRequestDTO;
import com.postech.mecanica.model.ItemServicoRequestDTO;
import com.postech.mecanica.model.OrdemServicoRequestDTO;
import com.postech.mecanica.model.OrdemServicoResponseDTO;
import com.postech.mecanica.model.TempoMedioPorFaseResponseDTO;
import com.postech.mecanica.model.TempoPorFaseResponseDTO;
import com.postech.mecanica.repository.EstoqueRepository;
import com.postech.mecanica.repository.OrdemServicoEstoqueRepository;
import com.postech.mecanica.repository.OrdemServicoRepository;
import com.postech.mecanica.repository.OrdemServicoStatusHistoricoRepository;
import com.postech.mecanica.repository.ServicoRepository;
import com.postech.mecanica.repository.VeiculoRepository;

@Service
public class OrdemServicoService {

    // ===================== dependências =====================

    private final OrdemServicoRepository ordemServicoRepository;
    private final VeiculoRepository veiculoRepository;
    private final EstoqueRepository estoqueRepository;
    private final ServicoRepository servicoRepository;
    private final OrdemServicoEstoqueRepository ordemServicoEstoqueRepository;
    private final OrdemServicoMapper ordemServicoMapper;
    private final OrdemServicoStatusHistoricoRepository ordemServicoStatusHistoricoRepository;
    private final OrdemServicoStatusHistoricoMapper ordemServicoStatusHistoricoMapper;

    public OrdemServicoService(OrdemServicoRepository ordemServicoRepository,
            VeiculoRepository veiculoRepository,
            EstoqueRepository estoqueRepository,
            ServicoRepository servicoRepository,
            OrdemServicoEstoqueRepository ordemServicoEstoqueRepository,
            OrdemServicoMapper ordemServicoMapper,
            OrdemServicoStatusHistoricoRepository ordemServicoStatusHistoricoRepository,
            OrdemServicoStatusHistoricoMapper ordemServicoStatusHistoricoMapper) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.veiculoRepository = veiculoRepository;
        this.estoqueRepository = estoqueRepository;
        this.servicoRepository = servicoRepository;
        this.ordemServicoEstoqueRepository = ordemServicoEstoqueRepository;
        this.ordemServicoMapper = ordemServicoMapper;
        this.ordemServicoStatusHistoricoRepository = ordemServicoStatusHistoricoRepository;
        this.ordemServicoStatusHistoricoMapper = ordemServicoStatusHistoricoMapper;
    }

    // ===================== métodos públicos (chamados pelo controller)

    public OrdemServicoResponseDTO criar(OrdemServicoRequestDTO requestDTO) {
        Veiculo veiculo = veiculoRepository.findById(requestDTO.getVeiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setVeiculo(veiculo);
        ordemServico.setDataHoraCriacao(LocalDateTime.now());

        OrdemServico salva = ordemServicoRepository.save(ordemServico); 

        mudarStatus(salva, StatusOrdemServico.CRIADA); 

        OrdemServico atualizada = ordemServicoRepository.save(salva); 

        return ordemServicoMapper.toResponseDTO(atualizada);
    }

    public OrdemServicoResponseDTO buscarPorId(Long id) {
        OrdemServico ordemServico = buscarEntidadePorId(id);
        return ordemServicoMapper.toResponseDTO(ordemServico);
    }

    public List<OrdemServicoResponseDTO> listarTodas() {
        return ordemServicoRepository.findAll().stream()
                .map(ordemServicoMapper::toResponseDTO)
                .toList();
    }

    // pra transições "simples": Criada -> Em Diagnóstico, e a bifurcação final
    public OrdemServicoResponseDTO atualizarStatus(Long id, StatusOrdemServico novoStatus) {
        OrdemServico ordemServico = buscarEntidadePorId(id);

        if (novoStatus == StatusOrdemServico.DIAGNOSTICO_CONCLUIDO) {
            throw new RegraNegocioException(
                    "Para concluir o diagnóstico, use o endpoint específico de conclusão");
        }

        validarTransicao(ordemServico.getStatus(), novoStatus);

        mudarStatus(ordemServico, novoStatus);
        
        return ordemServicoMapper.toResponseDTO(ordemServicoRepository.save(ordemServico));
    }

    public OrdemServicoResponseDTO concluirDiagnostico(Long id, DiagnosticoConcluidoRequestDTO requestDTO) {
        OrdemServico ordemServico = buscarEntidadePorId(id);

        validarTransicao(ordemServico.getStatus(), StatusOrdemServico.DIAGNOSTICO_CONCLUIDO);

        boolean semPecas = requestDTO.getPecas() == null || requestDTO.getPecas().isEmpty();
        boolean semServicos = requestDTO.getServicos() == null || requestDTO.getServicos().isEmpty();
        if (semPecas && semServicos) {
            throw new RegraNegocioException(
                    "É necessário informar ao menos uma peça ou serviço para concluir o diagnóstico");
        }

        boolean todasPecasDisponiveis = vincularPecas(ordemServico, requestDTO.getPecas());
        vincularServicos(ordemServico, requestDTO.getServicos());
        calcularValorTotal(ordemServico);
        mudarStatus(ordemServico, todasPecasDisponiveis
                ? StatusOrdemServico.DIAGNOSTICO_CONCLUIDO
                : StatusOrdemServico.PENDENTE_ESTOQUE);

        return ordemServicoMapper.toResponseDTO(ordemServicoRepository.save(ordemServico));
    }

    // chamado pelo EstoqueService quando um item de estoque é atualizado
    public void reconciliarPecasPendentes(Estoque estoque) {
        List<OrdemServicoEstoque> pendentes = ordemServicoEstoqueRepository
                .findPendentesPorEstoqueOrdenadoPorAntiguidade(estoque.getId());

        for (OrdemServicoEstoque item : pendentes) {
            OrdemServico ordemServico = item.getOrdemServico();

            if (ordemServico.getStatus() != StatusOrdemServico.PENDENTE_ESTOQUE) {
                continue;
            }
            if (estoque.getQuantidadeItem() < item.getQuantidade()) {
                continue;
            }

            item.setDisponivel(true);
            item.setValorUnitario(estoque.getValorItem());
            estoque.setQuantidadeItem(estoque.getQuantidadeItem() - item.getQuantidade());

            calcularValorTotal(ordemServico);

            boolean todasPecasDisponiveis = ordemServico.getPecas().stream()
                    .allMatch(OrdemServicoEstoque::getDisponivel);
            if (todasPecasDisponiveis) {
                ordemServico.setStatus(StatusOrdemServico.DIAGNOSTICO_CONCLUIDO);
            }

            ordemServico.setDataHoraAtualizacao(LocalDateTime.now());
            ordemServicoRepository.save(ordemServico);
        }
    }

    // ===================== helpers privados (só usados aqui dentro)

    private static final Map<StatusOrdemServico, Set<StatusOrdemServico>> TRANSICOES_PERMITIDAS = Map.of(
            StatusOrdemServico.CRIADA, Set.of(StatusOrdemServico.EM_DIAGNOSTICO),
            StatusOrdemServico.EM_DIAGNOSTICO, Set.of(StatusOrdemServico.DIAGNOSTICO_CONCLUIDO),
            StatusOrdemServico.DIAGNOSTICO_CONCLUIDO, Set.of(StatusOrdemServico.PENDENTE_NOTIFICACAO_CLIENTE),
            StatusOrdemServico.PENDENTE_NOTIFICACAO_CLIENTE, Set.of(StatusOrdemServico.PENDENTE_APROVACAO_CLIENTE),
            StatusOrdemServico.PENDENTE_APROVACAO_CLIENTE, Set.of(
                    StatusOrdemServico.APROVADA,
                    StatusOrdemServico.CANCELADA),
            StatusOrdemServico.APROVADA, Set.of(StatusOrdemServico.EM_EXECUCAO),
            StatusOrdemServico.EM_EXECUCAO, Set.of(StatusOrdemServico.FINALIZADA),
            StatusOrdemServico.FINALIZADA, Set.of(StatusOrdemServico.PENDENTE_ENTREGA),
            StatusOrdemServico.PENDENTE_ENTREGA, Set.of(StatusOrdemServico.ENTREGUE));

    private OrdemServico buscarEntidadePorId(Long id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço não encontrada"));
    }

    private void validarTransicao(StatusOrdemServico atual, StatusOrdemServico novo) {
        Set<StatusOrdemServico> permitidos = TRANSICOES_PERMITIDAS.get(atual);
        if (permitidos == null || !permitidos.contains(novo)) {
            throw new RegraNegocioException(
                    "Não é possível mudar de " + atual + " para " + novo);
        }
    }

    // retorna true se TODAS as peças ficaram disponíveis
    private boolean vincularPecas(OrdemServico ordemServico, List<ItemEstoqueRequestDTO> pecasRequest) {
        if (pecasRequest == null)
            return true;

        boolean todasDisponiveis = true;

        for (ItemEstoqueRequestDTO itemRequest : pecasRequest) {
            Estoque estoque = estoqueRepository.findById(itemRequest.getEstoqueId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Peça não encontrada: " + itemRequest.getEstoqueId()));

            boolean disponivel = estoque.getQuantidadeItem() >= itemRequest.getQuantidade();

            OrdemServicoEstoque item = new OrdemServicoEstoque();
            item.setOrdemServico(ordemServico);
            item.setEstoque(estoque);
            item.setQuantidade(itemRequest.getQuantidade());
            item.setValorUnitario(estoque.getValorItem());
            item.setDisponivel(disponivel);
            ordemServico.getPecas().add(item);

            if (disponivel) {
                estoque.setQuantidadeItem(estoque.getQuantidadeItem() - itemRequest.getQuantidade());
                estoqueRepository.save(estoque);
            } else {
                todasDisponiveis = false;
            }
        }

        return todasDisponiveis;
    }

    private void vincularServicos(OrdemServico ordemServico, List<ItemServicoRequestDTO> servicosRequest) {
        if (servicosRequest == null)
            return;

        for (ItemServicoRequestDTO itemRequest : servicosRequest) {
            Servico servico = servicoRepository.findById(itemRequest.getServicoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Serviço não encontrado: " + itemRequest.getServicoId()));

            OrdemServicoServico item = new OrdemServicoServico();
            item.setOrdemServico(ordemServico);
            item.setServico(servico);
            item.setValorAplicado(servico.getValorServico());
            ordemServico.getServicos().add(item);
        }
    }

    private void calcularValorTotal(OrdemServico ordemServico) {
        double totalPecas = ordemServico.getPecas().stream()
                .mapToDouble(item -> item.getQuantidade() * item.getValorUnitario())
                .sum();

        double totalServicos = ordemServico.getServicos().stream()
                .mapToDouble(item -> item.getValorAplicado())
                .sum();

        ordemServico.setValorTotal(totalPecas + totalServicos);
    }

    private void mudarStatus(OrdemServico ordemServico, StatusOrdemServico novoStatus) {
        ordemServicoStatusHistoricoRepository.findByOrdemServicoIdAndDataHoraFimIsNull(ordemServico.getId())
                .ifPresent(historicoAberto -> {
                    historicoAberto.setDataHoraFim(LocalDateTime.now());
                    ordemServicoStatusHistoricoRepository.save(historicoAberto);
                });

        OrdemServicoStatusHistorico novoHistorico = new OrdemServicoStatusHistorico();
        novoHistorico.setOrdemServico(ordemServico);
        novoHistorico.setStatus(novoStatus);
        novoHistorico.setDataHoraInicio(LocalDateTime.now());
        ordemServicoStatusHistoricoRepository.save(novoHistorico);

        ordemServico.setStatus(novoStatus);
        ordemServico.setDataHoraAtualizacao(LocalDateTime.now());
    }

    public List<TempoPorFaseResponseDTO> buscarTemposPorOrdemServico(Long id) {
        buscarEntidadePorId(id); // garante 404 se a OS não existir

        return ordemServicoStatusHistoricoRepository.findByOrdemServicoIdOrderByDataHoraInicioAsc(id).stream()
                .map(ordemServicoStatusHistoricoMapper::toResponseDTO)
                .toList();
    }

    public List<TempoMedioPorFaseResponseDTO> calcularTempoMedioPorFase() {
        List<OrdemServicoStatusHistorico> fechados = ordemServicoStatusHistoricoRepository.findByDataHoraFimIsNotNull();

        Map<StatusOrdemServico, List<Long>> duracoesPorStatus = new LinkedHashMap<>();
        for (OrdemServicoStatusHistorico h : fechados) {
            long minutos = Duration.between(h.getDataHoraInicio(), h.getDataHoraFim()).toMinutes();
            duracoesPorStatus.computeIfAbsent(h.getStatus(), k -> new ArrayList<>()).add(minutos);
        }

        return duracoesPorStatus.entrySet().stream()
                .map(e -> ordemServicoStatusHistoricoMapper.toMediaResponseDTO(
                        e.getKey(),
                        (long) e.getValue().stream().mapToLong(Long::longValue).average().orElse(0),
                        e.getValue().size()))
                .toList();
    }
}