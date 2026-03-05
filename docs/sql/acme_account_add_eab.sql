-- 添加 EAB 字段到 acme_account 表
ALTER TABLE `acme_account`
    ADD COLUMN `eab_kid` VARCHAR(255) NULL COMMENT 'EAB Key ID (External Account Binding)' AFTER `account_key_pair`,
    ADD COLUMN `eab_hmac_key` TEXT NULL COMMENT 'EAB HMAC Key (加密存储)' AFTER `eab_kid`;
