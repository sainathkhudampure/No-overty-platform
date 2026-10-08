package com.nopoverty.platform.config;

import com.nopoverty.platform.model.*;
import com.nopoverty.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private SavedSchemeRepository savedSchemeRepository;

    @Autowired
    private ApplicationRecordRepository applicationRecordRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() > 0 && schemeRepository.count() > 0) {
            System.out.println("Data already initialized. Skipping auto-seed.");
            return;
        }

        System.out.println("Initializing Seed Data for NO POVERTY Platform...");

        // 1. Roles
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER", "Standard citizen / beneficiary user role")));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN", "Welfare scheme and portal administrator role")));
        Role devRole = roleRepository.findByName("ROLE_DEVELOPER_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_DEVELOPER_ADMIN", "Technical administrator with full analytics")));

        // 2. Users
        String defaultCitizenPass = passwordEncoder.encode("password123");
        String defaultAdminPass = passwordEncoder.encode("admin123");
        String defaultDevPass = passwordEncoder.encode("devadmin123");

        User citizen = new User();
        citizen.setFullName("Ramesh Kumar");
        citizen.setEmail("citizen@example.com");
        citizen.setMobile("9876543210");
        citizen.setPasswordHash(defaultCitizenPass);
        citizen.setState("Karnataka");
        citizen.setDistrict("Bengaluru Urban");
        citizen.setDateOfBirth(LocalDate.of(1988, 6, 15));
        citizen.setPreferredLanguage("en");
        citizen.getRoles().add(userRole);
        userRepository.save(citizen);

        UserProfile citizenProfile = new UserProfile();
        citizenProfile.setUser(citizen);
        citizenProfile.setGender("MALE");
        citizenProfile.setAddress("Village Kengeri, Hobli");
        citizenProfile.setPincode("560060");
        citizenProfile.setAnnualFamilyIncome(new BigDecimal("140000.00"));
        citizenProfile.setEmploymentStatus("SELF_EMPLOYED");
        citizenProfile.setFamilySize(4);
        citizenProfile.setEducationLevel("HIGHER_SECONDARY");
        citizenProfile.setFarmerStatus(true);
        citizenProfile.setLandSizeAcres(new BigDecimal("2.50"));
        citizenProfile.setCropType("Ragi & Vegetables");
        citizenProfile.setHouseOwnership("OWNED_KUTCHA");
        citizenProfile.setAreaType("RURAL");
        citizenProfile.setCompletionPercentage(85);
        userProfileRepository.save(citizenProfile);

        // Citizen Family
        FamilyMember fm1 = new FamilyMember();
        fm1.setUser(citizen);
        fm1.setName("Parvathi Kumar");
        fm1.setRelationship("SPOUSE");
        fm1.setAge(34);
        fm1.setGender("FEMALE");
        fm1.setOccupation("HOMEMAKER");
        fm1.setIncomeContribution(BigDecimal.ZERO);
        familyMemberRepository.save(fm1);

        FamilyMember fm2 = new FamilyMember();
        fm2.setUser(citizen);
        fm2.setName("Basappa Kumar");
        fm2.setRelationship("FATHER");
        fm2.setAge(68);
        fm2.setGender("MALE");
        fm2.setOccupation("RETIRED_FARMER");
        fm2.setIncomeContribution(new BigDecimal("15000.00"));
        fm2.setSeniorCitizenStatus(true);
        familyMemberRepository.save(fm2);

        // Student User
        User student = new User();
        student.setFullName("Pooja Sharma");
        student.setEmail("pooja.student@example.com");
        student.setMobile("9811223344");
        student.setPasswordHash(defaultCitizenPass);
        student.setState("Maharashtra");
        student.setDistrict("Pune");
        student.setDateOfBirth(LocalDate.of(2003, 9, 22));
        student.setPreferredLanguage("en");
        student.getRoles().add(userRole);
        userRepository.save(student);

        UserProfile studentProfile = new UserProfile();
        studentProfile.setUser(student);
        studentProfile.setGender("FEMALE");
        studentProfile.setAddress("Shivajinagar, Flat 4B");
        studentProfile.setPincode("411005");
        studentProfile.setAnnualFamilyIncome(new BigDecimal("95000.00"));
        studentProfile.setEmploymentStatus("UNEMPLOYED");
        studentProfile.setFamilySize(3);
        studentProfile.setEducationLevel("UNDERGRADUATE");
        studentProfile.setStudentStatus(true);
        studentProfile.setInstitutionType("GOVERNMENT_COLLEGE");
        studentProfile.setCourseName("B.Sc Computer Science");
        studentProfile.setHouseOwnership("RENTED");
        studentProfile.setAreaType("URBAN");
        studentProfile.setCompletionPercentage(90);
        userProfileRepository.save(studentProfile);

        // Admin User
        User admin = new User();
        admin.setFullName("System Administrator");
        admin.setEmail("admin@nopoverty.gov.in");
        admin.setMobile("9899887766");
        admin.setPasswordHash(defaultAdminPass);
        admin.setState("Delhi");
        admin.setDistrict("New Delhi");
        admin.setDateOfBirth(LocalDate.of(1980, 1, 1));
        admin.setPreferredLanguage("en");
        admin.getRoles().add(adminRole);
        admin.getRoles().add(userRole);
        userRepository.save(admin);

        // Dev Admin User
        User dev = new User();
        dev.setFullName("Chief Developer");
        dev.setEmail("dev@nopoverty.gov.in");
        dev.setMobile("9800112233");
        dev.setPasswordHash(defaultDevPass);
        dev.setState("Delhi");
        dev.setDistrict("New Delhi");
        dev.setDateOfBirth(LocalDate.of(1985, 5, 12));
        dev.setPreferredLanguage("en");
        dev.getRoles().add(devRole);
        dev.getRoles().add(adminRole);
        dev.getRoles().add(userRole);
        userRepository.save(dev);

        // 3. Beneficiary Categories
        Map<String, Category> catMap = new HashMap<>();
        String[][] catData = {
            {"student", "Students & Youth", "Scholarships, fee waivers, education loans, and higher education assistance", "fa-solid fa-graduation-cap", "1"},
            {"farmer", "Farmers & Agriculture", "Crop insurance, income support, seeds, fertilizers, and irrigation credit", "fa-solid fa-seedling", "2"},
            {"senior_citizen", "Senior Citizens", "Old age pension, medical assistance, and elder care welfare programs", "fa-solid fa-person-cane", "3"},
            {"employment", "Job Seekers & Livelihood", "Skill development training, rural employment guarantees, and micro-credit", "fa-solid fa-briefcase", "4"},
            {"housing", "Housing & Shelter", "Affordable housing subsidies for rural and urban low-income families", "fa-solid fa-house-chimney", "5"},
            {"education", "Education Assistance", "Primary, secondary, and vocational technical education support", "fa-solid fa-book-open-reader", "6"},
            {"food_assistance", "Food Security & Nutrition", "Subsidized rations, mid-day meals, and nutritional food support", "fa-solid fa-bowl-rice", "7"},
            {"healthcare", "Healthcare & Medical Relief", "Comprehensive health insurance, free treatments, and maternal healthcare", "fa-solid fa-heart-pulse", "8"},
            {"women_children", "Women & Children", "Maternal cash benefits, girl child education, and self-help group loans", "fa-solid fa-person-breastfeeding", "9"},
            {"disability", "Persons with Disabilities", "Divyangjan pension, assistive aids, accessible education, and transport concessions", "fa-solid fa-wheelchair", "10"}
        };

        for (String[] cd : catData) {
            Category c = new Category(cd[0], cd[1], cd[2], cd[3], Integer.parseInt(cd[4]));
            catMap.put(cd[0], categoryRepository.save(c));
        }

        // 4. Schemes Seeding
        createScheme(
            "PM-KISAN", "PM Kisan Samman Nidhi Yojana", "Ministry of Agriculture & Farmers Welfare",
            "Financial assistance of ₹6,000 per year in three equal installments directly transferred into Aadhaar-linked bank accounts of landholding farmer families.",
            "The Pradhan Mantri Kisan Samman Nidhi (PM-KISAN) is a central sector scheme with 100% funding from Government of India. The scheme aims to augment the income of all landholding farmers families across the country to enable them to take care of expenses related to agriculture and domestic needs.",
            "To provide income support to all landholding eligible farmers families across the country to buy farm inputs and secure food safety.",
            "Direct benefit transfer (DBT) of ₹6,000 annually payable in 3 quarterly installments of ₹2,000 directly to verified bank account.",
            "Step 1: Check eligibility criteria. Step 2: Ensure Aadhaar is linked to bank account and mobile number. Step 3: Visit official portal at https://pmkisan.gov.in. Step 4: Click on Farmers Corner > New Farmer Registration. Step 5: Enter Aadhaar number and land records details. Step 6: Submit application and note Acknowledgement Number. Step 7: Track verification via state land revenue office.",
            "All India", "https://pmkisan.gov.in", "https://pmkisan.gov.in/RegistrationFormNew.aspx", 1420L, 230L,
            Set.of(catMap.get("farmer")),
            18, 100, new BigDecimal("9999999"), "ALL", false, true, false, false, "All India",
            "Must be a cultivable landholding farmer family whose names appear in official state land records.",
            List.of(
                new SchemeDocument("Aadhaar Card", true, "Mandatory biometric Aadhaar linked to bank account"),
                new SchemeDocument("Land Ownership Record (Khata/RoR)", true, "Copy of Record of Rights showing cultivable land holding"),
                new SchemeDocument("Bank Passbook / Cancelled Cheque", true, "DBT-enabled bank account details")
            )
        );

        createScheme(
            "PMAY-G", "Pradhan Mantri Awaas Yojana - Gramin (PMAY-G)", "Ministry of Rural Development",
            "Financial grant of up to ₹1,20,000 in plain areas and ₹1,30,000 in hilly/difficult areas for pucca house construction with basic amenities for homeless & kutcha house dwellers.",
            "PMAY-G aims to provide a pucca house with basic amenities to all houseless households and households living in kutcha and dilapidated houses in rural areas. Beneficiary selection is conducted objectively through SECC 2011 and Awaas+ surveys verified by Gram Sabhas.",
            "To help rural poor households construct durable, disaster-resilient housing with sanitation facilities and clean energy connections.",
            "Direct financial assistance of ₹1.20 Lakh in plains and ₹1.30 Lakh in hilly/North-Eastern states, plus 90-95 days of unskilled labor under MGNREGA and ₹12,000 toilet grant under Swachh Bharat.",
            "Step 1: Verify family name in SECC/Awaas+ priority list at Gram Panchayat. Step 2: Collect bank passbook and land possession proof. Step 3: Gram Rozgar Sevak or Panchayat Secretary uploads geotagged photos of existing house. Step 4: Sanction order issued by Block Development Officer. Step 5: Construction installments released in 3-4 stages following geotag inspection.",
            "All India", "https://pmayg.nic.in", "https://awaassoft.nic.in", 980L, 185L,
            Set.of(catMap.get("housing")),
            18, 100, new BigDecimal("150000"), "ALL", false, false, false, false, "All India",
            "Houseless families or households living in zero, one, or two-room houses with kutcha wall and roof.",
            List.of(
                new SchemeDocument("Aadhaar Card of Head of Family", true, "Biometric identity proof of applicant"),
                new SchemeDocument("BPL Ration Card / SECC Number", true, "Verification of low-income rural status"),
                new SchemeDocument("Land / Site Possession Certificate", true, "Proof of site possession for pucca house construction"),
                new SchemeDocument("Bank Account Passbook", true, "Active bank account linked to Aadhaar")
            )
        );

        createScheme(
            "PM-JAY", "Ayushman Bharat PM Jan Arogya Yojana (AB PM-JAY)", "National Health Authority / MoHFW",
            "Health insurance coverage of up to ₹5,00,000 per family per year for secondary and tertiary care hospitalization across public and empaneled private hospitals.",
            "Ayushman Bharat PM-JAY is the world’s largest government-funded healthcare assurance scheme. It covers over 12 crore poor and vulnerable families (approximately 55 crore beneficiaries) without any cap on family size or age, ensuring cashless treatment for over 1,900 medical procedures.",
            "To eliminate catastrophic health expenditures and provide universal secondary and tertiary healthcare security to low-income families.",
            "Cashless and paperless inpatient health insurance cover of up to ₹5 Lakhs per family per year, covering pre-hospitalization, tests, surgery, ICU, and post-discharge medicines.",
            "Step 1: Check eligibility online at https://beneficiary.nha.gov.in using mobile number or Ration Card number. Step 2: Visit nearest Common Service Centre (CSC) or Ayushman Mitra desk at any empaneled hospital. Step 3: Complete e-KYC using biometric Aadhaar authentication. Step 4: Download Ayushman Bharat Golden Card. Step 5: Present card during hospital admission for cashless treatment.",
            "All India", "https://pmjay.gov.in", "https://beneficiary.nha.gov.in", 2150L, 412L,
            Set.of(catMap.get("healthcare")),
            0, 120, new BigDecimal("250000"), "ALL", false, false, false, false, "All India",
            "Households identified under rural deprivation categories or designated occupational criteria under SECC database.",
            List.of(
                new SchemeDocument("Aadhaar Card / Ration Card", true, "Identification and family entitlement verification"),
                new SchemeDocument("Ration Card (NFSA / BPL)", true, "State-issued food security card showing family member names")
            )
        );

        createScheme(
            "NSP-POST-MATRIC", "National Post-Matric Scholarship for SC/ST/OBC Students", "Ministry of Social Justice and Empowerment",
            "Full tuition fee reimbursement and monthly maintenance allowance for underprivileged students pursuing post-matriculation, technical, or university degree courses.",
            "Centrally sponsored scholarship scheme designed to provide financial assistance to students belonging to SC, ST, OBC, and economically weaker sections studying at post-matriculation or post-secondary stage to enable them to complete their education.",
            "To drastically reduce drop-out rates in higher education among underprivileged students and empower youth through technical and university credentials.",
            "Complete reimbursement of compulsory non-refundable college/university tuition fees, plus monthly maintenance allowance up to ₹1,200/month, book grants, and study tour allowances.",
            "Step 1: Check income eligibility (family income <= ₹2.5 Lakhs/year). Step 2: Visit National Scholarship Portal (NSP) at https://scholarships.gov.in. Step 3: Register as New Student with Aadhaar and Student OTR. Step 4: Fill academic marks, institute code, and bank details. Step 5: Upload income certificate and caste certificate. Step 6: Institute and State Nodal Officer verify credentials online. Step 7: Scholarship credited directly via PFMS DBT.",
            "All India", "https://scholarships.gov.in", "https://scholarships.gov.in/fresh/newstdRegfrmInstruction", 1840L, 310L,
            Set.of(catMap.get("student"), catMap.get("education")),
            15, 35, new BigDecimal("250000"), "ALL", true, false, false, false, "All India",
            "Currently enrolled regular student in Class 11, 12, ITI, Diploma, Undergraduate, or Postgraduate course with annual family income <= ₹2,50,000.",
            List.of(
                new SchemeDocument("Aadhaar Card of Student", true, "Identity verification on National Scholarship Portal"),
                new SchemeDocument("Income Certificate", true, "Revenue authority certificate showing family income under ₹2.5 Lakhs"),
                new SchemeDocument("Caste Certificate (SC/ST/OBC)", true, "Official caste certificate issued by competent Tehsildar/SDM"),
                new SchemeDocument("Previous Year Marksheet", true, "Academic proof of qualifying exam passing marks"),
                new SchemeDocument("Current Year Fee Receipt / Bonafide", true, "College enrollment and fee payment verification")
            )
        );

        createScheme(
            "PM-SVANIDHI", "PM Street Vendor's AtmaNirbhar Nidhi (PM SVANidhi)", "Ministry of Housing and Urban Affairs",
            "Collateral-free working capital micro-credit starting at ₹10,000 with 7% interest subsidy and cashback incentives on digital transactions for urban street vendors.",
            "PM SVANidhi was launched to facilitate affordable working capital loans to street vendors to resume their livelihoods. Vendors who repay on time become eligible for subsequent loan tranches of ₹20,000 and ₹50,000 with subsidized interest rates.",
            "To empower urban informal workers and micro-vendors with formal institutional banking credit and foster digital financial inclusion.",
            "Tranche 1: ₹10,000 loan; Tranche 2: ₹20,000 loan; Tranche 3: ₹50,000 loan with 7% interest subsidy credited directly to bank account, and up to ₹1,200 annual cashback on digital transactions.",
            "Step 1: Ensure possession of Urban Local Body (ULB) vending certificate or recommendation letter. Step 2: Visit https://pmsvanidhi.mohua.gov.in. Step 3: Enter Aadhaar-linked mobile number for OTP login. Step 4: Fill loan application form and select preferred lending bank. Step 5: ULB verifies vendor profile. Step 6: Bank sanctions and disburses loan without physical collateral.",
            "All India", "https://pmsvanidhi.mohua.gov.in", "https://pmsvanidhi.mohua.gov.in/Default/ViewLoanApplication", 720L, 115L,
            Set.of(catMap.get("employment")),
            18, 70, new BigDecimal("300000"), "ALL", false, false, false, false, "All India",
            "Street vendors, hawkers, thelewala, and informal retailers operating in urban centers with ULB Vending Certificate.",
            List.of(
                new SchemeDocument("Aadhaar Card", true, "Identity and mobile linkage"),
                new SchemeDocument("Vending Certificate / Urban ID Card", true, "Proof of vending issued by Urban Local Body"),
                new SchemeDocument("Bank Account Details", true, "Account for loan disbursement and interest subsidy credit")
            )
        );

        createScheme(
            "IGNOAPS", "Indira Gandhi National Old Age Pension Scheme (NSAP)", "Ministry of Rural Development",
            "Monthly social security cash pension for citizens aged 60 and above living below the poverty line (BPL) to ensure dignified elderly living.",
            "A key component of the National Social Assistance Programme (NSAP), IGNOAPS provides non-contributory direct income support to elderly senior citizens from BPL households who have no other regular source of livelihood.",
            "To provide basic economic security and dignified social protection to indigent elderly citizens in rural and urban areas.",
            "Monthly direct cash pension of ₹200-₹500 from Central Government plus state-specific matching top-up (amounting to ₹1,000 - ₹2,500/month depending on state) transferred to DBT savings account.",
            "Step 1: Verify BPL card status and age proof (Aadhaar, Voter ID, or Birth Certificate). Step 2: Download or collect NSAP pension application form from Block Development Office, Municipal Ward, or Gram Panchayat. Step 3: Attach Aadhaar, BPL card, and bank passbook. Step 4: Submit to Sub-Divisional Magistrate (SDM) / Taluk office. Step 5: Social welfare officer conducts field verification. Step 6: Pension sanctioned and disbursed monthly.",
            "All India", "https://nsap.nic.in", "https://nsap.nic.in/citizenRegistration.do", 1100L, 240L,
            Set.of(catMap.get("senior_citizen")),
            60, 120, new BigDecimal("120000"), "ALL", false, false, false, true, "All India",
            "Senior citizens aged 60 years or above living below the poverty line (BPL) with documented proof of residency.",
            List.of(
                new SchemeDocument("Aadhaar Card / Age Proof", true, "Proof of age 60+ (Aadhaar or Voter ID)"),
                new SchemeDocument("BPL Ration Card", true, "Proof of Below Poverty Line household status"),
                new SchemeDocument("Bank / Post Office Passbook", true, "Single-holder savings account for monthly pension deposit")
            )
        );

        createScheme(
            "SSY", "Sukanya Samriddhi Yojana (Beti Bachao Beti Padhao)", "Ministry of Women and Child Development & MoF",
            "High-interest sovereign savings scheme for girl children below 10 years of age, offering tax exemption under 80C and funds for higher education and marriage.",
            "Sukanya Samriddhi Yojana is a small deposit scheme backed by the Government of India designed exclusively for the girl child. It offers one of the highest interest rates among sovereign small savings schemes (~8.2% p.a.) along with complete triple tax exemption (EEE).",
            "To secure the future financial independence of girl children in India, incentivizing secondary education and preventing early marriage.",
            "Compound interest rate ~8.2% p.a., tax deductions under Section 80C up to ₹1.5 Lakhs annually, tax-free interest and maturity corpus, 50% partial withdrawal permitted upon turning 18 for university education.",
            "Step 1: Collect birth certificate of girl child (age under 10 years). Step 2: Visit nearest Post Office or authorized commercial bank branch. Step 3: Fill SSY Account Opening Form with guardian KYC (Aadhaar & PAN). Step 4: Deposit initial amount (minimum ₹250). Step 5: Receive SSY Passbook. Step 6: Maintain yearly deposit (minimum ₹250, maximum ₹1,50,000).",
            "All India", "https://www.indiapost.gov.in", "https://www.indiapost.gov.in/Financial/Pages/Content/Sukanya-Samriddhi-Account.aspx", 1650L, 380L,
            Set.of(catMap.get("women_children"), catMap.get("education")),
            0, 10, new BigDecimal("99999999"), "FEMALE", false, false, false, false, "All India",
            "Resident girl child under 10 years of age. Maximum two accounts permitted per family.",
            List.of(
                new SchemeDocument("Girl Child Birth Certificate", true, "Government birth certificate proving age under 10"),
                new SchemeDocument("Parent / Guardian Aadhaar Card", true, "KYC proof of biological parent or legal guardian"),
                new SchemeDocument("Parent / Guardian PAN Card", true, "Financial KYC for bank/post office deposit operations")
            )
        );

        createScheme(
            "DIVYANGJAN-ADIP", "Assistance to Disabled Persons for Purchase/Fitting of Aids (ADIP)", "Ministry of Social Justice and Empowerment",
            "Free modern assistive aids, motorized tricycles, braille kits, hearing aids, and artificial limbs for persons with benchmark disabilities from low-income families.",
            "The ADIP Scheme is implemented through ALIMCO and district social welfare agencies to assist needy disabled persons in procuring durable, sophisticated, and scientifically manufactured aids and appliances to promote their physical, social, and psychological rehabilitation.",
            "To eliminate physical barriers and foster independent living, education, and livelihood opportunities for persons with disabilities.",
            "100% grant for assistive aids and motorized appliances costing up to ₹15,000 (and up to ₹50,000 for motorized tricycles/cochlear implants) for beneficiaries with family income up to ₹15,000/month; 50% grant for income ₹15,001 - ₹20,000/month.",
            "Step 1: Obtain Unique Disability ID (UDID) Card with 40%+ disability certificate. Step 2: Prepare income certificate (family income <= ₹20,000/month). Step 3: Register online at https://swavlambancard.gov.in or attend local ALIMCO assessment camp. Step 4: Assessment by rehabilitation medical team. Step 5: Free aid fitted and distributed at public distribution camp.",
            "All India", "https://swavlambancard.gov.in", "https://adip.depwd.gov.in", 640L, 140L,
            Set.of(catMap.get("disability")),
            0, 100, new BigDecimal("240000"), "ALL", false, false, true, false, "All India",
            "Persons holding valid Disability Certificate / UDID card with 40% or more benchmark disability and annual family income up to ₹2,40,000.",
            List.of(
                new SchemeDocument("Unique Disability ID (UDID) / Disability Certificate", true, "Certificate showing 40% or more benchmark disability"),
                new SchemeDocument("Income Certificate", true, "Revenue authority certificate showing family income"),
                new SchemeDocument("Aadhaar Card", true, "Proof of identity and residence")
            )
        );

        // 5. Connect initial saved schemes & applications for demo
        Scheme pmkisan = schemeRepository.findByCode("PM-KISAN").orElse(null);
        Scheme nsp = schemeRepository.findByCode("NSP-POST-MATRIC").orElse(null);
        Scheme pmay = schemeRepository.findByCode("PMAY-G").orElse(null);

        if (pmkisan != null) {
            savedSchemeRepository.save(new SavedScheme(citizen, pmkisan));

            ApplicationRecord app1 = new ApplicationRecord();
            app1.setUser(citizen);
            app1.setScheme(pmkisan);
            app1.setApplicationNumber("PMKISAN-KA-2026-99124");
            app1.setStatus("Under Verification");
            app1.setAppliedDate(LocalDate.of(2026, 8, 10));
            app1.setNotes("Land record documents verified at Taluk office. Awaiting state approval.");
            app1.setOfficialPortalAcknowledged(true);
            applicationRecordRepository.save(app1);
        }

        if (pmay != null) {
            savedSchemeRepository.save(new SavedScheme(citizen, pmay));

            ApplicationRecord app2 = new ApplicationRecord();
            app2.setUser(citizen);
            app2.setScheme(pmay);
            app2.setApplicationNumber("PMAY-G-BLR-88712");
            app2.setStatus("Submitted");
            app2.setAppliedDate(LocalDate.of(2026, 9, 2));
            app2.setNotes("Survey completed by Gram Rozgar Sevak. Waiting for Gram Sabha approval list.");
            app2.setOfficialPortalAcknowledged(true);
            applicationRecordRepository.save(app2);
        }

        if (nsp != null) {
            savedSchemeRepository.save(new SavedScheme(student, nsp));

            ApplicationRecord app3 = new ApplicationRecord();
            app3.setUser(student);
            app3.setScheme(nsp);
            app3.setApplicationNumber("NSP-MAH-2026-55210");
            app3.setStatus("Approved");
            app3.setAppliedDate(LocalDate.of(2026, 7, 15));
            app3.setVerificationDate(LocalDate.of(2026, 8, 20));
            app3.setNotes("College nodal officer and district social welfare officer verified. First installment sanctioned.");
            app3.setOfficialPortalAcknowledged(true);
            applicationRecordRepository.save(app3);
        }

        // 6. Notifications
        Notification n1 = new Notification();
        n1.setUser(citizen);
        n1.setTitle("Application Update: PM-KISAN");
        n1.setMessage("Your application PMKISAN-KA-2026-99124 has progressed to Under Verification stage.");
        n1.setType("ALERT");
        n1.setPriority("NORMAL");
        n1.setIsRead(false);
        n1.setLinkUrl("application-status.html");
        notificationRepository.save(n1);

        Notification n2 = new Notification();
        n2.setUser(student);
        n2.setTitle("NSP Scholarship Approved!");
        n2.setMessage("Congratulations! Your Post-Matric Scholarship application NSP-MAH-2026-55210 has been approved.");
        n2.setType("SCHEME");
        n2.setPriority("HIGH");
        n2.setIsRead(false);
        n2.setLinkUrl("application-status.html");
        notificationRepository.save(n2);

        Notification nGlobal = new Notification();
        nGlobal.setUser(null);
        nGlobal.setTitle("SDG 1 Awareness: Welfare Schemes Expanded");
        nGlobal.setMessage("New central and state welfare initiatives for youth education and street vendors added to the platform.");
        nGlobal.setType("INFO");
        nGlobal.setPriority("LOW");
        nGlobal.setIsRead(false);
        nGlobal.setLinkUrl("scheme-finder.html");
        notificationRepository.save(nGlobal);

        // 7. Feedback
        if (pmkisan != null) {
            Feedback f1 = new Feedback();
            f1.setUser(citizen);
            f1.setScheme(pmkisan);
            f1.setRating(5);
            f1.setUsefulnessRating(5);
            f1.setEaseRating(4);
            f1.setComments("Found all required document details and direct link to PM-KISAN registration portal. Very helpful!");
            feedbackRepository.save(f1);
        }

        System.out.println("Data Seeding Complete: Roles, Users, Categories, Schemes, Criteria, Documents, Applications, Notifications initialized.");
    }

    private void createScheme(String code, String name, String department, String shortDesc, String fullDesc,
                              String objective, String benefits, String appProcess, String state,
                              String portalUrl, String appUrl, Long views, Long saved,
                              Set<Category> categories, int minAge, int maxAge, BigDecimal maxIncome,
                              String gender, boolean studentOnly, boolean farmerOnly, boolean disabilityOnly,
                              boolean seniorOnly, String allowedStates, String criteriaDesc,
                              List<SchemeDocument> docs) {
        Scheme s = new Scheme();
        s.setCode(code);
        s.setName(name);
        s.setDepartment(department);
        s.setShortDescription(shortDesc);
        s.setFullDescription(fullDesc);
        s.setObjective(objective);
        s.setBenefits(benefits);
        s.setApplicationProcess(appProcess);
        s.setStateAvailability(state);
        s.setOfficialPortalUrl(portalUrl);
        s.setOfficialApplicationUrl(appUrl);
        s.setViewsCount(views);
        s.setSavedCount(saved);
        s.setCategories(categories);

        SchemeEligibilityCriteria crit = new SchemeEligibilityCriteria();
        crit.setMinAge(minAge);
        crit.setMaxAge(maxAge);
        crit.setMaxIncome(maxIncome);
        crit.setRequiredGender(gender);
        crit.setStudentOnly(studentOnly);
        crit.setFarmerOnly(farmerOnly);
        crit.setDisabilityOnly(disabilityOnly);
        crit.setSeniorOnly(seniorOnly);
        crit.setAllowedStates(allowedStates);
        crit.setCriteriaDescription(criteriaDesc);
        s.setEligibilityCriteria(crit);

        s.setDocuments(docs);
        schemeRepository.save(s);
    }
}

