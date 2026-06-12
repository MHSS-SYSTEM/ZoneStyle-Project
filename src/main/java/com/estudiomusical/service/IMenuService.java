package com.estudiomusical.service;

import com.estudiomusical.model.Menu;

import java.util.List;

public interface IMenuService extends IGenericService<Menu, Integer> {
    List<Menu> getMenusByUsername();
}
