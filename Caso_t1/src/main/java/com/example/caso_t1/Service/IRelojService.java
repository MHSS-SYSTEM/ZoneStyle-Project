package com.example.caso_t1.Service;

import java.util.List;
import com.example.caso_t1.model.Reloj;
public interface IRelojService {
    Reloj save(Reloj reloj) throws Exception;

    Reloj update(Reloj reloj, Integer id) throws Exception;

    List<Reloj> findAll() throws Exception;

    Reloj findById(Integer id) throws Exception;

    void delete(Integer id) throws Exception;
}
