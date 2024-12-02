# Ingegneria del Software - Progetto 2023/24

![Java](https://img.shields.io/badge/language-Java-brightgreen)
![Version](https://img.shields.io/badge/version-1.1.0-blue)
![Build Status](https://img.shields.io/github/actions/workflow/status/Samurai016/ingswproject/build.yml)

## Descrizione del Progetto

Questo progetto è stato sviluppato come parte del corso di **Ingegneria del Software** (Anno Accademico 2023/24) ed è volto a supportare lo **scambio di prestazioni d’opera** senza utilizzo di denaro.  
L'applicazione permette a organizzazioni di gestire categorie di attività scambiabili e supporta un processo incrementale, a partire dalla prima versione che include funzionalità di configurazione.

### Funzionalità chiave (Versione 1.0.0)
- Gestione delle gerarchie di categorie.
- Configurazione di fattori di conversione tra categorie foglia.
- Gestione persistente dei dati.
- Interfaccia CLI per configuratori.

## Installazione

L'applicazione è distribuita come file eseguibile (.jar o .exe).  
Per lanciare il programma, assicurarsi di avere configurato il file di configurazione `application.yaml` nella directory:
- **Windows**: `%APPDATA%/ingsw_project`
- **Mac/Linux**: `$HOME/.ingsw_project`

### Configurazione del Database
Nel file `application.yaml` è possibile specificare i datasource da utilizzare, seguendo la sintassi supportata da [Ebean ORM](https://ebean.io/docs).  
Il programma utilizzerà il database di default indicato nel file, se non specificato diversamente.

### Parametri opzionali
Il programma accetta i seguenti parametri in input:
- `-p` o `--platform`: Specifica il frontend da utilizzare. Valori supportati: `cli` (default).
- `-db` o `--database`: Specifica il database da utilizzare (nome specificato in `application.yaml`).

Esempio di esecuzione:
```bash
java -jar ingswproject-VERSION.jar -p cli -db my_database
```

## Requisiti

- **Java** 21 o superiore.
 
Per lo sviluppo:
- **Ebean ORM** per la gestione del database.
- **Maven** per la gestione del progetto.

## Autori

- **Pierpaolo Bonalda** - [p.bonalda@studenti.unibs.it](mailto:p.bonalda@studenti.unibs.it) - [GitHub](https://github.com/PierpaoloBonalda)
- **Shpetim Daiu** - [s.daiu@studenti.unibs.it](mailto:s.daiu@studenti.unibs.it) - [GitHub](https://github.com/shpetimdaiu)
- **Nicolo' Rebaioli** - [n.rebaioli@studenti.unibs.it](mailto:n.rebaioli@studenti.unibs.it) - [GitHub](https://github.com/Samurai016)

## Repository e Release

Il codice sorgente e la release corrente sono disponibili su [GitHub](https://github.com/Samurai016/ingswproject).  
Per scaricare la versione 1.0.0, visitare la sezione [Releases](https://github.com/Samurai016/ingswproject/releases).

---

© 2024 Progetto realizzato per il corso di Ingegneria del Software, corso di laurea in Ing. Informatica, Università degli Studi di Brescia.
