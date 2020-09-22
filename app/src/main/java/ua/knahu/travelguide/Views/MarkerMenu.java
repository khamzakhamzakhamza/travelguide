package ua.knahu.travelguide.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ua.knahu.travelguide.R;

/**
 * Контекстное меню для работы с локациями на карте.
 */
public class MarkerMenu extends RelativeLayout {

    private IMarkerMenu mListener;

    private ArrayList<String> MenuItems;

    private ListView listView;

    public MarkerMenu(Context context) {
        super(context);
        init();
    }

    public MarkerMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarkerMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MarkerMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * Добавляет интерфейс для дальнейшей работы с локациями.
     * @param mListener
     */
    public void setMListener(IMarkerMenu mListener) {
        this.mListener = mListener;
    }


    /**
     * Метод, который запускается при загрузке элемента.
     */
    private void init () {

        inflate(getContext(), R.layout.marker_menu, this);

        MenuItems = new ArrayList<>();MenuItems.add("Открыть");MenuItems.add("Редактировать");MenuItems.add("Удалить");

        listView = (ListView) findViewById(R.id.menu_ListView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, MenuItems);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                switch (position) {

                    case (0): { if (mListener != null){mListener.openArticle();}}break;

                    case (1): { if (mListener != null){mListener.editArticle();}}break;

                    case (2): { if (mListener != null){mListener.deleteSightseeing();}}break;

                }
            }
        });

        listView.setAdapter(adapter);

        listView.setVisibility(View.GONE);
    }

    /**
     * Скрывает контекстное меню.
     */
    public void hideMenu() {

        listView.setAlpha(1f);
        listView.setVisibility(View.GONE);

        listView.animate()
                .alpha(0f)
                .setDuration(getResources().getInteger(
                        android.R.integer.config_shortAnimTime))
                .setListener(null);

    }

    /**
     * Показывает контекстное меню.
     */
    public void showMenu() {

        listView.setAlpha(0f);
        listView.setVisibility(View.VISIBLE);

        listView.animate()
                .alpha(1f)
                .setDuration(getResources().getInteger(
                        android.R.integer.config_shortAnimTime))
                .setListener(null);
    }


}
