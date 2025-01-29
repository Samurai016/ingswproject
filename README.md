<picture>
  <source media="(min-width: 769px)" srcset="docs/banner.png">
  <source media="(max-width: 768px)" srcset="docs/banner-mobile.png">
  <img src="docs/banner.png" title="Ingegneria del Software - Progetto 2023/24">
</picture>

# Ingegneria del Software - Progetto 2023/24

![Java](https://img.shields.io/badge/language-Java-brightgreen)
![Version](https://img.shields.io/badge/version-4.1.1-blue)


Questo progetto è stato sviluppato come parte del corso di **Ingegneria del Software** (Anno Accademico 2023/24) ed è volto a supportare lo **scambio di prestazioni d’opera** senza utilizzo di denaro.  
L'applicazione permette a organizzazioni di gestire categorie di attività scambiabili e supporta un processo incrementale, a partire dalla prima versione che include funzionalità di configurazione.

### 🎯 Funzionalità chiave 
* **[Versione 1.0.0](https://github.com/Samurai016/ingswproject/releases/tag/v1.0.0)**
  - Gestione delle gerarchie di categorie.
  - Configurazione di fattori di conversione tra categorie foglia.
  - Gestione persistente dei dati.
  - Interfaccia CLI per configuratori.
* **[Versione 2.0.0](https://github.com/Samurai016/ingswproject/releases/tag/v2.0.0)**
  - Interfaccia CLI per fruitori.
* **[Versione 3.0.0](https://github.com/Samurai016/ingswproject/releases/tag/v3.0.0)**
  - Possibilità di inserire nuove proposte di scambio per i fruitori.
* **[Versione 4.0.0](https://github.com/Samurai016/ingswproject/releases/tag/v4.0.0)**
  - Sistema di chiusura automatico delle proposte di scambio.
  - Sistema di notifica delle proposte di scambio chiuse ai configuratori.
  - Visualizzazione avanzate delle proposte.
  - Possibilità di ritirare le proposte di scambio.
  - Storico delle modifiche delle proposte di scambio.

## 🚀 Installazione

* Scaricare il file `ingswproject-setup.exe` dalla sezione [Releases](https://github.com/Samurai016/ingswproject/releases/latest).  
* Eseguire il file di setup e seguire le istruzioni a schermo.
* Prima di lanciare il programma, assicurarsi di avere configurato la connessione al database.
  Per fare questo, fare riferimento alla [prossima sezione](#-configurazione-della-connessione-al-database).

### 🔧 Configurazione della connessione al database
Il programma include uno tool per la configurazione automatica del database.
Lo strumento viene lanciato automaticamente all'avvio del programma, se non è stata ancora configurata una connessione al database.
In alternativa, è possibile lanciare lo strumento manualmente eseguendo il comando `ingswproject.exe --dbtool`.

In ogni caso, nel file di configurazione `application.yaml` disponibile nella cartella `%APPDATA%/ingsw_project` (Windows) o `$HOME/.ingsw_project` (Mac/Linux) è possibile specificare i datasource da utilizzare, seguendo la sintassi supportata da [Ebean ORM](https://ebean.io/docs).  
Il programma utilizzerà il database di default indicato nel file, se non specificato diversamente utilizzando l'opzione `-db` (vedi [Parametri opzionali](#-parametri-opzionali)).

### 📚 Parametri opzionali
Il programma accetta i seguenti parametri in input opzionali:
- `-h` o `--help`: Mostra l'elenco dei parametri supportati.
- `-p` o `--platform`: Specifica il frontend da utilizzare. Valori supportati: `cli` (default).
- `-db` o `--database`: Specifica il database da utilizzare (nome specificato in `application.yaml`).
- `-u` o `--username`: Se specificato, insieme al parametro `-pw`, permette di autenticarsi automaticamente al software in fase di avvio.
- `-pw` o `--password`: Password per l'autenticazione automatica.
- `-l` o `--language`: Specifica la lingua da utilizzare per l'interfaccia utente (codici ISO 639). Se non specificato oppure se la lingua non è supportata, verrà utilizzata la lingua di sistema.
- `-v` o `--verbose`: Abilita la modalità di logging avanzata degli errori.
- `-dbt` o `--dbtool`: Avvia lo strumento di configurazione del database.

Esempio di esecuzione tramite riga di comando:
```bash
ingswproject.exe -p cli -db my_database -u admin -pw admin -l en -v
```

## 💡 Requisiti

- **Java** 23 o superiore.
 
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

© 2025 Progetto realizzato per il corso di Ingegneria del Software, corso di laurea in Ing. Informatica, Università degli Studi di Brescia.
