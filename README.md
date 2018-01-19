# notesBackend
Backend logic for notes application

# PROCEDURE D'INSTALLATION DU GESTION NOTE BACKEND UBUNTU

## PREMIÈRE ETAPE
**[Installer Netbeans 8.2](https://netbeans.org/downloads/)**
Pour installer netbeans a cette version il faut dabord installer la version 8  de la jdk. 

- [Il faut telecharger la jdk version 8](https://doc.ubuntu-fr.org/openjdk)
- apres avoir telecharger copier le contenu dezippe dans le dossier /opt/
- puis tapez la commande `sudo update-alternatives --install /usr/bin/java java /opt/jdk_dir_namebin/ 100`
- en replacant `jdk_dir_namebin` par le nom du dossier jdk.
- Lancer netbeans en donnant au fichier télécharge les droits d’exécution, puis l’exécuter

**Cloner [le projet en ligne](https://github.com/infotel-iss/notesBackend)**

- Pour cloner il faut [installer git](https://www.supinfo.com/articles/single/1410-installer-utiliser-git-ubuntu), puis le configurer. `sudo apt-get install git`
- taper la commande dans le dossier ou vous voulez garder le projet `git clone https://github.com/infotel-iss/notesBackend.git`
- Branchez vous a promo8 `git checkout promo8`

**Importez le projet dans netbeans**
Ouvrez netbeans. FIle > Open projet > dir_projet > open project.
Faire un clean and Build du projet.

**Creer la base de donnees**

- [Installer mysql server ](https://doc.ubuntu-fr.org/mysql)
- Configurez le mot de passe en `root` et utilisateur racine en `root`
- Se logger sur mysql en tappant `mysql -u root -p` puis root
- Creer la base de donnees `create database notes;`

## DEUXIEME ETAPE
### Installer la DAO 

- Il faut premièrement [télécharger la DAO](https://www.dropbox.com/s/wog83m224fdu9un/com.zip?dl=0)
- Aller dans son dossier /home/ tapez `ctrl+h` pour afficher les dossiers caches.
- Dezipez le dossier douwe et écrasez celui qui existe dans les sous dossier du dossier **.m2**

### Installez paraya
**[telecharger paraya](https://www.payara.fish/downloads)**

- Dezipez paraya dans votre dossier /home/
- Ouvrez `Netbeans > Service > Servers `
- Enlever le serveur Grassfish
- Ajouter un nouveau serveur en choisissant le dossier ou se trouve **paraya**
- Faire un Run sur le projet

### [Configurer paraya](http://localhost:4848/common/index.jsf)

- Aller sur [localhost:4848](http://localhost:4848/common/index.jsf)
- Allez a `Ressources > JDBC > JDBC connexion Pool > New` le nom sera **notesPool**, le type de ressource **javax.sql.XADataSource** et le driver **mysql**
- ![Image du formulaire a remplir](https://user-images.githubusercontent.com/28540205/29662014-c4e5544a-88bd-11e7-86dd-18f84b92af8d.png)
- Selectionnez le pool que vous venez de creer  `Ressources > JDBC > JDBC connexion Pool > notesPool > Additional Properties`
  - ajoutez **user** a la valeur **root**
  - ajoutez **password** a la valeur **root**
  - ajoutez **URL** a la valeur **jdbc:mysql://localhost:3306/notes**
  - ajoutez **url** a la valeur **jdbc:mysql://localhost:3306/notes**
  - ajoutez **serverName** a la valeur **localhost**
  - ajoutez **databaseName** a la valeur **notes** 
- Allez a `Ressources > JDBC > JDBC Ressources > New` le nom sera **jdbc/notesDatasource**, le Pool **notesPool**.

## TROISIEME ETAPE 

- Faire un clean and Build
- Faire un 
- lancer le lien [localhost:8080/notesBackend/](http://localhost:8080/notesBackend/)
![image de l application](https://user-images.githubusercontent.com/28540205/29739883-10aa3d26-8a41-11e7-8aef-c67a88932dd1.png)
