package com.wanted.yamyam.api.domain.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.wanted.yamyam.api.common.dto.response.LocationUploadResponse;
import com.wanted.yamyam.api.domain.service.LocationService;
import com.wanted.yamyam.domain.location.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * 시군구 데이터 관리용 엔드포인트를 지정하는 Controller 입니다.
 * @author 정성국
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    /**
     * 업로드 요청한 csv 파일을 데이터베이스에 저장한 후 저장된 데이터 갯수를 반환합니다.
     * @param file csv 파일
     * @return 저장된 데이터 갯수를 반환합니다.
     * @throws IOException parsing 중 오류가 발생
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LocationUploadResponse> save(@RequestParam("file") MultipartFile file) throws IOException {

        int countTotal = locationService.saveAllLocations(parseCsvFile(file));

        return ResponseEntity
                .created(URI.create("/api/v1/locations"))
                .body(new LocationUploadResponse(countTotal));
    }

    private List<Location> parseCsvFile(MultipartFile file) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        ObjectReader objectReader = csvMapper.readerFor(Location.class).with(csvSchema);
        MappingIterator<Location> locationsIter = objectReader.readValues(file.getInputStream());
        return locationsIter.readAll();
    }

}
