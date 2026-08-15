package com.postech.mecanica.model;

import com.postech.mecanica.validation.CpfCnpj;

import jakarta.validation.Valid;

public class ClienteVeiculoRequestDTO {

    @CpfCnpj
    private String cpfCNpj;
    private String nome;
    private String email;
    private String telefone;
    private String dataDeNascimento;
    @Valid
    private VeiculoDTO veiculo;

    public String getCpfCNpj() {
        return cpfCNpj;
    }

    public void setCpfCNpj(String cpfCNpj) {
        this.cpfCNpj = cpfCNpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
    public VeiculoDTO getVeiculo() {
        return veiculo;
    }
    public void setVeiculo(VeiculoDTO veiculo) {
        this.veiculo = veiculo;
    }
    

    
}