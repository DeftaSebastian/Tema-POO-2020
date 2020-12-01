package action;

import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actions {
    public final String favorite(UserInputData userInputData, String title) {
        Map<String, Integer> historyMap = userInputData.getHistory();
        for (int i = 0; i < userInputData.getFavoriteMovies().size(); i++) {
            if (userInputData.getFavoriteMovies().get(i).equals(title)) {
                return "error -> " + title + " is already in favourite list";
            }
        }
        if (historyMap.get(title) == null) {
            return "error -> " + title + " is not seen";
        } else {
            userInputData.getFavoriteMovies().add(
                    userInputData.getFavoriteMovies().size(), title);
            return "success -> " + title + " was added as favourite";
        }
    }


    public String view(UserInputData userInputData, String title) {
        Map<String, Integer> historyMap = userInputData.getHistory();
        if (historyMap.get(title) == null) {
            historyMap.put(title, 1);
            return "success -> " + title + " was viewed with total views of " +
                    historyMap.get(title);
        }
        historyMap.replace(title, historyMap.get(title), historyMap.get(title) + 1);
        return "success -> " + title + " was viewed with total views of " + historyMap.get(title);
    }

    public String rate(UserInputData userInputData, MovieInputData movieInputData, Double rating) {
        for (int i = 0; i < userInputData.getHistory().size(); i++) {
            if (userInputData.getHistory().get(movieInputData.getTitle()) == null) {
                return "error -> " + movieInputData.getTitle() + " is not seen";
            }
        }
        for (int i = 0; i < userInputData.getTitlesRated().size(); i++) {
            if (userInputData.getTitlesRated().get(i).equals(movieInputData.getTitle())) {
                return "error -> " + movieInputData.getTitle() + " has been already rated";
            }
        }
        movieInputData.getRatings().add(rating);
        userInputData.getTitlesRated().add(movieInputData.getTitle());
        return "success -> " + movieInputData.getTitle() + " was rated with " + rating + " by " +
                userInputData.getUsername();
    }

    public String rate(UserInputData userInputData, SerialInputData serialInputData,
                       Double rating, Integer seasonNumber) {
        for(int i = 0; i < userInputData.getHistory().size(); i++){
            if(userInputData.getHistory().get(serialInputData.getTitle()) == null){
                return "error -> " + serialInputData.getTitle() + " is not seen";
            }
        }
        for (int i = 0; i < userInputData.getSerialsRated().size(); i++) {
            if (userInputData.getSerialsRated().get(serialInputData.getTitle()) != null) {
                for (int j = 0;
                     j < userInputData.getSerialsRated().get(serialInputData.getTitle()).size();
                     j++) {
                    if (userInputData.getSerialsRated().get(serialInputData.getTitle()).get(j)
                            .equals(seasonNumber)) {
                        return "error -> " + serialInputData.getTitle() + " has been already rated";
                    }
                }
            }
        }
        serialInputData.getRatings().get(seasonNumber - 1).add(rating);
        ArrayList<Integer> ratingList = new ArrayList<>();
        ratingList.add(seasonNumber);
        userInputData.getSerialsRated().put(serialInputData.getTitle(), ratingList);
        return "success -> " + serialInputData.getTitle() + " was rated with " + rating + " by " +
                userInputData.getUsername();
    }

    public Double average(MovieInputData movieInputData) {
        double average = 0.0;
        int i;
        for (i = 0; i < movieInputData.getRatings().size(); i++) {
            average = average + movieInputData.getRatings().get(i);
        }
        if (i != 0) {
            average = average / i;
        }
        return average;
    }

    public Double average(SerialInputData serialInputData) {
        double averageSeason = 0.0;
        double average = 0.0;
        int i;
        int j;
        for (i = 0; i < serialInputData.getRatings().size(); i++) {
            for (j = 0; j < serialInputData.getRatings().get(i).size(); j++) {
                averageSeason = averageSeason + serialInputData.getRatings().get(i).get(j);
            }
            if (j != 0) {
                average = average + averageSeason / j;
            }
            averageSeason = 0.0;
        }
        if (i != 0) {
            average = average / i;
        }
        return average;
    }

    public Double average(ActorInputData actorInputData, List<MovieInputData> movieInputDataList,
                          List<SerialInputData> serialInputDataList) {
        double average = 0.0;
        int i;
        int number = 0;
        for (i = 0; i < actorInputData.getFilmography().size(); i++) {
            for (MovieInputData movieInputData : movieInputDataList) {
                if (actorInputData.getFilmography().get(i)
                        .equals(movieInputData.getTitle())) {
                    average = average + average(movieInputData);
                    if (average(movieInputData) != 0.0) {
                        number++;
                    }
                }
            }
            for (SerialInputData serialInputData : serialInputDataList) {
                if (actorInputData.getFilmography().get(i)
                        .equals(serialInputData.getTitle())) {
                    average = average + average(serialInputData);
                    if (average(serialInputData) != 0.0) {
                        number++;
                    }
                }
            }
        }
        if (number != 0) {
            average = average / number;
        }
        return average;
    }
}
