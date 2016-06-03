package moviles.curso.app.topihernandez.com.cardview;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by Topi on 26/05/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    List<Partido> partidos;
    boolean votaste = false;
    String votoPor = "nadie";
    PartidosDataSource dataSource;

    public RecyclerAdapter(List<Partido> partidos){
        this.partidos = partidos;
        Log.i("LOGTAG", "En recycler adapter: " + partidos.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        Log.i("LOGTAG", "Entra al onCreateViewHolder");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int[] votos = new int[partidos.size()];
        final boolean[] votado = new boolean[partidos.size()];
        final ContentValues cantVotos = new ContentValues();
        final ContentValues votoPorValue = new ContentValues();
        votado[position] = false;
        votos[position]  = partidos.get(position).votos;
        String imgid = partidos.get(position).imgid;
        Uri uriImgid = Uri.parse(imgid);
        MainActivity main = new MainActivity();

        final UsuariosDataSource dataSource1 = new UsuariosDataSource(holder.itemView.getContext());
        dataSource1.open();

        //String userName = holder.TVUserName.getText().toString();

        Toast.makeText(holder.itemView.getContext(), "Nombre: " + main.userName, Toast.LENGTH_SHORT).show();

        String[] colums = new String[]{
                DBOpenHelper.USUARIOS_COLUMN_ID,
                DBOpenHelper.USUARIOS_COLUMN_NOMBRE,
                DBOpenHelper.USUARIOS_COLUMN_VOTO,
                DBOpenHelper.USUARIOS_COLUMN_CLAVE
        };
        Cursor cursor = dataSource1.database.query(DBOpenHelper.TABLE_USUARIOS, colums, DBOpenHelper.USUARIOS_COLUMN_NOMBRE + "='" + main.userName + "'", null, null, null, null);

        if(cursor.getCount() > 0){
            long idUser = cursor.getLong(0);
            String nombreUser = cursor.getString(1);
            votoPor = cursor.getString(2);
            String claveUser = cursor.getString(3);

            Log.i("LOGTAG", "Nombre: " + nombreUser + "\nVoto Por: " + votoPor + "\nClave: " + claveUser);
        }else{
            Log.i("LOGTAG", "No se encuentra el nombre: " + main.userName);
        }

        holder.TVNombre.setText(partidos.get(position).nombre);
        holder.TVCandidato.setText(partidos.get(position).candidato);
        holder.TVVotos.setText("Votos: " + Integer.toString(votos[position]));
        holder.IVLogo.setImageURI(uriImgid);

        holder.ly.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dataSource = new PartidosDataSource(v.getContext());
                dataSource.open();
                MainActivity main = new MainActivity();

                if(!votado[position] && !votaste && (votoPor == "nadie")){

                    holder.ly.setBackgroundColor(Color.rgb(204,255,144));
                    Log.i("LOGTAG", Integer.toString(votos[position] + 1));

                    votoPorValue.put(DBOpenHelper.USUARIOS_COLUMN_VOTO, partidos.get(position).nombre);
                    cantVotos.put(DBOpenHelper.PARTIDOS_COLUMN_VOTOS, Integer.toString(votos[position] + 1));

                    dataSource.database.update(DBOpenHelper.TABLE_USUARIOS, votoPorValue, DBOpenHelper.USUARIOS_COLUMN_NOMBRE + "='" + main.getUserName() + "'", null);
                    dataSource.database.update(DBOpenHelper.TABLE_PARTIDOS, cantVotos, DBOpenHelper.PARTIDOS_COLUMN_ID+ "=" + partidos.get(position).id, null);

                    Toast.makeText(v.getContext(), "Votaste por " + holder.TVCandidato.getText().toString(), Toast.LENGTH_SHORT).show();

                    votado[position] = true;
                    votaste = true;
                    votoPor = partidos.get(position).nombre;

                    dataSource.database.close();
                    dataSource1.database.close();

                    votos[position] = partidos.get(position).votos + 1;

                    holder.TVVotos.setText("Votos: " + Integer.toString(votos[position]));

                    /*Intent refresh = new Intent(v.getContext(), MainActivity.class);
                    refresh.putExtra("votoPor", partidos.get(position).nombre);
                    v.getContext().startActivity(refresh);*/

                }else if(!votado[position] && votaste && votoPor != "nadie"){

                    Toast.makeText(v.getContext(), "Ya has votado por alguien", Toast.LENGTH_SHORT).show();

                }/*else if(votoPor != "nadie"){
                    Toast.makeText(v.getContext(), "Votaste", Toast.LENGTH_SHORT).show();
                }*/
                else{

                    holder.ly.setBackgroundColor(Color.rgb(255,255,255));
                    cantVotos.put(DBOpenHelper.PARTIDOS_COLUMN_VOTOS, Integer.toString(votos[position] - 1));
                    dataSource.database.update(DBOpenHelper.TABLE_PARTIDOS, cantVotos, DBOpenHelper.PARTIDOS_COLUMN_ID+ "=" + partidos.get(position).id, null);

                    Toast.makeText(v.getContext(), "Cancelaste voto por " + holder.TVCandidato.getText().toString(), Toast.LENGTH_SHORT).show();

                    votado[position] = false;
                    votaste = false;
                    votoPor = "nadie";

                    dataSource.close();
                    votos[position] = partidos.get(position).votos;

                    holder.TVVotos.setText("Votos: " + Integer.toString(votos[position]));

                    /*Intent refresh = new Intent(v.getContext(), v.getClass());
                    v.getContext().startActivity(refresh);*/

                }
                //MainActivity main= new MainActivity();
                //main.refresh(v);
                return false;
            }
        });

        Log.i("LOGTAG", "En onbind " + partidos.get(position).nombre + "\n" );
    }

    @Override
    public int getItemCount() {
        return partidos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView TVNombre, TVCandidato, TVVotos;
        public LinearLayout ly;
        public ImageView IVLogo;
        public CardView cv;
        public TextView TVUserName;

        public ViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            TVNombre = (TextView) itemView.findViewById(R.id.TVNombre);
            TVCandidato = (TextView) itemView.findViewById(R.id.TVCandidato);
            TVVotos = (TextView) itemView.findViewById(R.id.TVVotos);
            IVLogo = (ImageView) itemView.findViewById(R.id.IVLogo);
            ly = (LinearLayout) itemView.findViewById(R.id.LYcv);
            //TVUserName = (TextView) itemView.getRootView()
        }
    }
}
