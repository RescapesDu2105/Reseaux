DROP SCHEMA `bd_airport` ;
CREATE SCHEMA `bd_airport` ;

CREATE TABLE `bd_airport`.`compagnies`(
	`IdCompagnie` INT NOT NULL,
    `Nom` VARCHAR(45) NOT NULL,
    CONSTRAINT IdCompagnie_PK PRIMARY KEY (`IdCompagnie`),
    CONSTRAINT NomUNIQUE UNIQUE(`Nom`)
);

CREATE TABLE `bd_airport`.`vols` (
	`IdVol` INT NOT NULL,
	`Destination` VARCHAR(45) NOT NULL,
	`HeureDepart` DATETIME NOT NULL,
	`HeureArrive` DATETIME NOT NULL,
	CONSTRAINT IdVol_PK PRIMARY KEY (`IdVol`)
);

CREATE TABLE `bd_airport`.`avions` (
	`IdAvion` INT NOT NULL,
	`NomAvion` VARCHAR(15) NOT NULL,
	`Modele` VARCHAR(25) NULL,
	`NbPlace` INT NULL,
	`IdCompagnie` INT NOT NULL,    
	`IdVol` INT NULL,
	PRIMARY KEY (`IdAvion`),
	INDEX `Vol_idx` (`IdVol` ASC),
	CONSTRAINT `Vol`
		FOREIGN KEY (`IdVol`)
		REFERENCES `bd_airport`.`vols` (`IdVol`),
	CONSTRAINT `Compagnie` 
		FOREIGN KEY (`IdCompagnie`)
		REFERENCES `bd_airport`.`compagnies` (`IdCompagnie`)
);

CREATE TABLE `bd_airport`.`billets` (
  `IdBillet` VARCHAR(17) NOT NULL,
  `CarteIdentite` VARCHAR(14) NOT NULL,
  `NomClient` VARCHAR(25) NOT NULL,
  `PrenomClient` VARCHAR(25) NOT NULL,
  `Classe` VARCHAR(12) NOT NULL,
  `NbAccompagnant` INT NOT NULL,
  `IdVol` INT NOT NULL,
  PRIMARY KEY (`IdBillet`),
  INDEX `Vol_idx` (`IdVol` ASC),
  CONSTRAINT `Vol_FK`
    FOREIGN KEY (`IdVol`)
    REFERENCES `bd_airport`.`vols` (`IdVol`),
  CONSTRAINT Classe_CK CHECK(Classe IN ('Premiere','Economique')),
  CONSTRAINT NbAccompagnant_CK CHECK(NbAccompagnant >=0 AND NbAccompagnant<=10)
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
  PRIMARY KEY (`IdBagage`),
  INDEX `Billet_idx` (`IdBillet` ASC),
  CONSTRAINT `Billet`
    FOREIGN KEY (`IdBillet`)
    REFERENCES `bd_airport`.`billets` (`IdBillet`),
   CONSTRAINT TypeBagage_CK CHECK(TypeBagage IN('VALISE','PASVALISE')),
   CONSTRAINT Receptionne_CK Check(Receptionne IN ('O', 'N')),
   CONSTRAINT Charge_CK Check(Charge IN ('O', 'N')),
   CONSTRAINT Verifie_CK Check(Verifie IN ('O', 'N'))
  -- , CONSTRAINT Poids_CK CHECK(Poids>=0)) , CONSTRAINT IdBagage_CK CHECK(IdBagage = )
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