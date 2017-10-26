INSERT INTO bd_airport.Compagnies VALUES ('1', 'WALABIES-AIRLINES');
INSERT INTO bd_airport.Compagnies VALUES ('2', 'POWDER-AIRLINES');
INSERT INTO bd_airport.Compagnies VALUES ('3', 'AIR FRANCE CANAILLE');

INSERT INTO bd_airport.Vols VALUES ('714', 'Sydney', '2017-10-26 15:30:00', '2017-10-27 11:00:00');
INSERT INTO bd_airport.Vols VALUES ('362', 'Peshawar', '2017-10-26 16:30:00', '2017-10-26 08:00:00');
INSERT INTO bd_airport.Vols VALUES ('152', 'Paris', '2017-10-26 17:20:00', '2017-10-26 18:20:00');

INSERT INTO bd_airport.Avions VALUES ('1','Sully','Airbus A320','30','3','152');
INSERT INTO bd_airport.Avions VALUES ('4','Atlas','Boeing 777','20','2','362');
INSERT INTO bd_airport.Avions VALUES ('5','Anaconda','Douglas DC-3','13','3','714');

INSERT INTO bd_airport.Billets VALUES ('714-26102017-0030','000-0709199-01','Tusset','Quentin','Premiere','0','714');
INSERT INTO bd_airport.Billets VALUES ('714-26102017-0031','000-5902781-01','Dimartino','Philippe','Economique','8','714');
INSERT INTO bd_airport.Billets VALUES ('362-26102017-0020','000-5687686-01','Verwimp','Jim','Economique','3','362');
INSERT INTO bd_airport.Billets VALUES ('362-26102017-0024','000-9343373-01','Charvilrom','Walter','Economique','1','362');
INSERT INTO bd_airport.Billets VALUES ('152-26102017-0010','000-5902781-01','Vilvens','Claude','Premiere','9','152');
INSERT INTO bd_airport.Billets VALUES ('152-26102017-0020','000-9878897-01','Laurie','Hugh','Premiere','7','152');
INSERT INTO bd_airport.Billets VALUES ('152-26102017-0028','000-4649878-01','Murdock','Matthew','Economique','2','152');
INSERT INTO bd_airport.Billets VALUES ('714-26102017-0010','000-3589748-01','Wayne','Bruce','Premiere','0','714');
INSERT INTO bd_airport.Billets VALUES ('362-26102017-0026','000-6559595-01','Miyazaki','Hayao','Economique','5','362');
INSERT INTO bd_airport.Billets VALUES ('362-26102017-0040','000-2554235-01','Elvis','Romeo','Premiere','10','362');

INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-QUTUSSET-26102017-0030-1','PasValise','69','714-26102017-0030');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-PHDIMARTINO-26102017-0031-1','Valise','10','714-26102017-0031');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-ROELVIS-26102017-0040-1','Valise','10','362-26102017-0040');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-CLVILVENS-26102017-0010-1','Valise','12','714-26102017-0010');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('152-MAMURDOCK-26102017-0028-1','PasValise','54','152-26102017-0028');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-WACHARVILROM-26102017-0024-1','Valise','24','362-26102017-0024');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-WACHARVILROM-26102017-0024-2','PasValise','12','362-26102017-0024');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-HULAURIE-26102017-0020-1','PasValise','16','362-26102017-0020');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-BRWAYNE-26102017-0010-2','Valise','89','714-26102017-0010');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-ROELVIS-26102017-0040-2','PasValise','23','362-26102017-0040');

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