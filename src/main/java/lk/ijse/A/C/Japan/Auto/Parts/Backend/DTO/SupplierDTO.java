package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.SupplierStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {
    private Long supplierId;
    private Long userId;
    private String userEmail;

    @JsonAlias({"name", "contactPerson"})
    private String supplierName;

    @JsonAlias({"businessName", "supplierBusinessName"})
    private String supplierBusinessName;

    @JsonAlias({"address", "businessAddress", "supplierBusinessAddress"})
    private String supplierBusinessAddress;

    @JsonAlias({"phone", "contactNumber", "phoneNumber", "supplierContactNumber"})
    private String supplierContactNumber;

    private String registrationDocUrl;

    @JsonAlias({"document", "businessDocument", "registrationDocument", "businessRegistrationDocumentPath"})
    private String businessRegistrationDocument;

    private Boolean hasBusinessDocument;
    private String businessRegistrationDocumentUrl;

    private SupplierStatus supplierStatus;
    private String rejectionReason;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;

    // Helper aliases for JSON serialization and frontend convenience
    @JsonProperty("businessName")
    public String getBusinessName() {
        return supplierBusinessName;
    }

    public void setBusinessName(String b) {
        if (this.supplierBusinessName == null || this.supplierBusinessName.isEmpty()) {
            this.supplierBusinessName = b;
        }
    }

    @JsonProperty("businessAddress")
    public String getBusinessAddress() {
        return supplierBusinessAddress;
    }

    public void setBusinessAddress(String a) {
        if (this.supplierBusinessAddress == null || this.supplierBusinessAddress.isEmpty()) {
            this.supplierBusinessAddress = a;
        }
    }

    @JsonProperty("address")
    public String getAddress() {
        return supplierBusinessAddress;
    }

    public void setAddress(String a) {
        if (this.supplierBusinessAddress == null || this.supplierBusinessAddress.isEmpty()) {
            this.supplierBusinessAddress = a;
        }
    }

    @JsonProperty("contactPerson")
    public String getContactPerson() {
        return supplierName;
    }

    public void setContactPerson(String c) {
        if (this.supplierName == null || this.supplierName.isEmpty()) {
            this.supplierName = c;
        }
    }

    @JsonProperty("phone")
    public String getPhone() {
        return supplierContactNumber;
    }

    public void setPhone(String p) {
        if (this.supplierContactNumber == null || this.supplierContactNumber.isEmpty()) {
            this.supplierContactNumber = p;
        }
    }

    @JsonProperty("email")
    public String getEmail() {
        return userEmail;
    }

    public void setEmail(String e) {
        this.userEmail = e;
    }

    @JsonProperty("businessRegistrationDocument")
    public String getBusinessRegistrationDocument() {
        if (businessRegistrationDocument != null && !businessRegistrationDocument.isEmpty()) {
            return businessRegistrationDocument;
        }
        return registrationDocUrl;
    }

    public void setBusinessRegistrationDocument(String doc) {
        this.businessRegistrationDocument = doc;
        if (this.registrationDocUrl == null || this.registrationDocUrl.isEmpty()) {
            this.registrationDocUrl = doc;
        }
    }

    @JsonProperty("hasBusinessDocument")
    public Boolean getHasBusinessDocument() {
        String doc = getBusinessRegistrationDocument();
        return doc != null && !doc.trim().isEmpty() && !doc.contains("documents/registration_doc.pdf");
    }

    @JsonProperty("businessRegistrationDocumentUrl")
    public String getBusinessRegistrationDocumentUrl() {
        if (Boolean.TRUE.equals(getHasBusinessDocument())) {
            Long id = supplierId != null ? supplierId : userId;
            if (id != null) {
                return "/api/v1/admin/suppliers/" + id + "/business-document";
            }
        }
        return null;
    }
}

