package br.com.alsoftware.androidtoolbar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Inflater;

/**
 * Created by AndreLuiz on 06/07/2015.
 */
public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.ViewHolder>  {
    private List<Carro> mLista;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListener mRecyclerViewOnClickListener;


    public CarroAdapter(Context context, List<Carro> lista){
        mLista = lista;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.item_lista_carro, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txtvNome.setText(mLista.get(i).getNome());
        viewHolder.txtvMarca.setText(mLista.get(i).getMarca());
        viewHolder.imgFoto.setImageResource(mLista.get(i).getFoto());

        try{
            YoYo.with(Techniques.Tada).duration(700).playOn(viewHolder.itemView);
        }catch(Exception e){
            Logger.getLogger(CarroAdapter.class.getName()).log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public void setLista(List<Carro> lista){
        mLista = lista;
    }

    public void adicionarItem(Carro carro, int posicao){
        if (posicao == mLista.size())
            mLista.add(carro);
        else
            mLista.add(posicao, carro);
        notifyItemInserted(posicao);
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener recyclerViewOnClickListener){
        this.mRecyclerViewOnClickListener = recyclerViewOnClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtvNome;
        public TextView txtvMarca;
        public ImageView imgFoto;


        public ViewHolder(View view){
            super(view);

            txtvNome = (TextView)view.findViewById(R.id.txtvNome);
            txtvMarca = (TextView)view.findViewById(R.id.txtvMarca);
            imgFoto = (ImageView)view.findViewById(R.id.imgFotoCarro);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListener != null){
                mRecyclerViewOnClickListener.onClick(v, getAdapterPosition());
            }
        }
    }
}
