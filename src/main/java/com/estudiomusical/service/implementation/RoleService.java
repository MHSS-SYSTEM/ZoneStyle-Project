package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Role;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.repository.IRoleRepository;
import com.estudiomusical.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService extends GenericService<Role, Integer> implements IRoleService {
    private final IRoleRepository repo;

    @Override
    protected IGenericRepository<Role, Integer> getRepo() {
        return repo;
    }
}
