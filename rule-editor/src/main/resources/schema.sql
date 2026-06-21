CREATE TABLE IF NOT EXISTS phone_model (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model_name VARCHAR(100) NOT NULL,
    model_code VARCHAR(50) NOT NULL UNIQUE,
    android_version VARCHAR(20),
    miui_version VARCHAR(20),
    description VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_model_code (model_code),
    INDEX idx_brand (brand),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS app_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    package_name VARCHAR(200) NOT NULL UNIQUE,
    app_name VARCHAR(200) NOT NULL,
    app_type VARCHAR(50),
    is_root_related BOOLEAN NOT NULL DEFAULT FALSE,
    is_detection_app BOOLEAN NOT NULL DEFAULT FALSE,
    description VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_package_name (package_name),
    INDEX idx_is_root_related (is_root_related),
    INDEX idx_is_detection_app (is_detection_app)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS hma_rule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone_model_id BIGINT NOT NULL,
    app_info_id BIGINT NOT NULL,
    rule_type VARCHAR(30) NOT NULL DEFAULT 'BLACKLIST',
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    scope VARCHAR(50) DEFAULT 'ALL',
    priority INT NOT NULL DEFAULT 0,
    notes VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_phone_app (phone_model_id, app_info_id),
    INDEX idx_phone_model_id (phone_model_id),
    INDEX idx_app_info_id (app_info_id),
    INDEX idx_is_enabled (is_enabled),
    CONSTRAINT fk_hma_rule_phone_model FOREIGN KEY (phone_model_id) REFERENCES phone_model(id) ON DELETE CASCADE,
    CONSTRAINT fk_hma_rule_app_info FOREIGN KEY (app_info_id) REFERENCES app_info(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
