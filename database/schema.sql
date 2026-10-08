-- ====================================================================
-- NO POVERTY — Smart Government Schemes Finder & Welfare Assistance Platform
-- Database Schema for MySQL 8.0+ / MySQL Workbench
-- UN Sustainable Development Goal: SDG 1 — NO POVERTY
-- ====================================================================

DROP DATABASE IF EXISTS no_poverty_db;
CREATE DATABASE no_poverty_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE no_poverty_db;

-- 1. ROLES TABLE
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. USERS TABLE
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    mobile VARCHAR(20) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    state VARCHAR(100),
    district VARCHAR(100),
    date_of_birth DATE,
    preferred_language VARCHAR(10) DEFAULT 'en',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_mobile (mobile),
    INDEX idx_user_state (state)
) ENGINE=InnoDB;

-- 3. USER ROLES JUNCTION TABLE
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. BENEFICIARY CATEGORIES
CREATE TABLE beneficiary_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon VARCHAR(100) DEFAULT 'fa-solid fa-hands-holding-child',
    is_active BOOLEAN DEFAULT TRUE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 5. USER PROFILES
CREATE TABLE profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    gender VARCHAR(20),
    address VARCHAR(255),
    pincode VARCHAR(10),
    annual_family_income DECIMAL(12, 2) DEFAULT 0.00,
    employment_status VARCHAR(50),
    family_size INT DEFAULT 1,
    education_level VARCHAR(50),
    student_status BOOLEAN DEFAULT FALSE,
    institution_type VARCHAR(100),
    course_name VARCHAR(150),
    farmer_status BOOLEAN DEFAULT FALSE,
    land_size_acres DECIMAL(6, 2) DEFAULT 0.00,
    crop_type VARCHAR(100),
    disability_status BOOLEAN DEFAULT FALSE,
    senior_citizen_status BOOLEAN DEFAULT FALSE,
    house_ownership VARCHAR(50),
    area_type VARCHAR(20) DEFAULT 'RURAL', -- RURAL, URBAN
    category_specific_data JSON,
    completion_percentage INT DEFAULT 20,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_profile_income (annual_family_income)
) ENGINE=InnoDB;

-- 6. FAMILY MEMBERS
CREATE TABLE family_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    relationship VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20),
    occupation VARCHAR(100),
    education VARCHAR(100),
    income_contribution DECIMAL(12, 2) DEFAULT 0.00,
    disability_status BOOLEAN DEFAULT FALSE,
    student_status BOOLEAN DEFAULT FALSE,
    senior_citizen_status BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_family_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_family_user (user_id)
) ENGINE=InnoDB;

-- 7. SCHEMES TABLE
CREATE TABLE schemes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    short_description VARCHAR(500) NOT NULL,
    full_description TEXT NOT NULL,
    objective TEXT NOT NULL,
    benefits TEXT NOT NULL,
    application_process TEXT NOT NULL,
    state_availability VARCHAR(100) DEFAULT 'All India',
    official_portal_url VARCHAR(500) NOT NULL,
    official_application_url VARCHAR(500) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    views_count BIGINT DEFAULT 0,
    saved_count BIGINT DEFAULT 0,
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_scheme_name (name),
    INDEX idx_scheme_department (department),
    INDEX idx_scheme_state (state_availability)
) ENGINE=InnoDB;

