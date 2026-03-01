package com.dwes.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dwes.security.dto.response.WeatherDTO;
import com.dwes.security.entities.Ciudad;
import com.dwes.security.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/api/clima")
@CrossOrigin(origins = "*") // Permite peticiones desde tu frontend
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // 1. Obtener el listado de ciudades guardadas
    @GetMapping("/ciudades")
    public ResponseEntity<List<Ciudad>> getCiudades() {
        return ResponseEntity.ok(weatherService.listarTodas());
    }

    // 2. Agregar una nueva ciudad a la lista
    @PostMapping("/ciudades")
    public ResponseEntity<Ciudad> agregarCiudad(@RequestBody Ciudad ciudad) {
        return ResponseEntity.ok(weatherService.guardarCiudad(ciudad));
    }

    // 3. Obtener el clima de una ciudad específica por sus coordenadas
    @GetMapping("/hoy")
    public ResponseEntity<WeatherDTO> getClima(
            @RequestParam double lat,
            @RequestParam double lon) {
        WeatherDTO clima = weatherService.obtenerClimaPorCoordenadas(lat, lon);
        return ResponseEntity.ok(clima);
    }

    // 4. Actualizar una ciudad existente (EDITAR)
    @PutMapping("/ciudades/{id}")
    public ResponseEntity<Ciudad> actualizarCiudad(
            @PathVariable Long id,
            @RequestBody Ciudad ciudadActualizada) {

        // Opción 1: devolver la entidad actualizada (más común en muchos proyectos)
        Ciudad ciudad = weatherService.actualizarCiudad(id, ciudadActualizada);
        return ResponseEntity.ok(ciudad);

    }

    // 5. Eliminar una ciudad
    @DeleteMapping("/ciudades/{id}")
    public ResponseEntity<Void> eliminarCiudad(@PathVariable Long id) {
        weatherService.eliminarCiudad(id);
        return ResponseEntity.noContent().build();  // 204 No Content → éxito sin cuerpo
    }
}