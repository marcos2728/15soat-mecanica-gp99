package com.postech.mecanica.model;

import com.postech.mecanica.validation.Placa;

public class VeiculoDTO {

    private Long id;
    @Placa
    private String placa;
    private String modelo;
    private String marca;
    private Integer anoFabricacao;

    public VeiculoDTO() {
    }

    public VeiculoDTO(Long id, String placa, String modelo, String marca, Integer anoFabricacao) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.anoFabricacao = anoFabricacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

}
