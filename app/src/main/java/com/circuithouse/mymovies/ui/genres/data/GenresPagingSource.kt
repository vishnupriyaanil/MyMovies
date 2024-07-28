import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.circuithouse.mymovies.data.remote.Genre
import com.circuithouse.mymovies.data.service.MovieService
import retrofit2.HttpException
import java.io.IOException

class GenersPagingSource(
    private val movieService: MovieService
) : PagingSource<Int, Genre>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Genre> {
        return try {
            val page = params.key ?: 1
            val response = movieService.fetchGenres()
            val movies = response.body()!!.genres
            val nextKey = null

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Genre>): Int = 1
}
