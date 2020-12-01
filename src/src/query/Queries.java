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
    public String actor(List<ActorInputData> actorInputDataList,
                        List<MovieInputData> movieInputDataList,
                        List<SerialInputData> serialInputDataList, Integer numberOfActors,
                        String sortType) {
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
        actors = sortDouble(actors, actorAverage, sortType);
        return String.valueOf(buildString(actors, numberOfActors, true));
    }

    public String actor(List<ActorInputData> actorInputDataList, List<String> awardsFilter,
                        String sortType) {
        Map<String, Integer> actorsFiltered = new HashMap<>();
        int k;
        int awardFound = 0;
        int totalAwards;
        for (ActorInputData actorInputData : actorInputDataList) {
            totalAwards = 0;
            k = 0;
            while (k < awardsFilter.size()) {
                awardFound = 0;
                if (actorInputData.getAwards().get(Utils.stringToAwards(awardsFilter.get(k))) !=
                        null) {
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
        actorsNames = sortInteger(actorsNames, actorsFiltered, sortType);
        return String.valueOf(buildString(actorsNames, 0, false));
    }

    public String actorFilter(List<ActorInputData> actorInputDataList, List<String> filter,
                              String sortType) {
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
        actorsNames = sortAlph(actorsNames, sortType);
        return String.valueOf(buildString(actorsNames, 0, false));
    }

    public ArrayList<String> sortAlph(ArrayList<String> actorsNames, String sortType) {
        String aux;
        if (sortType.equals("asc")) {
            for (int i = 0; i < actorsNames.size(); i++) {
                for (int j = i + 1; j < actorsNames.size(); j++) {
                    if (actorsNames.get(i).compareTo(actorsNames.get(j)) > 0) {
                        aux = actorsNames.get(i);
                        actorsNames.set(i, actorsNames.get(j));
                        actorsNames.set(j, aux);
                    }
                }
            }
        } else if (sortType.equals("desc")) {
            for (int i = 0; i < actorsNames.size(); i++) {
                for (int j = i + 1; j < actorsNames.size(); j++) {
                    if (actorsNames.get(i).compareTo(actorsNames.get(j)) < 0) {
                        aux = actorsNames.get(i);
                        actorsNames.set(i, actorsNames.get(j));
                        actorsNames.set(j, aux);
                    }
                }
            }
        }
        return actorsNames;
    }

    public ArrayList<String> sortInteger(ArrayList<String> actorsNames,
                                         Map<String, Integer> actorsMapInt,
                                         String sortType) {
        String aux;
        if (sortType.equals("asc")) {
            for (int i = 0; i < actorsNames.size(); i++) {
                for (int j = i + 1; j < actorsNames.size(); j++) {
                    if (actorsMapInt.get(actorsNames.get(i)) >
                            actorsMapInt.get(actorsNames.get(j))) {
                        aux = actorsNames.get(i);
                        actorsNames.set(i, actorsNames.get(j));
                        actorsNames.set(j, aux);
                    }
                    if (actorsMapInt.get(actorsNames.get(i))
                            .equals(actorsMapInt.get(actorsNames.get(j)))) {
                        if (actorsNames.get(i).compareTo(actorsNames.get(j)) > 0) {
                            aux = actorsNames.get(i);
                            actorsNames.set(i, actorsNames.get(j));
                            actorsNames.set(j, aux);
                        }
                    }
                }
            }
        }
        if (sortType.equals("desc")) {
            for (int i = 0; i < actorsNames.size(); i++) {
                for (int j = i + 1; j < actorsNames.size(); j++) {
                    if (actorsMapInt.get(actorsNames.get(i)) <
                            actorsMapInt.get(actorsNames.get(j))) {
                        aux = actorsNames.get(i);
                        actorsNames.set(i, actorsNames.get(j));
                        actorsNames.set(j, aux);
                    }
                    if (actorsMapInt.get(actorsNames.get(i))
                            .equals(actorsMapInt.get(actorsNames.get(j)))) {
                        if (actorsNames.get(i).compareTo(actorsNames.get(j)) < 0) {
                            aux = actorsNames.get(i);
                            actorsNames.set(i, actorsNames.get(j));
                            actorsNames.set(j, aux);
                        }
                    }
                }
            }
        }
        return actorsNames;
    }

    public ArrayList<String> sortDouble(ArrayList<String> actorsNames,
                                        Map<String, Double> actorsMap,
                                        String sortType) {
        String aux;
        if (sortType.equals("asc")) {
            for (int i = 0; i < actorsNames.size(); i++) {
                for (int j = i + 1; j < actorsNames.size(); j++) {
                    if (actorsMap.get(actorsNames.get(i))
                            > actorsMap.get(actorsNames.get(j))) {
                        aux = actorsNames.get(i);
                        actorsNames.set(i, actorsNames.get(j));
                        actorsNames.set(j, aux);
                    }
                    if (actorsMap.get(actorsNames.get(i))
                            .equals(actorsMap.get(actorsNames.get(j)))) {
                        if (actorsNames.get(i).compareTo(actorsNames.get(j)) > 0) {
                            aux = actorsNames.get(i);
                            actorsNames.set(i, actorsNames.get(j));
                            actorsNames.set(j, aux);
                        }
                    }
                }
            }
        }
        if (sortType.equals("desc")) {
            for (int i = 0; i < actorsNames.size(); i++) {
                for (int j = i + 1; j < actorsNames.size(); j++) {
                    if (actorsMap.get(actorsNames.get(i))
                            < actorsMap.get(actorsNames.get(j))) {
                        aux = actorsNames.get(i);
                        actorsNames.set(i, actorsNames.get(j));
                        actorsNames.set(j, aux);
                    }
                    if (actorsMap.get(actorsNames.get(i))
                            .equals(actorsMap.get(actorsNames.get(j)))) {
                        if (actorsNames.get(i).compareTo(actorsNames.get(j)) < 0) {
                            aux = actorsNames.get(i);
                            actorsNames.set(i, actorsNames.get(j));
                            actorsNames.set(j, aux);
                        }
                    }
                }
            }
        }
        return actorsNames;
    }

    public StringBuilder buildString(ArrayList<String> actorsNames, Integer number,
                                     Boolean needNumber) {
        StringBuilder stringBuilder = new StringBuilder("Query result: [");
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

    public String movieRatings(List<MovieInputData> movieInputDataList, List<List<String>> filter,
                               Integer numberOfMovies,
                               String sortType) {
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
        moviesNames = sortMoviesDouble(movieRatings, moviesNames, sortType);
        return String.valueOf(buildString(moviesNames, numberOfMovies, true));
    }

    public String moviesFavourite(List<UserInputData> userInputDataList,
                                  List<MovieInputData> movieInputDataList,
                                  List<List<String>> filter, String sortType,
                                  Integer numberOfMovies) {
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
        moviesNames = sortMoviesInteger(moviesFavourites, moviesNames, sortType);
        return String.valueOf(buildString(moviesNames, numberOfMovies, true));
    }

    public String moviesLongest(List<MovieInputData> movieInputDataList, List<List<String>> filter,
                                String sortType, Integer numberOfMovies) {
        Map<String, Integer> moviesLength = new HashMap<>();
        ArrayList<String> moviesNames = new ArrayList<>();
        ArrayList<MovieInputData> moviesFiltered;

        moviesFiltered = filterMovies(movieInputDataList, filter);
        for (MovieInputData movieInputData : moviesFiltered) {
            moviesLength.put(movieInputData.getTitle(), movieInputData.getDuration());
            moviesNames.add(movieInputData.getTitle());
        }
        moviesNames = sortMoviesInteger(moviesLength, moviesNames, sortType);
        return String.valueOf(buildString(moviesNames, numberOfMovies, true));
    }

    public String moviesMostViewed(List<UserInputData> userInputDataList,
                                   List<MovieInputData> movieInputDataList,
                                   List<List<String>> filter, String sortType,
                                   Integer numberOfMovies) {
        Map<String, Integer> moviesViews = new HashMap<>();
        int numberOfViews;
        ArrayList<String> moviesNames = new ArrayList<>();
        ArrayList<MovieInputData> moviesFiltered;

        moviesFiltered = filterMovies(movieInputDataList, filter);
        for (MovieInputData movieInputData : moviesFiltered) {
            numberOfViews = 0;
            for (UserInputData userInputData : userInputDataList) {
                if (userInputData.getHistory().get(movieInputData.getTitle()) != null) {
                    numberOfViews = numberOfViews +
                            userInputData.getHistory().get(movieInputData.getTitle());
                }
            }
            if (numberOfViews != 0) {
                moviesViews.put(movieInputData.getTitle(), numberOfViews);
                moviesNames.add(movieInputData.getTitle());
            }
        }
        moviesNames = sortInteger(moviesNames, moviesViews, sortType);
        return String.valueOf(buildString(moviesNames, numberOfMovies, true));
    }

    public String showsRatings(List<SerialInputData> serialInputDataList, List<List<String>> filter,
                               String sortType, Integer numberOfShows) {
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
        showsNames = sortDouble(showsNames, showsRatings, sortType);
        return String.valueOf((buildString(showsNames, numberOfShows, true)));
    }

    public String showsFavourite(List<UserInputData> userInputDataList,
                                 List<SerialInputData> serialInputDataList,
                                 List<List<String>> filter, String sortType,
                                 Integer numberOfShows) {
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
        showsNames = sortMoviesInteger(showsFavourites, showsNames, sortType);
        return String.valueOf(buildString(showsNames, numberOfShows, true));
    }

    public String showsLongest(List<SerialInputData> serialInputDataList, List<List<String>> filter,
                               String sortType, Integer numberOfShows) {
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
        showsNames = sortInteger(showsNames, showsLength, sortType);
        return String.valueOf(buildString(showsNames, numberOfShows, true));
    }

    public String showsMostViewed(List<UserInputData> userInputDataList,
                                  List<SerialInputData> serialInputDataList,
                                  List<List<String>> filter, String sortType,
                                  Integer numberOfShows) {
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
        showsNames = sortInteger(showsNames, showsViews, sortType);
        return String.valueOf(buildString(showsNames, numberOfShows, true));
    }

    public String usersMostActive(List<UserInputData> userInputDataList, String sortType,
                                  Integer numberOfUsers) {
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
        usersNames = sortInteger(usersNames, userActivity, sortType);
        return String.valueOf(buildString(usersNames, numberOfUsers, true));
    }

    public ArrayList<MovieInputData> filterMovies(List<MovieInputData> movieInputDataList,
                                                  List<List<String>> filter) {
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

    public ArrayList<SerialInputData> filterSerials(List<SerialInputData> serialInputDataList,
                                                    List<List<String>> filter) {
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

    public ArrayList<String> sortMoviesDouble(Map<String, Double> movieMap,
                                              ArrayList<String> moviesNames, String sortType) {
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

    public ArrayList<String> sortMoviesInteger(Map<String, Integer> movieMap,
                                               ArrayList<String> moviesNames, String sortType) {
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
