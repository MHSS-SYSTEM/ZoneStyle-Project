package com.example.caso_t1.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import com.example.caso_t1.Repository.IRelojRepository;
import com.example.caso_t1.model.Reloj;

@Service
@RequiredArgsConstructor
public class RelojService implements IRelojService{
    private final IRelojRepository repo;

    @Override
    public Reloj save(Reloj reloj) throws Exception {
        return repo.save(reloj);
    }

    @Override
    public Reloj update(Reloj reloj, Integer id) throws Exception {
        reloj.setIdReloj(id);
        return repo.save(reloj);
    }

    @Override
    public List<Reloj> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Reloj findById(Integer id) throws Exception {
        return repo.findById(id).orElse(new Reloj());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }
}
