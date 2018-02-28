package sqlite.feature.typeadapter.bitmap;

import java.nio.ByteBuffer;

import com.abubusoft.kripton.android.BindSQLTypeAdapter;
import com.abubusoft.kripton.exception.KriptonRuntimeException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapTypeAdapter implements BindSQLTypeAdapter<Bitmap, byte[]> {

	@Override
	public Bitmap toJava(byte[] dataValue) {
		if (dataValue == null)
			return null;		
		Bitmap bitmap_tmp = BitmapFactory.decodeByteArray(dataValue, 0, dataValue.length);

		return bitmap_tmp;
	}

	@Override
	public byte[] toData(Bitmap bitmap) {
		if (bitmap == null)
			return null;
		int size = bitmap.getRowBytes() * bitmap.getHeight();
		ByteBuffer byteBuffer = ByteBuffer.allocate(size);
		bitmap.copyPixelsToBuffer(byteBuffer);
		byte[] byteArray = byteBuffer.array();

		return byteArray;
	}

	@Override
	public String toString(Bitmap javaValue) {
		throw (new KriptonRuntimeException("Unsupported operation!"));
	}

}
