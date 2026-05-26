package com.estudiomusical.service.implementation;

import com.estudiomusical.exception.ModelNotFoundException;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.service.IGenericService;

import java.util.List;

public abstract class GenericService<T,ID> implements IGenericService<T,ID> {

    protected abstract IGenericRepository<T,ID> getRepo();

    @Override
    public T save(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public T update(T t, ID id) throws Exception {
        // Validar por ID
        return getRepo().save(t);
    }

    @Override
    public List<T> findAll() throws Exception {
        return getRepo().findAll();
    }

    @Override
    public T findById(ID id) throws Exception {
        return getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + id));
    }

    @Override
    public void delete(ID id) throws Exception {
        getRepo().deleteById(id);
    }
}
