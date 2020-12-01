package query;

import action.Actions;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Queries {
    /**
     * functia creeaza o lista cu un numar de actori ordonati dupa rating-urile obtinute
     *
     * @param actorInputDataList  lista care contine actorii care trebuie sa fie odronati
     * @param movieInputDataList  lista cu potentialele filme in care au jucat actorii
     * @param serialInputDataList lista cu potentialele seriale in care au jucat actoii
     * @param numberOfActors      numarul de actori care sa fie pusi in lista finala
     * @param sortType            tipul de sortare al actorilor in lista
     * @return intoarece lista de actori ordonati
     */
    public final String actor(final List<ActorInputData> actorInputDataList,
                              final List<MovieInputData> movieInputDataList,
                              final List<SerialInputData> serialInputDataList,
                              final Integer numberOfActors,
                              final String sortType) {
        Map<String, Double> actorAverage = new HashMap<>();
        Actions actions = new Actions();
        ArrayList<String> actors = new ArrayList<>();
        for (ActorInputData actorInputData : actorInputDataList) {
            if (actions.average(actorInputData, movieInputDataList,
                    serialInputDataList) != 0.0) {
                actorAverage.put(actorInputData.getName(),
                        actions.average(actorInputData, movieInputDataList,
                                serialInputDataList));
            }
        }
        for (ActorInputData actorInputData : actorInputDataList) {
            if (actorAverage.get(actorInputData.getName()) != null) {
                actors.add(actorInputData.getName());
            }
        }
        sortDouble(actors, actorAverage, sortType);
        return String.valueOf(buildString(actors, numberOfActors, true));
    }

    /**
     * functia pune numele actorilor intr-o lista daca acestia au castigat anumite
     * award-uri, ei fiind ordonati dupa numarul total de award-uri castigate
     *
     * @param actorInputDataList contine lista actorilor care trebuie sa fie ordonati
     * @param awardsFilter       contine award-urile pe care actorii trebuie sa le fi castigat
     * @param sortType           contine felul in care o sa fie ordonati actorii in lista
     * @return intoarce lista ordonata cu numele actorilor
     */
    public final String actor(final List<ActorInputData> actorInputDataList,
                              final List<String> awardsFilter,
                              final String sortType) {
        Map<String, Integer> actorsFiltered = new HashMap<>();
        int k;
        int awardFound = 0;
        int totalAwards;
        for (ActorInputData actorInputData : actorInputDataList) {
            totalAwards = 0;
            k = 0;
            while (k < awardsFilter.size()) {
                awardFound = 0;
                if (actorInputData.getAwards().get(Utils.stringToAwards(awardsFilter.get(k)))
                        != null) {
                    awardFound = 1;
                }
                if (awardFound == 0) {
                    break;
                }
                k++;
            }
            if (awardFound == 1) {
                for (Integer number : actorInputData.getAwards().values()) {
                    totalAwards = totalAwards + number;
                }
                actorsFiltered.put(actorInputData.getName(), totalAwards);
            }
        }

        ArrayList<String> actorsNames = new ArrayList<>(actorsFiltered.keySet());
        sortInteger(actorsNames, actorsFiltered, sortType);
        return String.valueOf(buildString(actorsNames, 0, false));
    }

    /**
     * functia creeaza o lista cu numele actorilor care contin anumite cuvinte in
     * decrierea carierei lor, lista fiind ordonata alfabetic
     *
     * @param actorInputDataList contine actorii care trebuie sa fie filtrati si ordonati
     * @param filter             contine cuvintele pe care actorii trebuie sa le aiba
     * @param sortType           contine metoda de sortare a actorilor in lista
     * @return intoarce lista ordonata de actori
     */
    public final String actorFilter(final List<ActorInputData> actorInputDataList,
                                    final List<String> filter,
                                    final String sortType) {
        int k;
        int wordFound = 0;
        ArrayList<String> actorsNames = new ArrayList<>();

        for (ActorInputData actorInputData : actorInputDataList) {
            k = 0;
            while (k < filter.size()) {
                wordFound = 0;
                String[] split = actorInputData.getCareerDescription()
                        .split("-|\\s|\\.|\t|\n|,|'|\\?");
                for (String s : split) {
                    if (s.equalsIgnoreCase(filter.get(k))) {
                        wordFound = 1;
                        break;
                    }
                }
                if (wordFound == 0) {
                    break;
                }
                k++;
            }
            if (wordFound == 1) {
                actorsNames.add(actorInputData.getName());
            }
        }
        sortAlph(actorsNames, sortType);
        return String.valueOf(buildString(actorsNames, 0, false));
    }

    /**
     * functia ordoneaza alfabetic o lista de string-uri primita ca parametru
     *
     * @param arrayNames lista de string-uri primita
     * @param sortType   modul in care o sa fie sortata lista
     */
    public final void sortAlph(final ArrayList<String> arrayNames,
                               final String sortType) {
        String aux;
        if (sortType.equals("asc")) {
            for (int i = 0; i < arrayNames.size(); i++) {
                for (int j = i + 1; j < arrayNames.size(); j++) {
                    if (arrayNames.get(i).compareTo(arrayNames.get(j)) > 0) {
                        aux = arrayNames.get(i);
                        arrayNames.set(i, arrayNames.get(j));
                        arrayNames.set(j, aux);
                    }
                }
            }
        } else if (sortType.equals("desc")) {
            for (int i = 0; i < arrayNames.size(); i++) {
                for (int j = i + 1; j < arrayNames.size(); j++) {
                    if (arrayNames.get(i).compareTo(arrayNames.get(j)) < 0) {
                        aux = arrayNames.get(i);
                        arrayNames.set(i, arrayNames.get(j));
                        arrayNames.set(j, aux);
                    }
                }
            }
        }
    }

    /**
     * functia sorteaza crescator sau descrescator o lista de string-uri bazata pe
     * integer-ul careia ii corespunde fiecareia
     *
     * @param arrayNames  lista de string-uri care trebuie sortata
     * @param arrayMapInt harta de integers care corespund string-urilor
     * @param sortType    metoda de sortare
     */
    public final void sortInteger(final ArrayList<String> arrayNames,
                                  final Map<String, Integer> arrayMapInt,
                                  final String sortType) {
        String aux;
        if (sortType.equals("asc")) {
            for (int i = 0; i < arrayNames.size(); i++) {
                for (int j = i + 1; j
                        < arrayNames.size(); j++) {
                    if (arrayMapInt.get(arrayNames.get(i))
                            > arrayMapInt.get(arrayNames.get(j))) {
                        aux = arrayNames.get(i);
                        arrayNames.set(i, arrayNames.get(j));
                        arrayNames.set(j, aux);
                    }
                    if (arrayMapInt.get(arrayNames.get(i))
                            .equals(arrayMapInt.get(arrayNames.get(j)))) {
                        if (arrayNames.get(i).compareTo(arrayNames.get(j)) > 0) {
                            aux = arrayNames.get(i);
                            arrayNames.set(i, arrayNames.get(j));
                            arrayNames.set(j, aux);
                        }
                    }
                }
            }
        }
        if (sortType.equals("desc")) {
            for (int i = 0; i < arrayNames.size(); i++) {
                for (int j = i + 1; j < arrayNames.size(); j++) {
                    if (arrayMapInt.get(arrayNames.get(i))
                            < arrayMapInt.get(arrayNames.get(j))) {
                        aux = arrayNames.get(i);
                        arrayNames.set(i, arrayNames.get(j));
                        arrayNames.set(j, aux);
                    }
                    if (arrayMapInt.get(arrayNames.get(i))
                            .equals(arrayMapInt.get(arrayNames.get(j)))) {
                        if (arrayNames.get(i).compareTo(arrayNames.get(j)) < 0) {
                            aux = arrayNames.get(i);
                            arrayNames.set(i, arrayNames.get(j));
                            arrayNames.set(j, aux);
                        }
                    }
                }
            }
        }
    }

    /**
     * functie creata impreuna cu getstring pentru a reduce duplicitatea
     *
     * @param arrayNames     lista de nume care trebuie sortate
     * @param arrayMapDouble harta de Doubles care corespund numelor
     * @param sortType       medota de sortare
     */
    public final void sortDouble(final ArrayList<String> arrayNames,
                                 final Map<String, Double> arrayMapDouble,
                                 final String sortType) {
        getStrings(arrayNames, arrayMapDouble, sortType);
    }

    /**
     * functia primeste o lista de nume care trebuie sortate in functie de
     * double-lului care le corespunde, in caz ca numerele sunt egale,
     * ele sunt ordonate alfabetic
     *
     * @param arrayNames     lista de nume care trebuie sortata
     * @param arrayMapDouble harta de Doubles care corespund fiecarui nume
     * @param sortType       metoda de sortare
     */
    private void getStrings(final ArrayList<String> arrayNames,
                            final Map<String, Double> arrayMapDouble,
                            final String sortType) {
        String aux;
        if (sortType.equals("asc")) {
            for (int i = 0; i < arrayNames.size(); i++) {
                for (int j = i + 1; j < arrayNames.size(); j++) {
                    if (arrayMapDouble.get(arrayNames.get(i))
                            > arrayMapDouble.get(arrayNames.get(j))) {
                        aux = arrayNames.get(i);
                        arrayNames.set(i, arrayNames.get(j));
                        arrayNames.set(j, aux);
                    }
                    if (arrayMapDouble.get(arrayNames.get(i))
                            .equals(arrayMapDouble.get(arrayNames.get(j)))) {
                        if (arrayNames.get(i).compareTo(arrayNames.get(j)) > 0) {
                            aux = arrayNames.get(i);
                            arrayNames.set(i, arrayNames.get(j));
                            arrayNames.set(j, aux);
                        }
                    }
                }
            }
        }
        if (sortType.equals("desc")) {
            for (int i = 0; i < arrayNames.size(); i++) {
                for (int j = i + 1; j < arrayNames.size(); j++) {
                    if (arrayMapDouble.get(arrayNames.get(i))
                            < arrayMapDouble.get(arrayNames.get(j))) {
                        aux = arrayNames.get(i);
                        arrayNames.set(i, arrayNames.get(j));
                        arrayNames.set(j, aux);
                    }
                    if (arrayMapDouble.get(arrayNames.get(i))
                            .equals(arrayMapDouble.get(arrayNames.get(j)))) {
                        if (arrayNames.get(i).compareTo(arrayNames.get(j)) < 0) {
                            aux = arrayNames.get(i);
                            arrayNames.set(i, arrayNames.get(j));
                            arrayNames.set(j, aux);
                        }
                    }
                }
            }
        }
    }

    /**
     * metoda care primeste o lista de string-uri si construieste un string
     * care sa contina un numar prestabilit, daca este necesar, de elemente din
     * lista originala
     *
     * @param arrayNames contine lista de string-uri pe baza careia sa se construiasca
     *                   string-ul
     * @param number     contine numarl prestabilit de elemente care sa fie puse in string
     * @param needNumber contine necesitatea parametrului number
     * @return intoarce noul string construit
     */
    public final StringBuilder buildString(final ArrayList<String> arrayNames,
                                           final Integer number,
                                           final Boolean needNumber) {
        StringBuilder stringBuilder = new StringBuilder("Query result: [");
        if (arrayNames.size() < number || !needNumber) {
            for (int i = 0; i < arrayNames.size(); i++) {
                stringBuilder.append(arrayNames.get(i));
                if (i + 1 != arrayNames.size()) {
                    stringBuilder.append(", ");
                }
            }
        } else {
            for (int i = 0; i < number; i++) {
                stringBuilder.append(arrayNames.get(i));
                if (i + 1 != number) {
                    stringBuilder.append(", ");
                }
            }
        }
        stringBuilder.append("]");
        return stringBuilder;
    }

    /**
     * functie care creeaza un string continand numele filmelor care
     * indeplinesc conditiile din filter si care sunt sortate dupa rating-ul acestora
     *
     * @param movieInputDataList contine lista de filme care trebuie sa fie sortate si filtrate
     * @param filter             contine conditiile pe care fiecare film trebuie sa le indeplineasca
     * @param numberOfMovies     contine numarul de filme care sa fie puse in string-ul nou format
     * @param sortType           contine felul in care o sa fie sortate filmele
     * @return intoarce string-ul nou creat
     */
    public final String movieRatings(final List<MovieInputData> movieInputDataList,
                                     final List<List<String>> filter,
                                     final Integer numberOfMovies,
                                     final String sortType) {
        Map<String, Double> movieRatings = new HashMap<>();
        Actions actions = new Actions();
        ArrayList<String> moviesNames = new ArrayList<>();
        ArrayList<MovieInputData> moviesFiltered;

        moviesFiltered = filterMovies(movieInputDataList, filter);
        for (MovieInputData movieInputData : moviesFiltered) {
            if (actions.average(movieInputData) != 0) {
                movieRatings
                        .put(movieInputData.getTitle(), actions.average(movieInputData));
                moviesNames.add(movieInputData.getTitle());
            }
        }
        sortMoviesDouble(movieRatings, moviesNames, sortType);
        return String.valueOf(buildString(moviesNames, numberOfMovies, true));
    }

    /**
     * functie care creeaza un string continand numele filmelor ordonate dupa
     * numarul de utilizatori care le contin la favorite si care indeplinesc conditiile de filtrare
     *
     * @param userInputDataList  contine lista de utilizatori care contin liste de favorite
     * @param movieInputDataList contine lista de filme care trebuie ordonata
     * @param filter             contine conditiile pe care toate filmele din noul
     *                           string trebuie sa le indeplineasca
     * @param sortType           contine felul in care o sa fie sortate fileme
     * @param numberOfMovies     contine numarul de filme care o sa apara in noul string
     * @return returneaza noul string creat
     */
    public final String moviesFavourite(final List<UserInputData> userInputDataList,
                                        final List<MovieInputData> movieInputDataList,
                                        final List<List<String>> filter, final String sortType,
                                        final Integer numberOfMovies) {
        Map<String, Integer> moviesFavourites = new HashMap<>();
        int numberOfFavourites = 0;
        ArrayList<String> moviesNames = new ArrayList<>();
        ArrayList<MovieInputData> moviesFiltered;

        moviesFiltered = filterMovies(movieInputDataList, filter);
        for (MovieInputData movieInputData : moviesFiltered) {
            for (UserInputData userInputData : userInputDataList) {
                for (int k = 0; k < userInputData.getFavoriteMovies().size(); k++) {
                    if (userInputData.getFavoriteMovies().get(k)
                            .equals(movieInputData.getTitle())) {
                        numberOfFavourites++;
                    }
                }
            }
            if (numberOfFavourites > 0) {
                moviesFavourites.put(movieInputData.getTitle(), numberOfFavourites);
                moviesNames.add(movieInputData.getTitle());
            }
            numberOfFavourites = 0;
        }
        sortMoviesInteger(moviesFavourites, moviesNames, sortType);
        return String.valueOf(buildString(moviesNames, numberOfMovies, true));
    }

    /**
     * functie care creeaza un string de nume de filme, care sunt filtrate dupa anumite
     * conditii, sortate dupa lungimea acestora
     *
     * @param movieInputDataList contine o lista de filme care trebuie sa fie sortate si filtrate
     * @param filter             contine conditiile pe care fiecare film trebuie sa le indeplineasca
     * @param sortType           contine felul in care o sa fie sortate filmele
     * @param numberOfMovies     contine numarul de filme care o sa apara in noul string creat
     * @return intoarce noul string creat
     */
    public final String moviesLongest(final List<MovieInputData> movieInputDataList,
                                      final List<List<String>> filter,
                                      final String sortType, final Integer numberOfMovies) {
        Map<String, Integer> moviesLength = new HashMap<>();
        ArrayList<String> moviesNames = new ArrayList<>();
        ArrayList<MovieInputData> moviesFiltered;

        moviesFiltered = filterMovies(movieInputDataList, filter);
        for (MovieInputData movieInputData : moviesFiltered) {
            moviesLength.put(movieInputData.getTitle(), movieInputData.getDuration());
            moviesNames.add(movieInputData.getTitle());
        }
        sortMoviesInteger(moviesLength, moviesNames, sortType);
        return String.valueOf(buildString(moviesNames, numberOfMovies, true));
    }

    /**
     * functie care creeaza un nou string de nume de filme, care sunt filtrate dupa anumite
     * conditii, dupa numarul de vizualizari pe care le au
     *
     * @param userInputDataList  contine lista de utilizatori cu ajutorul carora o sa calculam
     *                           numarul de vizualizari ale filmelor
     * @param movieInputDataList contine lista de filme care trebuie sa fie sortata si filtrata
     * @param filter             contine conditiile pe care toate filmele
     *                           trebuie sa le indeplineasca
     * @param sortType           contine felul in care o sa fie sortata lista de filme
     * @param numberOfMovies     contine numarul de filme care o sa apara in noul string creat
     * @return intoarce noul string creat
     */
    public final String moviesMostViewed(final List<UserInputData> userInputDataList,
                                         final List<MovieInputData> movieInputDataList,
                                         final List<List<String>> filter, final String sortType,
                                         final Integer numberOfMovies) {
        Map<String, Integer> moviesViews = new HashMap<>();
        int numberOfViews;
        ArrayList<String> moviesNames = new ArrayList<>();
        ArrayList<MovieInputData> moviesFiltered;

        moviesFiltered = filterMovies(movieInputDataList, filter);
        for (MovieInputData movieInputData : moviesFiltered) {
            numberOfViews = 0;
            for (UserInputData userInputData : userInputDataList) {
                if (userInputData.getHistory().get(movieInputData.getTitle()) != null) {
                    numberOfViews = numberOfViews
                            + userInputData.getHistory().get(movieInputData.getTitle());
                }
            }
            if (numberOfViews != 0) {
                moviesViews.put(movieInputData.getTitle(), numberOfViews);
                moviesNames.add(movieInputData.getTitle());
            }
        }
        sortInteger(moviesNames, moviesViews, sortType);
        return String.valueOf(buildString(moviesNames, numberOfMovies, true));
    }

    /**
     * functie care creeaza un string care contine numele serialelor, filtrare dupa anumite
     * conditii, sortate dupa rating-urile obinute
     *
     * @param serialInputDataList contine lista de seriale care trebuie filtrate
     *                            si sortate
     * @param filter              contine conditiile pe care toate serialele trebuie
     *                            sa le indeplineasca
     * @param sortType            contine felul in care o sa fie sortate serialele
     * @param numberOfShows       contine numarul de seriale care o sa apara in
     *                            noul string format
     * @return intoarce noul string format
     */
    public final String showsRatings(final List<SerialInputData> serialInputDataList,
                                     final List<List<String>> filter,
                                     final String sortType, final Integer numberOfShows) {
        Map<String, Double> showsRatings = new HashMap<>();
        Actions actions = new Actions();
        ArrayList<String> showsNames = new ArrayList<>();
        ArrayList<SerialInputData> showsFiltered;

        showsFiltered = filterSerials(serialInputDataList, filter);
        for (SerialInputData serialInputData : showsFiltered) {
            if (actions.average(serialInputData) != 0) {
                showsRatings.put(serialInputData.getTitle(), actions.average(serialInputData));
                showsNames.add(serialInputData.getTitle());

            }
        }
        sortDouble(showsNames, showsRatings, sortType);
        return String.valueOf((buildString(showsNames, numberOfShows, true)));
    }

    /**
     * functie care creeaza un string continand numele serialelor, filtrate dupa
     * anumite conditii, sortate dupa numarul de utilizatori care le contine
     * la favorite
     *
     * @param userInputDataList   lista de utilizatori care contin listele de titluri
     *                            pe care le au la favorite
     * @param serialInputDataList lista de seriale care trebuie sortate si filtrate
     * @param filter              lista de conditii pe care toate serialele
     *                            trebuie sa le indeplineasca
     * @param sortType            felul in care o sa fie sortate serialele
     * @param numberOfShows       numarul de seriale care o sa apara in noul string format
     * @return intoarce noul string format
     */
    public final String showsFavourite(final List<UserInputData> userInputDataList,
                                       final List<SerialInputData> serialInputDataList,
                                       final List<List<String>> filter, final String sortType,
                                       final Integer numberOfShows) {
        Map<String, Integer> showsFavourites = new HashMap<>();
        int numberOfFavourites = 0;
        ArrayList<String> showsNames = new ArrayList<>();
        ArrayList<SerialInputData> showsFiltered;

        showsFiltered = filterSerials(serialInputDataList, filter);
        for (SerialInputData serialInputData : showsFiltered) {
            for (UserInputData userInputData : userInputDataList) {
                for (int k = 0; k < userInputData.getFavoriteMovies().size(); k++) {
                    if (userInputData.getFavoriteMovies().get(k)
                            .equals(serialInputData.getTitle())) {
                        numberOfFavourites++;
                    }
                }
            }
            if (numberOfFavourites > 0) {
                showsFavourites.put(serialInputData.getTitle(), numberOfFavourites);
                showsNames.add(serialInputData.getTitle());
            }
            numberOfFavourites = 0;
        }
        sortMoviesInteger(showsFavourites, showsNames, sortType);
        return String.valueOf(buildString(showsNames, numberOfShows, true));
    }

    /**
     * functie care creeaza un string de titluri de seriale, filtrate dupa anumite
     * conditii, sortate dupa durata acestora
     *
     * @param serialInputDataList lista de seriale care trebuie sa fie filtrate
     *                            si sortate
     * @param filter              lista de conditii pe care toate serialele
     *                            trebuie sa le indeplineasca
     * @param sortType            felul in care serialele o sa fie sortate
     * @param numberOfShows       numarul de seriale care o sa apara in noul string format
     * @return intoarce noul string format
     */
    public final String showsLongest(final List<SerialInputData> serialInputDataList,
                                     final List<List<String>> filter,
                                     final String sortType, final Integer numberOfShows) {
        Map<String, Integer> showsLength = new HashMap<>();
        int length;
        ArrayList<String> showsNames = new ArrayList<>();
        ArrayList<SerialInputData> showsFiltered;

        showsFiltered = filterSerials(serialInputDataList, filter);
        for (SerialInputData serialInputData : showsFiltered) {
            length = 0;
            for (int i = 0; i < serialInputData.getNumberSeason(); i++) {
                length = length + serialInputData.getSeasons().get(i).getDuration();
            }
            showsLength.put(serialInputData.getTitle(), length);
            showsNames.add(serialInputData.getTitle());
        }
        sortInteger(showsNames, showsLength, sortType);
        return String.valueOf(buildString(showsNames, numberOfShows, true));
    }

    /**
     * functie care creeaza un string de titluri de seriale, filtrate dupa anumite
     * conditii, sortate dupa numarul de vizualizari ale acestora
     *
     * @param userInputDataList   lista de utilizatori cu ajutorul carora calculam numarl de
     *                            vizualizari ale serialelor
     * @param serialInputDataList lista de seriale care trebuie sa fie filtrate si sortate
     * @param filter              lista de conditii pe care toate serialele
     *                            trebuie sa le indeplineasca
     * @param sortType            felul in care serialele trebuie sa fie sortate
     * @param numberOfShows       numarul de seriale care o sa apara in noul string format
     * @return intoarce noul string format
     */
    public final String showsMostViewed(final List<UserInputData> userInputDataList,
                                        final List<SerialInputData> serialInputDataList,
                                        final List<List<String>> filter, final String sortType,
                                        final Integer numberOfShows) {
        Map<String, Integer> showsViews = new HashMap<>();
        int views;
        ArrayList<String> showsNames = new ArrayList<>();
        ArrayList<SerialInputData> showsFiltered;

        showsFiltered = filterSerials(serialInputDataList, filter);
        for (SerialInputData serialInputData : showsFiltered) {
            views = 0;
            for (UserInputData userInputData : userInputDataList) {
                if (userInputData.getHistory().get(serialInputData.getTitle()) != null) {
                    views = views + userInputData.getHistory().get(serialInputData.getTitle());
                }
            }
            if (views != 0) {
                showsViews.put(serialInputData.getTitle(), views);
                showsNames.add(serialInputData.getTitle());
            }
        }
        sortInteger(showsNames, showsViews, sortType);
        return String.valueOf(buildString(showsNames, numberOfShows, true));
    }

    /**
     * functie care creeaza un string de nume de utilizatori care au dat cele mai multe
     * rating-uri
     *
     * @param userInputDataList lista de utilizatori care trebuie sa fie sortati
     * @param sortType          felul in care sortam utilizatorii
     * @param numberOfUsers     numarul de utilizatori care o sa apara in noul string
     * @return intoarce noul string format
     */
    public final String usersMostActive(final List<UserInputData> userInputDataList,
                                        final String sortType,
                                        final Integer numberOfUsers) {
        Map<String, Integer> userActivity = new HashMap<>();
        ArrayList<String> usersNames = new ArrayList<>();
        int numberOfRatings;

        for (UserInputData userInputData : userInputDataList) {
            numberOfRatings = 0;
            numberOfRatings = numberOfRatings + userInputData.getTitlesRated().size();
            numberOfRatings = numberOfRatings + userInputData.getSerialsRated().size();
            if (numberOfRatings != 0) {
                userActivity.put(userInputData.getUsername(), numberOfRatings);
                usersNames.add(userInputData.getUsername());
            }
        }
        sortInteger(usersNames, userActivity, sortType);
        return String.valueOf(buildString(usersNames, numberOfUsers, true));
    }

    /**
     * functie care creeaza o lista de filme filtrate dintr-o lista mai mare de filme
     *
     * @param movieInputDataList lista de filme care trebuie sa fie filtrata
     * @param filter             lista care contine conditiile pe care toate filmele trebuie sa
     *                           le indeplineasca
     * @return intoarce lista de filme nou creata
     */
    public final ArrayList<MovieInputData> filterMovies(
            final List<MovieInputData> movieInputDataList,
            final List<List<String>> filter) {
        int year;
        ArrayList<MovieInputData> moviesFiltered = new ArrayList<>();

        for (MovieInputData movieInputData : movieInputDataList) {
            if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                year = Integer.parseInt(filter.get(0).get(0));
                if (movieInputData.getYear() == year) {
                    for (int j = 0; j < movieInputData.getGenres().size(); j++) {
                        if (movieInputData.getGenres().get(j).equals(filter.get(1).get(0))) {
                            moviesFiltered.add(movieInputData);
                        }
                    }
                }
            } else if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                year = Integer.parseInt(filter.get(0).get(0));
                if (movieInputData.getYear() == year) {
                    moviesFiltered.add(movieInputData);
                }
            } else if (filter.get(0).get(0) == null && filter.get(1).get(0) != null) {
                for (int j = 0; j < movieInputData.getGenres().size(); j++) {
                    if (movieInputData.getGenres().get(j).equals(filter.get(1).get(0))) {
                        moviesFiltered.add(movieInputData);
                    }
                }
            } else if (filter.get(0).get(0) == null && filter.get(1).get(0) == null) {
                moviesFiltered.add(movieInputData);
            }
        }
        return moviesFiltered;
    }

    /**
     * functie care creeaza o lista de seriale filtrate dintr-o lista mai mare de seriale
     *
     * @param serialInputDataList lista de seriale care trebuie sa fie filtrate
     * @param filter              lista de condtii pe care toate serialele
     *                            trebuie sa le indeplineasca
     * @return intoarce noua lista de seriale creata
     */
    public final ArrayList<SerialInputData> filterSerials(
            final List<SerialInputData> serialInputDataList,
            final List<List<String>> filter) {
        int year;
        ArrayList<SerialInputData> showsFiltered = new ArrayList<>();

        for (SerialInputData serialInputData : serialInputDataList) {
            if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                year = Integer.parseInt(filter.get(0).get(0));
                if (serialInputData.getYear() == year) {
                    for (int j = 0; j < serialInputData.getGenres().size(); j++) {
                        if (serialInputData.getGenres().get(j).equals(filter.get(1).get(0))) {
                            showsFiltered.add(serialInputData);
                        }
                    }
                }
            } else if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                year = Integer.parseInt(filter.get(0).get(0));
                if (serialInputData.getYear() == year) {
                    showsFiltered.add(serialInputData);
                }
            } else if (filter.get(0).get(0) == null && filter.get(1).get(0) != null) {
                for (int j = 0; j < serialInputData.getGenres().size(); j++) {
                    if (serialInputData.getGenres().get(j).equals(filter.get(1).get(0))) {
                        showsFiltered.add(serialInputData);
                    }
                }
            } else if (filter.get(0).get(0) == null && filter.get(1).get(0) == null) {
                showsFiltered.add(serialInputData);
            }
        }
        return showsFiltered;
    }

    /**
     * functie care primeste o lista de nume de filme si le ordoneaza
     * dupa rating-urile acestora
     *
     * @param movieMap    harta care contine rating-urile filmelor
     * @param moviesNames lista care contine numele filmelor
     * @param sortType    felul in care sa fie sortate filmele
     */
    public final void sortMoviesDouble(final Map<String, Double> movieMap,
                                       final ArrayList<String> moviesNames,
                                       final String sortType) {
        getStrings(moviesNames, movieMap, sortType);
    }

    /**
     * functie care creeaza o lista de nume de filme care sa fie sortate
     * dupa numerele care le sunt corespunzatoare acestora
     *
     * @param movieMap    harta care contine numerele corespunzatoare filmelor
     * @param moviesNames lista de nume de filme care sa fie sortate
     * @param sortType    felul in care sa fie sortate filmele
     * @return intoarce lista de filme sortata
     */
    public final ArrayList<String> sortMoviesInteger(final Map<String, Integer> movieMap,
                                                     final ArrayList<String> moviesNames,
                                                     final String sortType) {
        String aux;
        if (sortType.equals("asc")) {
            for (int i = 0; i < moviesNames.size(); i++) {
                for (int j = i + 1; j < moviesNames.size(); j++) {
                    if (movieMap.get(moviesNames.get(i))
                            > movieMap.get(moviesNames.get(j))) {
                        aux = moviesNames.get(i);
                        moviesNames.set(i, moviesNames.get(j));
                        moviesNames.set(j, aux);
                    }
                    if (movieMap.get(moviesNames.get(i)).equals(movieMap.get(moviesNames.get(j)))) {
                        if (moviesNames.get(i).compareTo(moviesNames.get(j)) > 0) {
                            aux = moviesNames.get(i);
                            moviesNames.set(i, moviesNames.get(j));
                            moviesNames.set(j, aux);
                        }
                    }
                }
            }
        }
        if (sortType.equals("desc")) {
            for (int i = 0; i < moviesNames.size(); i++) {
                for (int j = i + 1; j < moviesNames.size(); j++) {
                    if (movieMap.get(moviesNames.get(i))
                            < movieMap.get(moviesNames.get(j))) {
                        aux = moviesNames.get(i);
                        moviesNames.set(i, moviesNames.get(j));
                        moviesNames.set(j, aux);
                    }
                    if (movieMap.get(moviesNames.get(i)).equals(movieMap.get(moviesNames.get(j)))) {
                        if (moviesNames.get(i).compareTo(moviesNames.get(j)) < 0) {
                            aux = moviesNames.get(i);
                            moviesNames.set(i, moviesNames.get(j));
                            moviesNames.set(j, aux);
                        }
                    }
                }
            }
        }
        return moviesNames;
    }
}
