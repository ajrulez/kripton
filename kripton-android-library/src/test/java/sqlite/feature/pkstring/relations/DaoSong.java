package sqlite.feature.pkstring.relations;

import java.util.List;

import com.abubusoft.kripton.android.annotation.BindDao;
import com.abubusoft.kripton.android.annotation.BindSqlParam;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;

@BindDao(Song.class)
public interface DaoSong extends DaoBase<Song> {

	@BindSqlSelect
	List<Song> selectAll();
	
	@BindSqlSelect(where="albumId=${albumId}")
	List<Song> selectByAlbumId(@BindSqlParam("albumId") String dummy);
}
