package com.postech.mecanica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.postech.mecanica.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.veiculo WHERE c.cpfCnpj = :cpfCnpj")
    Optional<Cliente> findByCpfCnpjComVeiculo(@Param("cpfCnpj") String cpfCnpj);

    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.veiculo WHERE c.id = :id")
    Optional<Cliente> findByIdComVeiculo(@Param("id") Long id);

    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.veiculo")
    List<Cliente> findAllComVeiculo();

    boolean existsByVeiculoId(Long veiculoId);

}
