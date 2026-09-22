package com.eskcti.algashop.product.catalog.infrastructure.storage.fake;

import com.eskcti.algashop.product.catalog.application.storage.FileReference;
import com.eskcti.algashop.product.catalog.application.storage.StorageProvider;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;

@Component
public class StorageProviderFakeImpl implements StorageProvider {

    @Override
    public URI requestUploadUrl(FileReference fileReference) {
        return URI.create(String.format("http://localhost:4566/%s?token=%s",
                fileReference.getFileName(), UUID.randomUUID()));
    }

    @Override
    public void deleteFile(String remoteFileName) {

    }

    @Override
    public boolean fileExists(String remoteFileName) {
        return false;
    }
}
