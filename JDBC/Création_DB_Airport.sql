DROP SCHEMA `bd_airport` ;
CREATE SCHEMA `bd_airport` ;

CREATE TABLE `bd_airport`.`compagnies`(
	`IdCompagnie` INT NOT NULL AUTO_INCREMENT,
    `NomCompagnie` VARCHAR(45) NOT NULL,
    CONSTRAINT IdCompagnie_PK PRIMARY KEY (`IdCompagnie`),
    CONSTRAINT NomUNIQUE UNIQUE(`NomCompagnie`)
);

CREATE TABLE `bd_airport`.`avions` (
	`IdAvion` INT NOT NULL AUTO_INCREMENT,
	`NomAvion` VARCHAR(15) NOT NULL,
	`Modele` VARCHAR(25) NULL,
	`NbPlaces` INT NULL,
	`IdCompagnie` INT NOT NULL,
	CONSTRAINT IdAvion_PK PRIMARY KEY (`IdAvion`),
	CONSTRAINT `Avions_Compagnie_FK` 
		FOREIGN KEY (`IdCompagnie`)
		REFERENCES `bd_airport`.`compagnies` (`IdCompagnie`)
);

CREATE TABLE `bd_airport`.`vols` (
	`IdVol` INT NOT NULL AUTO_INCREMENT,
    `NumeroVol` INT NOT NULL,
	`Destination` VARCHAR(45) NOT NULL,
	`HeureDepart` DATETIME NOT NULL,
	`HeureArrivee` DATETIME NOT NULL,
    `PlacesRestantes` INT NOT NULL,
    `IdAvion` INT NOT NULL,
	CONSTRAINT IdVol_PK PRIMARY KEY (`IdVol`, `HeureDepart`),
	CONSTRAINT `Vols_IdAvion_FK`
		FOREIGN KEY (`IdAvion`)
		REFERENCES `bd_airport`.`avions` (`IdAvion`),
	CONSTRAINT PlacesRestantes_CK CHECK(PlacesRestantes >= 0)
);

CREATE TABLE `bd_airport`.`clients` (
	`IdClient` INT NOT NULL AUTO_INCREMENT,
    `Nom` VARCHAR(50) NOT NULL,
    `Prenom` VARCHAR(50) NOT NULL,
	`Login` VARCHAR(25) NOT NULL,
	`Password` VARCHAR(25) NOT NULL,  
	CONSTRAINT IdClient_PK PRIMARY KEY (`IdClient`),
	CONSTRAINT LoginUNIQUE UNIQUE(`Login`)
);

CREATE TABLE `bd_airport`.`billets` (
  `IdBillet` VARCHAR(17) NOT NULL,
  /*`CarteIdentite` VARCHAR(14) NOT NULL,*/
  /*`Classe` VARCHAR(12) NOT NULL,*/
  `NbAccompagnants` INT NOT NULL,
  `IdClient` INT NOT NULL,
  `IdVol` INT NOT NULL,
  CONSTRAINT IdBillet_PK PRIMARY KEY (`IdBillet`),
  INDEX `Vol_idx` (`IdVol` ASC),
  CONSTRAINT `Billets_Vol_FK`
    FOREIGN KEY (`IdVol`)
    REFERENCES `bd_airport`.`vols` (`IdVol`),
  CONSTRAINT `Billets_Client_FK`
    FOREIGN KEY (`IdClient`)
    REFERENCES `bd_airport`.`clients` (`IdClient`),
  /*CONSTRAINT Classe_CK CHECK(Classe IN ('Premiere','Economique')),*/
  CONSTRAINT NbAccompagnant_CK CHECK(NbAccompagnants >=0 AND NbAccompagnants<=10)
  );

CREATE TABLE `bd_airport`.`bagages` (
  `IdBagage` VARCHAR(50) NOT NULL,
  `Poids` FLOAT NOT NULL,
  `TypeBagage` VARCHAR(9) NOT NULL,
  `Receptionne` CHAR(1) DEFAULT 'N',
  `Charge` CHAR(1) DEFAULT 'N',
  `Verifie` CHAR(1) DEFAULT 'N',
  `Remarques` VARCHAR(100) DEFAULT 'NEANT',
  `IdBillet` VARCHAR(17) NOT NULL,
  CONSTRAINT IdBagage_PK PRIMARY KEY (`IdBagage`),
  INDEX `Billet_idx` (`IdBillet` ASC),
  CONSTRAINT `Bagages_Billet_FK`
    FOREIGN KEY (`IdBillet`)
    REFERENCES `bd_airport`.`billets` (`IdBillet`),
   CONSTRAINT TypeBagage_CK CHECK(TypeBagage IN('VALISE','PASVALISE')),
   CONSTRAINT Receptionne_CK Check(Receptionne IN ('O', 'N')),
   CONSTRAINT Charge_CK Check(Charge IN ('O', 'N')),
   CONSTRAINT Verifie_CK Check(Verifie IN ('O', 'N'))
);

CREATE TABLE `bd_airport`.`agents` (
  `IdAgent` INT NOT NULL,
  `Nom` VARCHAR(25) NOT NULL,
  `Prenom` VARCHAR(25) NOT NULL,
  `DateNaissance` DATE NOT NULL,
  `Poste` VARCHAR(45) NOT NULL,
  `Login` VARCHAR(25),
  `Password` VARCHAR(25),
  CONSTRAINT `LoginUnique` UNIQUE(`Login`),
  PRIMARY KEY (`IdAgent`));
  
CREATE TABLE `bd_airport`.`promesses`(
	`IdPromesse` INT NOT NULL AUTO_INCREMENT,
    `NbAccompagnants` INT NOT NULL,
    `DateTimePromesse` TIMESTAMP NOT NULL,
	`IdClient` INT NOT NULL,
	`IdVol` INT NOT NULL,
    CONSTRAINT IdPromesse_PK PRIMARY KEY (`IdPromesse`),
	CONSTRAINT `Promesses_Client_FK`
		FOREIGN KEY (`IdClient`)
		REFERENCES `bd_airport`.`clients` (`IdClient`),
	CONSTRAINT `Promesses_Vol_FK`
		FOREIGN KEY (`IdVol`)
		REFERENCES `bd_airport`.`vols` (`IdVol`)
);