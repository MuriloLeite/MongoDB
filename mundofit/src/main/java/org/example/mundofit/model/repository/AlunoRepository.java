package org.example.mundofit.model.repository;

import org.example.mundofit.model.Aluno;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlunoRepository extends MongoRepository<Aluno, String> {
    void deleteById(String id);
    List<Aluno> findBySexo(String sexo);
    List<Aluno> findAll();
}
