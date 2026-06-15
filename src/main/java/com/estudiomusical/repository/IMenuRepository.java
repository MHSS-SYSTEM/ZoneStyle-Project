package com.estudiomusical.repository;

import com.estudiomusical.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMenuRepository extends IGenericRepository<Menu, Integer> {
    @Query(value = """
            select m.* from menu_role mr
            inner join user_role ur on ur.id_role = mr.id_role
            inner join menu m on m.id_menu = mr.id_menu
            inner join users u on u.id_user = ur.id_user
            where u.username = :username
            """, nativeQuery = true)
    List<Menu> getMenusByUsername(@Param("username") String username);
}
