# TravelApp

TravelApp è una applicazione sviluppata da Luigi Previdente e Bernardo Giordano per il corso di Laboratorio di sviluppo software, A.A.2017.

### Descrizione

L'applicazione nasce per aiutare e facilitare l'utente nell'organizzazione di un viaggio (di piccola o lunga durata), nella fase di progettazione e nel durante.

### Interfaccia

All'avvio, l'applicazione mostra all'utente la lista dei viaggi che ha già programmato (nell'eventualità che ce ne siano già), altrimenti notifica l'utente mediante un messaggio a schermo che non ha alcun viaggio in lista.
La possibilità di aggiungere un nuovo viaggio è data tramite un pulsante nell'angolo in basso a destra dello schermo, che porta ad una nuova activity che permette la creazione del nuovo viaggio.

La nuova activity si presenta con un campo di testo con autocompletamento, che aiuta l'utente a scegliere delle destinazioni in base a quello che ha già scritto al suo interno. 
Inoltre viene chiesta la data di partenza e di arrivo.

Una volta completati tutti i campi e confermata la creazione del nuovo viaggio, esso viene mostrato nell'activity descritta precedentemente, dove è possibile compiere delle azioni sui viaggi già esistenti:

 * vedere i dettagli relativi al viaggio selezionato (**mediante tap sul viaggio**)
 * modificare un viaggio già esistente (**mediante pressione lunga sul viaggio  e con la relativa opzione**)
 * cancellare (**mediante pressione lunga sul viaggio e con la relativa opzione**)
 
Quando l'utente tappa su un viaggio, mostriamo una nuova activity che mostra la lista dei giorni di permanenza nel luogo scelto, che saranno aggiunti automaticamente partendo dalle informazioni ricavate precedentemente.

Tappando sul giorno desiderato, l'utente sarà in grado di vedere tutti gli eventi che potranno essere effettuati nel giorno scelto, i quali potranno essere aggiunti mediante bottone apposito.

Gli eventi possibili sono di **tre tipi**:

 * **Luogo di interesse**
 * **Mezzo di trasporto**
 * **Accomodamento**
 
In fase di creazione di un nuovo evento, l'utente sarà in grado di sceglierne il titolo, l'ora, una nota aggiuntiva e la possibilità di esserne notificato tot tempo prima.

Se i giorni visibili nella lista apposita hanno degli eventi, questi vengono mostrati mediante una preview nella lista dei giorni del viaggio.

### Implementazione

La struttura delle classi è la seguente:

| Classe | Descrizione |
|:---:|:---:|
**Datastore** | Permette la gestione delle informazioni dell'utente, sottoforma di dato su disco, accessibile privatamente dall'applicazione. Contiene metodi che permettono di accedere alla struttura dati innestata che useremo
**Trip** | Modella l'oggetto che verrà usato per contenere le informazioni relative al viaggio
**Day** | Modella l'oggetto che verrà usato per contenere le informazioni relative al giorno
**Event** |  Modella l'oggetto che verrà usato per contenere le informazioni relative agli eventi possibili (luogo di interesse, accomodamento e mezzo di trasporto)
**TripListActivity** | Activity per la gestione della visualizzione e l'interfaccia con la lista dei viaggi
**DayListActivity** | Activity per la gestione della visualizzione e l'interfaccia con la lista dei giorni per ciasciun viaggio
**EventListActivity** | Activity per la gestione della visualizzione e l'interfaccia con la lista degli eventi per ciasciun giorno
**TripListAdapter** | Adapter per la relativa activity
**DayListAdapter** | Adapter per la relativa activity
**EventListAdapter** | Adapter per la relativa activity
**CityActivity** | Activity per la creazione/modifica di un nuovo viaggio
**EventActivity** | Activity per la creazione/modifica di un nuovo evento
**AutocompleteAdapter** | Adapter extra per la gestione dell'autocompletamento