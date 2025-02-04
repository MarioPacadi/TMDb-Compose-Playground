package com.sample.tmdb.data.repository.tvshow.detail

import com.sample.tmdb.data.source.entity.asDatabaseModel
import com.sample.tmdb.data.source.local.TVShowDao
import com.sample.tmdb.domain.model.TVShow
import com.sample.tmdb.domain.repository.BookmarkDetailsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkTVShowDetailsRepositoryImpl @Inject constructor(private val tvShowDao: TVShowDao) :
    BookmarkDetailsRepository<TVShow> {
    override suspend fun addBookmark(item: TVShow) {
        tvShowDao.addBookmark(item.asDatabaseModel())
    }

    override suspend fun deleteBookmark(id: Int) {
        tvShowDao.deleteBookmark(id)
    }

    override suspend fun isBookmarked(id: Int): Boolean = tvShowDao.isBookmarked(id)
}
