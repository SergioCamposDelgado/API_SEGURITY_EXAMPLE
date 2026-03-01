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
        return ciudadRepository.save(ciudad);
    }

    @Override
    public WeatherDTO obtenerClimaPorCoordenadas(double lat, double lon) {
        // Usamos Locale.US para asegurar que los decimales sean puntos (.) y no comas (,)
        String url = String.format(Locale.US,
                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true&daily=temperature_2m_max,temperature_2m_min&timezone=auto",
                lat, lon);
        
        logger.info("Llamando a Open-Meteo: {}", url);

        try {
            WeatherDTO response = restTemplate.getForObject(url, WeatherDTO.class);
            if (response == null) {
                throw new RuntimeException("La API devolvió una respuesta vacía");
            }
            return response;
        } catch (Exception e) {
            logger.error("Error al llamar a la API de clima: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener el clima de Open-Meteo");
        }
    }
}