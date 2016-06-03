package moviles.curso.app.topihernandez.com.cardview;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InicioScrollingActivity extends AppCompatActivity {
    UsuariosDataSource dataSource;
    String[] campos = new String[]{DBOpenHelper.USUARIOS_COLUMN_NOMBRE,DBOpenHelper.USUARIOS_COLUMN_CLAVE};
    String[] allColums = new String[]{
            DBOpenHelper.USUARIOS_COLUMN_ID,
            DBOpenHelper.USUARIOS_COLUMN_NOMBRE,
            DBOpenHelper.USUARIOS_COLUMN_CLAVE,
            DBOpenHelper.USUARIOS_COLUMN_VOTO
    };

    EditText ETName, ETPass;
    String pass, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("Iniciar Sesión");

        dataSource = new UsuariosDataSource(this);
        dataSource.open();

        ETName = (EditText) findViewById(R.id.ETName);
        ETPass = (EditText) findViewById(R.id.ETPass);

        Cursor cursor = dataSource.database.query(DBOpenHelper.TABLE_USUARIOS, allColums, null, null, null, null, null);
        if(cursor.getCount() == 0){
            createData();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    boolean admin = false;
                    name = ETName.getText().toString();
                    pass = ETPass.getText().toString();

                    Cursor cursor = dataSource.database.query(DBOpenHelper.TABLE_USUARIOS, campos, DBOpenHelper.USUARIOS_COLUMN_CLAVE + "='" + pass + "'", null, null, null, null);

                    if(cursor.getCount() == 0){
                        dialog();
                    }else if(cursor.getCount()>0){
                        Intent intent = new Intent(InicioScrollingActivity.this, MainActivity.class);

                        if(name == "Admin"){
                            admin = false;
                            Toast.makeText(InicioScrollingActivity.this, "Iniciando como Administrador + " + pass, Toast.LENGTH_SHORT).show();
                        }else{
                            admin = true;
                            Toast.makeText(InicioScrollingActivity.this, "Iniciando como " + name + " + " + pass, Toast.LENGTH_SHORT).show();
                        }
                        intent.putExtra("admin", admin);
                        intent.putExtra("name", name);
                        Log.i("LOGTAG", Boolean.toString(admin));
                        startActivity(intent);
                    }
                }catch (SQLException ex){
                    Snackbar.make(view, "Upss, ha ocurrido un error, intentalo más tarde", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    private void dialog() {
        final CharSequence[] options = {"Soy nuevo", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(InicioScrollingActivity.this);
        builder.setTitle("No estás registrado");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        createUsuario(name, pass);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    private void createUsuario(String name, String pass) {
        Usuario usuario = new Usuario(name, pass, "nadie");
        dataSource.create(usuario);
        Toast.makeText(InicioScrollingActivity.this, "Usuario creado", Toast.LENGTH_SHORT);
        Log.i("LOGTAG", "Se a creado un usuario con Nombre = " + name + " y Clave = " + pass);
    }

    private void createData(){
        Usuario usuario = new Usuario("Admin", "claveAdmin", "nadie");
        dataSource.create(usuario);
        Log.i("LOGTAG", "Se a creado e el admin");
    }
}
