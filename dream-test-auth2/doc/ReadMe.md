# 文档

# dream-test-auth2-web-core

* 认证服务

# dream-test-auth2-web-mgt

* 管理服务

# JKS

## 生成

keytool.exe -genkey -alias AuthServerKey -keyalg RSA -keysize 1024 -validity 36500 -keypass Dream
FlyingFlower -storepass DreamFlyingFlower -keystore ../keystore/samlServerKeystore.jks