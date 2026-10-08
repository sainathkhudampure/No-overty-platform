-- ====================================================================
-- NO POVERTY — Smart Government Schemes Finder & Welfare Assistance Platform
-- Sample Data Script for MySQL 8.0+
-- UN Sustainable Development Goal: SDG 1 — NO POVERTY
-- NOTE: Scheme URLs and guidelines reference authentic public Government of India portals.
-- Demo user accounts and application records are provided for testing and evaluation.
-- ====================================================================

USE no_poverty_db;

-- 1. INSERT ROLES
INSERT INTO roles (id, name, description) VALUES
(1, 'ROLE_USER', 'Standard citizen / beneficiary user role'),
(2, 'ROLE_ADMIN', 'Welfare scheme and portal administrator role'),
(3, 'ROLE_DEVELOPER_ADMIN', 'Technical administrator with full analytics and system access');

-- 2. INSERT USERS
-- BCrypt password for 'password123' is $2a$10$e8K7bF1t4uK051j02LsmqOiF32dM.vXl4eRzQeBq8KzS4N7v7m/1W
-- BCrypt password for 'admin123' is $2a$10$wS6Q7M5qM9m7A3Z1qK8T7ee6QzE9fB1W4q5T6Y7U8I9O0P1A2S3D4
INSERT INTO users (id, full_name, email, mobile, password_hash, state, district, date_of_birth, preferred_language, status) VALUES
(1, 'Ramesh Kumar', 'citizen@example.com', '9876543210', '$2a$10$e8K7bF1t4uK051j02LsmqOiF32dM.vXl4eRzQeBq8KzS4N7v7m/1W', 'Karnataka', 'Bengaluru Urban', '1988-06-15', 'en', 'ACTIVE'),
(2, 'Pooja Sharma', 'pooja.student@example.com', '9811223344', '$2a$10$e8K7bF1t4uK051j02LsmqOiF32dM.vXl4eRzQeBq8KzS4N7v7m/1W', 'Maharashtra', 'Pune', '2003-09-22', 'en', 'ACTIVE'),
(3, 'System Administrator', 'admin@nopoverty.gov.in', '9899887766', '$2a$10$e8K7bF1t4uK051j02LsmqOiF32dM.vXl4eRzQeBq8KzS4N7v7m/1W', 'Delhi', 'New Delhi', '1980-01-01', 'en', 'ACTIVE'),
(4, 'Chief Developer', 'dev@nopoverty.gov.in', '9800112233', '$2a$10$e8K7bF1t4uK051j02LsmqOiF32dM.vXl4eRzQeBq8KzS4N7v7m/1W', 'Delhi', 'New Delhi', '1985-05-12', 'en', 'ACTIVE');

-- 3. USER ROLE MAPPINGS
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 3);

-- 4. INSERT BENEFICIARY CATEGORIES
INSERT INTO beneficiary_categories (id, code, name, description, icon, is_active, display_order) VALUES
(1, 'student', 'Students & Youth', 'Scholarships, fee waivers, education loans, and higher education assistance', 'fa-solid fa-graduation-cap', TRUE, 1),
(2, 'farmer', 'Farmers & Agriculture', 'Crop insurance, income support, seeds, fertilizers, and irrigation credit', 'fa-solid fa-seedling', TRUE, 2),
(3, 'senior_citizen', 'Senior Citizens', 'Old age pension, medical assistance, and elder care welfare programs', 'fa-solid fa-person-cane', TRUE, 3),
(4, 'employment', 'Job Seekers & Livelihood', 'Skill development training, rural employment guarantees, and micro-credit', 'fa-solid fa-briefcase', TRUE, 4),
(5, 'housing', 'Housing & Shelter', 'Affordable housing subsidies for rural and urban low-income families', 'fa-solid fa-house-chimney', TRUE, 5),
(6, 'education', 'Education Assistance', 'Primary, secondary, and vocational technical education support', 'fa-solid fa-book-open-reader', TRUE, 6),
(7, 'food_assistance', 'Food Security & Nutrition', 'Subsidized rations, mid-day meals, and nutritional food support', 'fa-solid fa-bowl-rice', TRUE, 7),
(8, 'healthcare', 'Healthcare & Medical Relief', 'Comprehensive health insurance, free treatments, and maternal healthcare', 'fa-solid fa-heart-pulse', TRUE, 8),
(9, 'women_children', 'Women & Children', 'Maternal cash benefits, girl child education, and self-help group loans', 'fa-solid fa-person-breastfeeding', TRUE, 9),
(10, 'disability', 'Persons with Disabilities', 'Divyangjan pension, assistive aids, accessible education, and transport concessions', 'fa-solid fa-wheelchair', TRUE, 10);

