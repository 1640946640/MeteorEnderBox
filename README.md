# MeteorEnderBox

MeteorEnderBox 是一个 Minecraft 插件，为玩家提供随身末影箱功能，支持多末影箱管理、管理员查看玩家末影箱等功能。

## 功能特点

- **多末影箱管理**：玩家可以解锁多个末影箱，每个末影箱可以设置不同的行数
- **管理员功能**：管理员可以查看和管理其他玩家的末影箱
- **经济系统支持**：支持使用金币或点券解锁末影箱
- **数据库存储**：使用 MySQL 存储玩家末影箱数据
- **并发访问控制**：防止多人同时操作同一个末影箱导致的物品丢失或刷物品问题
- **跨服支持**：数据存储在 MySQL 中，支持跨服使用
- **版本适配**：自动检测服务器版本并加载对应的配置文件

## 安装说明

1. 确保服务器已安装以下插件：
   - Vault（经济系统支持）
   - PlayerPoints（点券系统支持）

2. 将编译好的 `MeteorEnderBox-1.0-SNAPSHOT-shaded.jar` 文件放入服务器的 `plugins` 目录

3. 启动服务器，插件会自动创建配置文件

4. 修改 `plugins/MeteorEnderBox/config.yml` 中的数据库配置

5. 重启服务器

## 使用方法

### 玩家命令
- `/enderbox open` 或 `/eb open`：打开自己的末影箱
- `/enderbox help` 或 `/eb help`：显示帮助信息

### 管理员命令
- `/enderbox open <玩家名称>`：打开指定玩家的末影箱
- `/enderbox reload`：重新加载插件配置

## 配置说明

配置文件位于 `plugins/MeteorEnderBox/config.yml`：

```yaml
#author Freak_64
#联系qq1640946640
#权限meteor.ender.* 可解锁末影箱数

mysql-info:
  ip: '127.0.0.1'    # 数据库IP
  port: 3306          # 数据库端口
  parm: 'useSsl=false' # 数据库参数
  user: 'root'        # 数据库用户名
  password: 'admin'    # 数据库密码
  database: 'test0'    # 数据库名称
  validSql: 'select knit from enderbox' # 验证SQL语句
setting:
  #默认可解锁多少格箱子
  default-maxlock: 10
  #末影箱总数
  max-amount: 20
  #从第几格仓库花费点卷解锁
  points-number: 10
  #解锁花费金币数
  take-money:
    2: 1000
    4: 3000
    6: 10000
  #解锁点卷数
  take-points:
    2: 100
    4: 200
    6: 400
```

## 开发环境

- **JDK版本**：JDK 8
- **构建工具**：Maven
- **测试服务器**：Catserver 1.12.2

## 版本支持

- **最低版本**：1.12.2
- **最高版本**：理论上支持到最新的Minecraft版本（如1.20+）
- **版本适配**：插件会自动检测服务器版本并加载对应的配置文件
  - 1.12.2及以下：使用 `message.yml`
  - 1.16.5：使用 `message_V1.16.5.yml`

## 版本扩展

要支持更高版本的Minecraft，只需：
1. 复制 `message_V1.16.5.yml` 文件并命名为 `message_V<版本号>.yml`
2. 修改文件中的材料名称以适配对应版本
3. 重启服务器

## 构建方法

1. 克隆项目到本地
2. 使用 Maven 编译：`mvn clean compile package`
3. 编译后的文件位于 `target` 目录

## 许可证

本项目采用 MIT 许可证，详见 LICENSE 文件。

## 作者

- **作者**：Freak_64
- **QQ**：1640946640

## 注意事项

- 插件需要 Vault 和 PlayerPoints 插件的支持
- 首次启动时会自动创建数据库表结构
- 建议定期备份数据库，以防数据丢失
