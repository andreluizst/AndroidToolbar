package br.com.alsoftware.androidtoolbar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndreLuiz on 06/07/2015.
 */
public class CarroFragmentList extends Fragment implements RecyclerViewOnClickListener, View.OnClickListener {
    public static final int DEFAULT_LIST = R.layout.item_lista_carro;
    public static final int CARD_LIST = R.layout.item_lista_carro_card_view;
    private static final String LAYOUT_LIST_EXTRA = "layoutListExtra";

    private RecyclerView mRecyclerView;
    private List<Carro> mLista;
    private LinearLayoutManager mLinearLayoutManager;
    private int mLayoutList;
    private ActionButton mFlatActionButtonCard = null;
    private FloatingActionMenu mFloatActionButtonMain = null;
    private int mCategoria;



    public CarroFragmentList(){
        mLayoutList = DEFAULT_LIST;
    }

    public static CarroFragmentList buildWithLayout(int layoutList){

        Log.i("CarroFragmentList", "buildWithLayout...");

        return buildWithParams(layoutList, null, -1);
    }

    public static CarroFragmentList buildWithParams(int layoutList, List<Carro> lista, int categoria){

        Log.w("CarroFragmentList", "buildWithParams...");

        CarroFragmentList fragment = new CarroFragmentList();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_LIST_EXTRA, layoutList);
        if (categoria >= 0)
            bundle.putInt("categoria", categoria);
        if (lista != null)
            bundle.putParcelableArrayList(CarroActivity.LISTA_CARROS_ESPORTIVOS_EXTRA, (ArrayList<Carro>)lista);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("CarroFragmentList", "onCreateView...");

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

                // qdo o está rolando para baixo, ou seja, os itens da lista estão subindo
                //dy possuin um valor positivo (>0)
                if (dy > 0) {
                    if (mFlatActionButtonCard != null)
                        mFlatActionButtonCard.hide();
                    if (mFloatActionButtonMain != null)
                        mFloatActionButtonMain.hideMenuButton(true);
                }
                else {
                    if (mFlatActionButtonCard != null)
                        mFlatActionButtonCard.show();
                    if (mFloatActionButtonMain != null)
                        mFloatActionButtonMain.showMenuButton(true);
                }

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                CarroAdapter adapter = (CarroAdapter)recyclerView.getAdapter();
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == mLista.size()-1){
                    List<Carro> listaAux = MainActivity.getListaDeCarrosCom(10, mCategoria);
                    for (int i=0;i<listaAux.size();i++){
                        adapter.adicionarItem(listaAux.get(i), mLista.size());
                    }
                }
            }
        });


        if (getArguments() != null) {
            mCategoria = getArguments().getInt("categoria", 0);
            mLista = getArguments().getParcelableArrayList(CarroActivity.LISTA_CARROS_ESPORTIVOS_EXTRA);
            mLayoutList = getArguments().getInt(LAYOUT_LIST_EXTRA, DEFAULT_LIST);
            Log.i("CarroFragmentList", "getArguments() != null...");
        }else {
            if (savedInstanceState != null) {
                mLayoutList = savedInstanceState.getInt(LAYOUT_LIST_EXTRA);
                mLista = savedInstanceState.getParcelableArrayList(CarroActivity.LISTA_CARROS_ESPORTIVOS_EXTRA);
                mCategoria = savedInstanceState.getInt("categoria");
            }
            else {
                mLayoutList = DEFAULT_LIST;
                mLista = MainActivity.getListaDeCarrosCom(10, 0);
            }
        }
        CarroAdapter adapter = new CarroAdapter(getActivity(), mLista, mLayoutList);

        //adapter.setRecyclerViewOnClickListener(this);
        //O método abaixo exemplifica uma outra forma de chamar OnClick e OnLongClick, é mais
        //trabalhosa e por isso é melhor usar quando precisar realmente trabalhar com TOQUE na tela
        //para arrastar views ou pegar as coordenadas X e Y da tela.
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        mRecyclerView.setAdapter(adapter);

        configActionButton();

        return view;
    }

    @Override
    public void onClick(View view, int position) {
        Log.w("RecyclerView", "onCLick...");
        Toast.makeText(getActivity(), "onClick na posição " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(View view, int position) {
        Toast.makeText(getActivity(), "onLongClick no item " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        String msg = "Floating Button clicked";
        switch (v.getId()){
            case R.id.fab1:
                msg = "Floating button 1 pressed";
                break;
            case R.id.fab2:
                msg = "Floating button 2 pressed";
                break;
            case R.id.fab3:
                msg = "Floating button 3 pressed";
                break;
            case R.id.fab4:
                msg = "Floating button 4 pressed";
                break;
            case R.id.fab5:
                msg = "Floating button 5 pressed";
                break;
        }
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
                    if (viewFilha != null && !(viewFilha instanceof FloatingActionMenu || viewFilha instanceof FloatingActionMenu)){
                        if (mFloatActionButtonMain != null && mFloatActionButtonMain.isOpened()) {
                            mFloatActionButtonMain.close(true);
                            return true;
                        }
                    }
                    Log.w("RecyclerView", "onSingleTapUp...");
                    return false;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAYOUT_LIST_EXTRA, mLayoutList);
        outState.putInt("categoria", mCategoria);
        outState.putParcelableArrayList(CarroActivity.LISTA_CARROS_ESPORTIVOS_EXTRA, (ArrayList<Carro>)mLista);
    }

    private void configActionButton(){
        if (mLayoutList == CARD_LIST){
            mFlatActionButtonCard = (ActionButton)getActivity().findViewById(R.id.actionFlatButton);
            mFlatActionButtonCard.setShowAnimation(ActionButton.Animations.SCALE_UP);
            mFlatActionButtonCard.setHideAnimation(ActionButton.Animations.SCALE_DOWN);
            mFlatActionButtonCard.playShowAnimation();
            mFlatActionButtonCard.setButtonColor(getResources().getColor(R.color.blue));
            mFlatActionButtonCard.setButtonColorPressed(getResources().getColor(R.color.blue_deep));
            mFlatActionButtonCard.setShadowColor(getResources().getColor(R.color.blue_pool));
        /*
        Conversão de DP para pixels explicada em http://developer.android.com/guide/practices/screens_support.html
         */
            float escala = getActivity().getResources().getDisplayMetrics().density;
            int dp = 3;
            int sombraEmPixel = (int)(dp * escala + 0.5f);
            mFlatActionButtonCard.setShadowRadius(sombraEmPixel); // referente ao tamnho da sombra. Sobrescreve o efeito do elevation padrão
            mFlatActionButtonCard.setOnClickListener(this);
        }else{
            mFloatActionButtonMain = (FloatingActionMenu)getActivity().findViewById(R.id.fab);
            mFloatActionButtonMain.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
                @Override
                public void onMenuToggle(boolean b) {
                    Toast.makeText(getActivity(), "FloatingActionMenu.onMenuToggle", Toast.LENGTH_SHORT).show();
                }
            });
            mFloatActionButtonMain.setClosedOnTouchOutside(true);
            ((FloatingActionButton)getActivity().findViewById(R.id.fab1)).setOnClickListener(this);
            ((FloatingActionButton)getActivity().findViewById(R.id.fab2)).setOnClickListener(this);
            ((FloatingActionButton)getActivity().findViewById(R.id.fab3)).setOnClickListener(this);
            ((FloatingActionButton)getActivity().findViewById(R.id.fab4)).setOnClickListener(this);
            ((FloatingActionButton)getActivity().findViewById(R.id.fab5)).setOnClickListener(this);
        }
    }

}
