package com.example.cardealership.controller;

import com.example.cardealership.dto.CarResponse;
import com.example.cardealership.exception.GlobalExceptionHandler;
import com.example.cardealership.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
@Import(GlobalExceptionHandler.class)
class CarControllerQueryFeaturesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Test
    void getAllCars_withPaginationParams_returnsPagedContent() throws Exception {
        when(carService.getAllCars(0, 2, "price", "desc")).thenReturn(
                new PageImpl<>(
                        List.of(
                                new CarResponse(1L, "BMW", "X5", 2024, "Blue", 62000),
                                new CarResponse(2L, "Tesla", "Model Y", 2024, "Red", 52000)
                        ),
                        PageRequest.of(0, 2),
                        10
                )
        );

        mockMvc.perform(get("/api/cars")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sortBy", "price")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].make").value("BMW"))
                .andExpect(jsonPath("$.content[1].model").value("Model Y"))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(5));

        verify(carService).getAllCars(0, 2, "price", "desc");
    }

    @Test
    void search_returnsMatchingCars() throws Exception {
        when(carService.searchCars("camry")).thenReturn(
                List.of(new CarResponse(1L, "Toyota", "Camry", 2023, "Silver", 28000))
        );

        mockMvc.perform(get("/api/cars/search").param("q", "camry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make").value("Toyota"))
                .andExpect(jsonPath("$[0].model").value("Camry"));

        verify(carService).searchCars("camry");
    }

    @Test
    void filter_returnsMatchingCars() throws Exception {
        when(carService.filterCars(eq("Toyota"), eq(null), eq(null), eq(null), eq(20000.0), eq(30000.0)))
                .thenReturn(List.of(new CarResponse(1L, "Toyota", "Camry", 2023, "Silver", 28000)));

        mockMvc.perform(get("/api/cars/filter")
                        .param("make", "Toyota")
                        .param("minPrice", "20000")
                        .param("maxPrice", "30000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make").value("Toyota"))
                .andExpect(jsonPath("$[0].price").value(28000.0));

        verify(carService).filterCars("Toyota", null, null, null, 20000.0, 30000.0);
    }
}
