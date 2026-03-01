package com.dwes.security.service;

import java.util.List;

import com.dwes.security.dto.response.WeatherDTO;
import com.dwes.security.entities.Ciudad;

public interface WeatherService {
	List<Ciudad> listarTodas();

	Ciudad guardarCiudad(Ciudad ciudad);

	WeatherDTO obtenerClimaPorCoordenadas(double lat, double lon);

	Ciudad actualizarCiudad(Long id, Ciudad ciudadActualizada);

	void eliminarCiudad(Long id);
}
