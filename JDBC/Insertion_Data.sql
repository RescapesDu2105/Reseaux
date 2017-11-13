INSERT INTO bd_airport.Compagnies(NomCompagnie) VALUES ('WALABIES-AIRLINES');
INSERT INTO bd_airport.Compagnies(NomCompagnie) VALUES ('POWDER-AIRLINES');
INSERT INTO bd_airport.Compagnies(NomCompagnie) VALUES ('AIR FRANCE CANAILLE');

INSERT INTO bd_airport.Avions(NomAvion, Modele, NbPlaces, IdCompagnie) VALUES ('Sully','Airbus A320','150','3');
INSERT INTO bd_airport.Avions(NomAvion, Modele, NbPlaces, IdCompagnie) VALUES ('Atlas','Boeing 777','200','2');
INSERT INTO bd_airport.Avions(NomAvion, Modele, NbPlaces, IdCompagnie) VALUES ('Anaconda','Douglas DC-3','13','3');

INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('1','Marshall','Matthers','1972-10-17','Directeur');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('2','Loxley','Swing','1995-09-07','Bagagistes');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('3','Primero','Isha','1981-08-03','Hotesse');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('4','Lome','Pall','1976-01-07','Co-Pilote');
INSERT INTO bd_airport.Agents VALUES ('5','Tusset','Quentin','1995-02-15','Bagagiste','Doublon','123');
INSERT INTO bd_airport.Agents VALUES ('6','Dimartino','Philippe','1995-09-07','Bagagiste', 'Zeydax','123');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('7','Poule','Cotcotcot','1997-10-17','Directeur');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('8','Vache','Meuuuuuuuuuuh','1989-04-28','Pilote');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('9','Poussin','QuiQui','1978-01-07','Hotesse');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('10','Sonnerie','DingDong','1977-01-07','Pilote');
INSERT INTO bd_airport.Agents VALUES ('11','Dimartino','Philippe','1995-02-15','Analyste', 'Zey','123');

INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('714', 'Sydney', '2017-11-12 15:30:00', '2017-11-05 11:00:00','150','3');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('362', 'Peshawar', '2017-11-12 16:30:00', '2017-11-05 08:00:00','200','2');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('152', 'Paris', '2017-11-12 17:20:00', '2017-11-04 18:20:00','13','1');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('714', 'Sydney', '2017-12-13 15:30:00', '2017-11-05 11:00:00','150','3');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('362', 'Peshawar', '2017-12-13 16:30:00', '2017-11-05 08:00:00','200','2');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('152', 'Paris', '2017-12-13 17:20:00', '2017-11-04 18:20:00','13','1');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('714', 'Sydney', '2017-01-14 15:30:00', '2017-11-05 11:00:00','150','3');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('362', 'Peshawar', '2017-01-14 16:30:00', '2017-11-05 08:00:00','200','2');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('152', 'Paris', '2017-01-14 17:20:00', '2017-11-04 18:20:00','13','1');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('714', 'Sydney', '2016-11-15 15:30:00', '2017-11-05 11:00:00','150','3');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('362', 'Peshawar', '2016-11-15 16:30:00', '2017-11-05 08:00:00','200','2');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('152', 'Paris', '2015-11-15 17:20:00', '2017-11-04 18:20:00','13','1');

INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('Zeydax', 'Dimartino', 'Philippe', '1234');
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('Doublon', 'Tusset', 'Quentin', '1234');
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('Stocka', 'Verwimp', 'Jim', '1234');
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('CharWal', 'Charvilrom', 'Walter', '1234');
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('BeauGosse', 'Vilvens', 'Claude', '1234');
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('DrHouse', 'Laurie', 'Hugh', '1234');
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('Daredevil', 'Murdock', 'Matthew', '1234');
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('Batman', 'Wayne', 'Bruce', '1234');
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('MiyaHay', 'Miyazaki', 'Hayao', '1234');
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Password) VALUES ('ElvisRo', 'Elvis', 'Romeo', '1234');

INSERT INTO bd_airport.Billets VALUES ('714-29102017-0030'/*,'000-0709199-01','Premiere'*/,'0','2','1');
INSERT INTO bd_airport.Billets VALUES ('714-29102017-0031'/*,'000-5902781-01','Economique'*/,'8','1','1');
INSERT INTO bd_airport.Billets VALUES ('362-29102017-0020'/*,'000-5687686-01','Economique'*/,'3','6','2');
INSERT INTO bd_airport.Billets VALUES ('362-29102017-0024'/*,'000-9343373-01','Economique'*/,'1','4','2');
INSERT INTO bd_airport.Billets VALUES ('152-29102017-0010'/*,'000-5902781-01','Premiere'*/,'9','8','6');
INSERT INTO bd_airport.Billets VALUES ('152-29102017-0020'/*,'000-9878897-01','Premiere','7'*/,'8','9','6');
INSERT INTO bd_airport.Billets VALUES ('152-29102017-0028'/*,'000-4649878-01','Economique'*/,'2','7','6');
INSERT INTO bd_airport.Billets VALUES ('714-29102017-0010'/*,'000-3589748-01','Premiere'*/,'0','5','1');
INSERT INTO bd_airport.Billets VALUES ('362-29102017-0026'/*,'000-6559595-01','Economique'*/,'5','3','2');
INSERT INTO bd_airport.Billets VALUES ('362-29102017-0040'/*,'000-2554235-01','Premiere'*/,'10','10','2');

INSERT INTO bd_airport.Billets VALUES ('152-12112017-0028'/*,'000-4649878-01','Economique'*/,'2','7','3');

INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-QUTUSSET-29102017-0030-1','PasValise','69','714-29102017-0030');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-PHDIMARTINO-29102017-0031-1','Valise','10','714-29102017-0031');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-ROELVIS-29102017-0040-1','Valise','10','362-29102017-0040');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-CLVILVENS-29102017-0010-1','Valise','12','714-29102017-0010');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('152-MAMURDOCK-29102017-0028-1','PasValise','54','152-29102017-0028');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-WACHARVILROM-29102017-0024-1','Valise','24','362-29102017-0024');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-WACHARVILROM-29102017-0024-2','PasValise','12','362-29102017-0024');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-HULAURIE-29102017-0020-1','PasValise','16','362-29102017-0020');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-BRWAYNE-29102017-0010-2','Valise','89','714-29102017-0010');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-ROELVIS-29102017-0040-2','PasValise','23','362-29102017-0040');

INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('152-MAMURDOCK-12112017-0028-1','PasValise','54','152-12112017-0028');