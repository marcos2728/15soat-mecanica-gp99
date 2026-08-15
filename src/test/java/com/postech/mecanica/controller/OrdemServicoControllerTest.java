package com.postech.mecanica.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.postech.mecanica.entity.StatusOrdemServico;
import com.postech.mecanica.model.OrdemServicoResponseDTO;
import com.postech.mecanica.service.OrdemServicoService;

class OrdemServicoControllerTest {

    private MockMvc mockMvc;
    private OrdemServicoService ordemServicoService;

    @BeforeEach
    void setUp() {
        ordemServicoService = Mockito.mock(OrdemServicoService.class);
        OrdemServicoController controller = new OrdemServicoController(ordemServicoService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void criar_deveRetornarOrdemCriada() throws Exception {
        OrdemServicoResponseDTO response = new OrdemServicoResponseDTO();
        response.setId(1L);
        response.setStatus(StatusOrdemServico.CRIADA);
        response.setVeiculoId(10L);
        response.setPlacaVeiculo("ABC1234");

        when(ordemServicoService.criar(any())).thenReturn(response);

        mockMvc.perform(post("/ordens-servicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"veiculoId\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CRIADA"));
    }

    @Test
    void listarTodas_deveRetornarOrdens() throws Exception {
        OrdemServicoResponseDTO response = new OrdemServicoResponseDTO();
        response.setId(2L);
        response.setStatus(StatusOrdemServico.EM_DIAGNOSTICO);

        when(ordemServicoService.listarTodas()).thenReturn(List.of(response));

        mockMvc.perform(get("/ordens-servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void atualizarStatus_deveRetornarStatusAtualizado() throws Exception {
        OrdemServicoResponseDTO response = new OrdemServicoResponseDTO();
        response.setId(3L);
        response.setStatus(StatusOrdemServico.EM_DIAGNOSTICO);

        when(ordemServicoService.atualizarStatus(3L, StatusOrdemServico.EM_DIAGNOSTICO)).thenReturn(response);

        mockMvc.perform(patch("/ordens-servicos/{id}/status", 3L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"EM_DIAGNOSTICO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_DIAGNOSTICO"));
    }

    @Test
    void buscarPorId_deveRetornarOrdem() throws Exception {
        OrdemServicoResponseDTO response = new OrdemServicoResponseDTO();
        response.setId(4L);
        response.setStatus(StatusOrdemServico.DIAGNOSTICO_CONCLUIDO);

        when(ordemServicoService.buscarPorId(4L)).thenReturn(response);

        mockMvc.perform(get("/ordens-servicos/{id}", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DIAGNOSTICO_CONCLUIDO"));
    }
}
