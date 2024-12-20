![Ingegneria del Software - Progetto 2023/24](docs/banner.png)
# Ingegneria del Software - Progetto 2023/24

![Java](https://img.shields.io/badge/language-Java-brightgreen)
![Version](https://img.shields.io/badge/version-2.1.0-blue)


Questo progetto è stato sviluppato come parte del corso di **Ingegneria del Software** (Anno Accademico 2023/24) ed è volto a supportare lo **scambio di prestazioni d’opera** senza utilizzo di denaro.  
L'applicazione permette a organizzazioni di gestire categorie di attività scambiabili e supporta un processo incrementale, a partire dalla prima versione che include funzionalità di configurazione.

### 🎯 Funzionalità chiave 
* **[Versione 1.0.0](/releases/tag/v1.0.0)**
  - Gestione delle gerarchie di categorie.
  - Configurazione di fattori di conversione tra categorie foglia.
  - Gestione persistente dei dati.
  - Interfaccia CLI per configuratori.
* **[Versione 2.0.0](/releases/tag/v2.0.0)**
    - Interfaccia CLI per fruitori.

## 🚀 Installazione

* Scaricare il file `ingswproject-setup.exe` dalla sezione [Releases](/releases/latest).  
* Eseguire il file di setup e seguire le istruzioni a schermo.
* Prima di lanciare il programma, assicurarsi di avere configurato la connessione al database nel file `application.yaml` nella directory:
  - **Windows**: `%APPDATA%/ingsw_project`
  - **Mac/Linux**: `$HOME/.ingsw_project`
  Per fare questo, fare riferimento alla [prossima sezione](#configurazione-della-connessione-al-database).

### 🔧 Configurazione della connessione al database
Nel file di configurazione `application.yaml` disponibile nella cartella `%APPDATA%/ingsw_project` (Windows) o `$HOME/.ingsw_project` (Mac/Linux) è possibile specificare i datasource da utilizzare, seguendo la sintassi supportata da [Ebean ORM](https://ebean.io/docs).  
Il programma utilizzerà il database di default indicato nel file, se non specificato diversamente utilizzando l'opzione `-db` (vedi [Parametri opzionali](#parametri-opzionali)).

### 📚 Parametri opzionali
Il programma accetta i seguenti parametri in input opzionali:
- `-p` o `--platform`: Specifica il frontend da utilizzare. Valori supportati: `cli` (default).
- `-db` o `--database`: Specifica il database da utilizzare (nome specificato in `application.yaml`).
- `-u` o `--username`: Se specificato, insieme al parametro `-pw`, permette di autenticarsi automaticamente al software in fase di avvio.
- `-pw` o `--password`: Password per l'autenticazione automatica.

Esempio di esecuzione tramite riga di comando:
```bash
ingswproject.exe -p cli -db my_database -u admin -pw admin
```

## 💡 Requisiti

- **Java** 21 o superiore.
 
Per lo sviluppo:
- **Ebean ORM** per la gestione del database.
- **Maven** per la gestione del progetto.

## 👤 Autori

- **Pierpaolo Bonalda** - [p.bonalda@studenti.unibs.it](mailto:p.bonalda@studenti.unibs.it) - [GitHub](https://github.com/PierpaoloBonalda)
- **Shpetim Daiu** - [s.daiu@studenti.unibs.it](mailto:s.daiu@studenti.unibs.it) - [GitHub](https://github.com/shpetimdaiu)
- **Nicolo' Rebaioli** - [n.rebaioli@studenti.unibs.it](mailto:n.rebaioli@studenti.unibs.it) - [GitHub](https://github.com/Samurai016)

## 📄 Licenza

Questo progetto è concesso in licenza sotto la licenza **GNU General Public License v3.0** - vedi il file [LICENSE](LICENSE) per i dettagli.

---

© 2024 Progetto realizzato per il corso di Ingegneria del Software, corso di laurea in Ing. Informatica, Università degli Studi di Brescia.
