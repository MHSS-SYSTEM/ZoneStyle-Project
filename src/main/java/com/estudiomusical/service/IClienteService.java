package com.estudiomusical.service;

import com.estudiomusical.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService extends IGenericService<Cliente, Integer> {
    /*Cliente save(Cliente cliente) throws  Exception;
    Cliente update(Cliente cliente, Integer id) throws  Exception;
    List<Cliente> findAll() throws  Exception;
    Cliente findById(Integer id) throws  Exception;
    void delete(Integer id) throws  Exception;*/
    Page<Cliente> listPage(Pageable pageable);
}