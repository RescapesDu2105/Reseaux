DROP SCHEMA `bd_compta` ;
CREATE SCHEMA `bd_compta` ;

CREATE TABLE `bd_compta`.`langues`
(
	Id INT AUTO_INCREMENT PRIMARY KEY,
    Code VARCHAR(5) UNIQUE NOT NULL
);


INSERT INTO bd_compta.langues (Code) VALUES ('fr_FR');
INSERT INTO bd_compta.langues (Code) VALUES ('en_EN');
INSERT INTO bd_compta.langues (Code) VALUES ('nl_NL');