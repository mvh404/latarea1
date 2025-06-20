package com.project.demo.logic.entity.rol;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Order(3)
@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createNormalUser();
    }

    private void createNormalUser() {
        String userEmail = "user@gmail.com";

        if (userRepository.findByEmail(userEmail).isPresent()) {
            System.out.println("Usuario normal ya existe, omitiendo creación");
            return;
        }

        Role userRole = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("Error: Rol USER no encontrado. Ejecuta RoleSeeder primero."));

        User user = new User();
        user.setName("Usuario");
        user.setLastname("Normal");
        user.setEmail(userEmail);
        user.setPassword(passwordEncoder.encode("user123")); // Contraseña encriptada
        user.setRole(userRole);

        userRepository.save(user);
        System.out.println("Usuario normal (USER) creado exitosamente");
    }
}