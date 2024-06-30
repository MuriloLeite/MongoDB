package org.example.mundofit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "alunos")
public class Aluno {

    @Id
    private String id;
    private String nome;
    private String sexo;
    private Date dataNascimento;
    private Date dataInscricao;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Date getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(Date dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public int getIdade() {
        Date dataAtual = new Date();
        long diferenca = dataAtual.getTime() - dataNascimento.getTime();
        return (int) (diferenca / (1000L * 60 * 60 * 24 * 365));
    }
}
