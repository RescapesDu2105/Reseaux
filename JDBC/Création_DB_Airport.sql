DROP SCHEMA `bd_airport` ;
CREATE SCHEMA `bd_airport` ;

CREATE TABLE `bd_airport`.`vols` (
  `IdVol` INT NOT NULL,
  `Destination` VARCHAR(45) NOT NULL,
  `HeureDepart` DATETIME NOT NULL,
  `HeureArrive` DATETIME NOT NULL,
	CONSTRAINT IdVol_PK PRIMARY KEY (`IdVol`));

CREATE TABLE `bd_airport`.`avions` (
  `IdAvion` INT NOT NULL,
  `NomAvion` VARCHAR(15) NOT NULL,
  `Modele` VARCHAR(25) NULL,
  `NbPlace` INT NULL,
  `Vol` INT NULL,
  PRIMARY KEY (`IdAvion`),
  INDEX `Vol_idx` (`Vol` ASC),
  CONSTRAINT `Vol`
    FOREIGN KEY (`Vol`)
    REFERENCES `bd_airport`.`vols` (`IdVol`));

CREATE TABLE `bd_airport`.`billets` (
  `IdBillet` INT NOT NULL,
  `CarteIdent` VARCHAR(14) NOT NULL,
  `NomClient` VARCHAR(25) NOT NULL,
  `PrenomClient` VARCHAR(25) NOT NULL,
  `Classe` VARCHAR(12) NOT NULL,
  `NbAccompagnant` INT NOT NULL,
  `Vol` INT NOT NULL,
  PRIMARY KEY (`IdBillet`),
  INDEX `Vol_idx` (`Vol` ASC),
  CONSTRAINT `Vol_FK`
    FOREIGN KEY (`Vol`)
    REFERENCES `bd_airport`.`vols` (`IdVol`),
  CONSTRAINT Classe_CK CHECK(Classe IN ('Premiere','Economique')),
  CONSTRAINT NbAccompagnant_CK CHECK(NbAccompagnant >=0 AND NbAccompagnant<=10)
  );

CREATE TABLE `bd_airport`.`bagages` (
  `IdBagage` INT NOT NULL,
  `TypeBagage` VARCHAR(9) NOT NULL,
  `Poids` INT NOT NULL,
  `Billet` INT NOT NULL,
  PRIMARY KEY (`IdBagage`),
  INDEX `Billet_idx` (`Billet` ASC),
  CONSTRAINT `Billet`
    FOREIGN KEY (`Billet`)
    REFERENCES `bd_airport`.`billets` (`IdBillet`),
   CONSTRAINT TypeBagage_CK CHECK(TypeBagage IN('Valise','PasValise')),
   CONSTRAINT Poids_CK CHECK(Poids>=0));

CREATE TABLE `bd_airport`.`agents` (
  `IdAgent` INT NOT NULL,
  `NomAgent` VARCHAR(25) NOT NULL,
  `PrenomAgent` VARCHAR(25) NOT NULL,
  `DateNaissance` DATE NOT NULL,
  `Poste` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`IdAgent`));
   
CREATE TABLE `bd_airport`.`comptes`(
  `IdCompte` INT NOT NULL,
  `Login` VARCHAR(25) NOT NULL,
  `Password` VARCHAR(25) NOT NULL,
  `IdAgent`INT NOT NULL,
  PRIMARY KEY(`IdCompte`),
  INDEX `Compte_idx` (`IdAgent` ASC),
  CONSTRAINT `IdAgent` FOREIGN KEY (`IdAgent`) REFERENCES `bd_airport`.`agents` (`IdAgent`),
  CONSTRAINT `LoginUnique` UNIQUE(`Login`)
);