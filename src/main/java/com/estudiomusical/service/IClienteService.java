package com.estudiomusical.service;

import com.estudiomusical.model.Cliente;

import java.util.List;

public interface IClienteService extends IGenericService<Cliente, Integer> {

    // Devuelve el cliente cuyo email coincide con el correo (username) del usuario autenticado, o null si no existe.
    Cliente findByEmail(String email) throws Exception;

    /*Cliente save(Cliente cliente) throws  Exception;
    Cliente update(Cliente cliente, Integer id) throws  Exception;
    List<Cliente> findAll() throws  Exception;
    Cliente findById(Integer id) throws  Exception;
    void delete(Integer id) throws  Exception;*/
}
