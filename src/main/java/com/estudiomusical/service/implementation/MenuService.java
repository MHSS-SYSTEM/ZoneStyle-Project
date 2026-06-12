package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Menu;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.repository.IMenuRepository;
import com.estudiomusical.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService extends GenericService<Menu, Integer> implements IMenuService {
    private final IMenuRepository repo;

    @Override
    protected IGenericRepository<Menu, Integer> getRepo() {
        return repo;
    }

    @Override
    public List<Menu> getMenusByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repo.getMenusByUsername(username);
    }
}
