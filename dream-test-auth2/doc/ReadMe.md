# JKS
keytool.exe -genkey -alias AuthServerKey -keyalg RSA -keysize 1024 -validity 36500 -keypass Dream
FlyingFlower -storepass DreamFlyingFlower -keystore ../keystore/samlServerKeystore.jks