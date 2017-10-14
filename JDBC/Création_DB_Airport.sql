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
  `TypeBagage` VARCHAR(7) NOT NULL,
  `Poids` INT NOT NULL,
  `Billet` INT NOT NULL,
  PRIMARY KEY (`IdBagage`),
  INDEX `Billet_idx` (`Billet` ASC),
  CONSTRAINT `Billet`
    FOREIGN KEY (`Billet`)
    REFERENCES `bd_airport`.`billets` (`IdBillet`),
   CONSTRAINT TypeBagage_CK CHECK(TypeBagage IN('Valise','Coffre')),
   CONSTRAINT Poids_CK CHECK(Poids>=0));

CREATE TABLE `bd_airport`.`agents` (
  `IdAgents` INT NOT NULL,
  `NomAgent` VARCHAR(25) NOT NULL,
  `PrenomAgent` VARCHAR(25) NOT NULL,
  `DateNaiss` DATE NOT NULL,
  `Poste` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`IdAgents`));
   