-- 5. USER PROFILES
INSERT INTO profiles (id, user_id, gender, address, pincode, annual_family_income, employment_status, family_size, education_level, student_status, institution_type, course_name, farmer_status, land_size_acres, crop_type, disability_status, senior_citizen_status, house_ownership, area_type, completion_percentage) VALUES
(1, 1, 'MALE', 'Village Kengeri, Hobli', '560060', 140000.00, 'SELF_EMPLOYED', 4, 'HIGHER_SECONDARY', FALSE, NULL, NULL, TRUE, 2.5, 'Ragi & Vegetables', FALSE, FALSE, 'OWNED_KUTCHA', 'RURAL', 85),
(2, 2, 'FEMALE', 'Shivajinagar, Flat 4B', '411005', 95000.00, 'UNEMPLOYED', 3, 'UNDERGRADUATE', TRUE, 'GOVERNMENT_COLLEGE', 'B.Sc Computer Science', FALSE, 0.00, NULL, FALSE, FALSE, 'RENTED', 'URBAN', 90);

-- 6. FAMILY MEMBERS
INSERT INTO family_members (id, user_id, name, relationship, age, gender, occupation, education, income_contribution, disability_status, student_status, senior_citizen_status) VALUES
(1, 1, 'Parvathi Kumar', 'SPOUSE', 34, 'FEMALE', 'HOMEMAKER', 'SECONDARY', 0.00, FALSE, FALSE, FALSE),
(2, 1, 'Manjunath Kumar', 'SON', 12, 'MALE', 'STUDENT', 'PRIMARY', 0.00, FALSE, TRUE, FALSE),
(3, 1, 'Basappa Kumar', 'FATHER', 68, 'MALE', 'RETIRED_FARMER', 'PRIMARY', 15000.00, FALSE, FALSE, TRUE),
(4, 2, 'Sunita Sharma', 'MOTHER', 52, 'FEMALE', 'TAILOR', 'SECONDARY', 60000.00, FALSE, FALSE, FALSE),
(5, 2, 'Aniket Sharma', 'BROTHER', 17, 'MALE', 'STUDENT', 'HIGHER_SECONDARY', 0.00, FALSE, TRUE, FALSE);

-- 7. SCHEMES TABLE
INSERT INTO schemes (id, code, name, department, short_description, full_description, objective, benefits, application_process, state_availability, official_portal_url, official_application_url, is_active, views_count, saved_count) VALUES
(1, 'PM-KISAN', 'PM Kisan Samman Nidhi Yojana', 'Ministry of Agriculture & Farmers Welfare',
 'Financial assistance of ₹6,000 per year in three equal installments directly transferred into Aadhaar-linked bank accounts of landholding farmer families.',
 'The Pradhan Mantri Kisan Samman Nidhi (PM-KISAN) is a central sector scheme with 100% funding from Government of India. The scheme aims to augment the income of all landholding farmers families across the country to enable them to take care of expenses related to agriculture and allied activities as well as domestic needs.',
 'To provide income support to all landholding eligible farmers families across the country to buy farm inputs and secure food safety.',
 'Direct benefit transfer (DBT) of ₹6,000 annually payable in 3 quarterly installments of ₹2,000 directly to verified bank account.',
 'Step 1: Check eligibility criteria. Step 2: Ensure Aadhaar is linked to bank account and mobile number. Step 3: Visit official portal at https://pmkisan.gov.in. Step 4: Click on Farmers Corner > New Farmer Registration. Step 5: Enter Aadhaar number and land records details. Step 6: Submit application and note Acknowledgement Number. Step 7: Track verification via state land revenue office.',
 'All India', 'https://pmkisan.gov.in', 'https://pmkisan.gov.in/RegistrationFormNew.aspx', TRUE, 1420, 230),

