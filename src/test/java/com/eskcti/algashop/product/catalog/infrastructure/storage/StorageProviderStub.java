package com.eskcti.algashop.product.catalog.infrastructure.storage;

import com.eskcti.algashop.product.catalog.application.storage.FileReference;
import com.eskcti.algashop.product.catalog.application.storage.StorageProvider;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Deterministic {@link StorageProvider} used by web layer tests to avoid
 * depending on a real S3 bucket.
 */
public class StorageProviderStub implements StorageProvider {

    @Override
    public boolean healthCheck() {
        return true;
    }

    @Override
    public URL requestUploadUrl(FileReference fileReference) {
        return url("http://localhost:4566/" + fileReference.getFileName() + "?token=stub");
    }

    @Override
    public void deleteFile(String remoteFileName) {
    }

    @Override
    public boolean fileExists(String remoteFileName) {
        return true;
    }

    private static URL url(String value) {
        try {
            return URI.create(value).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
