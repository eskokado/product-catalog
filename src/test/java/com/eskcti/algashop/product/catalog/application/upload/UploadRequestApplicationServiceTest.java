package com.eskcti.algashop.product.catalog.application.upload;

import com.eskcti.algashop.product.catalog.application.storage.FileReference;
import com.eskcti.algashop.product.catalog.application.storage.StorageProvider;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UploadRequestApplicationServiceTest {

    private final StorageProvider storageProvider = Mockito.mock(StorageProvider.class);
    private final UploadRequestApplicationService service =
            new UploadRequestApplicationService(storageProvider);

    private UploadRequestInput input(String originalFileName, Long contentLength) {
        UploadRequestInput input = new UploadRequestInput();
        input.setOriginalFileName(originalFileName);
        input.setContentLength(contentLength);
        return input;
    }

    @Test
    void shouldRequestPresignedUrlForPngFile() {
        Mockito.when(storageProvider.requestUploadUrl(Mockito.any()))
                .thenReturn(URI.create("http://localhost:4566/upload?token=abc"));

        UploadResponseOutput output = service.requestPreSignedUrl(input("photo.png", 2048L));

        assertThat(output.getUploadSignedUrl())
                .isEqualTo("http://localhost:4566/upload?token=abc");
        assertThat(output.getContentType()).isEqualTo("image/png");
        assertThat(output.getContentLength()).isEqualTo(2048L);
        assertThat(output.getRemoteFileName()).endsWith(".png");

        ArgumentCaptor<FileReference> captor = ArgumentCaptor.forClass(FileReference.class);
        Mockito.verify(storageProvider).requestUploadUrl(captor.capture());
        assertThat(captor.getValue().getContentType()).isEqualTo(MediaType.IMAGE_PNG);
        assertThat(captor.getValue().getExpiresIn()).isEqualTo(Duration.ofMinutes(5));
    }

    @Test
    void shouldRequestPresignedUrlForJpgFile() {
        Mockito.when(storageProvider.requestUploadUrl(Mockito.any()))
                .thenReturn(URI.create("http://localhost:4566/upload?token=abc"));

        UploadResponseOutput output = service.requestPreSignedUrl(input("photo.jpg", 1024L));

        assertThat(output.getContentType()).isEqualTo("image/jpeg");
        assertThat(output.getRemoteFileName()).endsWith(".jpg");

        ArgumentCaptor<FileReference> captor = ArgumentCaptor.forClass(FileReference.class);
        Mockito.verify(storageProvider).requestUploadUrl(captor.capture());
        assertThat(captor.getValue().getContentType()).isEqualTo(MediaType.IMAGE_JPEG);
    }

    @Test
    void shouldSetExpiresAtFiveMinutesInTheFuture() {
        Mockito.when(storageProvider.requestUploadUrl(Mockito.any()))
                .thenReturn(URI.create("http://localhost:4566/upload?token=abc"));

        OffsetDateTime before = OffsetDateTime.now().plus(Duration.ofMinutes(5));
        UploadResponseOutput output = service.requestPreSignedUrl(input("photo.png", 2048L));
        OffsetDateTime after = OffsetDateTime.now().plus(Duration.ofMinutes(5));

        assertThat(output.getExpiresAt())
                .isBetween(before.minusSeconds(1), after.plusSeconds(1));
    }

    @Test
    void shouldGenerateRandomRemoteFileName() {
        Mockito.when(storageProvider.requestUploadUrl(Mockito.any()))
                .thenReturn(URI.create("http://localhost:4566/upload?token=abc"));

        UploadResponseOutput first = service.requestPreSignedUrl(input("photo.png", 2048L));
        UploadResponseOutput second = service.requestPreSignedUrl(input("photo.png", 2048L));

        assertThat(first.getRemoteFileName()).isNotEqualTo(second.getRemoteFileName());
        assertThat(first.getRemoteFileName()).endsWith(".png");
    }

    @Test
    void shouldRejectUnsupportedImageType() {
        assertThatThrownBy(() -> service.requestPreSignedUrl(input("animation.gif", 2048L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid image type");

        Mockito.verifyNoInteractions(storageProvider);
    }
}
