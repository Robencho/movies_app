// data/models/movie_model.dart
import 'package:json_annotation/json_annotation.dart';
import 'package:movies_app/core/constants/environment.dart';

import '../../domain/entities/movie.dart';

part 'movie_model.g.dart';

@JsonSerializable()
class MovieModel {
  @JsonKey(name: 'adult')
  final bool adult;
   @JsonKey(name: 'backdrop_path')
  final String? backdropPath;
   @JsonKey(name: 'genre_ids')
  final List<int>? genreIds;
   @JsonKey(name: 'id')
  final int id;
   @JsonKey(name: 'original_language')
  final String originalLanguage;
   @JsonKey(name: 'original_title')
  final String originalTitle;
   @JsonKey(name: 'overview')
  final String? overview;
   @JsonKey(name: 'popularity', fromJson: _parseDouble, defaultValue: 0)
  final double popularity;
   @JsonKey(name: 'poster_path')
  final String? posterPath;
   @JsonKey(name: 'release_date')
  final String? releaseDate;
   @JsonKey(name: 'title')
  final String title;
   @JsonKey(name: 'video')
  final bool video;
   @JsonKey(name: 'vote_average')
  final double voteAverage;
   @JsonKey(name: 'vote_count')
  final int voteCount;
  @JsonKey(name: 'total_pages')
  final int? totalPages;

  MovieModel({
   required this.adult,
    required this.backdropPath,
    required this.genreIds,
    required this.id,
    required this.originalLanguage,
    required this.originalTitle,
    required this.overview,
    required this.popularity,
    required this.posterPath,
    required this.releaseDate,
    required this.title,
    required this.video,
    required this.voteAverage,
    required this.voteCount,
    required this.totalPages
  });

  factory MovieModel.fromJson(Map<String, dynamic> json) =>
      _$MovieModelFromJson(json);

  Map<String, dynamic> toJson() => _$MovieModelToJson(this);

  static double _parseDouble(dynamic value)=>
    value is int? value.toDouble(): value as double;

    // En tu modelo o en una clase helper (ej: core/utils/image_utils.dart)
  String getFullPosterPath(String? posterPath) {
    if (posterPath != null && posterPath.isNotEmpty) {
      return '${Environment.imagesBaseurl}$posterPath';
    } else {
      // Imagen de relleno desde un servicio o assets
      return 'https://dzinejs.lv/wp-content/plugins/lightbox/images/No-image-found.jpg';
    }
  }

  Movie toEntity() {
    return Movie(
      adult: adult,
      backdropPath: getFullPosterPath(backdropPath),
      genreIds: genreIds ?? [],
      id: id,
      originalLanguage: originalLanguage,
      originalTitle: originalTitle,
      overview: overview ?? 'No description',
      popularity: popularity,
      posterPath:getFullPosterPath(posterPath),
      releaseDate: releaseDate ?? 'No disponible',
      title: title,
      video: video,
      voteAverage: voteAverage,
      voteCount: voteCount,
      totalPages: totalPages
    );
  }
}