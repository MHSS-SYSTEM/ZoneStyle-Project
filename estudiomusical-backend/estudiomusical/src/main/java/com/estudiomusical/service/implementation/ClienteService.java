package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Cliente;
import com.estudiomusical.repository.IClienteRepository;
import com.estudiomusical.service.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService implements IClienteService {

    private final IClienteRepository repo;

    @Override
    public Cliente save(Cliente cliente) throws Exception {
        return repo.save(cliente);
    }

    @Override
    public Cliente update(Cliente cliente, Integer id) throws Exception {
        // Validamos existencia para que PUT no cree registros con IDs inexistentes.
        if (!repo.existsById(id)) {
            throw notFound(id);
        }
        cliente.setIdCliente(id);
        return repo.save(cliente);
    }

    @Override
    public List<Cliente> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Cliente findById(Integer id) throws Exception {
        return repo.findById(id).orElseThrow(() -> notFound(id));
    }

    @Override
    public void delete(Integer id) throws Exception {
        if (!repo.existsById(id)) {
            throw notFound(id);
        }
        repo.deleteById(id);
    }

    private ResponseStatusException notFound(Integer id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado: " + id);
    }
}
