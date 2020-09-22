package ua.knahu.travelguide.Fragments;

import com.google.android.gms.maps.model.LatLng;
import ua.knahu.travelguide.Sightseeing;

public interface IMapFragment {

        void openArticle(Sightseeing sightseeing);
        void editArticle(Sightseeing sightseeing);
        void deleteArticle(Integer id);
        void createSightseeing (LatLng latLng);
}
