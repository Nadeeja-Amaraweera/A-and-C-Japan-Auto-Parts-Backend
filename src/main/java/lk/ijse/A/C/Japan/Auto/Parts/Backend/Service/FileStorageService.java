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
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png", "webp", "gif", "svg")
    );

    public FileStorageService(@Value("${file.upload-dir:uploads/vehicles}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("📁 File upload directory initialized at: " + this.fileStorageLocation);
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
}
