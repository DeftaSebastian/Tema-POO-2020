package recommendation;

import action.Actions;
import entertainment.Genre;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import query.Queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommendations {
    /**
     * functie care imi cauta un user dupa username-ul acestuia si intoarce
     * primul video nevizualizat de acesta
     *
     * @param userInputDataList   lista userilor
     * @param userName            unsername-ul utilizatorului cautuat
     * @param movieInputDataList  lista de filme din care cautam un video nevizualizat
     * @param serialInputDataList lista de seriale din care cautam un serial nevizualizat
     * @return intoarce video-ul nevizualizat
     */
    public final String standard(final List<UserInputData> userInputDataList, final String userName,
                                 final List<MovieInputData> movieInputDataList,
                                 final List<SerialInputData> serialInputDataList) {
        int i = 0;
        int j = 0;
        for (UserInputData userInputData : userInputDataList) {
            if (userInputData.getUsername().equals(userName)) {
                while (true) {
                    if (i >= movieInputDataList.size() && j >= serialInputDataList.size()) {
                        return "StandardRecommendation cannot be applied!";
                    }
                    while (i < movieInputDataList.size()) {
                        if (userInputData.getHistory().get(movieInputDataList.get(i).getTitle())
                                == null) {
                            return "StandardRecommendation result: "
                                    + movieInputDataList.get(i).getTitle();
                        }
                        i++;
                    }
                    while (j < serialInputDataList.size()) {
                        if (userInputData.getHistory().get(serialInputDataList.get(j).getTitle())
                                == null) {
                            return "StandardRecommendation result: "
                                    + serialInputDataList.get(j).getTitle();
                        }
                        j++;
                    }
                }
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    /**
     * functie care calculeaza rating-ul pentru fiecare video
     * si intoarece primul video nevizualizat de user-ul cautat
     *
     * @param userInputDataList   lista de useri pe baza carora gasim user-ul cautat dupa username
     * @param userName            numele user-ului cautat
     * @param movieInputDataList  lista de filme pe care vrem sa o ordonam
     * @param serialInputDataList lista de seriale pe care vrem sa le ordonam
     * @return intoarce primul video nevizualizat din lista de video-uri sortate
     */
    public final String bestUnseen(final List<UserInputData> userInputDataList,
                                   final String userName,
                                   final List<MovieInputData> movieInputDataList,
                                   final List<SerialInputData> serialInputDataList) {
        Map<String, Double> moviesRatings = new HashMap<>();
        Map<String, Double> serialsRatings = new HashMap<>();
        Actions actions = new Actions();
        ArrayList<String> moviesNames = new ArrayList<>();
        ArrayList<String> serialsNames = new ArrayList<>();

        for (MovieInputData movieInputData : movieInputDataList) {
            moviesRatings.put(movieInputData.getTitle(), actions.average(movieInputData));
            moviesNames.add(movieInputData.getTitle());
        }
        for (SerialInputData serialInputData : serialInputDataList) {
            serialsRatings.put(serialInputData.getTitle(), actions.average(serialInputData));
            serialsNames.add(serialInputData.getTitle());
        }
        sortMoviesRatingDesc(moviesNames, moviesRatings, movieInputDataList);
        sortSerialsByRatingDesc(serialsNames, serialsRatings, serialInputDataList);
        int i = 0;
        int j = 0;
        for (UserInputData userInputData : userInputDataList) {
            if (userInputData.getUsername().equals(userName)) {
                while (true) {
                    if (i >= moviesNames.size() && j >= serialsNames.size()) {
                        return "BestRatedUnseenRecommendation cannot be applied!";
                    }
                    while (i < moviesNames.size()) {
                        if (userInputData.getHistory().get(moviesNames.get(i))
                                == null) {
                            return "BestRatedUnseenRecommendation result: "
                                    + moviesNames.get(i);
                        }
                        i++;
                    }
                    while (j < serialsNames.size()) {
                        if (userInputData.getHistory().get(serialsNames.get(j))
                                == null) {
                            return "BestRatedUnseenRecommendation result: "
                                    + serialsNames.get(j);
                        }
                        j++;
                    }
                }
            }
        }
        return "BestRatedUnseenRecommendation cannot be applied!";

    }

    /**
     * functie care sorteaza genurile dupa numarul de vizualizari ale acestora
     * si cauta primul video din acel gen nevizualizat de user
     *
     * @param userInputDataList   lista de useri pe baza carora calculam numarul de
     *                            vizualizari ale genurilor
     * @param userInputData       user-ul pentru care vrem sa gasim un video
     * @param movieInputDataList  lista de filme pe baza careia calculam popularitatea
     *                            genurilor
     * @param serialInputDataList lista de seriale pe baza careia calculam popularitatea
     *                            genurilor
     * @return intoarcem primul video nevizualizat de user din lista de genuri sortate
     */
    public final String popular(final List<UserInputData> userInputDataList,
                                final UserInputData userInputData,
                                final List<MovieInputData> movieInputDataList,
                                final List<SerialInputData> serialInputDataList) {
        Map<String, Integer> genresViews = new HashMap<>();
        ArrayList<String> genresList = new ArrayList<>();
        int numberOfViews;
        for (Genre genre : Genre.values()) {
            numberOfViews = 0;
            for (MovieInputData movieInputData : movieInputDataList) {
                for (int i = 0; i < movieInputData.getGenres().size(); i++) {
                    if (movieInputData.getGenres().get(i).equalsIgnoreCase(genre.toString())) {
                        numberOfViews =
                                numberOfViews + numberOfViews(userInputDataList, movieInputData);
                    }
                }
            }
            for (SerialInputData serialInputData : serialInputDataList) {
                for (int i = 0; i < serialInputData.getGenres().size(); i++) {
                    if (serialInputData.getGenres().get(i).equalsIgnoreCase(genre.toString())) {
                        numberOfViews =
                                numberOfViews + numberOfViews(userInputDataList, serialInputData);
                    }
                }
            }
            genresViews.put(genre.toString(), numberOfViews);
            genresList.add((genre.toString()));
        }
        sortPopular(genresList, genresViews);
        for (String s : genresList) {
            for (MovieInputData movieInputData : movieInputDataList) {
                for (int j = 0; j < movieInputData.getGenres().size(); j++) {
                    if (movieInputData.getGenres().get(j).equalsIgnoreCase(s)) {
                        if (userInputData.getHistory().get(movieInputData.getTitle()) == null) {
                            return "PopularRecommendation result: " + movieInputData.getTitle();
                        }
                    }
                }
            }
            for (SerialInputData serialInputData : serialInputDataList) {
                for (int j = 0; j < serialInputData.getGenres().size(); j++) {
                    if (serialInputData.getGenres().get(j).equalsIgnoreCase(s)) {
                        if (userInputData.getHistory().get(serialInputData.getTitle()) == null) {
                            return "PopularRecommendation result: " + serialInputData.getTitle();
                        }
                    }
                }
            }
        }
        return "PopularRecommendation cannot be applied!";
    }

    /**
     * functie care gaseste primul video nevizualizat de user dintr-o lista de titluri
     * sortate dupa aparitia acestora in listele de  favorite ale altor useri
     *
     * @param userInputDataList   listele de date ale altori useri
     * @param userInputData       user-ul pentru care cautam video
     * @param movieInputDataList  lista de filme pe care o sortam
     * @param serialInputDataList lista de seriale pe care o sortam
     * @return intoarcem primul video nevizualizat de user din lista sortata cu cele
     * mai favorite video-uri
     */
    public final String favorite(final List<UserInputData> userInputDataList,
                                 final UserInputData userInputData,
                                 final List<MovieInputData> movieInputDataList,
                                 final List<SerialInputData> serialInputDataList) {
        Map<String, Integer> moviesFavorites = new HashMap<>();
        Map<String, Integer> serialsFavorites = new HashMap<>();
        ArrayList<String> moviesNames = new ArrayList<>();
        ArrayList<String> serialsNames = new ArrayList<>();
        ArrayList<String> mixedNames;
        int numberOfFavorites;
        for (MovieInputData movieInputData : movieInputDataList) {
            numberOfFavorites = 0;
            for (UserInputData user : userInputDataList) {
                for (int i = 0; i < user.getFavoriteMovies().size(); i++) {
                    if (user.getFavoriteMovies().get(i)
                            .equalsIgnoreCase(movieInputData.getTitle())) {
                        numberOfFavorites++;
                    }
                }
            }
            if (numberOfFavorites != 0) {
                moviesFavorites.put(movieInputData.getTitle(), numberOfFavorites);
                moviesNames.add(movieInputData.getTitle());
            }
        }
        for (SerialInputData serialInputData : serialInputDataList) {
            numberOfFavorites = 0;
            for (UserInputData user : userInputDataList) {
                for (int i = 0; i < user.getFavoriteMovies().size(); i++) {
                    if (user.getFavoriteMovies().get(i)
                            .equalsIgnoreCase(serialInputData.getTitle())) {
                        numberOfFavorites++;
                    }
                }
            }
            if (numberOfFavorites != 0) {
                serialsFavorites.put(serialInputData.getTitle(), numberOfFavorites);
                serialsNames.add(serialInputData.getTitle());
            }
        }
        sortMoviesFavorites(moviesNames, moviesFavorites, movieInputDataList);
        sortSerialsFavorites(serialsNames, serialsFavorites, serialInputDataList);
        mixedNames = mix(moviesNames, moviesFavorites, serialsNames, serialsFavorites);
        for (String s : mixedNames) {
            if (userInputData.getHistory().get(s) == null) {
                return "FavoriteRecommendation result: " + s;
            }
        }
        return "FavoriteRecommendation cannot be applied!";
    }

    /**
     * functie care cauta toate video-urile nevazute, sortate in ordine crescatoare
     * dupa rating si care indeplinesc conditiile unui filtru
     *
     * @param userInputData       user-ul caruia vrem sa ii cautam video-uri
     * @param movieInputDataList  lista de filme pe care o sortam si filtram
     * @param serialInputDataList lista de seriale pe care o sortam si filtram
     * @param genre               conditia impusa ca filtru
     * @return intoarce titlurile din lista ordonata de video-uri
     */
    public final String search(final UserInputData userInputData,
                               final List<MovieInputData> movieInputDataList,
                               final List<SerialInputData> serialInputDataList,
                               final String genre) {
        Queries queries = new Queries();
        Actions actions = new Actions();
        Map<String, Double> showsRatings = new HashMap<>();
        ArrayList<String> showsNames = new ArrayList<>();
        for (MovieInputData movieInputData : movieInputDataList) {
            if (userInputData.getHistory().get(movieInputData.getTitle()) == null) {
                for (int i = 0; i < movieInputData.getGenres().size(); i++) {
                    if (movieInputData.getGenres().get(i).equalsIgnoreCase(genre)) {
                        showsRatings
                                .put(movieInputData.getTitle(), actions.average(movieInputData));
                        showsNames.add(movieInputData.getTitle());
                    }
                }
            }
        }
        for (SerialInputData serialInputData : serialInputDataList) {
            if (userInputData.getHistory().get(serialInputData.getTitle()) == null) {
                for (int i = 0; i < serialInputData.getGenres().size(); i++) {
                    if (serialInputData.getGenres().get(i).equalsIgnoreCase(genre)) {
                        showsRatings
                                .put(serialInputData.getTitle(), actions.average(serialInputData));
                        showsNames.add(serialInputData.getTitle());
                    }
                }
            }
        }
        queries.sortDouble(showsNames, showsRatings, "asc");
        if (showsNames.size() == 0) {
            return "SearchRecommendation cannot be applied!";
        }
        return "SearchRecommendation result: "
                + buildString(showsNames, 0, false);
    }

    /**
     * functie care sorteaza crescator o lista de titluri pe baza vizualizarilor acestora
     *
     * @param arrayNames lista de titluri care trebuie sa fie sortata
     * @param arrayViews harta cu vizulizari care corespund titlurilor
     */
    public final void sortPopular(final ArrayList<String> arrayNames,
                                  final Map<String, Integer> arrayViews) {
        String aux;

        for (int i = 0; i < arrayNames.size(); i++) {
            for (int j = i + 1; j < arrayNames.size(); j++) {
                if (arrayViews.get(arrayNames.get(i))
                        < arrayViews.get(arrayNames.get(j))) {
                    aux = arrayNames.get(i);
                    arrayNames.set(i, arrayNames.get(j));
                    arrayNames.set(j, aux);
                }
            }
        }
    }

    /**
     * functie care sorteaza o lista de filme dupa aparitia acestora in
     * listele de favorite ale userilor, al doilea criteriu de sortare
     * fiind aparitia acestora in baza de date
     *
     * @param arrayNames     titlurile filmelor care trebuie sa fie sortate
     * @param arrayFavorites harta de favorite care corespund fiecarui film
     * @param movieDataList  lista de filme pe baza carora cautam in baza de date
     * @return intoarce lista sortata de titluri de filme
     */
    public final ArrayList<String> sortMoviesFavorites(final ArrayList<String> arrayNames,
                                                       final Map<String, Integer> arrayFavorites,
                                                       final List<MovieInputData> movieDataList) {
        String aux;
        for (int i = 0; i < arrayNames.size(); i++) {
            for (int j = i + 1; j < arrayNames.size(); j++) {
                if (arrayFavorites.get(arrayNames.get(i))
                        < arrayFavorites.get(arrayNames.get(j))) {
                    aux = arrayNames.get(i);
                    arrayNames.set(i, arrayNames.get(j));
                    arrayNames.set(j, aux);
                }
                if (arrayFavorites.get(arrayNames.get(i)).equals(
                        arrayFavorites.get((arrayNames.get(j))))) {
                    for (MovieInputData movieInputData : movieDataList) {
                        if (movieInputData.getTitle().equals(arrayNames.get(i))) {
                            break;
                        }
                        if (movieInputData.getTitle().equals(arrayNames.get(j))) {
                            aux = arrayNames.get(i);
                            arrayNames.set(i, arrayNames.get(j));
                            arrayNames.set(j, aux);
                            break;
                        }
                    }
                }
            }
        }
        return arrayNames;
    }

    /**
     * functie care sorteaza o lista de seriale dupa numarul de aparitii
     * ale acestora in listele de favorite ale userilor, al doilea criteriu
     * de sortare fiind aparitina in baza de date
     *
     * @param arrayNames     titlurile serialelor care trebuie sa fie sortate
     * @param arrayFavorites harta de favorite care corespund titlurilor de seriale
     * @param serialDataList lista de seriale pe baza carora cautam in baza de date
     */
    public final void sortSerialsFavorites(final ArrayList<String> arrayNames,
                                           final Map<String, Integer> arrayFavorites,
                                           final List<SerialInputData> serialDataList) {
        String aux;
        for (int i = 0; i < arrayNames.size(); i++) {
            for (int j = i + 1; j < arrayNames.size(); j++) {
                if (arrayFavorites.get(arrayNames.get(i))
                        < arrayFavorites.get(arrayNames.get(j))) {
                    aux = arrayNames.get(i);
                    arrayNames.set(i, arrayNames.get(j));
                    arrayNames.set(j, aux);
                }
                if (arrayFavorites.get(arrayNames.get(i)).equals(
                        arrayFavorites.get((arrayNames.get(j))))) {
                    for (SerialInputData serialInputData : serialDataList) {
                        if (serialInputData.getTitle().equals(arrayNames.get(i))) {
                            break;
                        }
                        if (serialInputData.getTitle().equals(arrayNames.get(j))) {
                            aux = arrayNames.get(i);
                            arrayNames.set(i, arrayNames.get(j));
                            arrayNames.set(j, aux);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * functie care imi construieste un string de nume de titluri, care contine
     * eventual doar un numar din acestea
     *
     * @param arrayNames titlurile video-urilor care o sa apara in string-ul nou format
     * @param number     numarul de titluri care sa apara in noul string
     * @param needNumber necesitatea parametrului number
     * @return intoarce noul string format
     */
    public final StringBuilder buildString(final ArrayList<String> arrayNames,
                                           final Integer number,
                                           final Boolean needNumber) {
        StringBuilder stringBuilder = new StringBuilder("[");
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
     * functie care calculeaza numarul de vizualiari ale unui film
     *
     * @param userInputDataList lista de utilizatori pe baza carora calculam
     *                          numar de vizualizari ale unui film
     * @param movieInputData    filmul caruia vrem sa ii calculma numarul de vizualizori
     * @return intoarce numarul de vizualizari ale filmului
     */
    public final Integer numberOfViews(final List<UserInputData> userInputDataList,
                                       final MovieInputData movieInputData) {
        int numberOfViews = 0;
        for (UserInputData userInputData : userInputDataList) {
            if (userInputData.getHistory().get(movieInputData.getTitle()) != null) {
                numberOfViews =
                        numberOfViews + userInputData.getHistory().get(movieInputData.getTitle());
            }
        }
        return numberOfViews;
    }

    /**
     * functie care calculeaza numarul de vizualizari ale unui serial
     *
     * @param userInputDataList lista de useri pe baza careia calculam numarul de
     *                          vizualizari a serialului
     * @param serialInputData   serialui pentru care vrem sa calculam numarul de vizualizari
     * @return intoarce numarul de vizualizari calculat
     */
    public final Integer numberOfViews(final List<UserInputData> userInputDataList,
                                       final SerialInputData serialInputData) {
        int numberOfViews = 0;
        for (UserInputData userInputData : userInputDataList) {
            if (userInputData.getHistory().get(serialInputData.getTitle()) != null) {
                numberOfViews =
                        numberOfViews + userInputData.getHistory().get(serialInputData.getTitle());
            }
        }
        return numberOfViews;
    }

    /**
     * functie care sorteaza o lista de titluri dupa rating-ul acestora, iar a doua
     * conditie de sortare este aparitia acestora in baza de date
     *
     * @param arrayNames    titlurile filmeor care trebuie sa fie sortate
     * @param arrayRatings  harta de rating-uri care corespund fiecarui film
     * @param movieDataList lista de filme pe baza careia sortam dupa baza de date
     */
    public final void sortMoviesRatingDesc(final ArrayList<String> arrayNames,
                                           final Map<String, Double> arrayRatings,
                                           final List<MovieInputData> movieDataList) {
        String aux;
        for (int i = 0; i < arrayNames.size(); i++) {
            for (int j = i + 1; j < arrayNames.size(); j++) {
                if (arrayRatings.get(arrayNames.get(i))
                        < arrayRatings.get(arrayNames.get(j))) {
                    aux = arrayNames.get(i);
                    arrayNames.set(i, arrayNames.get(j));
                    arrayNames.set(j, aux);
                }
                if (arrayRatings.get(arrayNames.get(i)).equals(
                        arrayRatings.get((arrayNames.get(j))))) {
                    for (MovieInputData movieInputData : movieDataList) {
                        if (movieInputData.getTitle().equals(arrayNames.get(i))) {
                            break;
                        }
                        if (movieInputData.getTitle().equals(arrayNames.get(j))) {
                            aux = arrayNames.get(i);
                            arrayNames.set(i, arrayNames.get(j));
                            arrayNames.set(j, aux);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * functie care sorteaza o lista de titluri dupa rating-urile acestora, a doua conditie
     * fiind aparitia acestora in baza de date
     *
     * @param arrayNames          titlurile serialelor care trebuie sa fie sortate
     * @param arrayRatings        harta de rating-uri care corespund fiecarui serial
     * @param serialInputDataList lista de seriale pe baza careia sortam dupa baza de date
     */
    public final void sortSerialsByRatingDesc(final ArrayList<String> arrayNames,
                                              final Map<String, Double> arrayRatings,
                                              final List<SerialInputData> serialInputDataList) {
        String aux;
        for (int i = 0; i < arrayNames.size(); i++) {
            for (int j = i + 1; j < arrayNames.size(); j++) {
                if (arrayRatings.get(arrayNames.get(i))
                        < arrayRatings.get(arrayNames.get(j))) {
                    aux = arrayNames.get(i);
                    arrayNames.set(i, arrayNames.get(j));
                    arrayNames.set(j, aux);
                }
                if (arrayRatings.get(arrayNames.get(i)).equals(
                        arrayRatings.get((arrayNames.get(j))))) {
                    for (SerialInputData serialInputData : serialInputDataList) {
                        if (serialInputData.getTitle().equals(arrayNames.get(i))) {
                            break;
                        }
                        if (serialInputData.getTitle().equals(arrayNames.get(j))) {
                            aux = arrayNames.get(i);
                            arrayNames.set(i, arrayNames.get(j));
                            arrayNames.set(j, aux);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * functie care imi combina doua liste de titluri de filme si seriale
     * pastrand sortarea acestora si a doua conditie fiind aparitia in baza de date
     *
     * @param moviesNames    titlurile filmelor deja sortate
     * @param moviesNumbers  harta de numere corespunzatoare fiecarui film
     * @param serialsNames   titlurile serialelor deja sortate
     * @param serialsNumbers harta de numere corespunzatoare fiecarui serial
     * @return intoarce lista de titluri amestecate
     */
    public final ArrayList<String> mix(final ArrayList<String> moviesNames,
                                       final Map<String, Integer> moviesNumbers,
                                       final ArrayList<String> serialsNames,
                                       final Map<String, Integer> serialsNumbers) {
        ArrayList<String> mixedNames = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < moviesNames.size() && j < serialsNames.size()) {
            if (moviesNumbers.get(moviesNames.get(i)) < serialsNumbers.get(serialsNames.get(j))) {
                mixedNames.add(serialsNames.get(j));
                j++;
            } else {
                mixedNames.add(moviesNames.get(i));
                i++;
            }
        }
        if (i < moviesNames.size()) {
            for (int k = i; k < moviesNames.size(); k++) {
                mixedNames.add(moviesNames.get(k));
            }
        }
        if (j < serialsNames.size()) {
            for (int k = j; k < serialsNames.size(); k++) {
                mixedNames.add(serialsNames.get(k));
            }
        }
        return mixedNames;
    }
}
