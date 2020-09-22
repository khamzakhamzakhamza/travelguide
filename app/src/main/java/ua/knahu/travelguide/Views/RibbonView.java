package ua.knahu.travelguide.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.knahu.travelguide.R;
import ua.knahu.travelguide.Sightseeing;

/**
 * Элемент, верхняя строка с поиском и навигацией.
 */
public class RibbonView extends RelativeLayout {


    private ArrayList<Sightseeing> lSightseeing;

    private SearchView SearchView;
    private LinearLayout SearchLayout;
    private ListView ListView;
    private int SearchLayoutVisibl = 0;
    private ImageButton SearchButton;
    private ImageButton BackButton;
    private TextView Title;

    private IRibbonView mListener;

    public RibbonView(Context context) {
        super(context);
        if (context instanceof IRibbonView) {
            mListener = (IRibbonView) context;}
        init();
    }

    public RibbonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof IRibbonView) {
            mListener = (IRibbonView) context;}
        init();
    }

    public RibbonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context instanceof IRibbonView) {
            mListener = (IRibbonView) context;}
        init();
    }

    public RibbonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (context instanceof IRibbonView) {
            mListener = (IRibbonView) context;}
        init();
    }


    /**
     * Метод, который запускается при загрузке элемента.
     */
    private void init () {

        inflate(getContext(), R.layout.upper_ribbon, this);

        ListView = (ListView) findViewById(R.id.ribbon_listView);
        Title = (TextView) findViewById(R.id.ribbon_title);
        SearchLayout = (LinearLayout) findViewById(R.id.ribbon_searchLayout);
        SearchButton = (ImageButton) findViewById(R.id.ribbon_searchButton);
        BackButton = (ImageButton) findViewById(R.id.ribbon_backButton);
        SearchView = (SearchView) findViewById(R.id.ribbon_searchView);


        BackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { if (mListener != null){mListener.backButtonClick();} } });
        SearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {searchButtonClick(v);} });

        SearchLayout.setVisibility(View.GONE);
    }

    private void composeList() {

    }

    /**
     * Метод, позволяющий указать название страницы.
     *
     * @param title
     */
    public void setTitle(String title) {Title.setText(title);}


    /**
     *
     * Методы, которые показывают или скрывают кнопки управления приложением.
     */
    public void showBackButton() { BackButton.setVisibility(View.VISIBLE);}

    public void hideBackButton() { BackButton.setVisibility(View.INVISIBLE);}

    public void showSearchButton() { SearchButton.setVisibility(View.VISIBLE);}

    public void hideSearchButton() { SearchButton.setVisibility(View.INVISIBLE);}


    /**
     * Метод, который срабатывает при нажатии на кнопку поиска.
     *
     * Отображение списка и поиска в этом списке сохраненных достопримечательностей.
     *
     * @param v
     */
    private void searchButtonClick(View v) {
        displaySearchLayout();
    }


    public void displaySearchLayout() {
        switch(SearchLayoutVisibl) {

            case (0): {

                SearchLayoutVisibl = 1;

                SearchLayout.setAlpha(0f);
                SearchLayout.setVisibility(View.VISIBLE);

                SearchLayout.animate()
                        .alpha(1f)
                        .setDuration(getResources().getInteger(
                                android.R.integer.config_shortAnimTime))
                        .setListener(null);
            } break;

            case(1): {

                SearchLayoutVisibl = 0;

                SearchLayout.setAlpha(1f);
                SearchLayout.setVisibility(View.GONE);

                SearchLayout.animate()
                        .alpha(0f)
                        .setDuration(getResources().getInteger(
                                android.R.integer.config_shortAnimTime))
                        .setListener(null);

            } break;

        }
    }

    /**
     * Метод, позволяющий добавить список достопримечательностей.
     *
     * @param lSightseeing
     */
    public void setlSightseeing(ArrayList<Sightseeing> lSightseeing) {
        this.lSightseeing = lSightseeing;
        fillListView(lSightseeing);
    }

    /**
     * Метод, добовляющий достопримечательностей в список для поиска.
     *
     */
    public void fillListView(ArrayList<Sightseeing>sightseeingList) {

        this.lSightseeing = sightseeingList;

        String [] aSightseeing = new String [lSightseeing.size()];

        for (int i = 0; i <aSightseeing.length; i++)
            aSightseeing[i] = lSightseeing.get(i).getTitle();

        final ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, aSightseeing);

        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

             if (mListener != null) {  mListener.listItemClicked(ListView.getItemAtPosition(position).toString());};

            }
        });

        SearchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }

}
