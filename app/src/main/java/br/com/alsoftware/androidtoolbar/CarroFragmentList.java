package br.com.alsoftware.androidtoolbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListenerAdapter;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

import br.com.alsoftware.androidtoolbar.adapters.CarroAdapter;
import br.com.alsoftware.androidtoolbar.util.Conexao;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by AndreLuiz on 06/07/2015.
 */
public class CarroFragmentList extends Fragment implements RecyclerViewOnClickListener,
        View.OnClickListener, View.OnLongClickListener {
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
    private CarroAdapter mAdapter;
    private Button mBtnFone;
    private MaterialDialog mMaterialDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;



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

        final View view = inflater.inflate(R.layout.fragment_list_carro, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rvCarros);
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setClickable(true);
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
            if (mLista == null)
                mLista = MainActivity.getListaDeCarrosCom(10, 0);
            mLayoutList = getArguments().getInt(LAYOUT_LIST_EXTRA, DEFAULT_LIST);
            Log.i("CarroFragmentList", "getArguments() != null...");
        }else {
            if (savedInstanceState != null) {
                mLayoutList = savedInstanceState.getInt(LAYOUT_LIST_EXTRA);
                mLista = savedInstanceState.getParcelableArrayList(CarroActivity.LISTA_CARROS_ESPORTIVOS_EXTRA);
                mCategoria = savedInstanceState.getInt("categoria", 0);
            }
            else {
                mLayoutList = DEFAULT_LIST;
                mLista = MainActivity.getListaDeCarrosCom(10, 0);
            }
        }
        mAdapter = new CarroAdapter(getActivity(), mLista, mLayoutList);

        mAdapter.setRecyclerViewOnClickListener(this);
        //O método abaixo exemplifica uma outra forma de chamar OnClick e OnLongClick, é mais
        //trabalhosa e por isso é melhor usar quando precisar realmente trabalhar com TOQUE na tela
        //para arrastar views ou pegar as coordenadas X e Y da tela.
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        mRecyclerView.setAdapter(mAdapter);

        configActionButton();

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeFragmentList);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Conexao.estaAtiva(getActivity())) {
                    CarroAdapter adapter = (CarroAdapter) mRecyclerView.getAdapter();
                    List<Carro> listaAux = MainActivity.getListaDeCarrosCom(3, mCategoria);
                    for (int i = 0; i < listaAux.size(); i++) {
                        adapter.adicionarItem(listaAux.get(i), 0);
                        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, 0);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(2000);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }).start();
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                    SnackbarManager.show(Snackbar.with(getActivity())
                                    .text("Sem conexão com a internet. Favor verificar o WiFi ou 3G!")
                                    .type(SnackbarType.MULTI_LINE)
                                    .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                                    .color(getActivity().getResources().getColor(R.color.cian))
                                    .textColor(getActivity().getResources().getColor(R.color.white))
                                    .actionLabel("Conectar")
                                    .actionColor(getActivity().getResources().getColor(R.color.lime))
                                    .actionListener(new ActionClickListener() {
                                        @Override
                                        public void onActionClicked(Snackbar snackbar) {
                                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                            startActivity(intent);
                                        }
                                    })
                                    .eventListener(new EventListenerAdapter() {
                                        @Override
                                        public void onShow(Snackbar snackbar) {
                                            super.onShow(snackbar);
                                            if (mFloatActionButtonMain != null) {
                                                ObjectAnimator.ofFloat(mFloatActionButtonMain,
                                                        "translationY", -snackbar.getHeight()).start();
                                            } else {
                                                if (mFlatActionButtonCard != null) {
                                                    ObjectAnimator.ofFloat(mFlatActionButtonCard,
                                                            "translationY", snackbar.getHeight()).start();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onDismiss(Snackbar snackbar) {
                                            super.onDismiss(snackbar);
                                            if (mFloatActionButtonMain != null) {
                                                ObjectAnimator.ofFloat(mFloatActionButtonMain, "translationY", 0).start();
                                            } else {
                                                if (mFlatActionButtonCard != null) {
                                                    ObjectAnimator.ofFloat(mFlatActionButtonCard, "translationY", 0).start();
                                                }
                                            }
                                        }
                                    }), (ViewGroup)view);
                }
            }
        });

        return view;
    }

    @Override
    public void onClickRecyclerView(View view, int position) {
        Log.w("RecyclerView", "onClickRecyclerView...");
        if (view.getId() == R.id.btnFone) {
            //Toast.makeText(getActivity(), "Telefone: 81 999-888-777 ", Toast.LENGTH_SHORT).show();
            mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(getActivity(), R.style.AppTheme_MaterialDialog))
                    .setTitle("MaterialDialog - Carro")
                    //.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.vyron))
                    .setMessage("Telefone teste: 81 999-888-777")
                    .setPositiveButton("Ok", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(Intent.ACTION_CALL);
                            it.setData(Uri.parse("tel:" + "81999888777")); // pegar o telefone de um obj carro
                            startActivity(it);
                        }
                    })
                    .setNegativeButton("Cancelar", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                             mMaterialDialog.dismiss();
                        }
                    });
            mMaterialDialog.show();
        }
        else
            Toast.makeText(getActivity(), "onClick na posição " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClickRecyclerView(View view, int position) {
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
            case R.id.btnFone:
                msg = "Botão para mostrar o telefone";
                break;
        }
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
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
                private boolean mLongClickDisparado = false;
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View viewFilha = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    /*if (viewFilha != null && mRecyclerViewOnClickListener != null){
                        mRecyclerViewOnClickListener.onClickRecyclerView(viewFilha, recyclerView.getChildAdapterPosition(viewFilha));
                    }*/
                    if (viewFilha != null && !(viewFilha instanceof FloatingActionMenu || viewFilha instanceof FloatingActionMenu)){
                        if (mFloatActionButtonMain != null && mFloatActionButtonMain.isOpened()) {

                            Log.w("RecyclerView", "onSingleTapUp...fechando FloatingActionMenu...");

                            mFloatActionButtonMain.close(true);
                            return true;
                        }
                    }
                       /* if (viewFilha != null && mRecyclerViewOnClickListener != null){
                            if (viewFilha.getId() == R.id.btnFone) {
                                Log.w(" --> onSingleTapUp", "viewFilha == R.id.btnFone");
                                return false;
                            }else {
                                mRecyclerViewOnClickListener.onClickRecyclerView(viewFilha, recyclerView.getChildAdapterPosition(viewFilha));
                                return true;
                            }
                        }*/

                    /*if (viewFilha != null && viewFilha.getId() == R.id.btnFone) {
                        Toast.makeText(getActivity(), "Botão TELEFONE", Toast.LENGTH_SHORT).show();
                    }
                    if (viewFilha != null && viewFilha instanceof TextView){
                        Toast.makeText(getActivity(), ((TextView)viewFilha).getText(), Toast.LENGTH_SHORT).show();
                    }*/
                    Log.w(" --> RecyclerView", "onSingleTapUp...");
                    return mLongClickDisparado;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    //super.onLongPress(e);
                    /*mLongClickDisparado = true;
                    View viewFilha = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (viewFilha != null && mRecyclerViewOnClickListener != null){
                        mRecyclerViewOnClickListener.onLongClickRecyclerView(viewFilha, recyclerView.getChildAdapterPosition(viewFilha));
                    }*/

                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            //mGestureDetector.onTouchEvent(e);
            return mGestureDetector.onTouchEvent(e);
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
        mFlatActionButtonCard = (ActionButton)getActivity().findViewById(R.id.actionFlatButton);
        if (mLayoutList == CARD_LIST && mFlatActionButtonCard != null){
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
            mFloatActionButtonMain = (FloatingActionMenu) getActivity().findViewById(R.id.fab);
            if (mFloatActionButtonMain != null) {
                mFloatActionButtonMain.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
                    @Override
                    public void onMenuToggle(boolean b) {
                        Toast.makeText(getActivity(), "FloatingActionMenu.onMenuToggle", Toast.LENGTH_SHORT).show();
                    }
                });
                mFloatActionButtonMain.setClosedOnTouchOutside(true);
                ((FloatingActionButton) getActivity().findViewById(R.id.fab1)).setOnClickListener(this);
                ((FloatingActionButton) getActivity().findViewById(R.id.fab2)).setOnClickListener(this);
                ((FloatingActionButton) getActivity().findViewById(R.id.fab3)).setOnClickListener(this);
                ((FloatingActionButton) getActivity().findViewById(R.id.fab4)).setOnClickListener(this);
                ((FloatingActionButton) getActivity().findViewById(R.id.fab5)).setOnClickListener(this);
            }
        }
    }

}
