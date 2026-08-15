package com.postech.mecanica.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.mecanica.model.ClienteResponseDTO;
import com.postech.mecanica.model.VeiculoDTO;
import com.postech.mecanica.service.ClienteService;

class ClienteControllerTest {

    private MockMvc mockMvc;
    private ClienteService clienteService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        clienteService = Mockito.mock(ClienteService.class);
        ClienteController controller = new ClienteController(clienteService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void criar_deveRetornarClienteCriado() throws Exception {
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setId(1L);
        response.setNome("Ana Souza");
        response.setCpfCnpj("12345678909");
        response.setEmail("ana@email.com");
        response.setTelefone("9898989898");

        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setId(5L);
        veiculo.setPlaca("ABC1234");
        veiculo.setModelo("Civic");
        veiculo.setMarca("Honda");
        veiculo.setAnoFabricacao(2020);
        response.setVeiculo(veiculo);

        when(clienteService.cadastrarComVeiculo(any())).thenReturn(response);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cpfCNpj\":\"12345678909\",\"nome\":\"Ana Souza\",\"email\":\"ana@email.com\",\"telefone\":\"9898989898\",\"dataDeNascimento\":\"1990-01-01\",\"veiculo\":{\"placa\":\"ABC1234\",\"modelo\":\"Civic\",\"marca\":\"Honda\",\"anoFabricacao\":2020}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Ana Souza"));
    }

    @Test
    void buscarPorCpfCnpj_deveRetornarCliente() throws Exception {
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setId(2L);
        response.setNome("João");
        response.setCpfCnpj("98765432100");

        when(clienteService.buscarPorCpfCnpj("98765432100")).thenReturn(response);

        mockMvc.perform(get("/clientes/{cpfCnpj}", "98765432100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpfCnpj").value("98765432100"));
    }

    @Test
    void atualizar_deveRetornarOk() throws Exception {
        mockMvc.perform(put("/clientes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cpfCNpj\":\"12345678909\",\"nome\":\"Cliente Atualizado\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void deletar_deveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/clientes/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