(2, 'PMAY-G', 'Pradhan Mantri Awaas Yojana - Gramin (PMAY-G)', 'Ministry of Rural Development',
 'Financial grant of up to ₹1,20,000 in plain areas and ₹1,30,000 in hilly/difficult areas for pucca house construction with basic amenities for homeless & kutcha house dwellers.',
 'PMAY-G aims to provide a pucca house with basic amenities to all houseless households and households living in kutcha and dilapidated houses in rural areas. The selection of beneficiaries is conducted objectively through Socio-Economic Caste Census (SECC) and Awaas+ surveys verified by Gram Sabhas.',
 'To help rural poor households construct durable, disaster-resilient housing with sanitation facilities and clean energy connections.',
 'Direct financial assistance of ₹1.20 Lakh in plains and ₹1.30 Lakh in hilly/North-Eastern states, plus 90-95 days of unskilled labor under MGNREGA and ₹12,000 toilet grant under Swachh Bharat.',
 'Step 1: Verify family name in SECC/Awaas+ priority list at Gram Panchayat. Step 2: Collect bank passbook and land possession proof. Step 3: Gram Rozgar Sevak or Panchayat Secretary uploads geotagged photos of existing house. Step 4: Sanction order issued by Block Development Officer. Step 5: Construction installments released in 3-4 stages following geotag inspection.',
 'All India', 'https://pmayg.nic.in', 'https://awaassoft.nic.in', TRUE, 980, 185),

(3, 'PM-JAY', 'Ayushman Bharat PM Jan Arogya Yojana (AB PM-JAY)', 'National Health Authority / MoHFW',
 'Health insurance coverage of up to ₹5,00,000 per family per year for secondary and tertiary care hospitalization across public and empaneled private hospitals.',
 'Ayushman Bharat PM-JAY is the world’s largest government-funded healthcare assurance scheme. It covers over 12 crore poor and vulnerable families (approximately 55 crore beneficiaries) without any cap on family size or age, ensuring cashless treatment for over 1,900 medical procedures.',
 'To eliminate catastrophic health expenditures and provide universal secondary and tertiary healthcare security to low-income families.',
 'Cashless and paperless inpatient health insurance cover of up to ₹5 Lakhs per family per year, covering pre-hospitalization, tests, surgery, ICU, and post-discharge medicines.',
 'Step 1: Check eligibility online at https://beneficiary.nha.gov.in using mobile number or Ration Card number. Step 2: Visit nearest Common Service Centre (CSC) or Ayushman Mitra desk at any empaneled hospital. Step 3: Complete e-KYC using biometric Aadhaar authentication. Step 4: Download Ayushman Bharat Golden Card. Step 5: Present card during hospital admission for cashless treatment.',
 'All India', 'https://pmjay.gov.in', 'https://beneficiary.nha.gov.in', TRUE, 2150, 412),

(4, 'NSP-POST-MATRIC', 'National Post-Matric Scholarship for SC/ST/OBC Students', 'Ministry of Social Justice and Empowerment',
 'Full tuition fee reimbursement and monthly maintenance allowance for underprivileged students pursuing post-matriculation, technical, or university degree courses.',
 'Centrally sponsored scholarship scheme designed to provide financial assistance to students belonging to Scheduled Castes, Scheduled Tribes, OBC, and economically weaker sections studying at post-matriculation or post-secondary stage to enable them to complete their education.',
 'To drastically reduce drop-out rates in higher education among underprivileged students and empower youth through technical and university credentials.',
 'Complete reimbursement of compulsory non-refundable college/university tuition fees, plus monthly maintenance allowance up to ₹1,200/month, book grants, and study tour allowances.',
 'Step 1: Check income eligibility (family income <= ₹2.5 Lakhs/year). Step 2: Visit National Scholarship Portal (NSP) at https://scholarships.gov.in. Step 3: Register as New Student with Aadhaar and Student OTR. Step 4: Fill academic marks, institute code, and bank details. Step 5: Upload income certificate and caste certificate. Step 6: Institute and State Nodal Officer verify credentials online. Step 7: Scholarship credited directly via PFMS DBT.',
 'All India', 'https://scholarships.gov.in', 'https://scholarships.gov.in/fresh/newstdRegfrmInstruction', TRUE, 1840, 310),

