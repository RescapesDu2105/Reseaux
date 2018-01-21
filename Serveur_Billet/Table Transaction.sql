CREATE TABLE `bd_airport`.`transaction` (
  `idTransaction` INT NOT NULL AUTO_INCREMENT,
  `Montant` INT ,
  `NumCarte` VARCHAR(45) ,
  `Valide` VARCHAR(45) ,
  `Client` INT,
  CONSTRAINT idTransaction_PK PRIMARY KEY (`idTransaction`),
  CONSTRAINT Transaction_Client_FK
    FOREIGN KEY (`Client`)
    REFERENCES `bd_airport`.`clients` (`IdClient`)
);