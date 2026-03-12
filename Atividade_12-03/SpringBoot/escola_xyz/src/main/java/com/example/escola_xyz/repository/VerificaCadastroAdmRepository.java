package com.example.escola_xyz.repository;

import com.example.escola_xyz.model.VerificaCadastroAdm;

import org.springframework.data.repository.CrudRepository;

public interface VerificaCadastroAdmRepository extends CrudRepository<VerificaCadastroAdm,String>{

    VerificaCadastroAdm findByCpf(String cpf);

}
                       