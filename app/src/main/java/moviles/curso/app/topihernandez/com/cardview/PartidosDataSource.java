package moviles.curso.app.topihernandez.com.cardview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Topi on 26/05/2016.
 */
public class PartidosDataSource {
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            DBOpenHelper.PARTIDOS_COLUMN_ID,
            DBOpenHelper.PARTIDOS_COLUMN_NAME,
            DBOpenHelper.PARTIDOS_COLUMN_CANDIDATO,
            DBOpenHelper.PARTIDOS_COLUMN_IMGID,
            DBOpenHelper.PARTIDOS_COLUMN_VOTOS,
            DBOpenHelper.PARTIDOS_COLUMN_COLOR
    };

    public PartidosDataSource(Context context){
        dbhelper = new DBOpenHelper(context);
    }

    public void open(){
        database = dbhelper.getWritableDatabase();
    }

    public void close(){
        dbhelper.close();
    }

    public Partido create(Partido partido){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.PARTIDOS_COLUMN_NAME, partido.getNombre());
        values.put(DBOpenHelper.PARTIDOS_COLUMN_CANDIDATO, partido.getCandidato());
        values.put(DBOpenHelper.PARTIDOS_COLUMN_IMGID, partido.getImgid());
        values.put(DBOpenHelper.PARTIDOS_COLUMN_VOTOS, partido.getVotos());
        values.put(DBOpenHelper.PARTIDOS_COLUMN_COLOR, partido.getColor());

        long insertid = database.insert(DBOpenHelper.TABLE_PARTIDOS, null, values);
        partido.setId(insertid);

        return partido;
    }

    public List<Partido> findAll(){
        Cursor cursor = database.query(DBOpenHelper.TABLE_PARTIDOS, allColumns, null, null, null, null, null);
        List<Partido> partidos = cursorToList(cursor);
        Log.i("LOGTAG", partidos.toString());
        return partidos;
    }

    public List<Partido> cursorToList(Cursor cursor){
        int id;
        String nombre;
        String candidato;
        String imgid;
        int votos;
        String color;

        List<Partido> partidos = new ArrayList<Partido>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBOpenHelper.PARTIDOS_COLUMN_ID)));
                nombre = cursor.getString(cursor.getColumnIndex(DBOpenHelper.PARTIDOS_COLUMN_NAME));
                candidato = cursor.getString(cursor.getColumnIndex(DBOpenHelper.PARTIDOS_COLUMN_CANDIDATO));
                imgid = cursor.getString(cursor.getColumnIndex(DBOpenHelper.PARTIDOS_COLUMN_IMGID));
                votos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBOpenHelper.PARTIDOS_COLUMN_VOTOS)));
                color = cursor.getString(cursor.getColumnIndex(DBOpenHelper.PARTIDOS_COLUMN_COLOR));

                Partido partido = new Partido(nombre, candidato, imgid, votos, color);
                partido.setId(id);
                partidos.add(partido);
                Log.i("LOGTAG", "\nPartido: " + nombre);
            }
        }
        return partidos;
    }
}