(5, 'PM-SVANIDHI', 'PM Street Vendor\'s AtmaNirbhar Nidhi (PM SVANidhi)', 'Ministry of Housing and Urban Affairs',
 'Collateral-free working capital micro-credit starting at ₹10,000 with 7% interest subsidy and cashback incentives on digital transactions for urban street vendors.',
 'PM SVANidhi was launched to facilitate affordable working capital loans to street vendors to resume their livelihoods adversely affected by economic disruptions. Vendors who repay on time become eligible for subsequent loan tranches of ₹20,000 and ₹50,000 with subsidized interest rates.',
 'To empower urban informal workers and micro-vendors with formal institutional banking credit and foster digital financial inclusion.',
 'Tranche 1: ₹10,000 loan; Tranche 2: ₹20,000 loan; Tranche 3: ₹50,000 loan with 7% interest subsidy credited directly to bank account, and up to ₹1,200 annual cashback on digital transactions.',
 'Step 1: Ensure possession of Urban Local Body (ULB) vending certificate or recommendation letter. Step 2: Visit https://pmsvanidhi.mohua.gov.in. Step 3: Enter Aadhaar-linked mobile number for OTP login. Step 4: Fill loan application form and select preferred lending bank. Step 5: ULB verifies vendor profile. Step 6: Bank sanctions and disburses loan without physical collateral.',
 'All India', 'https://pmsvanidhi.mohua.gov.in', 'https://pmsvanidhi.mohua.gov.in/Default/ViewLoanApplication', TRUE, 720, 115),

(6, 'IGNOAPS', 'Indira Gandhi National Old Age Pension Scheme (NSAP)', 'Ministry of Rural Development',
 'Monthly social security cash pension for citizens aged 60 and above living below the poverty line (BPL) to ensure dignified elderly living.',
 'A key component of the National Social Assistance Programme (NSAP), IGNOAPS provides non-contributory direct income support to elderly senior citizens from BPL households who have no other regular source of livelihood.',
 'To provide basic economic security and dignified social protection to indigent elderly citizens in rural and urban areas.',
 'Monthly direct cash pension of ₹200-₹500 from Central Government plus state-specific matching top-up (amounting to ₹1,000 - ₹2,500/month depending on state) transferred to DBT savings account.',
 'Step 1: Verify BPL card status and age proof (Aadhaar, Voter ID, or Birth Certificate). Step 2: Download or collect NSAP pension application form from Block Development Office, Municipal Ward, or Gram Panchayat. Step 3: Attach Aadhaar, BPL card, and bank passbook. Step 4: Submit to Sub-Divisional Magistrate (SDM) / Taluk office. Step 5: Social welfare officer conducts field verification. Step 6: Pension sanctioned and disbursed monthly.',
 'All India', 'https://nsap.nic.in', 'https://nsap.nic.in/citizenRegistration.do', TRUE, 1100, 240),

