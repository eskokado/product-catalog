package com.eskcti.algashop.product.catalog.presentation;

import com.eskcti.algashop.product.catalog.application.upload.UploadRequestApplicationService;
import com.eskcti.algashop.product.catalog.infrastructure.storage.fake.StorageProviderFakeImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UploadRequestController.class)
@Import({UploadRequestApplicationService.class, StorageProviderFakeImpl.class})
class UploadRequestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPresignedUrlForPngFile() throws Exception {
        mockMvc.perform(post("/api/v1/upload-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "originalFileName": "photo.png",
                                  "contentLength": 2048
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contentType").value("image/png"))
                .andExpect(jsonPath("$.contentLength").value(2048))
                .andExpect(jsonPath("$.remoteFileName").isString())
                .andExpect(jsonPath("$.remoteFileName", endsWith(".png")))
                .andExpect(jsonPath("$.uploadSignedUrl").value(startsWith("http://localhost:4566/")))
                .andExpect(jsonPath("$.expiresAt").isNotEmpty());
    }

    @Test
    void shouldReturnPresignedUrlForJpgFile() throws Exception {
        mockMvc.perform(post("/api/v1/upload-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "originalFileName": "photo.jpg",
                                  "contentLength": 1024
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contentType").value("image/jpeg"))
                .andExpect(jsonPath("$.remoteFileName", endsWith(".jpg")));
    }

    @Test
    void shouldReturnBadRequestWhenOriginalFileNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/upload-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "originalFileName": " ",
                                  "contentLength": 2048
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid fields"))
                .andExpect(jsonPath("$.fields.originalFileName").exists());
    }

    @Test
    void shouldReturnBadRequestWhenContentLengthIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/upload-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "originalFileName": "photo.png"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid fields"))
                .andExpect(jsonPath("$.fields.contentLength").exists());
    }
}
