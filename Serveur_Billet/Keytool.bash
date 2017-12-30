#########################################KEYSTORE CLIENT######################################################
#executer dans le dossier du JDK jdk1.8.0_73\bin
.\keytool.exe -genkey -alias ClesCLient -keyalg RSA -keysize 1024 -dname "CN=Tusset Dimartino, O=INPRES, C=B"
#Les mot de passe pour le keystore et la cle "ClesCLient" : 123Soleil
#Il m'a crée dans le dossier : C:\Users\Doublon\.keystore
#je met le certificat dans un fichier appart : 
.\keytool.exe -export -alias ClesClient -file ClesClient.cer
#creattion du fichier .csr :
.\keytool.exe -certreq -alias ClesClient -file ClesClient.csr -keypass 123Soleil -storepass 123Soleil -v
#il m'a creer les fichier .csr et .cer dans : jdk1.8.0_73\bin


#########################################KEYSTORE SERVEUR######################################################
.\keytool.exe -genkey -alias ClesServeur -keyalg RSA -keysize 1024 -dname "CN=Tusset Dimartino, O=INPRES, C=B"
#les mdp sont touhjours  : 123Soleil