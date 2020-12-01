package action;

import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.List;


public class Actions {
    /**
     * adauga un titlu in lista de favorite a unui user daca acesta nu se afla deja acolo
     * si daca acesta a fost vazut
     *
     * @param userInputData contine datele user-ului
     * @param title         contine titlul video-ului
     * @return intoarce mesaj de eroare daca video-ul nu a fost adaugat la favorite
     * si success daca a fost adaugat
     */
    public final String favorite(final UserInputData userInputData, final String title) {
        for (int i = 0; i < userInputData.getFavoriteMovies().size(); i++) {
            if (userInputData.getFavoriteMovies().get(i).equals(title)) {
                return "error -> " + title + " is already in favourite list";
            }
        }
        if (userInputData.getHistory().get(title) == null) {
            return "error -> " + title + " is not seen";
        } else {
            userInputData.getFavoriteMovies().add(
                    userInputData.getFavoriteMovies().size(), title);
            return "success -> " + title + " was added as favourite";
        }
    }

    /**
     * functia incrementeaza numarul de vizualizari a unui video
     *
     * @param userInputData cotine informatiile utilizatorului
     * @param title         contine titlul video-ului pe care vrem sa il incrementam
     * @return returneaza success impreuna cu numarul de vizualizari nou
     */
    public final String view(final UserInputData userInputData, final String title) {
        if (userInputData.getHistory().get(title) == null) {
            userInputData.getHistory().put(title, 1);
            return "success -> " + title + " was viewed with total views of "
                    + userInputData.getHistory().get(title);
        }
        userInputData.getHistory()
                .replace(title, userInputData.getHistory().get(title),
                        userInputData.getHistory().get(title) + 1);
        return "success -> " + title + " was viewed with total views of "
                + userInputData.getHistory().get(title);
    }

    /**
     * functia da rate la un film daca acesta a fost vazut si nu a primit rate de la
     * acest user pana acum
     *
     * @param userInputData  contine informatia utilizatorului care vrea sa dea rate
     * @param movieInputData filmul caruia vrem sa ii dam rate
     * @param rating         nota care vrem sa o atribuim filmului
     * @return returneaza un mesaj pentru cazul in care filmul nu a fost vazut,
     * a primit deja rate de la acest utilizator si in cazul in care totul a decurs cu succes
     */
    public final String rate(final UserInputData userInputData, final MovieInputData movieInputData,
                             final Double rating) {
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
        return "success -> " + movieInputData.getTitle() + " was rated with " + rating + " by "
                + userInputData.getUsername();
    }

    /**
     * functia da rate la un anumit sezon dintr-un serial daca acesta a fost vazut si nu a
     * primit pana cum rate de la acest utilizator
     *
     * @param userInputData   contine informatia utilizatorului care vrea sa dea rate
     * @param serialInputData contine informatia serialului caruia vrem sa ii dam rate
     * @param rating          nota care ii se atribuie serialului
     * @param seasonNumber    sezonul caruia vrem sa ii dam rate
     * @return returneaza un mesaj pentru cazul in care serialul nu a fost vizionat,
     * a primit deja rate de la acest utilizator si pentru cazul in care totul
     * a decurs cu succes
     */
    public final String rate(final UserInputData userInputData,
                             final SerialInputData serialInputData,
                             final Double rating, final Integer seasonNumber) {
        for (int i = 0; i < userInputData.getHistory().size(); i++) {
            if (userInputData.getHistory().get(serialInputData.getTitle()) == null) {
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
        return "success -> " + serialInputData.getTitle() + " was rated with " + rating + " by "
                + userInputData.getUsername();
    }

    /**
     * functia calculeaza rating-ul mediu al unui film
     *
     * @param movieInputData contine informatiile filmului caruia vrei sa ii
     *                       calculam rating-ul mediu, care acesta contine
     *                       toate rating-urile primite pana acum
     * @return returneaza rating-ul mediu al filmului
     */
    public final Double average(final MovieInputData movieInputData) {
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

    /**
     * functia calculeaza rating-ul mediu al unui serial
     *
     * @param serialInputData contine informatiile serialului caruia vrem sa ii caulculam
     *                        rating-ul mediu
     * @return intoarce rating-ul mediu al serialului
     */
    public final Double average(final SerialInputData serialInputData) {
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

    /**
     * functia calculeaza rating-ul mediu al unui actor bazat pe rating-urile obtinute
     * in filmele si serialele in care a jucat
     *
     * @param actorInputData      contine informatia actorului a carui rating vrem sa il aflam
     * @param movieInputDataList  contine o lista cu toate filmele in care e posibil ca
     *                            actorul sa fi jucat
     * @param serialInputDataList contine o list cu toate serialele in care e posibil ca
     *                            actorul sa fi jucat
     * @return intoarce rating-ul actorului
     */
    public final Double average(final ActorInputData actorInputData,
                                final List<MovieInputData> movieInputDataList,
                                final List<SerialInputData> serialInputDataList) {
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
