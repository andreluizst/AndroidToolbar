package br.com.alsoftware.androidtoolbar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by AndreLuiz on 06/07/2015.
 */
public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.ViewHolder>  {
    private List<Carro> mLista;
    private LayoutInflater mLayoutInflater;

    public CarroAdapter(Context context, List<Carro> lista){
        mLista = lista;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public void setLista(List<Carro> lista){
        mLista = lista;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtvNome;
        public TextView txtvMarca;
        public ImageView imgFoto;


        public ViewHolder(View view){
            super(view);

            //txtvNome = (TextView)view.findViewById(//id do textView na view item_carro_lista)
        }
    }
}
