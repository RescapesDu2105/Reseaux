.SILENT:

CC = gcc -c -m64
LIBNET = SocketUtilities
LIBCSV = libCSV
CHKIN = CIMP
LIBUTILE = libUtile
ABB = AccessBilBag
LIBCONFIG = libConfig

all: Client Serveur

Client: $(CHKIN)/Client.o $(LIBNET)/SocketUtilities.o $(LIBCSV)/CSV.o $(LIBUTILE)/FctUtile.o $(ABB)/AccessBilBag.o $(LIBCONFIG)/Config.o
		echo creation de l\'executable Client
		gcc -m64 -o Client $(CHKIN)/Client.o $(LIBNET)/SocketUtilities.o $(LIBCSV)/CSV.o $(LIBUTILE)/FctUtile.o $(ABB)/AccessBilBag.o $(LIBCONFIG)/Config.o -lm

Serveur: $(CHKIN)/Serveur.o $(LIBNET)/SocketUtilities.o $(LIBCSV)/CSV.o $(LIBUTILE)/FctUtile.o $(ABB)/AccessBilBag.o $(LIBCONFIG)/Config.o
		echo creation de l\'executable Serveur
		gcc -m64 -o Serveur $(CHKIN)/Serveur.o $(LIBNET)/SocketUtilities.o $(LIBCSV)/CSV.o $(LIBUTILE)/FctUtile.o $(ABB)/AccessBilBag.o $(LIBCONFIG)/Config.o -lrt -lpthread -lSDL -lm

$(CHKIN)/Client.o: $(CHKIN)/Client.c $(CHKIN)/Protocoles.h
		echo creation d\'Client.o
		$(CC) -o $(CHKIN)/Client.o $(CHKIN)/Client.c -lm

$(CHKIN)/Serveur.o: $(CHKIN)/Serveur.c $(CHKIN)/Protocoles.h
		echo creation de Serveur.o
		$(CC) -o $(CHKIN)/Serveur.o $(CHKIN)/Serveur.c -lm

$(LIBNET)/SocketUtilities.o: $(LIBNET)/SocketUtilities.c $(LIBNET)/SocketUtilities.h
		echo creation de SocketUtilities.o
		$(CC) -o $(LIBNET)/SocketUtilities.o $(LIBNET)/SocketUtilities.c

$(LIBCSV)/CSV.o : $(LIBCSV)/CSV.c $(LIBCSV)/CSV.h
		echo creation de CSV.o
		$(CC) -o $(LIBCSV)/CSV.o $(LIBCSV)/CSV.c

$(LIBUTILE)/FctUtile.o : $(LIBUTILE)/FctUtile.c $(LIBUTILE)/FctUtile.h
		echo creation de FctUtile.o
		$(CC) -o $(LIBUTILE)/FctUtile.o $(LIBUTILE)/FctUtile.c -lm

$(ABB)/AccessBilBag.o : $(ABB)/AccessBilBag.c $(ABB)/AccessBilBag.h
		echo creation de AccessBilBag.o
		$(CC) -o $(ABB)/AccessBilBag.o $(ABB)/AccessBilBag.c

$(LIBCONFIG)/Config.o : $(LIBCONFIG)/Config.c $(LIBCONFIG)/Config.h
		echo creation de Config.o
		$(CC) -o $(LIBCONFIG)/Config.o $(LIBCONFIG)/Config.c

clean:
	@rm -f $(CHKIN)/Client.o $(CHKIN)/Serveur.o $(LIBPROT)/Protocoles.o $(LIBNET)/SocketUtilities.o $(LIBCSV)/CSV.o $(LIBUTILE)/FctUtile.o $(ABB)/AccessBilBag.o $(LIBCONFIG)/Config.o 362_22082017_lug.csv core
	
clobber: clean
	@rm -f Client Serveur