(7, 'SSY', 'Sukanya Samriddhi Yojana (Beti Bachao Beti Padhao)', 'Ministry of Women and Child Development & MoF',
 'High-interest sovereign savings scheme for girl children below 10 years of age, offering tax exemption under 80C and funds for higher education and marriage.',
 'Sukanya Samriddhi Yojana is a small deposit scheme backed by the Government of India designed exclusively for the girl child. It offers one of the highest interest rates among sovereign small savings schemes (currently ~8.2% p.a. compounded yearly) along with complete triple tax exemption (EEE).',
 'To secure the future financial independence of girl children in India, incentivizing secondary education and preventing early marriage.',
 'Compound interest rate ~8.2% p.a., tax deductions under Section 80C up to ₹1.5 Lakhs annually, tax-free interest and maturity corpus, 50% partial withdrawal permitted upon turning 18 for university education.',
 'Step 1: Collect birth certificate of girl child (age under 10 years). Step 2: Visit nearest Post Office or authorized commercial bank branch. Step 3: Fill SSY Account Opening Form with guardian KYC (Aadhaar & PAN). Step 4: Deposit initial amount (minimum ₹250). Step 5: Receive SSY Passbook. Step 6: Maintain yearly deposit (minimum ₹250, maximum ₹1,50,000).',
 'All India', 'https://www.indiapost.gov.in', 'https://www.indiapost.gov.in/Financial/Pages/Content/Sukanya-Samriddhi-Account.aspx', TRUE, 1650, 380),

(8, 'MGNREGA', 'Mahatma Gandhi National Rural Employment Guarantee Act', 'Ministry of Rural Development',
 'Legal guarantee of 100 days of wage employment in every financial year to adult members of any rural household willing to do public unskilled manual work.',
 'MGNREGA is the world’s largest social security rights-based labor program. It guarantees wage employment while creating durable rural community assets like rainwater harvesting ponds, farm roads, check dams, and irrigation canals.',
 'To provide livelihood security to vulnerable rural households while regenerating rural environment and natural infrastructure.',
 'Guaranteed minimum 100 days of wage work at statutory state wage rates (₹240-₹375/day) paid directly into bank accounts within 15 days; entitlement to unemployment allowance if work not provided within 15 days of demand.',
 'Step 1: Adult family members apply for Job Card at Gram Panchayat. Step 2: Gram Panchayat verifies local residency and issues free Job Card with photograph within 15 days. Step 3: Submit written work demand specifying days. Step 4: Gram Panchayat allocates work within 5 km radius. Step 5: Weekly muster roll signed and wage transferred to bank account via DBT.',
 'All India', 'https://nrega.nic.in', 'https://nrega.nic.in/netnrega/home.aspx', TRUE, 1310, 195),

(9, 'DIVYANGJAN-ADIP', 'Assistance to Disabled Persons for Purchase/Fitting of Aids (ADIP)', 'Ministry of Social Justice and Empowerment',
 'Free modern assistive aids, motorized tricycles, braille kits, hearing aids, and artificial limbs for persons with benchmark disabilities from low-income families.',
 'The ADIP Scheme is implemented through ALIMCO (Artificial Limbs Manufacturing Corporation) and district social welfare agencies to assist needy disabled persons in procuring durable, sophisticated, and scientifically manufactured aids and appliances to promote their physical, social, and psychological rehabilitation.',
 'To eliminate physical barriers and foster independent living, education, and livelihood opportunities for persons with disabilities.',
 '100% grant for assistive aids and motorized appliances costing up to ₹15,000 (and up to ₹50,000 for motorized tricycles/cochlear implants) for beneficiaries with family income up to ₹15,000/month; 50% grant for income ₹15,001 - ₹20,000/month.',
 'Step 1: Obtain Unique Disability ID (UDID) Card with 40%+ disability certificate. Step 2: Prepare income certificate (family income <= ₹20,000/month). Step 3: Register online at https://swavlambancard.gov.in or attend local ALIMCO assessment camp. Step 4: Assessment by rehabilitation medical team. Step 5: Free aid fitted and distributed at public distribution camp.',
 'All India', 'https://swavlambancard.gov.in', 'https://adip.depwd.gov.in', TRUE, 640, 140),

