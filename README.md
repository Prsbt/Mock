# Mock
Mock API for job interview

sito dove ho ricavato le info per il cf: https://www.agenziaentrate.gov.it/portale/web/guest/schede/istanze/richiesta-ts_cf/informazioni-codificazione-pf


Non gestisce gli omonimi non avendo accesso ad alcun db su cui confrontare il cf;

Non gestisce l'ultimo carattere sempre per il discorso sugli omonimi;

Non controlla se i comuni sono corretti non potendo attingere ad alcuna risorsa esterna valida;

Per swagger l'url è: http://localhost:8080/swagger.html


# Docker
- entrare nella cartella del progetto dove è presente Dockerfile;
- eseguire ```docker build -t springapi .```;
- eseguire ```docker images``` per controllare se è stata creata l'immagine;
- eseguire ```docker run -p 8080:8080 springapi``` o usare le porte che si vogliono;
- adesso dall'url di swagger sarà possibile testare l'API;
- per stoppare il container ```docker stop {idContainer}```;


