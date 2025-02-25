USE cosmetics_supervision;

-- 初始化管理员用户
INSERT INTO sys_user (username, password, real_name, status, remark)
VALUES ('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', 1, '系统管理员')
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

-- 初始化角色
INSERT INTO sys_role (role_name, role_key, role_sort, status, remark)
VALUES 
('超级管理员', 'admin', 1, 1, '超级管理员'),
('系统管理员', 'system', 2, 1, '系统管理员'),
('监管人员', 'supervisor', 3, 1, '监管人员'),
('企业用户', 'enterprise', 4, 1, '企业用户')
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

-- 初始化权限
INSERT INTO sys_permission (parent_id, permission_name, permission_key, permission_type, path, component, icon, sort, status)
VALUES 
-- 系统管理
(0, '系统管理', 'system', 'menu', '/system', 'Layout', 'system', 1, 1),
(1, '用户管理', 'system:user', 'menu', 'user', 'system/user/index', 'user', 1, 1),
(1, '角色管理', 'system:role', 'menu', 'role', 'system/role/index', 'peoples', 2, 1),
(1, '菜单管理', 'system:menu', 'menu', 'menu', 'system/menu/index', 'tree-table', 3, 1),

-- 用户管理按钮
(2, '用户查询', 'system:user:query', 'button', '', '', '', 1, 1),
(2, '用户新增', 'system:user:add', 'button', '', '', '', 2, 1),
(2, '用户修改', 'system:user:edit', 'button', '', '', '', 3, 1),
(2, '用户删除', 'system:user:remove', 'button', '', '', '', 4, 1),
(2, '重置密码', 'system:user:resetPwd', 'button', '', '', '', 5, 1),

-- 角色管理按钮
(3, '角色查询', 'system:role:query', 'button', '', '', '', 1, 1),
(3, '角色新增', 'system:role:add', 'button', '', '', '', 2, 1),
(3, '角色修改', 'system:role:edit', 'button', '', '', '', 3, 1),
(3, '角色删除', 'system:role:remove', 'button', '', '', '', 4, 1),

-- 菜单管理按钮
(4, '菜单查询', 'system:menu:query', 'button', '', '', '', 1, 1),
(4, '菜单新增', 'system:menu:add', 'button', '', '', '', 2, 1),
(4, '菜单修改', 'system:menu:edit', 'button', '', '', '', 3, 1),
(4, '菜单删除', 'system:menu:remove', 'button', '', '', '', 4, 1)

ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

-- 初始化用户角色关联
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id
FROM sys_user u, sys_role r
WHERE u.username = 'admin' AND r.role_key = 'admin'
ON DUPLICATE KEY UPDATE sys_user_role.id = sys_user_role.id;

-- 初始化角色权限关联
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM sys_role r, sys_permission p
WHERE r.role_key = 'admin'
ON DUPLICATE KEY UPDATE sys_role_permission.id = sys_role_permission.id; 