package com.example.escola_xyz.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.escola_xyz.model.Administrador;

public interface AdministradorRepository extends CrudRepository<Administrador,String>{
        //se precisar criar algum método específico de busca no banco eu crio aqui

        Administrador findByCpf(String cpf);//Busca pelo CPF no banco 

}
