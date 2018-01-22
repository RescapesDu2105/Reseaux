INSERT INTO bd_airport.Compagnies(NomCompagnie) VALUES ('WALABIES-AIRLINES');
INSERT INTO bd_airport.Compagnies(NomCompagnie) VALUES ('POWDER-AIRLINES');
INSERT INTO bd_airport.Compagnies(NomCompagnie) VALUES ('AIR FRANCE CANAILLE');

INSERT INTO bd_airport.Avions(NomAvion, Modele, NbPlaces, IdCompagnie) VALUES ('Sully','Airbus A320','150','3');
INSERT INTO bd_airport.Avions(NomAvion, Modele, NbPlaces, IdCompagnie) VALUES ('Atlas','Boeing 777','200','2');
INSERT INTO bd_airport.Avions(NomAvion, Modele, NbPlaces, IdCompagnie) VALUES ('Anaconda','Douglas DC-3','13','3');

INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('1','Marshall','Matthers','1972-10-17','Directeur');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('2','Loxley','Swing','1995-09-07','Bagagiste');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('3','Primero','Isha','1981-08-03','Hotesse');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('4','Lome','Pall','1976-01-07','Co-Pilote');
INSERT INTO bd_airport.Agents VALUES ('5','Tusset','Quentin','1995-02-15','Bagagiste','Doublon','123');
INSERT INTO bd_airport.Agents VALUES ('6','Dimartino','Philippe','1995-09-07','Bagagiste', 'Zeydax','123');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('7','Poule','Cotcotcot','1997-10-17','Directeur');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('8','Vache','Meuuuuuuuuuuh','1989-04-28','Pilote');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('9','Poussin','QuiQui','1978-01-07','Hotesse');
INSERT INTO bd_airport.Agents(IdAgent, Nom, Prenom, DateNaissance, Poste) VALUES ('10','Sonnerie','DingDong','1977-01-07','Pilote');
INSERT INTO bd_airport.Agents VALUES ('11','Dimartino','Philippe','1995-02-15','Analyste', 'Zey','123');
INSERT INTO bd_airport.Agents VALUES ('12','Dimartino','Philippe','1995-02-15','Employe', 'Xyadez','123');

INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('714', 'Sydney', '2018-01-22 17:30:00', '2018-01-23 06:30:00','150','3');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('362', 'Peshawar', '2018-01-22 18:30:00','2018-01-22 23:30:00','200','2');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('152', 'Paris', '2018-01-22 19:30:00', '2018-01-22 20:30:00','13','1');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('714', 'Sydney', '2018-01-23 17:30:00', '2018-01-24 06:30:00','150','3');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('362', 'Peshawar', '2018-01-23 18:30:00', '2018-01-23 23:30:00','200','2');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('152', 'Paris', '2018-01-23 19:30:00', '2018-01-23 20:30:00','13','1');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('714', 'Sydney', '2018-01-24 17:30:00', '2018-01-25 06:30:00','150','3');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('362', 'Peshawar', '2018-01-24 18:30:00', '2018-01-24 23:30:00','200','2');
INSERT INTO bd_airport.Vols(NumeroVol, Destination, HeureDepart, HeureArrivee, PlacesRestantes, IdAvion) VALUES ('152', 'Paris', '2018-01-24 19:30:00', '2018-01-24 20:30:00','13','1');

INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('Zeydax', 'Dimartino', 'Philippe', 'dimartino@u2.tech.hepl.local', '1234'); -- 1
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('Doublon', 'Tusset', 'Quentin', 'tusset@u2.tech.hepl.local', '1234'); -- 2
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('Stocka', 'Verwimp', 'Jim', 'verwimp@u2.tech.hepl.local', '1234'); -- 3
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('CharWal', 'Charvilrom', 'Walter', 'charvilrom@u2.tech.hepl.local', '1234'); -- 4
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('BeauGosse', 'Vilvens', 'Claude', 'vilvens@u2.tech.hepl.local', '1234'); -- 5
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('DrHouse', 'Laurie', 'Hugh', 'laurie@u2.tech.hepl.local', '1234'); -- 6
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('Daredevil', 'Murdock', 'Matthew', 'murdock@u2.tech.hepl.local', '1234'); -- 7
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('Batman', 'Wayne', 'Bruce', 'wayne@u2.tech.hepl.local', '1234'); -- 8
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('MiyaHay', 'Miyazaki', 'Hayao', 'miyazaki@u2.tech.hepl.local', '1234'); -- 9
INSERT INTO bd_airport.clients(Login, Nom, Prenom, Mail, Password) VALUES ('ElvisRo', 'Elvis', 'Romeo', 'elvis@u2.tech.hepl.local', '1234'); -- 10

INSERT INTO bd_airport.Billets VALUES ('714-19112017-0001'/*,'000-5902781-01','Economique'*/,'1','1');
INSERT INTO bd_airport.Billets VALUES ('714-19112017-0002'/*,'000-0709199-01','Premiere'*/,'2','1');
INSERT INTO bd_airport.Billets VALUES ('714-19112017-0003'/*,'000-3589748-01','Premiere'*/,'5','1');
INSERT INTO bd_airport.Billets VALUES ('362-19112017-0001'/*,'000-6559595-01','Economique'*/,'3','2');
INSERT INTO bd_airport.Billets VALUES ('362-19112017-0002'/*,'000-9343373-01','Economique'*/,'4','2');
INSERT INTO bd_airport.Billets VALUES ('362-19112017-0003'/*,'000-5687686-01','Economique'*/,'6','2');
INSERT INTO bd_airport.Billets VALUES ('362-19112017-0004'/*,'000-2554235-01','Premiere'*/,'10','2');
INSERT INTO bd_airport.Billets VALUES ('152-19112017-0001'/*,'000-4649878-01','Economique'*/,'7','6');
INSERT INTO bd_airport.Billets VALUES ('152-19112017-0002'/*,'000-5902781-01','Premiere'*/,'8','6');
INSERT INTO bd_airport.Billets VALUES ('152-19112017-0003'/*,'000-9878897-01','Premiere','7'*/,'9','6');

INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-PHDIMARTINO-19112017-0001-1','Valise','10','714-19112017-0001');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-QUTUSSET-19112017-0002-1','PasValise','69','714-19112017-0002');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-CLVILVENS-19112017-0003-1','Valise','12','714-19112017-0003');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-WACHARVILROM-19112017-0002-1','Valise','24','362-19112017-0002');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-WACHARVILROM-19112017-0002-2','PasValise','12','362-19112017-0002');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-HULAURIE-19112017-0003-1','PasValise','16','362-19112017-0003');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-ROELVIS-19112017-0004-1','Valise','10','362-19112017-0004');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-ROELVIS-19112017-0004-2','PasValise','23','362-19112017-0004');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('152-MAMURDOCK-19112017-0001-1','PasValise','54','152-19112017-0001');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('152-BRWAYNE-19112017-0002-1','Valise','89','152-19112017-0002');
