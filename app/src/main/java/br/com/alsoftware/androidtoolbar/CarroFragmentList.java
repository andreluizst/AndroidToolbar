package br.com.alsoftware.androidtoolbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by AndreLuiz on 06/07/2015.
 */
public class CarroFragmentList extends Fragment {
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
        mRecyclerView.setAdapter(adapter);

        return view;
    }
}
