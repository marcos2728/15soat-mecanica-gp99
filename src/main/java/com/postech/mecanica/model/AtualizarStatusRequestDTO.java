package com.postech.mecanica.model;

import com.postech.mecanica.entity.StatusOrdemServico;

public class AtualizarStatusRequestDTO {

    private StatusOrdemServico status;

    public AtualizarStatusRequestDTO() {
    }

    public StatusOrdemServico getStatus() {
        return status;
    }

    public void setStatus(StatusOrdemServico status) {
        this.status = status;
    }

    
}