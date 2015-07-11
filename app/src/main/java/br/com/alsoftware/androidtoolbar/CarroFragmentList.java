package br.com.alsoftware.androidtoolbar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

/**
 * Created by AndreLuiz on 06/07/2015.
 */
public class CarroFragmentList extends Fragment implements RecyclerViewOnClickListener {
    private RecyclerView mRecyclerView;
    private List<Carro> mLista;
    private LinearLayoutManager mLinearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_carro, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rvCarros);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                CarroAdapter adapter = (CarroAdapter)recyclerView.getAdapter();
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == mLista.size()-1){
                    List<Carro> listaAux = ((MainActivity)getActivity()).getListaDeCarrosCom(10);
                    for (int i=0;i<listaAux.size();i++){
                        adapter.adicionarItem(listaAux.get(i), mLista.size());
                    }
                }
            }
        });

        mLista = ((MainActivity)getActivity()).getListaDeCarrosCom(10);
        CarroAdapter adapter = new CarroAdapter(getActivity(), mLista);

        //adapter.setRecyclerViewOnClickListener(this);
        //O método abaixo exemplifica uma outra forma de chamar OnClick e OnLongClick, é mais
        //trabalhosa e por isso é melhor usar quando precisar realmente trabalhar com TOQUE na tela
        //para arrastar views ou pegar as coordenadas X e Y da tela.
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        mRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(getActivity(), "onClick na posição " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(View view, int position) {
        Toast.makeText(getActivity(), "onLongClick no item " + position, Toast.LENGTH_SHORT).show();
    }

    public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListener mRecyclerViewOnClickListener;

        public RecyclerViewTouchListener(Context context, final RecyclerView recyclerView,
                                         RecyclerViewOnClickListener recyclerViewOnClickListener){
            mContext = context;
            mRecyclerViewOnClickListener = recyclerViewOnClickListener;
            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View viewFilha = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (viewFilha != null && mRecyclerViewOnClickListener != null){
                        mRecyclerViewOnClickListener.onClick(viewFilha, recyclerView.getChildAdapterPosition(viewFilha));
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View viewFilha = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (viewFilha != null && mRecyclerViewOnClickListener != null){
                        mRecyclerViewOnClickListener.onLongClick(viewFilha, recyclerView.getChildAdapterPosition(viewFilha));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
