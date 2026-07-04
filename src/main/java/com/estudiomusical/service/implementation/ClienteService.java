package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Cliente;
import com.estudiomusical.repository.IClienteRepository;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.service.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService extends GenericService<Cliente, Integer> implements IClienteService {

    // Autowired
    private final IClienteRepository repo;

    @Override
    protected IGenericRepository<Cliente, Integer> getRepo() {
        return repo;
    }

    @Override
    public Page<Cliente> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    /*
    @Override
    public Cliente save(Cliente cliente) throws Exception {
        return repo.save(cliente);
    }

    @Override
    public Cliente update(Cliente cliente, Integer id) throws Exception {
        // EVALUAR CON EL ID
        // API REFLECTION
        cliente.setIdCliente(id);
        return repo.save(cliente);
    }

    @Override
    public List<Cliente> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Cliente findById(Integer id) throws Exception {
        return repo.findById(id).orElse(new Cliente());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }
    */
}