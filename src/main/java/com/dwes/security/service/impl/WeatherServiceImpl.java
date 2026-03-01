package com.dwes.security.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.dwes.security.dto.response.WeatherDTO;
import com.dwes.security.entities.Ciudad;
import com.dwes.security.repository.CiudadRepository;
import com.dwes.security.service.WeatherService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private final CiudadRepository ciudadRepository;
    private final RestTemplate restTemplate;

    public WeatherServiceImpl(CiudadRepository ciudadRepository) {
        this.ciudadRepository = ciudadRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<Ciudad> listarTodas() {
        return ciudadRepository.findAll();
    }

    @Override
    public Ciudad guardarCiudad(Ciudad ciudad) {
        if (ciudad.getNombre() == null || ciudad.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad es obligatorio");
        }
        return ciudadRepository.save(ciudad);
    }

    @Override
    public WeatherDTO obtenerClimaPorCoordenadas(double lat, double lon) {
        // Usamos Locale.US para forzar punto decimal
        String url = String.format(Locale.US,
                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true&daily=temperature_2m_max,temperature_2m_min&timezone=auto",
                lat, lon);

        logger.info("Consultando Open-Meteo API: {}", url);

        try {
            WeatherDTO response = restTemplate.getForObject(url, WeatherDTO.class);
            if (response == null) {
                throw new RuntimeException("Respuesta vacía desde la API de Open-Meteo");
            }
            return response;
        } catch (Exception e) {
            logger.error("Error al obtener clima para lat={}, lon={}: {}", lat, lon, e.getMessage(), e);
            throw new RuntimeException("No se pudo obtener el clima. Intenta más tarde.", e);
        }
    }

    @Override
    public Ciudad actualizarCiudad(Long id, Ciudad ciudadActualizada) {
        if (id == null) {
            throw new IllegalArgumentException("El ID es obligatorio para actualizar");
        }

        Optional<Ciudad> optionalCiudad = ciudadRepository.findById(id);

        if (optionalCiudad.isEmpty()) {
            throw new RuntimeException("No se encontró la ciudad con ID: " + id);
            // Alternativa más limpia: crear CiudadNotFoundException personalizada
        }

        Ciudad ciudadExistente = optionalCiudad.get();

        // Actualizamos SOLO los campos permitidos / que vengan informados
        if (ciudadActualizada.getNombre() != null && !ciudadActualizada.getNombre().trim().isEmpty()) {
            ciudadExistente.setNombre(ciudadActualizada.getNombre().trim());
        }

        if (ciudadActualizada.getLatitud() != null) {
            ciudadExistente.setLatitud(ciudadActualizada.getLatitud());
        }

        if (ciudadActualizada.getLongitud() != null) {
            ciudadExistente.setLongitud(ciudadActualizada.getLongitud());
        }

        // Guardamos y devolvemos la versión actualizada
        return ciudadRepository.save(ciudadExistente);
    }

    @Override
    public void eliminarCiudad(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID es obligatorio para eliminar");
        }

        if (!ciudadRepository.existsById(id)) {
            throw new RuntimeException("No se encontró la ciudad con ID: " + id);
        }

        ciudadRepository.deleteById(id);
        logger.info("Ciudad eliminada con éxito - ID: {}", id);
    }
}