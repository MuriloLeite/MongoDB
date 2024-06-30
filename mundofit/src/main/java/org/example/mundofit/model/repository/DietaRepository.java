package org.example.mundofit.model.repository;

import org.example.mundofit.model.Dieta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DietaRepository extends MongoRepository<Dieta, String> {
}
