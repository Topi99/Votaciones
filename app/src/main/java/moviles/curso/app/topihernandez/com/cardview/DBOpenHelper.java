package moviles.curso.app.topihernandez.com.cardview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Topi on 25/05/2016.
 */
public class DBOpenHelper extends SQLiteOpenHelper{

    private static final String LOGTAG = "LOGTAG";

    private static final String DATABASE_NAME = "votaciones.db";
    private static final int DATABASE_VERSION = 5;

    public static final String TABLE_PARTIDOS  = "partidos";
    public static final String PARTIDOS_COLUMN_ID = "id";
    public static final String PARTIDOS_COLUMN_NAME = "nombre";
    public static final String PARTIDOS_COLUMN_CANDIDATO = "candidato";
    public static final String PARTIDOS_COLUMN_IMGID = "imgid";
    public static final String PARTIDOS_COLUMN_VOTOS = "votos";
    public static final String PARTIDOS_COLUMN_COLOR = "color";

    public static final String TABLE_USUARIOS = "usuarios";
    public static final String USUARIOS_COLUMN_ID = "id";
    public static final String USUARIOS_COLUMN_NOMBRE = "nombre";
    public static final String USUARIOS_COLUMN_CLAVE = "clave";
    public static final String USUARIOS_COLUMN_VOTO = "voto";

    public static final String TABLE_PARTIDOS_CREATE =
            "CREATE TABLE " + TABLE_PARTIDOS + " (" +
                    PARTIDOS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PARTIDOS_COLUMN_NAME + " TEXT, " +
                    PARTIDOS_COLUMN_CANDIDATO + " TEXT, " +
                    PARTIDOS_COLUMN_IMGID + " TEXT, " +
                    PARTIDOS_COLUMN_VOTOS + " INTEGER, " +
                    PARTIDOS_COLUMN_COLOR + " TEXT " +
                    ")";

    public static final String TABLE_USUARIOS_CREATE =
            "CREATE TABLE " + TABLE_USUARIOS + " (" +
                    USUARIOS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USUARIOS_COLUMN_NOMBRE + " TEXT, " +
                    USUARIOS_COLUMN_CLAVE + " TEXT, " +
                    USUARIOS_COLUMN_VOTO + " TEXT " +
                    ")";

    public DBOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_PARTIDOS_CREATE);
        db.execSQL(TABLE_USUARIOS_CREATE);
        Log.i(LOGTAG, "LA TABLA PARTIDOS SE HA CREADO");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTIDOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }
}
