package ua.knahu.travelguide.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.knahu.travelguide.R;
import ua.knahu.travelguide.Sightseeing;

/**
 * Фрагмент, необходимый для демонстрации местоположения.
 */
public class ArticleFragment extends Fragment {


    private Sightseeing sightseeing;
    private ImageView imageView;
    private TextView textView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setSightseeing(Sightseeing sightseeing) { this.sightseeing = sightseeing;}

    /**
     * Конструктор.
     */
    public ArticleFragment() { }

    /**
     *
     * @param Sightseeing sightseeing.
     * @return A new instance of fragment MapFragment.
     */
    public static ArticleFragment newInstance(Sightseeing sightseeing) {
        ArticleFragment fragment = new ArticleFragment();
        fragment.setSightseeing(sightseeing);
        return fragment;
    }

    /**
     * Срабатывает при создании фрагмента.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article, container, false);

        imageView = (ImageView) view.findViewById(R.id.article_imageView);
        textView = (TextView) view.findViewById(R.id.article_textView_discription);

        fillArticle();

        return view;
    }

    /**
     * Добавляет в саттью фотографию и описание.
     *
     */
    public void fillArticle() {

        if (sightseeing.getPicture()!= null)
            imageView.setImageURI(sightseeing.getPicture());
        if (sightseeing.getAbout() != null)
            textView.setText(sightseeing.getAbout());
    }

}
