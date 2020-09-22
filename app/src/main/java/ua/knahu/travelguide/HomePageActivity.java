package ua.knahu.travelguide;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ua.knahu.travelguide.Database.DatabaseHelper;
import ua.knahu.travelguide.Fragments.ArticleEditFragment;
import ua.knahu.travelguide.Fragments.ArticleFragment;
import ua.knahu.travelguide.Fragments.IArticleEditFragment;
import ua.knahu.travelguide.Fragments.IMapFragment;
import ua.knahu.travelguide.Fragments.MapFragment;
import ua.knahu.travelguide.Views.IRibbonView;
import ua.knahu.travelguide.Views.RibbonView;

/**
 * Основной класс, отвечает за работу приложения.
 */
public class HomePageActivity extends AppCompatActivity implements IRibbonView, IMapFragment, IArticleEditFragment, IHomePageActivity {

    private RibbonView ribbonView;

    private ArrayList<Sightseeing> lSightseeings;

    private MapFragment mapFargment;
    private ArticleEditFragment articleEditFragment;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    /**
     * Срабатывает при активизации класса.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        ActivityCompat.requestPermissions(HomePageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(HomePageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(HomePageActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        lSightseeings = new ArrayList<>();
        dbHelper = new DatabaseHelper(this.getBaseContext());
        db = dbHelper.getWritableDatabase();
        dbHelper.getListFromDB(db,this.lSightseeings);

        ribbonView = (RibbonView)findViewById(R.id.main_ribbon); ribbonView.hideBackButton(); ribbonView.setTitle("Главная");
        ribbonView.setlSightseeing(this.lSightseeings);

        this.mapFargment = MapFragment.newInstance(this.lSightseeings);

        replaceFragment(this.mapFargment, false, false, "MAP_FRAGMENT");
    }

    /**
     * Производит смену фрагментов.
     *
     * @param fragment
     * @param addToBackStack
     * @param cleanBackStack
     * @param tag
     */
    private void replaceFragment(Fragment fragment, boolean addToBackStack, boolean cleanBackStack, String tag) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack)
            ft.addToBackStack(tag);

        if (cleanBackStack){
            for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                manager.popBackStack();
            }
        }

        ft.setCustomAnimations(R.anim.entering_from_right,R.anim.moving_to_right,R.anim.entering_from_right,R.anim.moving_to_right);

        ft.replace(R.id.main_fragment_container, fragment, tag).commit();
    }

    /**
     *
     */
    @Override
    public void backButtonClick() { onBackPressed(); }

    /**
     * Редактирует верхнюю строку при нажатии клавиши назад.
     */
    @Override
    public void ribbonMainPage() {
        ribbonView.hideBackButton();
        ribbonView.showSearchButton();
        ribbonView.setTitle("Главная");
        ribbonView.fillListView(lSightseeings);
        this.mapFargment.setlSightseeing(this.lSightseeings);
    }

    /**
     * Открывает локацию выбранную в списке-поиске.
     *
     * @param title
     */
    @Override
    public void listItemClicked(String title) {

        ribbonView.displaySearchLayout();
        ribbonView.showBackButton();
        ribbonView.hideSearchButton();
        ribbonView.setTitle(title);

        Sightseeing sightseeing = lSightseeings.get(searchForSightseeing(title));
        replaceFragment(ArticleFragment.newInstance(sightseeing),true,false,"ARTICLE_FRAGMENT");
    }

    /**
     * Поиск локации по её названию.
     * Позволяет найти локацию, когда она выбрана из списка-поиска.
     *
     * @param title
     * @return
     */
    public int searchForSightseeing (String title){
        for (int i = 0; i < lSightseeings.size(); i++)
        {
            if (lSightseeings.get(i).getTitle().equals(title))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Открывает для просмотра выбранную локацию.
     *
     * @param sightseeing
     */
    @Override
    public void openArticle(Sightseeing sightseeing) {

        ribbonView.showBackButton();
        ribbonView.hideSearchButton();
        if(sightseeing.getTitle()!= null)
            ribbonView.setTitle(sightseeing.getTitle());
        else
            ribbonView.setTitle("Статья");

        replaceFragment(ArticleFragment.newInstance(sightseeing),true,false,"ARTICLE_FRAGMENT");
    }

    /**
     * Открывает фрагмент для редактирования выбранной локации.
     *
     * @param sightseeing
     */
    @Override
    public void editArticle(Sightseeing sightseeing) {

        ribbonView.showBackButton();
        ribbonView.hideSearchButton();
        ribbonView.setTitle("Редактировать статью");
        articleEditFragment = ArticleEditFragment.newInstance(sightseeing);

        replaceFragment(articleEditFragment,true,false,"ARTICLE_EDIT_FRAGMENT");
    }

    /**
     * Удаляет локацию из списка и базы данных.
     *
     * @param id
     */
    @Override
    public void deleteArticle(Integer id) {

        lSightseeings.remove(id);
        dbHelper.deleteRecord(db,id);
        ribbonView.fillListView(lSightseeings);
    }

    /**
     * Открывает фрагмент для редактирования.
     * Будет создана новая локация с данными, полученными из этого фрагмента.
     *
     * @param latLng
     */
    @Override
    public void createSightseeing(LatLng latLng) {
        ribbonView.showBackButton();
        ribbonView.hideSearchButton();
        ribbonView.setTitle("Добавить статью");
        articleEditFragment = ArticleEditFragment.newInstance(latLng);

        replaceFragment(articleEditFragment,true,false,"ARTICLE_EDIT_FRAGMENT");
    }

    /**
     * Сохраняет отредактированную или новую статью.
     *
     * @param sightseeing
     */
    @Override
    public void ArticleSave(Sightseeing sightseeing) {

        if(sightseeing.getId() == -1) {
            sightseeing.setId(lSightseeings.size()+1);
            lSightseeings.add(sightseeing);
            dbHelper.addRecord(db,sightseeing);
        }
        else {
            try {
                lSightseeings.set(sightseeing.getId()-1, sightseeing);
                dbHelper.updateRecord(db, sightseeing);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        this.mapFargment = MapFragment.newInstance(this.lSightseeings);

        replaceFragment(this.mapFargment, false,true, "MAP_FRAGMENT");
    }

    /**
     * Получает фотографии из галереи.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);

            if (articleEditFragment != null && ActivityCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                String[] columns = {MediaStore.Images.Media.DATA};

                Uri imageURI = data.getData();

                Cursor cursor = getContentResolver().query(imageURI, columns, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(columns[0]);
                String imagePath = cursor.getString(columnIndex);

                cursor.close();

                articleEditFragment.setImagePath(imagePath);
            }
        }
    }
}
