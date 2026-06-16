package com.estudiomusical.config;

import com.estudiomusical.model.Menu;
import com.estudiomusical.model.Role;
import com.estudiomusical.model.User;
import com.estudiomusical.repository.IMenuRepository;
import com.estudiomusical.repository.IRoleRepository;
import com.estudiomusical.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final IMenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role admin = saveRole(1001, "ADMIN", "Administrador del sistema");
        Role ingeniero = saveRole(1002, "INGENIERO", "Ingeniero de sonido");
        Role cliente = saveRole(1003, "CLIENTE", "Cliente o artista");

        saveUser(1001, "admin@zonestyle.com", "admin159", List.of(admin));
        saveUser(1002, "ingeniero@zonestyle.com", "ingeniero159", List.of(ingeniero));
        saveUser(1003, "cliente@zonestyle.com", "cliente159", List.of(cliente));

        saveMenu(1001, "people", "Clientes", "/pages/clientes", List.of(admin, ingeniero));
        saveMenu(1002, "meeting_room", "Salas", "/pages/salas", List.of(admin, ingeniero));
        saveMenu(1003, "music_note", "Servicios", "/pages/servicios", List.of(admin, ingeniero));
        saveMenu(1004, "event", "Reservas", "/pages/reservas", List.of(admin, ingeniero, cliente));
        saveMenu(1005, "mic", "Equipos Tecnicos", "/pages/equipos", List.of(admin, ingeniero));
        saveMenu(1006, "dashboard", "Reportes", "/pages/reportes", List.of(admin));
    }

    private Role saveRole(Integer id, String name, String description) {
        Role role = roleRepository.findById(id).orElseGet(Role::new);
        role.setIdRole(id);
        role.setName(name);
        role.setDescription(description);
        return roleRepository.save(role);
    }

    private void saveUser(Integer id, String username, String rawPassword, List<Role> roles) {
        User user = userRepository.findOneByUsername(username);

        if (user == null) {
            user = new User();
            user.setIdUser(id);
            user.setUsername(username);
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);
        user.setRoles(roles);
        userRepository.save(user);
    }

    private void saveMenu(Integer id, String icon, String name, String url, List<Role> roles) {
        Menu menu = menuRepository.findById(id).orElseGet(Menu::new);
        menu.setIdMenu(id);
        menu.setIcon(icon);
        menu.setName(name);
        menu.setUrl(url);
        menu.setRoles(roles);
        menuRepository.save(menu);
    }
}
