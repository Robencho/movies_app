import 'package:movies_app/domain/usecases/movie_usecase.dart';

import '../entities/movie.dart';
import '../repositories/movie_repositorie.dart';

class GetPopularMoviesUseCase implements MovieUsecase {
  final MovieRepository repository;

  GetPopularMoviesUseCase(this.repository);

  @override
  Future<({List<Movie>movies, int totalPages})> execute({int page = 1}) async {
    return await repository.getPopularMovies(page: page);
  }
}