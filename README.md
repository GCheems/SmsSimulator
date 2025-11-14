# SMS Simulator

一个 Android 短信模拟器应用，可以模拟接收短信并写入系统短信数据库。

## 📱 项目简介

SMS Simulator 是一个用于测试和开发目的的 Android 应用，允许您模拟接收短信。该应用需要成为系统的默认短信应用才能写入短信到系统数据库。

## ✨ 功能特性

- ✅ **请求默认短信应用权限** - 支持 Android Q (API 29) 及以上版本的 RoleManager，以及旧版本的默认应用切换
- ✅ **写入模拟短信** - 将自定义的发送者和内容写入系统短信数据库
- ✅ **接收真实短信** - 作为默认短信应用时，可以接收并记录真实短信
- ✅ **MMS 支持** - 支持接收多媒体消息 (MMS)
- ✅ **响应消息服务** - 支持通过消息响应功能
- ✅ **友好的用户界面** - 简洁直观的操作界面

## 🛠 技术栈

- **开发语言**: Kotlin
- **最低 SDK 版本**: Android 5.0 (API 21)
- **目标 SDK 版本**: Android 14 (API 34)
- **编译 SDK 版本**: Android 14 (API 34)
- **构建工具**: Gradle 8.2.2
- **Kotlin 版本**: 1.9.22

### 主要依赖

- `androidx.core:core-ktx:1.12.0`
- `androidx.appcompat:appcompat:1.6.1`
- `com.google.android.material:material:1.11.0`
- `androidx.constraintlayout:constraintlayout:2.1.4`

## 📋 系统要求

- Android 5.0 (API 21) 或更高版本
- 需要成为默认短信应用才能写入短信

## 🚀 安装和构建

### 前置要求

- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 17 或更高版本
- Android SDK (API 34)

### 构建步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/GCheems/SmsSimulator
   cd SmsSimulator
   ```

2. **使用 Gradle 构建**
   
   Windows:
   ```bash
   gradlew.bat assembleDebug
   ```
   
   Linux/Mac:
   ```bash
   ./gradlew assembleDebug
   ```

3. **安装到设备**
   ```bash
   ./gradlew installDebug
   ```

   或者使用 Android Studio:
   - 打开项目
   - 点击 "Run" 按钮或按 `Shift+F10`

## 📖 使用方法

### 1. 授予权限

首次启动应用时，系统会要求授予以下权限：
- 读取短信
- 接收短信
- 发送短信
- 接收 WAP Push
- 接收 MMS
- 写入短信（系统权限）

### 2. 设置为默认短信应用

1. 点击主界面上的 "请求默认应用" 按钮
2. 在系统设置中选择本应用作为默认短信应用
3. 状态栏会显示当前是否为默认应用

### 3. 写入模拟短信

1. 在 "发送者" 输入框中输入发送者号码或名称
2. 在 "内容" 输入框中输入短信内容
3. 点击 "写入短信" 按钮
4. 短信将被写入系统短信数据库，可以在系统短信应用中查看

### 4. 恢复默认短信应用

写入短信后，建议切换回原来的默认短信应用，以免影响正常收发短信。应用会提示您进行切换。

## 🔐 权限说明

应用需要以下权限才能正常工作：

| 权限 | 用途 |
|------|------|
| `READ_SMS` | 读取短信内容 |
| `WRITE_SMS` | 写入短信到系统数据库（系统权限） |
| `SEND_SMS` | 发送短信功能 |
| `RECEIVE_SMS` | 接收短信广播 |
| `RECEIVE_WAP_PUSH` | 接收 WAP Push 消息 |
| `RECEIVE_MMS` | 接收多媒体消息 |
| `SEND_RESPOND_VIA_MESSAGE` | 响应消息服务 |

## 📁 项目结构

```
SmsSimulator/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/smssimulator/
│   │   │   │   ├── MainActivity.kt          # 主界面活动
│   │   │   │   ├── ComposeSmsActivity.kt    # 短信编写活动
│   │   │   │   ├── SmsReceiver.kt          # 短信接收器
│   │   │   │   ├── MmsReceiver.kt          # MMS 接收器
│   │   │   │   ├── RespondViaMessageService.kt  # 响应消息服务
│   │   │   │   └── SmsUtils.kt             # 短信工具类
│   │   │   ├── res/                        # 资源文件
│   │   │   └── AndroidManifest.xml         # 应用清单
│   │   ├── androidTest/                    # Android 测试
│   │   └── test/                           # 单元测试
│   └── build.gradle.kts                    # 应用构建配置
├── build.gradle.kts                        # 项目构建配置
├── settings.gradle.kts                     # 项目设置
└── gradle.properties                       # Gradle 属性

```

## ⚠️ 注意事项

1. **系统权限限制**: `WRITE_SMS` 是系统级权限，只有默认短信应用才能使用。因此必须将本应用设置为默认短信应用才能写入短信。

2. **Android 版本差异**: 
   - Android Q (API 29) 及以上版本使用 RoleManager 管理默认应用
   - Android 5.0-8.1 使用传统的默认应用切换方式

3. **测试用途**: 本应用主要用于开发和测试目的，不建议长期作为默认短信应用使用。

4. **数据安全**: 写入的短信会永久保存在系统数据库中，请谨慎使用。

## 🧪 测试

运行单元测试：
```bash
./gradlew test
```

运行 Android 测试：
```bash
./gradlew connectedAndroidTest
```

## 📝 版本信息

- **版本号**: 1.0
- **版本代码**: 1

## 👤 作者

飞宇

## 📄 许可证

本项目仅供学习和测试使用。

---

**免责声明**: 本应用仅用于开发和测试目的。使用本应用写入的模拟短信会保存在系统数据库中，请谨慎使用。作者不对因使用本应用而导致的任何问题负责。

