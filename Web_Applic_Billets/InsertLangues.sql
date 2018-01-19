DROP SCHEMA `bd_compta` ;
CREATE SCHEMA `bd_compta` ;

CREATE TABLE `bd_compta`.`langues`
(
	Id INT AUTO_INCREMENT PRIMARY KEY,
    Nom VARCHAR(25) UNIQUE NOT NULL
);


INSERT INTO bd_compta.langues (Nom) VALUES ('fr_FR');
INSERT INTO bd_compta.langues (Nom) VALUES ('en_EN');
INSERT INTO bd_compta.langues (Nom) VALUES ('nl_NL');