# CQRS_project

Aplikacja składa się z 2 mikroserwisów (ETL + Serwis REST) oraz bazy postgres. Całość jest upakowana w kontenery dokerowe i spięta przez docker-compose.

JokesETL pobiera dane wystawione przez publiczne api REST (https://sv443.net/jokeapi/v2/), w paczkach po N elementów.
Po pobraniu 300 (props jokes.etl.rest.batchSize) elementów aplikacja jokes-etl-app jest zamykana. Poziom zrównoleglenia pracy ETL można skalować za pomocą aktorów
akkowych (props jokes.etl.rest.parallelism). To jakie dokładnie dane zostaną pobrane z api źródłowego można dostosować w samym kodzie mikroserwisu.

JokesSearchEngine wystawia REST api (http://localhost:8089/jokes ; Swagger: http://127.0.0.1:8089/api-docs/) które pozwala na pobieranie elementów z bazy postgres.
Całość stoi na akka-http, więc za pomocą propsów akkowych oraz wielkości connection poola można skalować przepustowość odczytu.

Tak więc odczyt i zapis są całkowicie od siebie niezależne po stronie aplikacji. Po rozrośnięciu się projektu, potencjalnym wąskim gardłem jest baza i tabela "joke",
do której dane są zapisywane i odczytywane.
Rozwiązać/zminimalizować można to na kilka sposobów:
- Dodać cache po stronie odczytu (np. https://doc.akka.io/docs/akka-http/current/common/caching.html)
- Zrobić lustrzaną tabelę np. joke_output, która będzie przechowywać dane identyczne jak bazowa joke, 
a przy tym będzie wykorzystywana wyłącznie do pobierania danych przez api. Dane mogą być w niej nieznormalizowane (żeby uniknąć np. joinów w czasie odpytywania), czy nawet (jeśli chcemy odciążyć aplikację) przechowywane jako gotowy do wysyłki json.
Struktura takiej tabeli powinna również zostać zoptymalizowana pod odczyt, np. poprzez odpowiednie indexy czy partycjonowanie.
- Rozwiązać to analogiczne jak w poprzednim punkcie, z tym że przeniesienie zapisu/odczytu odbyłoby się na inny typ bazy danych, a nie do innej tabeli.
- Jeśli chodzi o aplikację w obecnej formie, to w przypadku odczytu tak naprawdę bazę w ogóle można ominąć, a dane do /jokes pobierać bezpośrednio ze źródłowego api ;) api -> jakiś enrichment -> /jokes

Naturalnie każde rozwiązanie ma swoje plusy i minusy. Należałoby dobrać je odpowiednio do wymagań.

Logi są printowane na konsolę oraz do pliku logs/logfile.log. Po stronie ETL, główną "metryką" jest ilość pobranych lub nie danych, 
a po stronie serwisów restowych logowany jest każde odpytanie api. Wszystkie parametry konfiguracyjne znajdują się w resources/application.conf.

Do kilku klas zostały napisane przykładowe testy. JokeJsonReadSupport została przetestowana dość kompleksowo,
a JokeEtlProcessTest jest przykładem nieco bardziej złożonego przypadku testowego.

Odnośnie mankamentów, to na GUI swaggera, wykonanie zapytania o dane zwraca "TypeError: NetworkError when attempting to fetch resource.".
Przyczyną jest konflikt bibliotek który pojawia się przy pakowaniu jarki, przyznam szczerze że nie miałem już cierpliwości
żeby szukać tej jednej libki która powoduje problem ;). Jednocześnie nie ma problemu żeby do API uderzyć przez postmana.

Limity zewnętrznego api:
- max 10 elementów w batchu
- max 120 requestów na minutę, później następuje ban na ip


------
Obsługa projektu:
- mvn clean package -Dmaven.test.skip
- budowa docker-compose

Projekt kompilowany na OpenJDK jdk-8.0.282.08-hotspot

Zarówno API jak i Swagger dostępne są na porcie 8089. Baza Postgres ma wystawiony port 5432.
