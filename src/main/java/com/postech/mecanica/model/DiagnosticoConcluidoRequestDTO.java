package com.postech.mecanica.model;

import java.util.List;

public class DiagnosticoConcluidoRequestDTO {
    private List<ItemEstoqueRequestDTO> pecas;
    private List<ItemServicoRequestDTO> servicos;

    public DiagnosticoConcluidoRequestDTO() {
    }

    public List<ItemEstoqueRequestDTO> getPecas() {
        return pecas;
    }

    public void setPecas(List<ItemEstoqueRequestDTO> pecas) {
        this.pecas = pecas;
    }

    public List<ItemServicoRequestDTO> getServicos() {
        return servicos;
    }

    public void setServicos(List<ItemServicoRequestDTO> servicos) {
        this.servicos = servicos;
    }
       
}