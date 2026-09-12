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
}
