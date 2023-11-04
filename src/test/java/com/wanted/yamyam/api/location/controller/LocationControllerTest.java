package com.wanted.yamyam.api.location.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.wanted.yamyam.api.location.service.LocationService;
import com.wanted.yamyam.domain.location.entity.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.IOException;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LocationController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class LocationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LocationService locationService;

    @Test
    @DisplayName("정상적인 시군구 데이터 요청 후 전체 시군구 데이터 수신")
    void shouldReturnAllLocations() throws Exception {
        Resource resource = new ClassPathResource("sgg_lat_lon.csv");
        List<Location> locations = parseCsvFile(resource);

        given(locationService.getAllLocations()).willReturn(locations);

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['locations'][0]['doSi']").value(locations.get(0).getDosi()))
                .andExpect(jsonPath("$.['locations'][0]['sgg']").value(locations.get(0).getSgg()))
                .andExpect(jsonPath("$.['locations'][0]['lon']").value(locations.get(0).getLon()))
                .andExpect(jsonPath("$.['locations'][0]['lat']").value(locations.get(0).getLat()))
                .andExpect(jsonPath("$.['locations'][294]['doSi']").value(locations.get(294).getDosi()))
                .andExpect(jsonPath("$.['locations'][294]['sgg']").value(locations.get(294).getSgg()))
                .andExpect(jsonPath("$.['locations'][294]['lon']").value(locations.get(294).getLon()))
                .andExpect(jsonPath("$.['locations'][294]['lat']").value(locations.get(294).getLat()));

    }

    private List<Location> parseCsvFile(Resource file) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        ObjectReader objectReader = csvMapper.readerFor(Location.class).with(csvSchema);
        MappingIterator<Location> locationsIter = objectReader.readValues(file.getInputStream());
        return locationsIter.readAll();
    }
}
