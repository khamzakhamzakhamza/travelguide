package ua.knahu.travelguide.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import ua.knahu.travelguide.R;
import ua.knahu.travelguide.Sightseeing;

/**
 * Фрагмент, нужный для редактирования локации.
 */
public class ArticleEditFragment extends Fragment {

    private EditText editTextTitle;
    private EditText editTextabout;
    private ImageView imageView;
    private Button saveButton;
    private ImageButton addPicture;
    private Uri image;
    private View view = null;

    private Sightseeing sightseeing;
    private LatLng latLng;

    private IArticleEditFragment mListener;

    public ArticleEditFragment() { }

    public void setLatLng(LatLng latLng) { this.latLng = latLng; }

    public void setSightseeing(Sightseeing sightseeing) { this.sightseeing = sightseeing; }

    /**
     *
     * @param sightseeing
     * @return
     */
    public static ArticleEditFragment newInstance(Sightseeing sightseeing) {
        ArticleEditFragment fragment = new ArticleEditFragment();
        fragment.setSightseeing(sightseeing);
        return fragment;
    }

    /**
     *
     * @param latLng
     * @return
     */
    public static ArticleEditFragment newInstance(LatLng latLng) {
        ArticleEditFragment fragment = new ArticleEditFragment();
        fragment.setLatLng(latLng);
        return fragment;
    }

    /**
     *
     * @param mListener
     */
    public void setMListener(IArticleEditFragment mListener) { this.mListener = mListener; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * При создании фрагмента.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null)
        {
            ViewGroup parent = (ViewGroup) container;
            if(parent !=null)
            {
                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.fragment_article_edit, container, false);

            editTextabout = (EditText) view.findViewById(R.id.edit_editTextAbout);
            editTextTitle = (EditText) view.findViewById(R.id.edit_editTextTitle);
            imageView = (ImageView) view.findViewById(R.id.edit_imageViewEdit);
            saveButton = (Button) view.findViewById(R.id.edit_saveButton);
            saveButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createSightseeing();
                }
            });
            addPicture = (ImageButton) view.findViewById(R.id.edit_addImageButton);
            addPicture.setOnClickListener(new ImageButton.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setImage();
                }
            });

            if (sightseeing != null)
                fillViews(sightseeing);
            else
                fillViews(latLng);
        } catch(Exception e){
            e.printStackTrace();
        }

        return view;
    }

    /**
     * Создает новую локацию.
     *
     */
    private void createSightseeing() {

        this.sightseeing.setTitle(editTextTitle.getText().toString());
        this.sightseeing.setAbout(editTextabout.getText().toString());
        if (image != null)
            this.sightseeing.setPicture(image);

        mListener.ArticleSave(this.sightseeing);
    }

    /**
     * Сохраняет данные про локацию.
     *
     * @param sightseeing
     */
    public void fillViews (Sightseeing sightseeing)
    {
        editTextTitle.setText(sightseeing.getTitle());
        editTextabout.setText(sightseeing.getAbout());
        imageView.setImageURI(sightseeing.getPicture());
    }

    /**
     * Сохраняет координаты локации.
     *
     * @param latLng
     */
    public void fillViews (LatLng latLng)
    {
        sightseeing = new Sightseeing();
        sightseeing.setCoordinates(latLng);
        sightseeing.setId(-1);
    }

    /**
     * Срабатывает, когда открывается фрагмент.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IArticleEditFragment) {
            mListener = (IArticleEditFragment) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Выбирает изображение из галереи.
     */
    private void setImage() {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
    }

    /**
     * Сохраняет адрес изображение.
     *
     * @param path
     */
    public void setImagePath(String path){
        if (path != null) {
            image = Uri.parse(path);
            imageView.setImageURI(image);
        }
    }
}
