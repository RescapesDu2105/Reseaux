CREATE USER BD_JournalDeBord IDENTIFIED BY Soleil123 ;
GRANT CONNECT,RESOURCE,DBA TO BD_JournalDeBord;
GRANT UNLIMITED TABLESPACE TO BD_JournalDeBord;
grant all privileges to BD_JournalDeBord; 
--Apres il faut se connecter avec sur SQL dev avec les meme param que pour la 12C

DROP TABLE INTERVENANTS;
DROP TABLE ACTIVITES;

CREATE TABLE INTERVENANTS(
    IdIntervenant NUMBER PRIMARY KEY,
    Nom varchar2(25),
    Prenom varchar2(25),
    Role varchar2(12),
    CONSTRAINT role_CK check(Role IN('Professeur','Etudiant')));
    

CREATE TABLE ACTIVITES(
    IdActivite NUMBER PRIMARY KEY ,
    Cours   VARCHAR2(25) ,
    Cours_Labo  VARCHAR2(25),
    Travail_Labo VARCHAR2(25),
    DateActivite    DATE ,
    DescriptionActivite VARCHAR2(50),
    Intervenant NUMBER ,
    CONSTRAINT intervenant_fk FOREIGN KEY (Intervenant) REFERENCES INTERVENANTS(IdIntervenant));
    