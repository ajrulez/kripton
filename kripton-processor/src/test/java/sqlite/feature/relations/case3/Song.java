package sqlite.feature.relations.case3;

import com.abubusoft.kripton.android.annotation.BindSqlColumn;
import com.abubusoft.kripton.android.annotation.BindTable;

@BindTable
public class Song {
	public long id;
	public String name;
	
	@BindSqlColumn(parentEntity=Album.class)
	public long albumId;
}