 Aplikacja EcoYou
===============================

Nasza aplikacja to nowoczesna pomoc w codziennym ekologicznym życiu. Dzięki zadaniom, osiągnięciom i innymi funkcjonalnościami chcemy spowodować by ludzie nabrali dobrych nawyków. Miało to być osiągnięte poprzez stworzenie codziennego organizera połączonego z systemem progressu oraz opieki.

### Link do API naszego servera
[ServerAPI](https://github.com/OlafEs/konkurs-api)

## Strona główna

Tak wyglądał przygotowany przez nas wstępny design naszej strony głównej, staraliśmy sie zaimplementować to co udało się wykonać w tym czasie, lecz nie mieliśmy jeszcze dokładnego pomysłu jakie elementy powinny się jeszcze wyświetlać. Nasz koncept jest związany z poziomem jaki ma użytkownik, który rośnie wraz z wykonywaniem zadań. Okrąg dookoła tej liczby pokazywałby właśnie stan procentowy jaki jest potrzebny do uzyskania następnego poziomu. Poza tym elementy po bokach to pozytywne i negatywne punkty, które się również otrzymuje dzięki zadaniom i osiągnięciom. Niżej chcielśmy wyświetlać najnowsze ciekawe artykuły naukowe oraz powiadomienia o codziennych zadaniach o wykonanych czynnościach itp.

![Main](https://i.imgur.com/NrZ1pYk.png, "Main")

## Zadania i osiągnięcia

Te dwie karty pokazują listę wyżej wymienionych rzeczy. Każdy z elementów ma mieć możliwość bycia klikniętym by otrzymać więcej szczegółów. Do karty z zadaniami będziemy uzupełniali listę zadań, zadaniami przygotowanymi przez nas z servera, ale chcemy dać również możliwość by ludzie dodawali własne pomysły i starali się żyć lepiej. 

![TODO](https://i.imgur.com/mVpzzGJ.png, "TODO")

## Statystyki

Ta sekcja ma zawierać kilka różnych funkcjonalności. Aktualnie udało nam się zaimplementować system map, pobierania danych o czystości powietrza w najbliższej lokalizacji oraz chcemy dodać również ogólne statystyki, które pozwolą zobrazować efekty naszych czynności np. co się dzieje jeśli zaprzestaniemy korzystania z jednorazowych torebek. W tej funkcji moglibyśmy również sprawdzać nasz ślad węglowy i na tej podstawie pokazywać jak statystycznie wyglądają te dane na tle innych ludzi lub narodów.

## Małe Środowisko

Jedna z ważniejszych, ale i najbardziej czasochłonnych funkcjonalności. Chcieliśmy przygotować system który właśnie byłby głównym elementem przyciągającym do codziennego korzystania. Za wykonywanie zadań i zdobywanie osiągnięć otrzymywalibyśmy punkty, za które mielibyśmy możliwość zakupu nowych rzeczy do naszego małego środowiska. Wszystkie dobre czynności powodowałyby powolny rozwój. Zaczynalibyśmy z pustą i lekko zanieczyszczoną polanką lub oceanem, a wraz z czasem wszystko stawałoby się coraz bardziej zielone i pełne życia. Natomiast każda zła czynność przyczyniałaby się do pogorszania sytuacji. W ten sposób chcemy pokazać jaki wpływ mają nasze czynności na nasze środowisko. Poniżej jedna z grafik ukazująca styl w jakim chcelibyśmy przygotować to wszystko.
![Drzewko](https://i.imgur.com/PgwZhb8.png, "Drzewko")

### Nasze zaangażowanie

Wybraliśmy język Kotlin, aby przygotować aplikację na Androida by trafić do ludzi mniej więcej w podobnym wieku do naszego, dla których technologia jest codziennością i czują, że mogą coś zrobić by polepszyć aktualną sytuację