(10, 'PMMVY', 'Pradhan Mantri Matru Vandana Yojana', 'Ministry of Women and Child Development',
 'Direct maternity benefit cash incentive of ₹5,000 in two installments to pregnant women and lactating mothers for health seeking behavior and wage loss compensation.',
 'PMMVY is a conditional cash transfer scheme providing nutritional support and partial wage compensation to pregnant and lactating mothers for the first live birth, and ₹6,000 on the birth of a second girl child to deter sex selection.',
 'To improve the nutritional and health status of pregnant and lactating mothers and reduce maternal and infant mortality.',
 'Cash incentive of ₹5,000 directly transferred to Aadhaar-seeded bank account upon registration of pregnancy, antenatal check-up (ANC), and child vaccination milestone.',
 'Step 1: Register pregnancy at local Anganwadi Centre (AWC) or approved health facility within 570 days of LMP. Step 2: Fill Form 1A with MCP card details and Aadhaar. Step 3: Complete at least one antenatal check-up for Installment 1. Step 4: Register child birth and complete initial vaccination cycle for Installment 2. Step 5: Verification done on PMMVY portal (pmmvy.wcd.gov.in).',
 'All India', 'https://pmmvy.wcd.gov.in', 'https://pmmvy.wcd.gov.in/Citizen/Login', TRUE, 890, 175);

-- 8. SCHEME CATEGORIES MAPPING
INSERT INTO scheme_categories (scheme_id, category_id) VALUES
(1, 2), -- PM-KISAN -> Farmer
(2, 5), -- PMAY-G -> Housing
(3, 8), -- PM-JAY -> Healthcare
(4, 1), -- NSP -> Student
(4, 6), -- NSP -> Education
(5, 4), -- PM-SVANidhi -> Employment
(6, 3), -- IGNOAPS -> Senior Citizen
(7, 9), -- Sukanya Samriddhi -> Women & Children
(7, 6), -- Sukanya Samriddhi -> Education
(8, 4), -- MGNREGA -> Employment
(8, 7), -- MGNREGA -> Food / Nutrition Livelihood
(9, 10), -- ADIP -> Disability
(10, 9), -- PMMVY -> Women & Children
(10, 8); -- PMMVY -> Healthcare

-- 9. SCHEME ELIGIBILITY RULES
INSERT INTO scheme_eligibility (scheme_id, min_age, max_age, max_income, min_income, required_gender, student_only, farmer_only, disability_only, senior_only, allowed_states, criteria_description) VALUES
(1, 18, 100, 9999999.00, 0.00, 'ALL', FALSE, TRUE, FALSE, FALSE, 'All India', 'Must be a cultivable landholding farmer family whose names appear in official state land records. Institutional landholders and high-bracket income tax payees excluded.'),
(2, 18, 100, 150000.00, 0.00, 'ALL', FALSE, FALSE, FALSE, FALSE, 'All India', 'Houseless families or households living in zero, one, or two-room houses with kutcha wall and roof as verified by SECC 2011 and Gram Sabha.'),
(3, 0, 120, 250000.00, 0.00, 'ALL', FALSE, FALSE, FALSE, FALSE, 'All India', 'Households identified under rural deprivation categories or designated occupational criteria of urban workers under SECC database.'),
(4, 15, 35, 250000.00, 0.00, 'ALL', TRUE, FALSE, FALSE, FALSE, 'All India', 'Currently enrolled regular student in Class 11, 12, ITI, Diploma, Undergraduate, or Postgraduate course in a recognized institution with annual family income <= ₹2,50,000.'),
(5, 18, 70, 300000.00, 0.00, 'ALL', FALSE, FALSE, FALSE, FALSE, 'All India', 'Street vendors, hawkers, thelewala, and informal retailers operating in urban, peri-urban, or rural centers with ULB Vending Certificate or Letter of Recommendation.'),
(6, 60, 120, 120000.00, 0.00, 'ALL', FALSE, FALSE, FALSE, TRUE, 'All India', 'Senior citizens aged 60 years or above living below the poverty line (BPL) with documented proof of residency and no substantial family support.'),
(7, 0, 10, 99999999.00, 0.00, 'FEMALE', FALSE, FALSE, FALSE, FALSE, 'All India', 'Resident girl child under 10 years of age. Maximum two accounts permitted per family (three in case of twins/triplets born subsequently).'),
(8, 18, 65, 200000.00, 0.00, 'ALL', FALSE, FALSE, FALSE, FALSE, 'All India', 'Adult resident members of rural households willing to do unskilled manual physical labor holding a verified Gram Panchayat Job Card.'),
(9, 0, 100, 240000.00, 0.00, 'ALL', FALSE, FALSE, TRUE, FALSE, 'All India', 'Persons holding valid Disability Certificate / UDID card with 40% or more benchmark disability and annual family income up to ₹2,40,000.'),
(10, 19, 45, 250000.00, 0.00, 'FEMALE', FALSE, FALSE, FALSE, FALSE, 'All India', 'Pregnant Women and Lactating Mothers (PW&LM) who are not in regular employment with Central or State Government or PSUs.');

