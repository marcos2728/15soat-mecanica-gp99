package com.postech.mecanica.model;

public class EstoqueDTO {

    private Long id;
    private String nomeItem;
    private Integer quantidadeItem;
    private Double valorItem;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNomeItem() {
        return nomeItem;
    }
    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }
    public Integer getQuantidadeItem() {
        return quantidadeItem;
    }
    public void setQuantidadeItem(Integer quantidadeItem) {
        this.quantidadeItem = quantidadeItem;
    }
    public Double getValorItem() {
        return valorItem;
    }
    public void setValorItem(Double valorItem) {
        this.valorItem = valorItem;
    }

    
    
}
