# TodoList API 接口测试指南

## 概述

本指南提供了 TodoList 后端 API 的完整测试流程，使用 Apifox 进行接口测试。

### 基础信息

- **Base URL**: `http://localhost:8080/api`
- **认证方式**: JWT Bearer Token
- **数据格式**: JSON

### 通用响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1640995200000
}
```

---

## 测试环境配置

### 1. 启动服务
```bash
./gradlew bootRun
```

### 2. 验证服务状态
访问: `http://localhost:8080/api/test/health`

---

## 接口测试流程

### 1. 健康检查

**请求**: `GET /test/health`

**响应示例**:
```json
{
  "code": 200,
  "message": "服务正常",
  "data": {
    "status": "UP",
    "timestamp": "2024-01-01T10:00:00"
  }
}
```

### 2. 用户注册

**请求**: `POST /auth/register`
**Content-Type**: `application/json`

**请求体**:
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 3. 用户登录

**请求**: `POST /auth/login`
**Content-Type**: `application/json`

**请求体**:
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

**重要**: 保存返回的token用于后续请求

### 4. 创建待办事项

**请求**: `POST /todos`
**Authorization**: `Bearer {token}`
**Content-Type**: `application/json`

**请求体**:
```json
{
  "title": "完成项目文档",
  "description": "编写完整的技术文档",
  "priority": "HIGH",
  "dueDate": "2024-01-15"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "title": "完成项目文档",
    "description": "编写完整的技术文档",
    "completed": false,
    "priority": "HIGH",
    "dueDate": "2024-01-15",
    "userId": 1,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 5. 获取待办事项列表

**请求**: `GET /todos`
**Authorization**: `Bearer {token}`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "title": "完成项目文档",
      "description": "编写完整的技术文档",
      "completed": false,
      "priority": "HIGH",
      "dueDate": "2024-01-15",
      "userId": 1,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 6. 获取单个待办事项

**请求**: `GET /todos/{id}`
**Authorization**: `Bearer {token}`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "title": "完成项目文档",
    "description": "编写完整的技术文档",
    "completed": false,
    "priority": "HIGH",
    "dueDate": "2024-01-15",
    "userId": 1,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 7. 更新待办事项

**请求**: `PUT /todos/{id}`
**Authorization**: `Bearer {token}`
**Content-Type**: `application/json`

**请求体**:
```json
{
  "title": "更新后的标题",
  "description": "更新后的描述",
  "priority": "MEDIUM",
  "dueDate": "2024-01-20"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "title": "更新后的标题",
    "description": "更新后的描述",
    "completed": false,
    "priority": "MEDIUM",
    "dueDate": "2024-01-20",
    "userId": 1,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T11:00:00"
  }
}
```

### 8. 切换完成状态

**请求**: `POST /todos/{id}/toggle`
**Authorization**: `Bearer {token}`

**响应示例**:
```json
{
  "code": 200,
  "message": "切换完成状态成功",
  "data": {
    "id": 1,
    "title": "更新后的标题",
    "description": "更新后的描述",
    "completed": true,
    "priority": "MEDIUM",
    "dueDate": "2024-01-20",
    "userId": 1,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T12:00:00"
  }
}
```

### 9. 获取过期待办事项

**请求**: `GET /todos/overdue`
**Authorization**: `Bearer {token}`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 2,
      "title": "过期的待办事项",
      "description": "已经过期的任务",
      "completed": false,
      "priority": "HIGH",
      "dueDate": "2023-12-31",
      "userId": 1,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 10. 删除待办事项

**请求**: `DELETE /todos/{id}`
**Authorization**: `Bearer {token}`

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 11. 获取当前用户信息

**请求**: `GET /auth/me`
**Authorization**: `Bearer {token}`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 12. 修改密码

**请求**: `PUT /auth/password`
**Authorization**: `Bearer {token}`
**Content-Type**: `application/json`

**请求体**:
```json
{
  "oldPassword": "password123",
  "newPassword": "newpassword123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null
}
```

---

## 错误响应示例

### 1. 未授权 (401)
```json
{
  "code": 401,
  "message": "未授权访问",
  "data": null
}
```

### 2. 资源不存在 (404)
```json
{
  "code": 404,
  "message": "待办事项不存在",
  "data": null
}
```

### 3. 参数错误 (400)
```json
{
  "code": 400,
  "message": "用户名已存在",
  "data": null
}
```

---

## Apifox 使用建议

### 1. 环境变量设置
- `baseUrl`: `http://localhost:8080/api`
- `token`: 登录后获取的JWT token

### 2. 全局请求头
- `Content-Type`: `application/json`
- `Authorization`: `Bearer {{token}}`

### 3. 测试用例组织
1. 认证相关接口
2. 待办事项CRUD接口
3. 错误处理测试

### 4. 自动化测试
- 设置接口依赖关系
- 使用前置脚本保存token
- 配置断言验证响应

---

## 测试要点

1. **认证流程**: 注册 → 登录 → 获取token → 使用token
2. **数据隔离**: 不同用户只能访问自己的数据
3. **权限控制**: 未认证用户无法访问受保护接口
4. **参数验证**: 测试各种边界条件和错误输入
5. **状态管理**: 验证待办事项状态切换正确

---

## 常见问题

**Q: Token过期怎么办？**
A: 重新调用登录接口获取新token

**Q: 如何测试数据隔离？**
A: 使用不同用户登录，验证只能看到自己的数据

**Q: 如何处理并发请求？**
A: 使用Apifox的并发测试功能验证数据一致性 