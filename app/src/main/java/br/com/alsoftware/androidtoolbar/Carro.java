package br.com.alsoftware.androidtoolbar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AndreLuiz on 06/07/2015.
 */
public class Carro implements Parcelable {

    private String nome;
    private String marca;
    private int foto;
    private String descricao;
    private String fone;
    private int categoria;

    public Carro(){

    }

    public Carro(String nome, String marca, int foto){
        this(nome, marca, foto, null, null, 0);
    }

    public Carro(String nome, String marca, int foto, String descricao, String fone, int categoria){
        this.nome = nome;
        this.marca = marca;
        this.foto = foto;
        this.descricao = descricao;
        this.fone = fone;
        this.categoria = categoria;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    //*** Implementação do parcelable

    /* OBS sobre parcelable:
        Caso a classe tenha atributos que são objetos, as classes desses objetos também devem implementar
        a interface Parcelable.
        Para guardar o objeto no parcel deve-se chamar o método parcel.writeValue(objeto).
        Para recuperar o objeto deve-se chamar parcel.readValue(NomeDaClasse.class.getClassLoader());
        Caso exista alguna atributo de lista deve-se antes de recuperar a lista do parcel, iniciar
        o atributo de lista. Ex.:
            List<MeuObj> lista = new ArrayList<MeuObj>();
            parcel.readList(lista, MeuObj.class.getClassLoader());
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getNome());
        dest.writeString(getMarca());
        dest.writeInt(getFoto());
        dest.writeString(getDescricao());
        dest.writeString(getFone());
        dest.writeInt(getCategoria());
    }

    public Carro(Parcel parcel){
        setNome(parcel.readString());
        setMarca(parcel.readString());
        setFoto(parcel.readInt());
        setDescricao(parcel.readString());
        setFone(parcel.readString());
        setCategoria(parcel.readInt());
    }

    public static final Parcelable.Creator<Carro> CREATOR = new Parcelable.Creator<Carro>(){

        @Override
        public Carro createFromParcel(Parcel source) {
            return new Carro(source);
        }

        @Override
        public Carro[] newArray(int size) {
            return new Carro[size];
        }
    };
}
