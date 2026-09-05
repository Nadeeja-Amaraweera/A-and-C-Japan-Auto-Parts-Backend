package lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    private long supplierId;
    private String supplierName;
    private String supplierBusinessName;
    private String supplierBusinessAddress;
    private String supplierContactNumber;

}