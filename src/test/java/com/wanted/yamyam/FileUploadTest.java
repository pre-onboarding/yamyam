package com.wanted.yamyam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("시군구 데이터 업로드 후 정상 응답 수신")
    void shouldSaveUploadedCsvFileAndReturn201() throws Exception {
        Resource resource = new ClassPathResource("sgg_lat_lon.csv");
        MockMultipartFile file = new MockMultipartFile(
                "file",
                resource.getFilename(),
                "text/csv",
                resource.getContentAsByteArray());

        mvc.perform(multipart(HttpMethod.POST, "/api/v1/locations")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/locations"))
                .andExpect(jsonPath("$.['countTotal']").value(295));
    }
}
