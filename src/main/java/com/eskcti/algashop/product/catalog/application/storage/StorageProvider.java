package com.eskcti.algashop.product.catalog.application.storage;

import java.net.URI;

public interface StorageProvider {
    URI requestUploadUrl(FileReference fileReference);
    void deleteFile(String remoteFileName);
    boolean fileExists(String remoteFileName);
}
