INSERT INTO bd_airport.Compagnies VALUES ('1', 'WALABIES-AIRLINES');
INSERT INTO bd_airport.Compagnies VALUES ('2', 'POWDER-AIRLINES');
INSERT INTO bd_airport.Compagnies VALUES ('3', 'AIR FRANCE CANAILLE');
/*INSERT INTO Compagnies VALUES ('4', 'LUTHANSA NE CRASH PAS');*/

/*INSERT INTO Vols VALUES ('1', 'EVS','2015-08-03 13:30:00','2015-08-03 14:30:00');
INSERT INTO Vols VALUES ('2','Tokyo','2015-12-24 15:10:00','2015-12-25 00:10:00');
INSERT INTO Vols VALUES ('3','Bruxelles','2016-01-07 21:50:00','2016-01-07 23:14:00');
INSERT INTO Vols VALUES ('4','Berlin','2016-04-28 04:25:00','2016-04-28 17:35:00');
INSERT INTO Vols VALUES ('5','Moscow','2016-01-07 21:50:00','2016-01-07 21:50:00');
INSERT INTO Vols VALUES ('6','Seoul','2017-08-03 13:30:00','2017-08-03 14:30:00');
INSERT INTO Vols VALUES ('7','Tatouine','2017-09-07 8:45:00','2017-08-03 10:30:00');
INSERT INTO Vols VALUES ('8','INPRES','2015-08-03 13:30:00','2015-08-03 14:30:00');
INSERT INTO Vols VALUES ('9','Silicon Valley','2017-08-03 13:30:00','2017-08-03 14:30:00');
INSERT INTO Vols VALUES ('10','Azeroth','2015-08-03 13:30:00','2015-08-03 14:30:00');*/
INSERT INTO bd_airport.Vols VALUES ('714', 'Sydney', '2017-10-23 05:30:00', '2017-10-23 01:00:00');
INSERT INTO bd_airport.Vols VALUES ('362', 'Peshawar', '2017-10-23 06:30:00', '2017-10-22 22:00:00');
INSERT INTO bd_airport.Vols VALUES ('152', 'Paris', '2017-10-23 07:20:00', '2017-10-22 08:20:00');

INSERT INTO bd_airport.Avions VALUES ('1','Sully','Airbus A320','30','3','152');
/*INSERT INTO Avions VALUES ('2','Albatross','Airbus A330','24','3','152');
INSERT INTO Avions VALUES ('3','Alizé','DC-8','69','3','714');*/
INSERT INTO bd_airport.Avions VALUES ('4','Atlas','Boeing 777','20','2','362');
INSERT INTO bd_airport.Avions VALUES ('5','Anaconda','Douglas DC-3','13','3','714');
/*INSERT INTO Avions VALUES ('6','Baby Clipper','Lockheed L-049','26','3','362');
INSERT INTO Avions VALUES ('7','Demon','Lockeed L-1011','12','3','362');
INSERT INTO Avions VALUES ('8','Duck','MD-90','24','3','152');
INSERT INTO Avions VALUES ('9','Neptune','Douglas DC-4A','42','3','152');
INSERT INTO Avions VALUES ('10','BatWing','Boeing 747','1','1','714');*/

INSERT INTO bd_airport.Billets VALUES ('714-23102017-0030','000-0709199-01','Tusset','Quentin','Premiere','0','714');
INSERT INTO bd_airport.Billets VALUES ('714-23102017-0031','000-5902781-01','Dimartino','Philippe','Economique','8','714');
INSERT INTO bd_airport.Billets VALUES ('362-23102017-0020','000-5687686-01','Verwimp','Jim','Economique','3','362');
INSERT INTO bd_airport.Billets VALUES ('362-23102017-0024','000-9343373-01','Charvilrom','Walter','Economique','1','362');
INSERT INTO bd_airport.Billets VALUES ('152-23102017-0010','000-5902781-01','Vilvens','Claude','Premiere','9','152');
INSERT INTO bd_airport.Billets VALUES ('152-23102017-0020','000-9878897-01','Laurie','Hugh','Premiere','7','152');
INSERT INTO bd_airport.Billets VALUES ('152-23102017-0028','000-4649878-01','Murdock','Matthew','Economique','2','152');
INSERT INTO bd_airport.Billets VALUES ('714-23102017-0010','000-3589748-01','Wayne','Bruce','Premiere','0','714');
INSERT INTO bd_airport.Billets VALUES ('362-23102017-0026','000-6559595-01','Miyazaki','Hayao','Economique','5','362');
INSERT INTO bd_airport.Billets VALUES ('362-23102017-0040','000-2554235-01','Elvis','Romeo','Premiere','10','362');

INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-QUTUSSET-23102017-0030-1','PasValise','69','714-23102017-0030');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-PHDIMARTINO-23102017-0031-1','Valise','10','714-23102017-0031');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-ROELVIS-23102017-0040-1','Valise','10','362-23102017-0040');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-CLVILVENS-23102017-0010-1','Valise','12','714-23102017-0010');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('152-MAMURDOCK-23102017-0028-1','PasValise','54','152-23102017-0028');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-WACHARVILROM-23102017-0024-1','Valise','24','362-23102017-0024');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-WACHARVILROM-23102017-0024-2','PasValise','12','362-23102017-0024');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-HULAURIE-23102017-0020-1','PasValise','16','362-23102017-0020');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('714-BRWAYNE-23102017-0010-2','Valise','89','714-23102017-0010');
INSERT INTO bd_airport.Bagages(IdBagage, TypeBagage, Poids, IdBillet) VALUES ('362-ROELVIS-23102017-0040-2','PasValise','23','362-23102017-0040');

INSERT INTO bd_airport.Agents VALUES ('1','Marshall','Matthers','1972-10-17','Directeur');
INSERT INTO bd_airport.Agents VALUES ('2','Loxley','Swing','1995-09-07','Bagagistes');
INSERT INTO bd_airport.Agents VALUES ('3','Primero','Isha','1981-08-03','Hotesse');
INSERT INTO bd_airport.Agents VALUES ('4','Lome','Pall','1976-01-07','Co-Pilote');
INSERT INTO bd_airport.Agents VALUES ('5','Tusset','Quentin','1995-02-15','Bagagiste');
INSERT INTO bd_airport.Agents VALUES ('6','Dimartino','Philippe','1995-09-07','Bagagiste');
INSERT INTO bd_airport.Agents VALUES ('7','Poule','Cotcotcot','1997-10-17','Directeur');
INSERT INTO bd_airport.Agents VALUES ('8','Vache','Meuuuuuuuuuuh','1989-04-28','Pilote');
INSERT INTO bd_airport.Agents VALUES ('9','Poussin','QuiQui','1978-01-07','Hotesse');
INSERT INTO bd_airport.Agents VALUES ('10','Sonnerie','DingDong','1977-01-07','Pilote');

INSERT INTO bd_airport.Comptes VALUES ('1','Zeydax','123','6');
INSERT INTO bd_airport.Comptes VALUES ('2','Doublon','123','5');