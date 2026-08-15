package com.postech.mecanica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.postech.mecanica.entity.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    
}
