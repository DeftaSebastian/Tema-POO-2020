package main;

import action.Actions;
import actor.ActorsAwards;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import entertainment.Genre;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import query.Queries;
import recommendation.Recommendations;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        List<UserInputData> usersList = input.getUsers();
        List<MovieInputData> moviesList = input.getMovies();
        List<SerialInputData> serialsList = input.getSerials();
        List<ActorInputData> actorList = input.getActors();
        //starting my implementation
        for (int i = 0; i < input.getCommands().size(); i++) {
            if ("command".equals(input.getCommands().get(i).getActionType())) {
                for (UserInputData userInputData : usersList) {
                    if (userInputData.getUsername()
                            .equals(input.getCommands().get(i).getUsername())) {
                        switch (input.getCommands().get(i).getType()) {
                            case "favorite" -> {
                                Actions actions = new Actions();
                                JSONObject object =
                                        fileWriter.writeFile(input.getCommands().get(i)
                                                        .getActionId(),
                                                input.getCommands().get(i)
                                                        .getCriteria(),
                                                actions.favorite(userInputData,
                                                        input.getCommands().get(i)
                                                                .getTitle()));
                                arrayResult.add(object);
                            }
                            case "view" -> {
                                Actions actions = new Actions();
                                JSONObject object =
                                        fileWriter.writeFile(input.getCommands().get(i)
                                                        .getActionId(),
                                                input.getCommands().get(i)
                                                        .getCriteria(),
                                                actions.view(userInputData,
                                                        input.getCommands().get(i)
                                                                .getTitle()));
                                arrayResult.add(object);
                            }
                            case "rating" -> {
                                for (MovieInputData movieInputData : moviesList) {
                                    if (movieInputData.getTitle()
                                            .equals(input.getCommands().get(i).getTitle())) {
                                        Actions actions = new Actions();
                                        JSONObject object = fileWriter
                                                .writeFile(input.getCommands().get(i).getActionId(),
                                                        input.getCommands().get(i).getCriteria(),
                                                        actions.rate(userInputData, movieInputData,
                                                                input.getCommands().get(i)
                                                                        .getGrade()));
                                        arrayResult.add(object);
                                    }
                                }
                                for (SerialInputData serialInputData : serialsList) {
                                    if (serialInputData.getTitle()
                                            .equals(input.getCommands().get(i).getTitle())) {
                                        Actions actions = new Actions();
                                        JSONObject object = fileWriter
                                                .writeFile(input.getCommands().get(i).getActionId(),
                                                        input.getCommands().get(i).getCriteria(),
                                                        actions.rate(userInputData,
                                                                serialInputData,
                                                                input.getCommands().get(i)
                                                                        .getGrade(),
                                                                input.getCommands().get(i)
                                                                        .getSeasonNumber()));
                                        arrayResult.add(object);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if ("query".equals(input.getCommands().get(i).getActionType())) {
                switch (input.getCommands().get(i).getObjectType()) {
                    case "actors" -> {
                        switch (input.getCommands().get(i).getCriteria()) {
                            case "average" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.actor(actorList, moviesList,
                                                        serialsList,
                                                        input.getCommands().get(i)
                                                                .getNumber(),
                                                        input.getCommands().get(i)
                                                                .getSortType()));
                                arrayResult.add(object);
                            }
                            case "awards" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.actor(actorList,
                                                        input.getCommands().get(i).getFilters()
                                                                .get(3),
                                                        input.getCommands().get(i).getSortType()));
                                arrayResult.add(object);
                            }
                            case "filter_description" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.actorFilter(actorList,
                                                        input.getCommands().get(i).getFilters()
                                                                .get(2),
                                                        input.getCommands().get(i).getSortType()));
                                arrayResult.add(object);
                            }
                        }
                    }
                    case "movies" -> {
                        switch (input.getCommands().get(i).getCriteria()) {
                            case "ratings" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.movieRatings(moviesList,
                                                        input.getCommands().get(i).getFilters(),
                                                        input.getCommands().get(i).getNumber(),
                                                        input.getCommands().get(i).getSortType()));
                                arrayResult.add(object);
                            }
                            case "favorite" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.moviesFavourite(usersList, moviesList,
                                                        input.getCommands().get(i).getFilters(),
                                                        input.getCommands().get(i).getSortType(),
                                                        input.getCommands().get(i).getNumber()));
                                arrayResult.add(object);
                            }
                            case "longest" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.moviesLongest(moviesList,
                                                        input.getCommands().get(i).getFilters(),
                                                        input.getCommands().get(i).getSortType(),
                                                        input.getCommands().get(i).getNumber()));
                                arrayResult.add(object);
                            }
                            case "most_viewed" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.moviesMostViewed(usersList, moviesList,
                                                        input.getCommands().get(i).getFilters(),
                                                        input.getCommands().get(i).getSortType(),
                                                        input.getCommands().get(i).getNumber()));
                                arrayResult.add(object);
                            }
                        }
                    }
                    case "shows" -> {
                        switch (input.getCommands().get(i).getCriteria()) {
                            case "ratings" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.showsRatings(serialsList,
                                                        input.getCommands().get(i).getFilters(),
                                                        input.getCommands().get(i).getSortType(),
                                                        input.getCommands().get(i).getNumber()));
                                arrayResult.add(object);
                            }
                            case "favorite" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.showsFavourite(usersList, serialsList,
                                                        input.getCommands().get(i).getFilters(),
                                                        input.getCommands().get(i).getSortType(),
                                                        input.getCommands().get(i).getNumber()));
                                arrayResult.add(object);
                            }
                            case "longest" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.showsLongest(serialsList,
                                                        input.getCommands().get(i).getFilters(),
                                                        input.getCommands().get(i).getSortType(),
                                                        input.getCommands().get(i).getNumber()));
                                arrayResult.add(object);
                            }
                            case "most_viewed" -> {
                                Queries query = new Queries();
                                JSONObject object = fileWriter
                                        .writeFile(input.getCommands().get(i).getActionId(),
                                                input.getCommands().get(i).getCriteria(),
                                                query.showsMostViewed(usersList, serialsList,
                                                        input.getCommands().get(i).getFilters(),
                                                        input.getCommands().get(i).getSortType(),
                                                        input.getCommands().get(i).getNumber()));
                                arrayResult.add(object);
                            }
                        }
                    }
                    case "users" -> {
                        Queries query = new Queries();
                        JSONObject object = fileWriter
                                .writeFile(input.getCommands().get(i).getActionId(),
                                        input.getCommands().get(i).getCriteria(),
                                        query.usersMostActive(usersList,
                                                input.getCommands().get(i).getSortType(),
                                                input.getCommands().get(i).getNumber()));
                        arrayResult.add(object);
                    }
                }
            }
            if ("recommendation".equals(input.getCommands().get(i).getActionType())) {
                switch (input.getCommands().get(i).getType()) {
                    case "standard" -> {
                        Recommendations recommendations = new Recommendations();
                        JSONObject object =
                                fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                                        input.getCommands().get(i).getCriteria(),
                                        recommendations.standard(usersList,
                                                input.getCommands().get(i).getUsername(),
                                                moviesList, serialsList));
                        arrayResult.add(object);
                    }
                    case "best_unseen" -> {
                        Recommendations recommendations = new Recommendations();
                        JSONObject object =
                                fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                                        input.getCommands().get(i).getCriteria(),
                                        recommendations.bestUnseen(usersList,
                                                input.getCommands().get(i).getUsername(),
                                                moviesList, serialsList));
                        arrayResult.add(object);
                    }
                    case "popular" -> {
                        for (UserInputData userInputData : usersList) {
                            if (userInputData.getUsername()
                                    .equals(input.getCommands().get(i).getUsername())) {
                                if (userInputData.getSubscriptionType().equals("PREMIUM")) {
                                    Recommendations recommendations = new Recommendations();
                                    JSONObject object =
                                            fileWriter.writeFile(
                                                    input.getCommands().get(i).getActionId(),
                                                    input.getCommands().get(i).getCriteria(),
                                                    recommendations
                                                            .popular(usersList, userInputData,
                                                                    moviesList, serialsList));
                                    arrayResult.add(object);
                                } else {
                                    JSONObject object =
                                            fileWriter.writeFile(
                                                    input.getCommands().get(i).getActionId(),
                                                    input.getCommands().get(i).getCriteria(),
                                                    "PopularRecommendation cannot be applied!");
                                    arrayResult.add(object);
                                }
                            }
                        }
                    }
                    case "favorite" -> {
                        for (UserInputData userInputData : usersList) {
                            if (userInputData.getUsername()
                                    .equals(input.getCommands().get(i).getUsername())) {
                                if (userInputData.getSubscriptionType().equals("PREMIUM")) {
                                    Recommendations recommendations = new Recommendations();
                                    JSONObject object =
                                            fileWriter.writeFile(
                                                    input.getCommands().get(i).getActionId(),
                                                    input.getCommands().get(i).getCriteria(),
                                                    recommendations
                                                            .favorite(usersList, userInputData,
                                                                    moviesList, serialsList));
                                    arrayResult.add(object);
                                } else {
                                    JSONObject object =
                                            fileWriter.writeFile(
                                                    input.getCommands().get(i).getActionId(),
                                                    input.getCommands().get(i).getCriteria(),
                                                    "PopularRecommendation cannot be applied!");
                                    arrayResult.add(object);
                                }
                            }
                        }
                    }
                    case "search" -> {
                        for (UserInputData userInputData : usersList) {
                            if (userInputData.getUsername()
                                    .equals(input.getCommands().get(i).getUsername())) {
                                if (userInputData.getSubscriptionType().equals("PREMIUM")) {
                                    Recommendations recommendations = new Recommendations();
                                    JSONObject object =
                                            fileWriter.writeFile(
                                                    input.getCommands().get(i).getActionId(),
                                                    input.getCommands().get(i).getCriteria(),
                                                    recommendations
                                                            .search(userInputData, moviesList,
                                                                    serialsList,
                                                                    input.getCommands().get(i)
                                                                            .getGenre()));
                                    arrayResult.add(object);
                                } else {
                                    JSONObject object =
                                            fileWriter.writeFile(
                                                    input.getCommands().get(i).getActionId(),
                                                    input.getCommands().get(i).getCriteria(),
                                                    "PopularRecommendation cannot be applied!");
                                    arrayResult.add(object);
                                }
                            }
                        }
                    }
                }
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}