package br.com.alsoftware.androidtoolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import br.com.alsoftware.androidtoolbar.util.ImageHelper;

/**
 * Created by AndreLuiz on 06/07/2015.
 */
public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.ViewHolder>  {
    private Context mContext;
    private List<Carro> mLista;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListener mRecyclerViewOnClickListener;
    private int mLaytoutId;
    private float mDensidadeDaTela;
    private int mLargura;
    private int mAltura;


    public CarroAdapter(Context context, List<Carro> lista){
        this(context, lista, -1);
    }

    public CarroAdapter(Context context, List<Carro> lista, int layoutId){
        mContext = context;
        mLista = lista;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLaytoutId = layoutId;
        mDensidadeDaTela = mContext.getResources().getDisplayMetrics().density;
        mLargura = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * mDensidadeDaTela + 0.5f);
        mAltura = (mLargura / 16) * 9;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Log.i("CarroAdapter", "onCreateViewHolder...");

        View view = null;
        if (mLaytoutId == -1)
            view = mLayoutInflater.inflate(R.layout.item_lista_carro, viewGroup, false);
        else
            view = mLayoutInflater.inflate(mLaytoutId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Log.i("CarroAdapter", "onBindViewHolder...");

        viewHolder.txtvNome.setText(mLista.get(i).getNome());
        viewHolder.txtvMarca.setText(mLista.get(i).getMarca());

        /*
        No exemplo do Thiengo as imagens tinhas seus cantos arredondados automaticamente a partir da
        API 21 do Android quando estavam no CardView, mas comigo mesmo na API 22 isso não aconteceu.
        Por esse motivo todas as imagens estão sendo processadas pela classe ImageHelper;
         */
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            ajustarImagemAoCardView(viewHolder);
        }
        else
            viewHolder.imgFoto.setImageResource(mLista.get(i).getFoto());*/

        //********  TESTAR A BIBLIOTECA ABAIXO PARA REALIZAR REDIMENTSIONAMENTO DE IMAGENS ********
        // https://github.com/pungrue26/SelectableRoundedImageView

        if (mLaytoutId == CarroFragmentList.CARD_LIST)
            ajustarImagemAoCardView(viewHolder, mLista.get(i).getFoto());
        else
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

    private void ajustarImagemAoCardView(ViewHolder viewHolder, int imgResource){
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), imgResource);
        bmp = Bitmap.createScaledBitmap(bmp, mLargura, mAltura, false);
        bmp = ImageHelper.getRoundedCornerBitmap(mContext, bmp, 10, mLargura, mAltura, false, false, true, true);
        viewHolder.imgFoto.setImageBitmap(bmp);
    }
}
