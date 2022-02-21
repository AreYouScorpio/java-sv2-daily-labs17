package day01;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MoviesRatingService {

    private MoviesRepository moviesRepository;

    private RatingsRepository ratingsRepository;

    public MoviesRatingService(MoviesRepository moviesRepository, RatingsRepository ratingsRepository) {
        this.moviesRepository = moviesRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public void addRatings(String title, Integer... ratings){
        Optional<Movie> actual = moviesRepository.findMovieByTitle(title);
        if(actual.isPresent()){
            //Arrays.asList(ratings);
            ratingsRepository.insertRating(actual.get().getId(), Arrays.asList(ratings));
            updateAvgRatingByTitle(title);
        }
        else {
            throw new IllegalArgumentException("Cannot find movie: " + title);
        }
    }

    //last week 1.day
    public void updateAvgRatingByTitle(String title){
        double avg = moviesRepository.getMovieAvgRating(title);
        moviesRepository.updateMovieAvgRating(title, avg);
    }
}
