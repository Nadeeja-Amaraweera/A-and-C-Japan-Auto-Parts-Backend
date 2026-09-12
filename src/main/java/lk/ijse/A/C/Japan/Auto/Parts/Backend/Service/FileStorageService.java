package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import jakarta.annotation.PostConstruct;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception.CustomeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final Path supplierDocStorageLocation;
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png", "webp", "gif", "svg")
    );
    private static final Set<String> ALLOWED_DOC_EXTENSIONS = new HashSet<>(
            Arrays.asList("pdf", "jpg", "jpeg", "png")
    );
    private static final long MAX_DOC_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

    public FileStorageService(
            @Value("${file.upload-dir:uploads/vehicles}") String uploadDir,
            @Value("${file.supplier-doc-dir:uploads/supplier-documents}") String supplierDocDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.supplierDocStorageLocation = Paths.get(supplierDocDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.supplierDocStorageLocation);
            System.out.println("📁 File upload directory initialized at: " + this.fileStorageLocation);
            System.out.println("📁 Supplier document upload directory initialized at: " + this.supplierDocStorageLocation);
        } catch (IOException ex) {
            throw new CustomeException(500, "Could not create the upload directory: " + ex.getMessage());
        }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomeException(400, "Cannot store empty or null file");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalFilename.contains("..")) {
            throw new CustomeException(400, "Filename contains invalid path sequence: " + originalFilename);
        }

        // Validate extension
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
            extension = originalFilename.substring(i + 1).toLowerCase();
        }
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new CustomeException(400, "File extension '." + extension + "' is not supported. Allowed: " + ALLOWED_EXTENSIONS);
        }

        // Sanitize name: remove spaces/special characters, prefix with unique UUID
        String baseName = i > 0 ? originalFilename.substring(0, i) : originalFilename;
        String sanitizedBase = baseName.replaceAll("[^a-zA-Z0-9_-]", "_");
        if (sanitizedBase.length() > 50) {
            sanitizedBase = sanitizedBase.substring(0, 50);
        }
        String uniqueFilename = UUID.randomUUID().toString().substring(0, 12) + "_" + sanitizedBase + "." + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            // Return public accessible endpoint path
            return "/api/v1/auth/images/" + uniqueFilename;
        } catch (IOException ex) {
            throw new CustomeException(500, "Could not store file " + uniqueFilename + ": " + ex.getMessage());
        }
    }

    public List<String> storeFiles(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        if (files == null || files.isEmpty()) {
            return urls;
        }
        for (MultipartFile f : files) {
            if (f != null && !f.isEmpty()) {
                urls.add(storeFile(f));
            }
        }
        return urls;
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new CustomeException(404, "File not found: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new CustomeException(404, "File not found: " + filename);
        }
    }

    public boolean deleteFile(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    public String storeSupplierDocument(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomeException(400, "Business registration document is required.");
        }

        if (file.getSize() > MAX_DOC_SIZE_BYTES) {
            throw new CustomeException(400, "Document size exceeds the maximum allowed size of 10MB.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalFilename.contains("..")) {
            throw new CustomeException(400, "Filename contains invalid path sequence: " + originalFilename);
        }

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        }

        if (!ALLOWED_DOC_EXTENSIONS.contains(extension)) {
            throw new CustomeException(400, "Unsupported document type: '." + extension + "'. Allowed formats: PDF, JPG, JPEG, PNG");
        }

        // Validate MIME type
        String contentType = file.getContentType();
        if (contentType != null && !contentType.isBlank()) {
            String lowerContent = contentType.toLowerCase();
            boolean validMime = lowerContent.equals("application/pdf")
                    || lowerContent.equals("image/jpeg")
                    || lowerContent.equals("image/jpg")
                    || lowerContent.equals("image/png")
                    || lowerContent.equals("application/octet-stream"); // fallback for some browsers
            if (!validMime) {
                throw new CustomeException(400, "Unsupported document content type: " + contentType + ". Allowed: PDF, JPG, JPEG, PNG");
            }
        }

        String baseName = dotIndex > 0 ? originalFilename.substring(0, dotIndex) : originalFilename;
        String sanitizedBase = baseName.replaceAll("[^a-zA-Z0-9_-]", "_");
        if (sanitizedBase.length() > 50) {
            sanitizedBase = sanitizedBase.substring(0, 50);
        }
        String uniqueFilename = "doc_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16) + "_" + sanitizedBase + "." + extension;

        try {
            Path targetLocation = this.supplierDocStorageLocation.resolve(uniqueFilename).normalize();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "uploads/supplier-documents/" + uniqueFilename;
        } catch (IOException ex) {
            throw new CustomeException(500, "Could not store business document " + uniqueFilename + ": " + ex.getMessage());
        }
    }

    public Resource loadSupplierDocumentAsResource(String documentRef) {
        if (documentRef == null || documentRef.trim().isEmpty()) {
            throw new CustomeException(404, "Business registration document not found.");
        }

        try {
            // Extract filename if full or relative path was stored
            String cleanFilename = Paths.get(documentRef).getFileName().toString();
            Path filePath = this.supplierDocStorageLocation.resolve(cleanFilename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new CustomeException(404, "Business registration document file not found: " + cleanFilename);
            }
        } catch (MalformedURLException ex) {
            throw new CustomeException(404, "Invalid document resource URL: " + ex.getMessage());
        }
    }

    public boolean deleteSupplierDocument(String documentRef) {
        if (documentRef == null || documentRef.trim().isEmpty()) {
            return false;
        }
        try {
            String cleanFilename = Paths.get(documentRef).getFileName().toString();
            Path filePath = this.supplierDocStorageLocation.resolve(cleanFilename).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }
}