-- 10. SCHEME DOCUMENTS
INSERT INTO scheme_documents (scheme_id, document_name, is_mandatory, description) VALUES
(1, 'Aadhaar Card', TRUE, 'Mandatory biometric Aadhaar linked to bank account'),
(1, 'Land Ownership Record (Khata/RoR)', TRUE, 'Copy of Record of Rights / Jamabandi showing cultivable land holding'),
(1, 'Bank Passbook / Cancelled Cheque', TRUE, 'DBT-enabled bank account details for direct transfer'),

(2, 'Aadhaar Card of Head of Family', TRUE, 'Biometric identity proof of applicant'),
(2, 'BPL Ration Card / SECC Number', TRUE, 'Verification of low-income rural status'),
(2, 'Land / Site Possession Certificate', TRUE, 'Proof of site possession for pucca house construction'),
(2, 'Bank Account Passbook', TRUE, 'Active bank account linked to Aadhaar'),

(3, 'Aadhaar Card / Ration Card', TRUE, 'Identification and family entitlement verification'),
(3, 'Ration Card (NFSA / BPL)', TRUE, 'State-issued food security card showing family member names'),

(4, 'Aadhaar Card of Student', TRUE, 'Identity verification on National Scholarship Portal'),
(4, 'Income Certificate', TRUE, 'Revenue authority issued certificate showing family income under ₹2.5 Lakhs'),
(4, 'Caste Certificate (SC/ST/OBC)', TRUE, 'Official caste certificate issued by competent Tehsildar/SDM'),
(4, 'Previous Year Marksheet', TRUE, 'Academic proof of qualifying exam passing marks'),
(4, 'Current Year Fee Receipt / Bonafide', TRUE, 'College enrollment and fee payment verification'),

(5, 'Aadhaar Card', TRUE, 'Identity and mobile linkage'),
(5, 'Vending Certificate / Urban ID Card', TRUE, 'Proof of vending issued by Urban Local Body or Town Vending Committee'),
(5, 'Bank Account Details', TRUE, 'Account for loan disbursement and interest subsidy credit'),

(6, 'Aadhaar Card / Age Proof', TRUE, 'Proof of age 60+ (Aadhaar or Voter ID)'),
(6, 'BPL Ration Card', TRUE, 'Proof of Below Poverty Line household status'),
(6, 'Bank / Post Office Passbook', TRUE, 'Single-holder savings account for monthly pension deposit'),

(7, 'Girl Child Birth Certificate', TRUE, 'Government birth certificate proving age under 10'),
(7, 'Parent / Guardian Aadhaar Card', TRUE, 'KYC proof of biological parent or legal guardian'),
(7, 'Parent / Guardian PAN Card', TRUE, 'Financial KYC for bank/post office deposit operations'),

(8, 'Aadhaar Card of Adult Members', TRUE, 'Identity proof for rural job card registration'),
(8, 'Ration Card', TRUE, 'Rural residency proof in respective Gram Panchayat'),
(8, 'Bank / Post Office Account Passbook', TRUE, 'Individual or joint DBT wage receiving account'),

