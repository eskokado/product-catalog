package com.eskcti.algashop.product.catalog.infrastructure.storage.s3;

import com.eskcti.algashop.product.catalog.application.storage.FileReference;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.URL;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StorageProviderAwsS3ImplTest {

    private static final String BUCKET_NAME = "algashop-product-image";
    private static final String FILE_NAME = "photo.png";

    private final StorageProviderAwsS3Properties properties = new StorageProviderAwsS3Properties();
    private final S3Template s3Template = Mockito.mock(S3Template.class);
    private final StorageProviderAwsS3Impl storageProvider =
            new StorageProviderAwsS3Impl(properties, s3Template);

    @BeforeEach
    void setUp() {
        properties.setBucketName(BUCKET_NAME);
    }

    private FileReference fileReference(boolean allowPublicRead) {
        return FileReference.builder()
                .fileName(FILE_NAME)
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(2048L)
                .expiresIn(Duration.ofMinutes(5))
                .allowPublicRead(allowPublicRead)
                .build();
    }

    private URL signedUrl() {
        try {
            return URI.create("http://localhost:4566/" + FILE_NAME + "?token=abc").toURL();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void stubSignedPutUrl(Exception exception) {
        Mockito.when(s3Template.createSignedPutURL(
                Mockito.eq(BUCKET_NAME),
                Mockito.eq(FILE_NAME),
                Mockito.any(Duration.class),
                Mockito.any(ObjectMetadata.class),
                Mockito.eq(MediaType.IMAGE_PNG_VALUE)))
                .thenThrow(exception);
    }

    @Test
    void shouldReturnTrueWhenBucketExists() {
        Mockito.when(s3Template.bucketExists(BUCKET_NAME)).thenReturn(true);

        assertThat(storageProvider.healthCheck()).isTrue();
    }

    @Test
    void shouldReturnFalseWhenBucketDoesNotExist() {
        Mockito.when(s3Template.bucketExists(BUCKET_NAME)).thenReturn(false);

        assertThat(storageProvider.healthCheck()).isFalse();
    }

    @Test
    void shouldReturnFalseWhenBucketCheckThrowsException() {
        Mockito.when(s3Template.bucketExists(BUCKET_NAME)).thenThrow(new RuntimeException("s3 down"));

        assertThat(storageProvider.healthCheck()).isFalse();
    }

    @Test
    void shouldReturnSignedPutUrlWhenRemoteFileDoesNotExist() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(false);
        Mockito.when(s3Template.createSignedPutURL(
                Mockito.eq(BUCKET_NAME),
                Mockito.eq(FILE_NAME),
                Mockito.any(Duration.class),
                Mockito.any(ObjectMetadata.class),
                Mockito.eq(MediaType.IMAGE_PNG_VALUE)))
                .thenReturn(signedUrl());

        URL signedPutUrl = storageProvider.requestUploadUrl(fileReference(false));

        assertThat(signedPutUrl.toString()).isEqualTo("http://localhost:4566/photo.png?token=abc");
    }

    @Test
    void shouldRequestPublicReadAclWhenFileAllowsPublicRead() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(false);
        Mockito.when(s3Template.createSignedPutURL(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(signedUrl());

        storageProvider.requestUploadUrl(fileReference(true));

        ArgumentCaptor<ObjectMetadata> captor = ArgumentCaptor.forClass(ObjectMetadata.class);
        Mockito.verify(s3Template).createSignedPutURL(
                Mockito.eq(BUCKET_NAME),
                Mockito.eq(FILE_NAME),
                Mockito.eq(Duration.ofMinutes(5)),
                captor.capture(),
                Mockito.eq(MediaType.IMAGE_PNG_VALUE));

        assertThat(captor.getValue().getAcl()).isEqualTo("public-read");
    }

    @Test
    void shouldNotSetAclWhenFileDoesNotAllowPublicRead() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(false);
        Mockito.when(s3Template.createSignedPutURL(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(signedUrl());

        storageProvider.requestUploadUrl(fileReference(false));

        ArgumentCaptor<ObjectMetadata> captor = ArgumentCaptor.forClass(ObjectMetadata.class);
        Mockito.verify(s3Template).createSignedPutURL(
                Mockito.eq(BUCKET_NAME),
                Mockito.eq(FILE_NAME),
                Mockito.eq(Duration.ofMinutes(5)),
                captor.capture(),
                Mockito.eq(MediaType.IMAGE_PNG_VALUE));

        assertThat(captor.getValue().getAcl()).isNull();
    }

    @Test
    void shouldThrowWhenRemoteFileAlreadyExists() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(true);

        assertThatThrownBy(() -> storageProvider.requestUploadUrl(fileReference(false)))
                .isInstanceOf(StorageProviderException.class)
                .hasMessage("Remote file photo.png already exists");

        Mockito.verify(s3Template, Mockito.never())
                .createSignedPutURL(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldWrapS3ExceptionWhenSignedPutUrlFails() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(false);
        stubSignedPutUrl(new S3Exception("s3 down", new RuntimeException("root cause")));

        assertThatThrownBy(() -> storageProvider.requestUploadUrl(fileReference(false)))
                .isInstanceOf(StorageProviderException.class)
                .hasMessage("Unknown error when tried to create presigned URL for file photo.png")
                .hasCauseInstanceOf(S3Exception.class);
    }

    @Test
    void shouldThrowWhenDeletingFileThatDoesNotExist() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(false);

        assertThatThrownBy(() -> storageProvider.deleteFile(FILE_NAME))
                .isInstanceOf(StorageProviderException.class)
                .hasMessage("Remote file photo.png was not found");

        Mockito.verify(s3Template, Mockito.never()).deleteObject(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldDeleteExistingFile() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(true);

        storageProvider.deleteFile(FILE_NAME);

        Mockito.verify(s3Template).deleteObject(BUCKET_NAME, FILE_NAME);
    }

    @Test
    void shouldWrapS3ExceptionWhenDeleteFails() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(true);
        Mockito.doThrow(new S3Exception("s3 down", new RuntimeException("root cause")))
                .when(s3Template).deleteObject(BUCKET_NAME, FILE_NAME);

        assertThatThrownBy(() -> storageProvider.deleteFile(FILE_NAME))
                .isInstanceOf(StorageProviderException.class)
                .hasMessage("Unknown error when tried to remove the file photo.png");
    }

    @Test
    void shouldDelegateFileExistsToS3Template() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(true);

        assertThat(storageProvider.fileExists(FILE_NAME)).isTrue();

        Mockito.verify(s3Template).objectExists(BUCKET_NAME, FILE_NAME);
    }

    @Test
    void shouldReturnFalseWhenFileDoesNotExist() {
        Mockito.when(s3Template.objectExists(BUCKET_NAME, FILE_NAME)).thenReturn(false);

        assertThat(storageProvider.fileExists(FILE_NAME)).isFalse();
    }
}
