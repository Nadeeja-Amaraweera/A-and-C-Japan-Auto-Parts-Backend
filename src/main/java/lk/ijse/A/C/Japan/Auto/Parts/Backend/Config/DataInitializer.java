package lk.ijse.A.C.Japan.Auto.Parts.Backend.Config;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.*;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.*;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleImageRepository vehicleImageRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Seed Admin User
        User admin = userRepository.findByUserEmail("admin@acjapan.com").orElse(null);
        if (admin == null) {
            admin = new User();
            admin.setUserStringId("U001");
            admin.setUserName("System Admin");
            admin.setUserEmail("admin@acjapan.com");
            admin.setUserPassword(passwordEncoder.encode("admin123"));
            admin.setUserRole(Role.ADMIN);
            admin.setUserStatus(UserStatus.ACTIVE);
            admin.setUserPhone("+81 90 1234 5678");
            admin.setUserAddress("Tokyo, Japan");
            admin = userRepository.save(admin);
            System.out.println("✅ Seeded Admin: admin@acjapan.com / admin123");
        }

        // 2. Seed Supplier User and Profile
        User supplierUser = userRepository.findByUserEmail("supplier@acjapan.com").orElse(null);
        if (supplierUser == null) {
            supplierUser = new User();
            supplierUser.setUserStringId("U002");
            supplierUser.setUserName("Tokyo Auto Spares");
            supplierUser.setUserEmail("supplier@acjapan.com");
            supplierUser.setUserPassword(passwordEncoder.encode("supplier123"));
            supplierUser.setUserRole(Role.SUPPLIER);
            supplierUser.setUserStatus(UserStatus.ACTIVE);
            supplierUser.setUserPhone("+81 80 9876 5432");
            supplierUser.setUserAddress("Yokohama, Japan");
            supplierUser = userRepository.save(supplierUser);
            System.out.println("✅ Seeded Supplier User: supplier@acjapan.com / supplier123");
        }

        Supplier supplier = supplierRepository.findByUser_UserId(supplierUser.getUserId()).orElse(null);
        if (supplier == null) {
            supplier = new Supplier();
            supplier.setUser(supplierUser);
            supplier.setSupplierName("Tokyo Auto Spares Co., Ltd");
            supplier.setSupplierBusinessName("Tokyo Auto Spares Co., Ltd");
            supplier.setSupplierBusinessAddress("1-2-3 Minato-ku, Tokyo, Japan");
            supplier.setSupplierContactNumber("+81 80 9876 5432");
            supplier.setRegistrationDocUrl("documents/supplier_reg_doc.pdf");
            supplier.setSupplierStatus(SupplierStatus.APPROVED);
            supplier.setCreatedAt(LocalDateTime.now());
            supplier = supplierRepository.save(supplier);
            System.out.println("✅ Seeded Approved Supplier Profile");
        }

        // 3. Seed Sample Customer User
        User customer = userRepository.findByUserEmail("customer@acjapan.com").orElse(null);
        if (customer == null) {
            customer = new User();
            customer.setUserStringId("U003");
            customer.setUserName("John Doe");
            customer.setUserEmail("customer@acjapan.com");
            customer.setUserPassword(passwordEncoder.encode("customer123"));
            customer.setUserRole(Role.CUSTOMER);
            customer.setUserStatus(UserStatus.ACTIVE);
            customer.setUserPhone("+1 555 019 2831");
            customer.setUserAddress("Los Angeles, CA, USA");
            customer = userRepository.save(customer);
            System.out.println("✅ Seeded Customer: customer@acjapan.com / customer123");
        }

        // 4. Seed Categories
        if (categoryRepository.count() == 0) {
            String[][] cats = {
                {"Engine Parts", "Complete engines, pistons, spark plugs, cylinder heads, timing belts and turbochargers"},
                {"Transmission & Drivetrain", "Manual & automatic gearboxes, clutches, flywheels, differentials, and drive shafts"},
                {"Brakes & Suspension", "Brake pads, rotors, calipers, coilovers, shock absorbers and sway bars"},
                {"Body & Lighting", "Headlights, tail lights, body kits, fenders, spoilers and bumpers"},
                {"Electrical & Sensors", "ECUs, alternators, oxygen sensors, ignition coils, and wiring harnesses"},
                {"Wheels & Tires", "JDM alloy wheels, racing rims, lug nuts and performance tires"}
            };
            for (String[] cat : cats) {
                Category c = new Category();
                c.setCategoryName(cat[0]);
                c.setCategorySlug(cat[0].toLowerCase().replaceAll("[^a-z0-9]+", "-"));
                c.setType("PART");
                c.setDescription(cat[1]);
                c.setIsActive(true);
                categoryRepository.save(c);
            }
            System.out.println("✅ Seeded 6 Categories");
        }

        List<Category> allCategories = categoryRepository.findAll();

        // 5. Seed Vehicles and Auctions
        if (auctionRepository.count() == 0) {
            // Vehicle 1: Toyota Land Cruiser Prado
            Vehicle v1 = new Vehicle();
            v1.setUser(supplierUser);
            v1.setTitle("2021 Toyota Land Cruiser Prado TX L");
            v1.setBrand("Toyota");
            v1.setModel("Land Cruiser Prado TX L");
            v1.setYear(2021);
            v1.setMileage(34500);
            v1.setPrice(BigDecimal.valueOf(38500.00));
            v1.setVehicleCondition(ConditionType.USED);
            v1.setTransmission(TransmissionType.AUTOMATIC);
            v1.setFuelType(FuelType.PETROL);
            v1.setEngineSize("2700cc");
            v1.setExteriorColor("Pearl White");
            v1.setVin("TRJ150-0198421");
            v1.setDescription("Meticulously maintained Japanese auction grade 4.5/B, sunroof, Modellista aero package, leather interior, multi-terrain monitor.");
            v1.setStatus("APPROVED");
            v1.setCreatedAt(LocalDateTime.now());
            v1.setUpdatedAt(LocalDateTime.now());
            v1 = vehicleRepository.save(v1);

            VehicleImage img1 = new VehicleImage();
            img1.setVehicle(v1);
            img1.setImageUrl("https://images.unsplash.com/photo-1594502184342-2e12f877aa73?auto=format&fit=crop&w=800&q=80");
            img1.setIsPrimary(true);
            vehicleImageRepository.save(img1);

            Auction a1 = new Auction();
            a1.setVehicle(v1);
            a1.setSeller(supplierUser);
            a1.setHighestBidder(customer);
            a1.setTitle("2021 Toyota Land Cruiser Prado TX L");
            a1.setStartingPrice(BigDecimal.valueOf(32000.00));
            a1.setCurrentBid(BigDecimal.valueOf(38500.00));
            a1.setReservePrice(BigDecimal.valueOf(36000.00));
            a1.setMinBidIncrement(BigDecimal.valueOf(500.00));
            a1.setStartDate(LocalDateTime.now().minusDays(1));
            a1.setEndDate(LocalDateTime.now().plusDays(4));
            a1.setStatus(AuctionStatus.ACTIVE);
            a1.setIsApproved(true);
            a1.setIsFeatured(true);
            a1.setBidCount(8);
            a1.setBidderCount(3);
            a1.setCreatedAt(LocalDateTime.now());
            a1.setUpdatedAt(LocalDateTime.now());
            a1 = auctionRepository.save(a1);

            // Vehicle 2: Nissan GT-R
            Vehicle v2 = new Vehicle();
            v2.setUser(supplierUser);
            v2.setTitle("2019 Nissan GT-R Premium (R35)");
            v2.setBrand("Nissan");
            v2.setModel("GT-R Premium R35");
            v2.setYear(2019);
            v2.setMileage(18200);
            v2.setPrice(BigDecimal.valueOf(74000.00));
            v2.setVehicleCondition(ConditionType.USED);
            v2.setTransmission(TransmissionType.AUTOMATIC);
            v2.setFuelType(FuelType.PETROL);
            v2.setEngineSize("3800cc Twin-Turbo");
            v2.setExteriorColor("Super Silver");
            v2.setVin("R35-0812991");
            v2.setDescription("Immaculate condition Godzilla! Full Nissan High Performance Center service records, Bose sound system, titanium exhaust.");
            v2.setStatus("APPROVED");
            v2.setCreatedAt(LocalDateTime.now());
            v2.setUpdatedAt(LocalDateTime.now());
            v2 = vehicleRepository.save(v2);

            VehicleImage img2 = new VehicleImage();
            img2.setVehicle(v2);
            img2.setImageUrl("https://images.unsplash.com/photo-1617814076367-b759c7d7e738?auto=format&fit=crop&w=800&q=80");
            img2.setIsPrimary(true);
            vehicleImageRepository.save(img2);

            Auction a2 = new Auction();
            a2.setVehicle(v2);
            a2.setSeller(supplierUser);
            a2.setTitle("2019 Nissan GT-R Premium (R35)");
            a2.setStartingPrice(BigDecimal.valueOf(65000.00));
            a2.setCurrentBid(BigDecimal.valueOf(74000.00));
            a2.setReservePrice(BigDecimal.valueOf(70000.00));
            a2.setMinBidIncrement(BigDecimal.valueOf(1000.00));
            a2.setStartDate(LocalDateTime.now().minusDays(2));
            a2.setEndDate(LocalDateTime.now().plusDays(3));
            a2.setStatus(AuctionStatus.ACTIVE);
            a2.setIsApproved(true);
            a2.setIsFeatured(true);
            a2.setBidCount(14);
            a2.setBidderCount(5);
            a2.setCreatedAt(LocalDateTime.now());
            a2.setUpdatedAt(LocalDateTime.now());
            a2 = auctionRepository.save(a2);

            // Vehicle 3: Honda Civic Type R
            Vehicle v3 = new Vehicle();
            v3.setUser(supplierUser);
            v3.setTitle("2020 Honda Civic Type R (FK8)");
            v3.setBrand("Honda");
            v3.setModel("Civic Type R FK8");
            v3.setYear(2020);
            v3.setMileage(21000);
            v3.setPrice(BigDecimal.valueOf(33500.00));
            v3.setVehicleCondition(ConditionType.USED);
            v3.setTransmission(TransmissionType.MANUAL);
            v3.setFuelType(FuelType.PETROL);
            v3.setEngineSize("2000cc VTEC Turbo");
            v3.setExteriorColor("Championship White");
            v3.setVin("FK8-1200455");
            v3.setDescription("Legendary hot hatch in iconic Championship White. 6-speed manual with rev-matching, Brembo brakes, bucket seats.");
            v3.setStatus("APPROVED");
            v3.setCreatedAt(LocalDateTime.now());
            v3.setUpdatedAt(LocalDateTime.now());
            v3 = vehicleRepository.save(v3);

            VehicleImage img3 = new VehicleImage();
            img3.setVehicle(v3);
            img3.setImageUrl("https://images.unsplash.com/photo-1605559424843-9e4c228bf1c2?auto=format&fit=crop&w=800&q=80");
            img3.setIsPrimary(true);
            vehicleImageRepository.save(img3);

            Auction a3 = new Auction();
            a3.setVehicle(v3);
            a3.setSeller(supplierUser);
            a3.setTitle("2020 Honda Civic Type R (FK8)");
            a3.setStartingPrice(BigDecimal.valueOf(28000.00));
            a3.setCurrentBid(BigDecimal.valueOf(33500.00));
            a3.setReservePrice(BigDecimal.valueOf(32000.00));
            a3.setMinBidIncrement(BigDecimal.valueOf(500.00));
            a3.setStartDate(LocalDateTime.now().minusDays(1));
            a3.setEndDate(LocalDateTime.now().plusDays(5));
            a3.setStatus(AuctionStatus.ACTIVE);
            a3.setIsApproved(true);
            a3.setIsFeatured(true);
            a3.setBidCount(6);
            a3.setBidderCount(2);
            a3.setCreatedAt(LocalDateTime.now());
            a3.setUpdatedAt(LocalDateTime.now());
            a3 = auctionRepository.save(a3);

            // Add sample bid on a1 by customer
            Bid b1 = new Bid();
            b1.setAuction(a1);
            b1.setUser(customer);
            b1.setBidAmount(BigDecimal.valueOf(38500.00));
            b1.setPlacedAt(LocalDateTime.now().minusHours(3));
            b1.setBidStatus(BidStatus.WINNING);
            bidRepository.save(b1);

            System.out.println("✅ Seeded 3 Sample Vehicles & Active Auctions");
        }

        // 6. Seed Products (Auto Parts)
        if (productRepository.count() == 0 && !allCategories.isEmpty()) {
            Category engineCat = allCategories.get(0);
            Category transCat = allCategories.size() > 1 ? allCategories.get(1) : engineCat;
            Category brakeCat = allCategories.size() > 2 ? allCategories.get(2) : engineCat;

            Object[][] prods = {
                {
                    "OEM Toyota High-Performance Brake Pad Set",
                    "04465-48150",
                    "Genuine Toyota front ceramic brake pads offering superior stopping power with minimal brake dust and zero rotor wear. Fits Land Cruiser Prado and Harrier.",
                    BigDecimal.valueOf(125.00),
                    35,
                    QualityType.BRAND_NEW,
                    "Toyota Land Cruiser Prado 150 / Harrier / Lexus RX",
                    "Toyota OEM",
                    brakeCat,
                    "https://images.unsplash.com/photo-1600705722908-bab1e61c0b4d?auto=format&fit=crop&w=600&q=80"
                },
                {
                    "Honda K20A / K24 Complete Timing Chain & Tensioner Kit",
                    "14401-PNA-004",
                    "Complete Japanese OEM timing kit including heavy duty timing chain, guide rails, and hydraulic tensioner. Essential preventative maintenance.",
                    BigDecimal.valueOf(249.00),
                    20,
                    QualityType.BRAND_NEW,
                    "Honda Civic Type R (EP3/FN2/FD2), Integra DC5, Accord Euro R",
                    "Honda Genuine Parts",
                    engineCat,
                    "https://images.unsplash.com/photo-1486006920555-c77dce18193b?auto=format&fit=crop&w=600&q=80"
                },
                {
                    "Nissan VR38DETT Twin Turbocharger Assembly (OEM Spec)",
                    "14411-JF01A",
                    "Twin IHI turbochargers calibrated to factory R35 specifications. High heat resistance turbine wheels, rapid spool response.",
                    BigDecimal.valueOf(1690.00),
                    6,
                    QualityType.REMANUFACTURED,
                    "Nissan GT-R R35 (2008-2022)",
                    "IHI / Nissan",
                    engineCat,
                    "https://images.unsplash.com/photo-1580273916550-e323be2ae537?auto=format&fit=crop&w=600&q=80"
                },
                {
                    "Exedy Hyper Single Clutch Kit",
                    "HH02SD",
                    "High torque capacity lightweight chromoly flywheel and cerametallic clutch disc. Delivers sharp engagement for track and spirited road driving.",
                    BigDecimal.valueOf(699.00),
                    12,
                    QualityType.BRAND_NEW,
                    "Honda Civic FK8 / FL5 / S2000 AP1/AP2",
                    "Exedy Japan",
                    transCat,
                    "https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=600&q=80"
                },
                {
                    "Tein Street Flex Z Fully Adjustable Coilovers",
                    "VSQ54-C1SS3",
                    "16-level damping force adjustment with twin-tube system for maximum ride comfort on city streets and controlled firmness on winding roads.",
                    BigDecimal.valueOf(870.00),
                    15,
                    QualityType.BRAND_NEW,
                    "Toyota 86 / Subaru BRZ (ZN6/ZC6)",
                    "Tein Japan",
                    brakeCat,
                    "https://images.unsplash.com/photo-1619642751034-765dfdf7c58e?auto=format&fit=crop&w=600&q=80"
                },
                {
                    "Denso Iridium Tough Spark Plugs (Set of 4)",
                    "VFXEH22",
                    "Ultra-fine 0.4mm iridium center electrode and platinum ground electrode ensuring stable ignition and up to 100,000 km lifespan.",
                    BigDecimal.valueOf(55.00),
                    50,
                    QualityType.BRAND_NEW,
                    "Universal Japanese 4-Cylinder Engines (Toyota, Nissan, Mazda, Honda)",
                    "Denso Japan",
                    engineCat,
                    "https://images.unsplash.com/photo-1597687210367-a49155529124?auto=format&fit=crop&w=600&q=80"
                }
            };

            for (Object[] pData : prods) {
                Product p = new Product();
                p.setProductName((String) pData[0]);
                p.setSku((String) pData[1]);
                p.setDescription((String) pData[2]);
                p.setPrice((BigDecimal) pData[3]);
                p.setStockQuantity((Integer) pData[4]);
                p.setQualityType((QualityType) pData[5]);
                p.setCompatibleModel((String) pData[6]);
                p.setBrand((String) pData[7]);
                p.setCategory((Category) pData[8]);
                p.setSupplier(supplierUser);
                p.setIsActive(true);
                p.setIsFeatured(true);
                p.setStatus("APPROVED");
                p.setCreatedAt(LocalDateTime.now());
                p.setUpdatedAt(LocalDateTime.now());
                p = productRepository.save(p);

                ProductImage pImg = new ProductImage();
                pImg.setProduct(p);
                pImg.setImageUrl((String) pData[9]);
                pImg.setIsPrimary(true);
                productImageRepository.save(pImg);
            }

            System.out.println("✅ Seeded 6 Sample Auto Parts Products");
        }
    }
}
