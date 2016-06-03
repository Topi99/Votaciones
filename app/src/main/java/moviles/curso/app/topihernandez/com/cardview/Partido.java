package moviles.curso.app.topihernandez.com.cardview;

/**
 * Created by Topi on 25/05/2016.
 */
public class Partido {
    long id;
    String nombre;
    String candidato;
    String imgid;
    int votos;
    String color;

    Partido (String nombre, String candidato, String imgid, int votos, String color){
        this.nombre = nombre;
        this.candidato = candidato;
        this.imgid = imgid;
        this.votos = votos;
        this.color = color;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getCandidato(){
        return this.candidato;
    }

    public String getImgid(){
        return this.imgid;
    }

    public int getVotos(){
        return this.votos;
    }

    public String getColor(){
        return this.color;
    }

}