-- 8. SCHEME CATEGORIES JUNCTION
CREATE TABLE scheme_categories (
    scheme_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (scheme_id, category_id),
    CONSTRAINT fk_scheme_cat_scheme FOREIGN KEY (scheme_id) REFERENCES schemes(id) ON DELETE CASCADE,
    CONSTRAINT fk_scheme_cat_category FOREIGN KEY (category_id) REFERENCES beneficiary_categories(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 9. SCHEME ELIGIBILITY CRITERIA
CREATE TABLE scheme_eligibility (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheme_id BIGINT NOT NULL,
    min_age INT DEFAULT 0,
    max_age INT DEFAULT 120,
    max_income DECIMAL(12, 2) DEFAULT 99999999.00,
    min_income DECIMAL(12, 2) DEFAULT 0.00,
    required_gender VARCHAR(20) DEFAULT 'ALL', -- ALL, MALE, FEMALE, TRANSGENDER
    required_occupation VARCHAR(100),
    required_education VARCHAR(100),
    student_only BOOLEAN DEFAULT FALSE,
    farmer_only BOOLEAN DEFAULT FALSE,
    disability_only BOOLEAN DEFAULT FALSE,
    senior_only BOOLEAN DEFAULT FALSE,
    allowed_states VARCHAR(255) DEFAULT 'All India',
    criteria_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_eligibility_scheme FOREIGN KEY (scheme_id) REFERENCES schemes(id) ON DELETE CASCADE,
    INDEX idx_eligibility_scheme (scheme_id)
) ENGINE=InnoDB;

-- 10. SCHEME DOCUMENTS REQUIRED
CREATE TABLE scheme_documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheme_id BIGINT NOT NULL,
    document_name VARCHAR(150) NOT NULL,
    is_mandatory BOOLEAN DEFAULT TRUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_scheme_doc_scheme FOREIGN KEY (scheme_id) REFERENCES schemes(id) ON DELETE CASCADE,
    INDEX idx_scheme_doc_scheme (scheme_id)
) ENGINE=InnoDB;

-- 11. USER DOCUMENTS STORE
CREATE TABLE documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    document_name VARCHAR(150) NOT NULL,
    document_type VARCHAR(100),
    file_path VARCHAR(500),
    file_size_bytes BIGINT DEFAULT 0,
    status VARCHAR(50) DEFAULT 'AVAILABLE', -- AVAILABLE, MISSING, VERIFIED
    notes VARCHAR(255),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_documents_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_documents_user (user_id)
) ENGINE=InnoDB;

-- 12. SAVED SCHEMES (BOOKMARKS)
CREATE TABLE saved_schemes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    scheme_id BIGINT NOT NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_scheme (user_id, scheme_id),
    CONSTRAINT fk_saved_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_saved_scheme FOREIGN KEY (scheme_id) REFERENCES schemes(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 13. INTERNAL APPLICATION TRACKER
CREATE TABLE applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    scheme_id BIGINT NOT NULL,
    application_number VARCHAR(100) NOT NULL,
    status VARCHAR(50) DEFAULT 'Draft', -- Draft, Submitted, Under Verification, Approved, Rejected, Benefit Received
    applied_date DATE,
    verification_date DATE,
    notes TEXT,
    official_portal_acknowledged BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_app_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_app_scheme FOREIGN KEY (scheme_id) REFERENCES schemes(id) ON DELETE CASCADE,
    INDEX idx_app_user (user_id),
    INDEX idx_app_status (status)
) ENGINE=InnoDB;

-- 14. NOTIFICATIONS
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT, -- NULL implies global broadcast
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) DEFAULT 'INFO', -- INFO, ALERT, DEADLINE, SCHEME, DOCUMENT
    priority VARCHAR(20) DEFAULT 'NORMAL', -- LOW, NORMAL, HIGH, URGENT
    is_read BOOLEAN DEFAULT FALSE,
    link_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_notif_user (user_id, is_read)
) ENGINE=InnoDB;

-- 15. CITIZEN FEEDBACK
CREATE TABLE feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    scheme_id BIGINT,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    usefulness_rating INT CHECK (usefulness_rating BETWEEN 1 AND 5),
    ease_rating INT CHECK (ease_rating BETWEEN 1 AND 5),
    comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_feedback_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_feedback_scheme FOREIGN KEY (scheme_id) REFERENCES schemes(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 16. SEARCH HISTORY
CREATE TABLE search_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    query_text VARCHAR(255) NOT NULL,
    category_filter VARCHAR(100),
    results_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_search_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_search_query (query_text)
) ENGINE=InnoDB;

-- 17. VIEW HISTORY
CREATE TABLE view_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    scheme_id BIGINT NOT NULL,
    viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_view_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_view_scheme FOREIGN KEY (scheme_id) REFERENCES schemes(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 18. ADMIN AUDIT LOGS
CREATE TABLE admin_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_id BIGINT NOT NULL,
    action VARCHAR(100) NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id BIGINT,
    details TEXT,
    ip_address VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_logs_admin FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

