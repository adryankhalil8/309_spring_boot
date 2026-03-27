package com.example.cardealership.controller;

import com.example.cardealership.dto.CarResponse;
import com.example.cardealership.exception.GlobalExceptionHandler;
import com.example.cardealership.exception.ResourceNotFoundException;
import com.example.cardealership.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
@Import(GlobalExceptionHandler.class)
class CarControllerExceptionHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Test
    void getCarById_notFound_returnsStandard404() throws Exception {
        when(carService.getCarById(999L)).thenThrow(new ResourceNotFoundException("Car", 999L));

        mockMvc.perform(get("/api/cars/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Car not found with id: 999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createCar_invalidRequest_returnsValidationErrors() throws Exception {
        String invalidBody = """
                {
                  "make": "",
                  "model": "",
                  "year": 0,
                  "color": "",
                  "price": 0
                }
                """;

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message").value("One or more fields are invalid"))
                .andExpect(jsonPath("$.fieldErrors.make").value("Make must be between 2 and 50 characters"))
                .andExpect(jsonPath("$.fieldErrors.color").value("Color is required"))
                .andExpect(jsonPath("$.fieldErrors.price").value("Price must be greater than zero"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void updateCar_notFound_returns404() throws Exception {
        String validBody = """
                {
                  "make": "Toyota",
                  "model": "Test",
                  "year": 2024,
                  "color": "Red",
                  "price": 30000
                }
                """;

        when(carService.updateCar(eq(999L), any())).thenThrow(new ResourceNotFoundException("Car", 999L));

        mockMvc.perform(put("/api/cars/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Car not found with id: 999"));
    }

    @Test
    void deleteCar_notFound_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Car", 999L)).when(carService).deleteCar(999L);

        mockMvc.perform(delete("/api/cars/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Car not found with id: 999"));
    }

    @Test
    void createCar_validRequest_returnsCreatedResponse() throws Exception {
        String validBody = """
                {
                  "make": "Toyota",
                  "model": "Camry",
                  "year": 2024,
                  "color": "Silver",
                  "price": 28000
                }
                """;

        when(carService.createCar(any())).thenReturn(
                new CarResponse(1L, "Toyota", "Camry", 2024, "Silver", 28000)
        );

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.make").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Camry"));
    }
}
