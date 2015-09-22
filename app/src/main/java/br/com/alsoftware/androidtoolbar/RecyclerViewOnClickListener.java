package br.com.alsoftware.androidtoolbar;

import android.view.View;

/**
 * Created by AndreLuiz on 08/07/2015.
 */
public interface RecyclerViewOnClickListener {
    public void onClickRecyclerView(View view, int position);
    public void onLongClickRecyclerView(View view, int position);
}
