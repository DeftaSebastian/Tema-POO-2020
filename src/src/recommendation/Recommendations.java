package recommendation;

import action.Actions;
import entertainment.Genre;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import query.Queries;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommendations {
    public String standard(List<UserInputData> userInputDataList, String userName,
                           List<MovieInputData> movieInputDataList,
                           List<SerialInputData> serialInputDataList) {
        int videoNotSeenFound = 0;
        int i = 0;
        int j = 0;
        for (UserInputData userInputData : userInputDataList) {
            if (userInputData.getUsername().equals(userName)) {
                while (videoNotSeenFound == 0) {
                    if (i >= movieInputDataList.size() && j >= serialInputDataList.size()) {
                        return "StandardRecommendation cannot be applied!";
                    }
                    while (i < movieInputDataList.size()) {
                        if (userInputData.getHistory().get(movieInputDataList.get(i).getTitle()) ==
                                null) {
                            return "StandardRecommendation result: " +
                                    movieInputDataList.get(i).getTitle();
                        }
                        i++;
                    }
                    while (j < serialInputDataList.size()) {
                        if (userInputData.getHistory().get(serialInputDataList.get(j).getTitle()) ==
                                null) {
                            return "StandardRecommendation result: " +
                                    serialInputDataList.get(j).getTitle();
                        }
                        j++;
                    }
                }
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    public String bestUnseen(List<UserInputData> userInputDataList, String userName,
                             List<MovieInputData> movieInputDataList,
                             List<SerialInputData> serialInputDataList) {
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
        moviesNames = sortMoviesByRatingDesc(moviesNames, moviesRatings, movieInputDataList);
        serialsNames = sortSerialsByRatingDesc(serialsNames, serialsRatings, serialInputDataList);
        int videoNotSeenFound = 0;
        int i = 0;
        int j = 0;
        for (UserInputData userInputData : userInputDataList) {
            if (userInputData.getUsername().equals(userName)) {
                while (videoNotSeenFound == 0) {
                    if (i >= moviesNames.size() && j >= serialsNames.size()) {
                        return "BestRatedUnseenRecommendation cannot be applied!";
                    }
                    while (i < moviesNames.size()) {
                        if (userInputData.getHistory().get(moviesNames.get(i)) ==
                                null) {
                            return "BestRatedUnseenRecommendation result: " +
                                    moviesNames.get(i);
                        }
                        i++;
                    }
                    while (j < serialsNames.size()) {
                        if (userInputData.getHistory().get(serialsNames.get(j)) ==
                                null) {
                            return "BestRatedUnseenRecommendation result: " +
                                    serialsNames.get(j);
                        }
                        j++;
                    }
                }
            }
        }
        return "BestRatedUnseenRecommendation cannot be applied!";

    }

    public String popular(List<UserInputData> userInputDataList, UserInputData userInputData,
                          List<MovieInputData> movieInputDataList,
                          List<SerialInputData> serialInputDataList) {
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
        genresList = sortPopular(genresList, genresViews);
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

    public String favorite(List<UserInputData> userInputDataList, UserInputData userInputData,
                           List<MovieInputData> movieInputDataList,
                           List<SerialInputData> serialInputDataList) {
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
        moviesNames = sortMoviesFavorites(moviesNames, moviesFavorites, movieInputDataList);
        serialsNames = sortSerialsFavorites(serialsNames, serialsFavorites, serialInputDataList);
        mixedNames = mix(moviesNames, moviesFavorites, serialsNames, serialsFavorites);
        for (String s : mixedNames) {
            if (userInputData.getHistory().get(s) == null) {
                return "FavoriteRecommendation result: " + s;
            }
        }
        return "FavoriteRecommendation cannot be applied!";
    }

    public String search(UserInputData userInputData, List<MovieInputData> movieInputDataList,
                         List<SerialInputData> serialInputDataList, String genre) {
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
        showsNames = queries.sortDouble(showsNames, showsRatings, "asc");
        if (showsNames.size() == 0) {
            return "SearchRecommendation cannot be applied!";
        }
        return "SearchRecommendation result: " +
                buildString(showsNames, 0, false);
    }

    public ArrayList<String> sortPopular(ArrayList<String> arrayNames,
                                         Map<String, Integer> arrayViews) {
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
        return arrayNames;
    }

    public ArrayList<String> sortMoviesFavorites(ArrayList<String> arrayNames,
                                                 Map<String, Integer> arrayFavorites,
                                                 List<MovieInputData> movieInputDataList) {
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
                    for (int k = 0; k < movieInputDataList.size(); k++) {
                        if (movieInputDataList.get(k).getTitle().equals(arrayNames.get(i))) {
                            break;
                        }
                        if (movieInputDataList.get(k).getTitle().equals(arrayNames.get(j))) {
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

    public ArrayList<String> sortSerialsFavorites(ArrayList<String> arrayNames,
                                                  Map<String, Integer> arrayFavorites,
                                                  List<SerialInputData> serialInputDataList) {
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
                    for (int k = 0; k < serialInputDataList.size(); k++) {
                        if (serialInputDataList.get(k).getTitle().equals(arrayNames.get(i))) {
                            break;
                        }
                        if (serialInputDataList.get(k).getTitle().equals(arrayNames.get(j))) {
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

    public StringBuilder buildString(ArrayList<String> actorsNames, Integer number,
                                     Boolean needNumber) {
        StringBuilder stringBuilder = new StringBuilder("[");
        if (actorsNames.size() < number || !needNumber) {
            for (int i = 0; i < actorsNames.size(); i++) {
                stringBuilder.append(actorsNames.get(i));
                if (i + 1 != actorsNames.size()) {
                    stringBuilder.append(", ");
                }
            }
        } else {
            for (int i = 0; i < number; i++) {
                stringBuilder.append(actorsNames.get(i));
                if (i + 1 != number) {
                    stringBuilder.append(", ");
                }
            }
        }
        stringBuilder.append("]");
        return stringBuilder;
    }

    public Integer numberOfViews(List<UserInputData> userInputDataList,
                                 MovieInputData movieInputData) {
        int numberOfViews = 0;
        for (UserInputData userInputData : userInputDataList) {
            if (userInputData.getHistory().get(movieInputData.getTitle()) != null) {
                numberOfViews =
                        numberOfViews + userInputData.getHistory().get(movieInputData.getTitle());
            }
        }
        return numberOfViews;
    }

    public Integer numberOfViews(List<UserInputData> userInputDataList,
                                 SerialInputData serialInputData) {
        int numberOfViews = 0;
        for (UserInputData userInputData : userInputDataList) {
            if (userInputData.getHistory().get(serialInputData.getTitle()) != null) {
                numberOfViews =
                        numberOfViews + userInputData.getHistory().get(serialInputData.getTitle());
            }
        }
        return numberOfViews;
    }

    public ArrayList<String> sortMoviesByRatingDesc(ArrayList<String> arrayNames,
                                                    Map<String, Double> arrayRatings,
                                                    List<MovieInputData> movieInputDataList) {
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
                    for (int k = 0; k < movieInputDataList.size(); k++) {
                        if (movieInputDataList.get(k).getTitle().equals(arrayNames.get(i))) {
                            break;
                        }
                        if (movieInputDataList.get(k).getTitle().equals(arrayNames.get(j))) {
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

    public ArrayList<String> sortSerialsByRatingDesc(ArrayList<String> arrayNames,
                                                     Map<String, Double> arrayRatings,
                                                     List<SerialInputData> serialInputDataList) {
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
                    for (int k = 0; k < serialInputDataList.size(); k++) {
                        if (serialInputDataList.get(k).getTitle().equals(arrayNames.get(i))) {
                            break;
                        }
                        if (serialInputDataList.get(k).getTitle().equals(arrayNames.get(j))) {
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

    public ArrayList<String> mix(ArrayList<String> moviesNames, Map<String, Integer> moviesNumbers,
                                 ArrayList<String> serialsNames,
                                 Map<String, Integer> serialsNumbers) {
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
