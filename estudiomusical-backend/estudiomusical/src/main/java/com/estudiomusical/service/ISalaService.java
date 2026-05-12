package com.estudiomusical.service;

import com.estudiomusical.model.Sala;

import java.util.List;

public interface ISalaService {

    Sala save(Sala sala) throws Exception;

    Sala update(Sala sala, Integer id) throws Exception;

    List<Sala> findAll() throws Exception;

    Sala findById(Integer id) throws Exception;

    void delete(Integer id) throws Exception;
}