(9, 'Unique Disability ID (UDID) / Disability Certificate', TRUE, 'Certificate showing 40% or more benchmark disability'),
(9, 'Income Certificate', TRUE, 'Revenue authority certificate showing family income'),
(9, 'Aadhaar Card', TRUE, 'Proof of identity and residence'),

(10, 'Aadhaar Card of Mother & Husband', TRUE, 'Identity proof for both parents'),
(10, 'Mother and Child Protection (MCP) Card', TRUE, 'RCH register registration proof with antenatal dates'),
(10, 'Bank Passbook in Mother\'s Name', TRUE, 'Individual bank account of pregnant woman for direct benefit transfer');

-- 11. USER SAVED SCHEMES
INSERT INTO saved_schemes (user_id, scheme_id) VALUES
(1, 1),
(1, 2),
(2, 4),
(2, 7);

-- 12. INTERNAL APPLICATIONS
INSERT INTO applications (user_id, scheme_id, application_number, status, applied_date, verification_date, notes, official_portal_acknowledged) VALUES
(1, 1, 'PMKISAN-KA-2026-99124', 'Under Verification', '2026-08-10', NULL, 'Land record documents verified at Taluk office. Awaiting state approval.', TRUE),
(1, 2, 'PMAY-G-BLR-88712', 'Submitted', '2026-09-02', NULL, 'Survey completed by Gram Rozgar Sevak. Waiting for Gram Sabha approval list.', TRUE),
(2, 4, 'NSP-MAH-2026-55210', 'Approved', '2026-07-15', '2026-08-20', 'College nodal officer and district social welfare officer verified. First installment sanctioned.', TRUE);

-- 13. USER NOTIFICATIONS
INSERT INTO notifications (user_id, title, message, type, priority, is_read, link_url) VALUES
(1, 'Application Update: PM-KISAN', 'Your application PMKISAN-KA-2026-99124 has progressed to Under Verification stage.', 'ALERT', 'NORMAL', FALSE, 'application-status.html'),
(1, 'PMAY-G Gram Sabha Schedule', 'Field survey completed for your housing application. Check your status timeline.', 'INFO', 'LOW', TRUE, 'application-status.html'),
(2, 'NSP Scholarship Approved!', 'Congratulations! Your Post-Matric Scholarship application NSP-MAH-2026-55210 has been approved.', 'SCHEME', 'HIGH', FALSE, 'application-status.html'),
(2, 'Document Reminder', 'Ensure your Bank Account is Aadhaar-seeded for direct DBT scholarship transfer.', 'DOCUMENT', 'NORMAL', FALSE, 'documents.html'),
(NULL, 'SDG 1 Awareness Campaign', 'New welfare schemes for youth skill training and street vendors added to the platform.', 'INFO', 'LOW', FALSE, 'scheme-finder.html');

-- 14. CITIZEN FEEDBACK
INSERT INTO feedback (user_id, scheme_id, rating, usefulness_rating, ease_rating, comments) VALUES
(1, 1, 5, 5, 4, 'Found all required document details and direct link to PM-KISAN registration portal. Very helpful!'),
(2, 4, 5, 5, 5, 'The eligibility checker accurately confirmed my scholarship eligibility before I submitted on NSP portal.');

-- 15. USER DOCUMENTS
INSERT INTO documents (user_id, document_name, document_type, file_path, status, is_verified, notes) VALUES
(1, 'Aadhaar Card', 'Identity Proof', '/uploads/docs/uid_ramesh.pdf', 'VERIFIED', TRUE, 'Aadhaar linked with mobile and bank account'),
(1, 'Land Khata RoR', 'Land Record', '/uploads/docs/khata_2.5acre.pdf', 'AVAILABLE', TRUE, 'Sub-registrar certified land document'),
(2, 'Aadhaar Card', 'Identity Proof', '/uploads/docs/uid_pooja.pdf', 'VERIFIED', TRUE, 'Biometric verified'),
(2, 'Income Certificate', 'Income Proof', '/uploads/docs/income_cert_2026.pdf', 'AVAILABLE', TRUE, 'Issued by Tahsildar Pune. Valid until March 2027.');

