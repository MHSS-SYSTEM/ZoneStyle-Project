package com.solweb.repository;

import com.solweb.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICancionRepository extends JpaRepository<Cancion, Integer> {
}
