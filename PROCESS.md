##Logboek Programmeerproject<br>

###Dag 1: 
<br>Proposal gemaakt, schetsen gemaakt en nagedacht over database. Eerste idee is om de database op josienjansen.nl te doen.<br>
###Dag 2: <br>
Database blijkt niet (gemakkelijk) mogelijk op josienjansen.nl, omdat je dan ook met php moet werken en server-oriented moet programmeren.<br> Daarom is er een overstap gemaakt naar Firebase. Er zijn wat nieuwe layout ideeën en er is nagedacht over de opzet van de app, welke classes etc er nodig zijn. Dit wordt morgen verder uitgewerkt.
###Dag 3: <br>
Nieuwe schetsen zijn gemaakt, het design document is klaar. Verder is er begonnen met de opmaak van de app, om een goed prototype uit te werken. Twijfel over de opzet van de app, alles is 1 grote database en dat is niet perse mijn ding <br>

###Dag 4:<br>
Database opzet helemaal uitgewerkt met een teamgenoot. Bleek veel complexer dan gedacht, 4 tabellen die allemaal onderling relaties hebben en deze moeten allemaal een Helpersclass hebben etc. Zie het idee eigenlijk niet meer zitten. 
Update: overgestapt naar een ander idee. Heleboel in te halen. Prototype gemaakt en proposal voor geschreven. In het weekend design document maken en alles bijwerken.<br>
###Dag 5:<br>
Proposal en prototype afgemaakt, voorbereidingen presentaties.<br>
###Weekend: <br>
Design Document gemaakt, proposal aangepast. Verder de login via Facebook af, uitloggen kan ook. Zitten nog een paar bugs in, maar ga eerst verder met de API van de NS.<br>
###Dag 6:<br>
Authenticatie van NS API is gelukt, even wat uitzoekwerk. Parsen moet in XML, bleek niet gemakkelijk. Na wat zoeken kwam ik uit op een 'simpele' json-library die XML gemakkelijk naar JSON om kon zetten. Omdat ik wel al wist hoe JSON parsen werkte, heb ik besloten om dat te doen. Veel uitgezocht en gekeken hoe het werkt.<br>
### Dag 7:<br>
Library werkt niet zoals ik gehoopt had. Bijft continu een error geven over iets waar in de XMLserializer(class vanuit de JSON-lib). Heel veel gegoogled, geprobeerd en gejammerd en nog niks verder.
###Dag 8<br>
Library bleek verkeerd toegevoegd te zijn. Dit opnieuw gedaan, kleine verbetering maar er blijft een error nadat de Search-button geklikt wordt. Ga nog 1 optie proberen met een extra library, als dat niet werkt ben ik er helemaal klaar mee en ga ik toch alles nog omgooien naar XML parsen.... Dat kan nog wel even duren voordat het lukt dus..<br>
###Dag 9<br>
Veel geprobeerd met het parsen van XML, ging niet erg lekker. Wel veel tutorials gekeken en meer duidelijkheid erover gekregen, begin staat erin.
UPDATE: 'savonds gelukt om XML te parsen!! Laat alleen nog niet zien in de ListView.
###Dag 10<br>
Presentatie en de geparste data proberen te laten zien in Listview. Lukte nog niet.
###Weekend<br>
Heel veel geprobeerd met de Adapter, kostte erg veel tijd en moeite, maar uiteindelijk gelukt om het te laten zien in de ListView. Er kan op geklikt worden en dan ga je naar het volgende scherm. Listview & Adapter voor het volgende scherm moet nog gefixt worden: dit gaat maandag gebeuren.

###Dag 11<br>
Dagje weg geweest, niet heel veel kunnen doen. Wel gefixt dat het tweede scherm naar Inchecken helemaal werkt en alles laat zien.
###Dag 12<br>
Database geïmplementeerd die de historie van inchecken bijhoudt. Verder bezig met het checken of Fbvrienden ingecheckt zijn in dezelfde trein, dit blijkt lastiger dan gedacht. Tot nu toe kan de app alleen zien welke FBvrienden de app ook gebruiken,maar gebeurt er verder nog niks mee; ook hier zitten al bugs in. Belangrijk dat dit uiterlijk donderdag werkend is.
###Dag 13<br>
Bugs met FBvrienden gefixt door middel van SharedPreferences; de FBvrienden zijn nu in elke activity op te halen. Link tussen ritnummer en facebookvrienden niet goed over nagedacht van te voren; is natuurlijk online storage voor nodig. Veel uitgezocht en gekeken wat handig is. Voor nu lijkt App42 Cloud API het handigst; deze kun je namelijk ook integreren met Facebook-accounts, dan is het zaak om enkel ritnummers daaraan toe te voegen en dit te vergelijken. Morgen hele dag hiermee aan de slag; als dit lukt is de MPV namelijk af.
###Dag 14<br>
App42 blijkt goed te werken. Na inloggen met Facebookaccount stored App42 dit ook in een Online Database, daarbij kunnen ook gelijk de FBvrienden die de app ook gebruiken opgehaald worden. Er kan ook data gestored worden, alleen nog niet het goede. Dit hoeft enkel het ritnummer te zijn in JSON-format. Dit moet vanavond nog werken, staat automatisch bij welk FB-id dit heeft toegevoegd; daarna is het zaak om die koppeling te maken en dan zouden we er in principe zijn!
###Dag 15<br>
Ritnummer wordt goed opgeslagen in de online database in JSON-format. Van het weekend druk bezig met het parsen van het ritnummer/fb-id en de fb-vrienden. Verder de FBmessenger geintegreerd, via de button kun je een afbeelding versturen waarin je aangeeft dat je in dezelfde trein zit, zo kun je gelijk verder praten via messenger en is de social integration ook geimplementeerd.
###Weekend<br>
Alle Facebook-bugs zijn weg, layout is verbeterd en code is gecleaned. Enige wat niet lukt is het juist parsen van de data van de online database App42.
###Dag 16, 17 en 18<br>
Flinke vorderingen gemaakt, hele app nu volledig werkend. Uiteraard ruimte voor verbetering, maar gezien de tijd besloten om het hierbij te laten qua functionaliteit en layout. De volgende dingen zijn werkend gekregen in de afgelopen dagen:<br>
-Parsen van de juiste data is gelukt<br>
-Het is mogelijk om te checken of je met je FBvrienden in de trein zit: dit werkt ook volledig.<br>
-Soms is het nodig om het scherm handmatig te refreshen, om dit zo makkelijk mogelijk te maken is er een aparte refreshknop aangemaakt.<br>
-Het is mogelijk om uit te checken in een trein, zodat de informatie up-to-date blijft.<br>
-Het is enkel mogelijk om in één trein tegelijk ingecheckt te zijn (een persoon kan altijd maar in één trein tegelijk zitten), hierdoor wordt er dus ook geen onjuiste informatie naar de FBvrienden verzonden.<br>
Nu is het tijd voor het verslag en het cleanen van de code!<br>
###Dag 19<br>
Toch nog een bug in de code na het cleanen, weer tijd kwijt om te fixen. Nog code gecleaned en verslag afgemaakt.
###Dag 20<br>
Presentaties & VAKANTIE
