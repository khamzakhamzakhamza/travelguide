package ua.knahu.travelguide.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.util.ArrayList;

import ua.knahu.travelguide.IHomePageActivity;
import ua.knahu.travelguide.R;
import ua.knahu.travelguide.Sightseeing;
import ua.knahu.travelguide.Views.IMarkerMenu;
import ua.knahu.travelguide.Views.MarkerMenu;

/**
 * Фрагмент, отображающий карту, на которой находится локации.
 */
public class MapFragment extends Fragment  implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener , GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraMoveListener, IMarkerMenu {

    private Boolean cameraInMotion = false;
    private int tipVisibl = 0;

    private Marker chosenMarker = null;

    private Boolean createNewSightseeing = false;

    private MarkerMenu markerMenu;
    private LinearLayout tipContainer;
    private FloatingActionButton fab;

    private ArrayList<Sightseeing> lSightseeing;

    private  SupportMapFragment mapFragment;

    private IMapFragment mListener;
    private IHomePageActivity mainListtener;

    private Sightseeing chosenSightseeing = null;

    private View view = null;

    /**
     * Конструктор.
     */
    public MapFragment() { }

    /**
     * Конструктор котрый добавляет список с локациями.
     *
     * @param lSightseeing
     * @return
     */
    public static MapFragment newInstance(ArrayList<Sightseeing> lSightseeing) {
        MapFragment fragment = new MapFragment();
        fragment.setlSightseeing(lSightseeing);
        return fragment;
    }

    /**
     * Добавляет список локаций.
     * @param lSightseeing
     */
    public void setlSightseeing(ArrayList<Sightseeing> lSightseeing) {this.lSightseeing =lSightseeing;}

    /**
     * Срабатывает при создании фрагмента.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_map);

        if(mapFragment == null)
        {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

    }

    /**
     * Срабатывает при активации фрагмента.
     * Во время создания и при нажатии кнопки назад.
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
        try
        {
            mainListtener.ribbonMainPage();

            view = inflater.inflate(R.layout.fragment_map, container, false);
            tipContainer = (LinearLayout) view.findViewById(R.id.map_tip_container);
            tipContainer.setVisibility(View.GONE);
            markerMenu = (MarkerMenu) view.findViewById(R.id.map_menu);
            markerMenu.setMListener((IMarkerMenu)this);

            fab = (FloatingActionButton) view.findViewById(R.id.map_floatingActionButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {fabButtonClick();}
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return view;
    }

    /**
     * Обрабатывает нажатие на кнопку добавить.
     * Позволяет добавить новую локацию.
     *
     */
    public void fabButtonClick() {

        switch(tipVisibl) {

            case (0): {

                tipVisibl = 1;
                createNewSightseeing = true;

                tipContainer.setAlpha(0f);
                tipContainer.setVisibility(View.VISIBLE);

                tipContainer.animate()
                        .alpha(1f)
                        .setDuration(getResources().getInteger(
                                android.R.integer.config_shortAnimTime))
                        .setListener(null);
            } break;

            case(1): {

                tipVisibl = 0;
                createNewSightseeing = false;

                tipContainer.setAlpha(1f);
                tipContainer.setVisibility(View.GONE);

                tipContainer.animate()
                        .alpha(0f)
                        .setDuration(getResources().getInteger(
                                android.R.integer.config_shortAnimTime))
                        .setListener(null);

            } break;

        }

    }

    /**
     * Срабатывает при открытии фрагмента.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IMapFragment) {
            mListener = (IMapFragment) context;
        }
        if (context instanceof IHomePageActivity) {
            mainListtener = (IHomePageActivity) context;
        }
    }

    /**
     * Срабатывает при закрытии фрагмента.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mainListtener = null;
    }

    /**
     * Открывает фрагмент для просмотра статьи посвященной выбранной локации.
     */
    @Override
    public void openArticle() {
        if(mListener != null && chosenSightseeing != null)
            mListener.openArticle(chosenSightseeing);}

    /**
     * Открывает фрагмент для редактирования выбранной локации.
     */
    @Override
    public void editArticle() {
        if(mListener != null && chosenSightseeing != null)
            mListener.editArticle(chosenSightseeing);}

    /**
     * Удаляет выбранную локацию.
     */
    @Override
    public void deleteSightseeing() {

        int id = markerSearch(chosenMarker);
        if(lSightseeing.size() != 0)
            lSightseeing.remove(id);
        markerMenu.hideMenu();
        chosenMarker.remove();
        chosenMarker = null;
        mListener.deleteArticle(id);
    }

    /**
     * Метод, который срабатывает при загрузке карты.
     * Создает и отображает локации на карте.
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                    .getString(R.string.style_json)));

            if (lSightseeing.size() != 0) {

                for (Sightseeing s : lSightseeing) {

                    Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.landmark);

                    Marker marker = googleMap.addMarker(new MarkerOptions().position(s.getCoordinates()).icon(BitmapDescriptorFactory.fromBitmap
                            (Bitmap.createScaledBitmap(icon, 120, 120, false))).title(s.getTitle()));
                    marker.showInfoWindow();
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lSightseeing.get(lSightseeing.size() - 1).getCoordinates(), 13));
            }
            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnMapClickListener(this);
            googleMap.setOnCameraMoveListener(this);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Метод, который срабатывает при нажатии на локацию.
     * Отображает контекстное меню.
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        cameraInMotion = true;

        markerMenu.showMenu();

        if (markerSearch(marker)!=null)
        chosenSightseeing = lSightseeing.get(markerSearch(marker));

        chosenMarker = marker;

        cameraInMotion = false;
        return false;
    }

    /**
     * Поиск номера локации на карте.
     * Для возможности дальнейшей работы с выбраной локацией.
     *
     * @param marker
     * @return
     */
    private Integer markerSearch(Marker marker) {
        for (int i =0; i<lSightseeing.size(); i++)
        {
            if (lSightseeing.get(i).getTitle().equals(marker.getTitle()))
                return i;
        }
        return null;
    }

    /**
     * Метод, который срабатывает при нажатии на карты.
     * Закрывает контекстное меню.
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {

        markerMenu.hideMenu();
        if (createNewSightseeing)
        {
            mListener.createSightseeing(latLng);
        }
    }

    /**
     * Метод, который срабатывает при движении камеры.
     * Закрывает контекстное меню.
     *
     */
    @Override
    public void onCameraMove() {

        if(!cameraInMotion)
            markerMenu.hideMenu();
    }

}
