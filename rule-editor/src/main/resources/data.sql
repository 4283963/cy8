INSERT IGNORE INTO phone_model (brand, model_name, model_code, android_version, miui_version, description) VALUES
('Xiaomi', 'Redmi K90 Pro', 'redmi_k90_pro', '14.0', 'OS8.0', '红米 K90 Pro 旗舰机型'),
('Xiaomi', 'Mi 14 Ultra', 'mi_14_ultra', '14.0', 'OS8.0', '小米 14 Ultra 旗舰机型'),
('Xiaomi', 'Redmi Note 13 Pro', 'redmi_note13_pro', '14.0', 'OS8.0', 'Redmi Note 13 Pro');

INSERT IGNORE INTO app_info (package_name, app_name, app_type, is_root_related, is_detection_app, description) VALUES
('com.keramidas.virtual.xposed.installer', 'LSPosed', 'XPOSED', TRUE, FALSE, 'LSPosed 框架'),
('org.lsposed.manager', 'LSPosed Manager', 'XPOSED', TRUE, FALSE, 'LSPosed 管理器'),
('de.robv.android.xposed.installer', 'Xposed Installer', 'XPOSED', TRUE, FALSE, 'Xposed 安装器'),
('eu.chainfire.supersu', 'SuperSU', 'ROOT', TRUE, FALSE, 'SuperSU 权限管理'),
('com.topjohnwu.magisk', 'Magisk', 'ROOT', TRUE, FALSE, 'Magisk 面具'),
('com.noshufou.android.su', 'Superuser', 'ROOT', TRUE, FALSE, 'Superuser 权限管理'),
('me.weishu.kernelsu', 'KernelSU', 'ROOT', TRUE, FALSE, 'KernelSU 内核级 Root'),
('io.github.vvb2060.magisk', 'Magisk Delta', 'ROOT', TRUE, FALSE, 'Magisk Delta 版本'),
('com.saurik.substrate', 'Substrate', 'XPOSED', TRUE, FALSE, 'Substrate 框架'),
('com.tsng.hidemyapplist', 'Hide My Applist', 'XPOSED', TRUE, FALSE, 'HMA 隐藏应用列表模块'),
('com.example.rootcheck', 'Root Checker', 'DETECTION', FALSE, TRUE, 'Root 检测应用示例'),
('com.google.android.play.core.integrity', 'Play Integrity', 'GOOGLE', FALSE, TRUE, 'Google Play Integrity API'),
('com.mipay.wallet', '小米钱包', 'FINANCE', FALSE, TRUE, '小米钱包 - 检测 Root'),
('com.sankuai.meituan', '美团', 'LIFESTYLE', FALSE, TRUE, '美团 - 检测 Root'),
('com.eg.android.AlipayGphone', '支付宝', 'FINANCE', FALSE, TRUE, '支付宝 - 检测 Root'),
('com.tencent.mm', '微信', 'SOCIAL', FALSE, TRUE, '微信 - 检测 Root');

INSERT IGNORE INTO hma_rule (phone_model_id, app_info_id, rule_type, is_enabled, scope, priority, notes) VALUES
(1, 1, 'BLACKLIST', TRUE, 'ALL', 100, '隐藏 LSPosed'),
(1, 2, 'BLACKLIST', TRUE, 'ALL', 100, '隐藏 LSPosed Manager'),
(1, 5, 'BLACKLIST', TRUE, 'ALL', 100, '隐藏 Magisk'),
(1, 7, 'BLACKLIST', TRUE, 'ALL', 100, '隐藏 KernelSU'),
(1, 10, 'BLACKLIST', TRUE, 'ALL', 100, '隐藏 HMA 自身'),
(1, 11, 'BLACKLIST', TRUE, 'ALL', 90, '对 Root 检测应用隐藏'),
(1, 12, 'BLACKLIST', TRUE, 'ALL', 90, '对 Play Integrity 隐藏'),
(1, 13, 'BLACKLIST', TRUE, 'ALL', 90, '对小米钱包隐藏'),
(1, 14, 'BLACKLIST', TRUE, 'ALL', 90, '对美团隐藏'),
(1, 15, 'BLACKLIST', TRUE, 'ALL', 90, '对支付宝隐藏'),
(1, 16, 'BLACKLIST', TRUE, 'ALL', 90, '对微信隐藏');
