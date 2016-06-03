package moviles.curso.app.topihernandez.com.cardview;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class NuevoPartido extends AppCompatActivity {

    Button btnImg;
    ImageView iv;
    TextView ETName, ETCandidate, ETRed, ETGreen, ETBlue;

    PartidosDataSource dataSource;
    private static final String LOGTAG = "LOGTAG";

    public String name, candidate, color, imgid;
    public int cRed, cGreen, cBlue;

    private String APP_DIRECTORY = "picturesPartidos/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal.jpg";

    String userName;
    boolean admin;

    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_partido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = getIntent().getStringExtra("name");
        admin = getIntent().getBooleanExtra("admin", false);

        iv = (ImageView) findViewById(R.id.iv);
        ETName = (EditText) findViewById(R.id.ETName);
        ETCandidate = (EditText) findViewById(R.id.ETCantidate);
        ETRed = (EditText) findViewById(R.id.ETCRed);
        ETGreen = (EditText) findViewById(R.id.ETCGreen);
        ETBlue = (EditText) findViewById(R.id.ETCBlue);

        FloatingActionButton done = (FloatingActionButton) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = ETName.getText().toString();
                candidate = ETCandidate.getText().toString();
                cRed = Integer.parseInt(ETRed.getText().toString());
                cGreen = Integer.parseInt(ETGreen.getText().toString());
                cBlue = Integer.parseInt(ETBlue.getText().toString());
                color = Integer.toString(cRed) + "," + Integer.toString(cGreen) + "," + Integer.toString(cBlue);

                dataSource = new PartidosDataSource(NuevoPartido.this);
                dataSource.open();

                Log.i(LOGTAG, "nombre " + name + "\ncandidato " + candidate + "\nimgid " + imgid + "\ncolor " + color);

                createPartido(name, candidate, imgid, color);

                Intent refresh = new Intent(NuevoPartido.this, MainActivity.class);
                refresh.putExtra("admin", admin);
                refresh.putExtra("name", userName);
                startActivityForResult(refresh, 1);
            }
        });



        btnImg = (Button) findViewById(R.id.btnImg);
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Tomar foto", "Elegir de glería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(NuevoPartido.this);
                builder.setTitle("Elige una opcion");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int select) {
                        switch(select){
                            case 0: //Tomar foto
                                //openCamera();
                                Toast.makeText(NuevoPartido.this, "Selecciona una foto desde galería", Toast.LENGTH_LONG);
                                break;
                            case 1: //Elegir de galería
                                Intent galeriaIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galeriaIntent.setType("image/*");
                                startActivityForResult(galeriaIntent.createChooser(galeriaIntent, "Seleciona app de imagen"), SELECT_PICTURE);
                                break;
                            case 2: //Cancelar
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            /*case PHOTO_CODE:
                if(resultCode == RESULT_OK){
                    String dir = Environment.getExternalStorageDirectory() + File.separator +
                            MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

                    decodeBitmap(dir);
                }
                break;
            */
            case SELECT_PICTURE:
                if(resultCode == RESULT_OK){

                    Uri path = data.getData();
                    imgid = path.getPath().toString();
                    Log.i(LOGTAG,"Lo mand yo: " + imgid + " " + path.toString());
                    iv.setImageURI(path);

                    String[] projection = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(path, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    imgid=cursor.getString(column_index);
                    cursor.close();

                }
                break;
        }
    }

    private void decodeBitmap(String dir) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(dir);

        iv.setImageBitmap(bitmap);
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        file.mkdirs();

        String path = Environment.getExternalStorageDirectory() + File.separator +
                MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

        File newFile = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        startActivityForResult(intent, PHOTO_CODE);
    }

    private void createPartido(String nombre, String candidato, String imgid, String color){
        Partido partido = new Partido(nombre, candidato, imgid, 0, color);
        dataSource.create(partido);
        Log.i(LOGTAG, "Nombre " + partido.nombre);
    }

}
