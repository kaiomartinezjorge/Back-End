package com.example.escola_xyz.model;

import java.io.Serializable;//COnverte para binario

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

//Classe para conectar como BD - Entidade do banco
//COlocar a anotaçao de entity
@Entity
public class Administrador implements Serializable{
    //Atributos
    @Id
    private String cpf;
    private String nome;
    private String email;
    private String senha;

    //Métodos (Getters and Setters)
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
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
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    
}
