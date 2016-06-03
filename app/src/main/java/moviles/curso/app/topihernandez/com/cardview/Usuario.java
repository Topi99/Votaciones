package moviles.curso.app.topihernandez.com.cardview;

/**
 * Created by Topi on 26/05/2016.
 */
public class Usuario {
    long id;
    String nombre;
    String clave;
    String voto;

    Usuario(String nombre, String clave, String voto){
        this.nombre = nombre;
        this.clave = clave;
        this.voto = voto;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getClave(){
        return this.clave;
    }

    public String getVoto(){
        return this.voto;
    }

}
