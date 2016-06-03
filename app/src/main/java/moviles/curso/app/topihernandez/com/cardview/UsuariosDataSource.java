package moviles.curso.app.topihernandez.com.cardview;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Topi on 02/06/2016.
 */
public class UsuariosDataSource {
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            DBOpenHelper.USUARIOS_COLUMN_ID,
            DBOpenHelper.USUARIOS_COLUMN_NOMBRE,
            DBOpenHelper.USUARIOS_COLUMN_CLAVE,
            DBOpenHelper.USUARIOS_COLUMN_VOTO,
    };

    public UsuariosDataSource(Context context){
        dbhelper = new DBOpenHelper(context);
    }

    public void open() {
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        dbhelper.close();
    }

    public Usuario create(Usuario usuario){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.USUARIOS_COLUMN_NOMBRE, usuario.getNombre());
        values.put(DBOpenHelper.USUARIOS_COLUMN_CLAVE, usuario.getClave());
        values.put(DBOpenHelper.USUARIOS_COLUMN_VOTO, usuario.getVoto());

        long insertid = database.insert(DBOpenHelper.TABLE_USUARIOS, null, values);
        usuario.setId(insertid);

        return usuario;
    }

}
