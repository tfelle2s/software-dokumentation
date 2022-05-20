# ARC42 Dokumentation Tool

Dieses Projekt basiert auf dem Framework Vaadin und kann mittels des Webservers Jetty gestartet werden.



Um direkt von Github darauf zuzugreifen, klonen Sie das Repository und importieren Sie das Projekt in die IDE Ihrer Wahl als Maven-Projekt. Sie müssen Java 16 haben.

Für den ersten Start emphielt es zunächst ein `mvn clean:install` auszuführen

### Maven-Setup
Starten Sie mit `mvn jetty:run` und öffnen Sie [http://localhost:8080](http://localhost:8080) im Browser.

Wenn Sie Ihre Anwendung lokal im Produktionsmodus ausführen möchten, führen Sie `mvn jetty:run -Pproduction` aus.


### Installation der neo4j-Datenbank

Um neo4j zu nutzen, musst vor ab die Desktop-Version runtergeladen werden. [neo4j-Download](
https://neo4j.com/download-center/#desktop).
Nachdem neo4j installiert wurde, kann eine lokale DBMS hinzugefügt werden. Folgende Konfiguration sollte verwendet werde:
1. **Name** &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;     `neo4j`
2. **Passwort**    `stones-principal-roars`
3. **Version** &nbsp;&nbsp; `4.4.5`

Anschließend den Server starten. 


### Erstellen einer Arc42 Dokumentation
Zum Erstellen einer Dokumentation muss vorab einer Konto in der Datenbank erstellt werden. Dies kann nach aufrufen der Seite  [http://localhost:8080](http://localhost:8080) mit Klicken _"Noch kein Konto? Hier registieren!"_ erledigt werden. Danach sollte man es ohne Probleme möglich sein einzuloggen und Dokumentation zu erstellen.  
