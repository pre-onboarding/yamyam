package com.wanted.yamyam.api.location.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.wanted.yamyam.api.location.dto.LocationResponse;
import com.wanted.yamyam.api.location.dto.LocationUploadResponse;
import com.wanted.yamyam.api.location.dto.LocationsListResponse;
import com.wanted.yamyam.api.location.service.LocationService;
import com.wanted.yamyam.domain.location.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
     * @throws IOException parsing 중 오류 발생
     * @author 정성국
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LocationUploadResponse> uploadLocationFile(@RequestParam("file") MultipartFile file)
            throws IOException {

        int countTotal = locationService.saveAllLocations(parseCsvFile(file));

        return ResponseEntity
                .created(URI.create("/api/v1/locations"))
                .body(new LocationUploadResponse(countTotal));
    }

    /**
     * 업로드된 csv 파일을 받아 parsing한 후 List 형태로 반환합니다.
     * @param file csv 파일
     * @return List로 parsing된 시군구 데이터를 반환합니다.
     * @throws IOException parsing 중 오류 발생
     * @author 정성국
     */
    private List<Location> parseCsvFile(MultipartFile file) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        ObjectReader objectReader = csvMapper.readerFor(Location.class).with(csvSchema);
        MappingIterator<Location> locationsIter = objectReader.readValues(file.getInputStream());
        return locationsIter.readAll();
    }

    /**
     * 전체 시군구 데이터를 반환합니다.
     * @return 시군구 데이터
     * @author 정성국
     */
    @GetMapping
    public ResponseEntity<LocationsListResponse> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(mapToDto(locations));
    }

    /**
     * 도메인 객체인 Location의 List를 DTO인 LocationsListResponse로 매핑하여 반환합니다.
     * @param locations 전체 시군구 데이터 목록
     * @return 클라이언트 반환용 DTO
     * @author 정성국
     */
    private LocationsListResponse mapToDto(List<Location> locations) {
        var locationResponses = new LocationResponse[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            locationResponses[i] = new LocationResponse(
                    locations.get(i).getDosi(),
                    locations.get(i).getSgg(),
                    locations.get(i).getLat(),
                    locations.get(i).getLon());
        }

        return new LocationsListResponse(locationResponses);
    }

}
