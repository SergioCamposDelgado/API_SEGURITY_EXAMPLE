package com.dwes.security.config;

import java.util.Locale;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dwes.security.entities.Ciudad;
import com.dwes.security.entities.Role;
import com.dwes.security.entities.Usuario; 
import com.dwes.security.repository.CiudadRepository;
import com.dwes.security.repository.UserRepository;
import com.github.javafaker.Faker;

/**
 * Inicializador de datos de demostración para el perfil 'demo'.
 * Configura el entorno con ciudades y usuarios de prueba.
 */
@Profile("demo")
@Component
public class DemoDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final boolean BORRAR_DATOS_EXISTENTES = true;

    @Override
    public void run(String... args) throws Exception {
        log.info("========================================");
        log.info("INICIALIZANDO DATOS DE DEMOSTRACIÓN (CLIMA)");
        log.info("========================================");

        // 1. Cargar ciudades para la App de Clima
        inicializarCiudades();

        // 2. Cargar usuarios de prueba
        inicializarUsuarios();

        log.info("========================================");
        log.info("DATOS CARGADOS CON ÉXITO");
        log.info("Total ciudades: {}", ciudadRepository.count());
        log.info("Total usuarios: {}", usuarioRepository.count());
        log.info("========================================");
    }

    /**
     * Crea las ciudades que aparecerán en el listado de tu App.
     */
    private void inicializarCiudades() {
        try {
            if (BORRAR_DATOS_EXISTENTES) {
                ciudadRepository.deleteAll();
                log.info("✓ Listado de ciudades reiniciado");
            }

            if (ciudadRepository.count() == 0) {
                List<Ciudad> ciudades = List.of(
                    new Ciudad("Sevilla", 37.3828, -5.9731),
                    new Ciudad("Madrid", 40.4168, -3.7038),
                    new Ciudad("Barcelona", 41.3851, 2.1734),
                    new Ciudad("Valencia", 39.4699, -0.3763),
                    new Ciudad("Bilbao", 43.2630, -2.9350),
                    new Ciudad("Granada", 37.1773, -3.5986),
                    new Ciudad("Vigo", 42.2406, -8.7207)
                );

                ciudadRepository.saveAll(ciudades);
                log.info("✓ {} ciudades estratégicas añadidas", ciudades.size());
            }
        } catch (Exception e) {
            log.error("✗ Error al inicializar ciudades: {}", e.getMessage());
        }
    }

    /**
     * Crea usuarios para probar la seguridad de la API.
     */
    private void inicializarUsuarios() {
        try {
            if (usuarioRepository.count() > 0) {
                log.info("⚠ Usuarios ya existentes, omitiendo creación.");
                return;
            }

            // Usuario Administrador
            Usuario admin = new Usuario();
            admin.setFirstName("Admin");
            admin.setLastName("App");
            admin.setEmail("admin@clima.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getRoles().add(Role.ROLE_ADMIN);
            usuarioRepository.save(admin);

            // Usuario Estándar
            Usuario user = new Usuario();
            user.setFirstName("User");
            user.setLastName("Demo");
            user.setEmail("user@clima.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.getRoles().add(Role.ROLE_USER);
            usuarioRepository.save(user);

            // Generar algunos usuarios extra con Faker
            Faker faker = new Faker(new Locale("es"));
            for (int i = 0; i < 3; i++) {
                Usuario randomUser = new Usuario();
                randomUser.setFirstName(faker.name().firstName());
                randomUser.setLastName(faker.name().lastName());
                randomUser.setEmail(faker.internet().emailAddress());
                randomUser.setPassword(passwordEncoder.encode("demo123"));
                randomUser.getRoles().add(Role.ROLE_USER);
                usuarioRepository.save(randomUser);
            }

            log.info("✓ Credenciales: admin@clima.com (admin123) | user@clima.com (user123)");

        } catch (Exception e) {
            log.error("✗ Error al inicializar usuarios: {}", e.getMessage());
        }
    }
}