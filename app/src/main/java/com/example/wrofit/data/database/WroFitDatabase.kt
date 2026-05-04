package com.example.wrofit.data.database

// Context jest potrzebny do stworzenia bazy i czytania plików assets.
import android.content.Context
// Adnotacja Database opisuje strukturę bazy Room.
import androidx.room.Database
// Room to builder tworzący instancję bazy.
import androidx.room.Room
// RoomDatabase to klasa bazowa wszystkich baz Room.
import androidx.room.RoomDatabase
// Importy DAO, które baza ma udostępniać.
import com.example.wrofit.data.dao.FoodDao
import com.example.wrofit.data.dao.GalleryImageDao
import com.example.wrofit.data.dao.TutorialVideoDao
// Importy encji opisujących tabele.
import com.example.wrofit.data.model.FoodEntry
import com.example.wrofit.data.model.GalleryImage
import com.example.wrofit.data.model.TutorialVideo
// CoroutineScope i Dispatchers służą do pracy w tle.
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
// JSONArray pozwala sparsować JSON z assets.
import org.json.JSONArray

// Ta adnotacja mówi Room:
// 1. jakie encje należą do bazy,
// 2. jaka jest wersja schematu,
// 3. czy eksportować schemat do plików.
@Database(
    entities = [FoodEntry::class, GalleryImage::class, TutorialVideo::class],
    version = 3,
    exportSchema = false
)
// Baza dziedziczy po RoomDatabase.
abstract class WroFitDatabase : RoomDatabase() {
    // Każda abstrakcyjna funkcja zwraca DAO dla odpowiedniej tabeli.
    abstract fun foodDao(): FoodDao
    abstract fun galleryImageDao(): GalleryImageDao
    abstract fun tutorialVideoDao(): TutorialVideoDao

    // companion object działa trochę jak sekcja statyczna w Javie.
    companion object {
        // @Volatile chroni pole przed problemami z wielowątkowością.
        @Volatile
        private var INSTANCE: WroFitDatabase? = null

        // To jest główna funkcja pobierająca instancję bazy.
        fun getDatabase(context: Context): WroFitDatabase {
            // Jeśli INSTANCE już istnieje, zwracamy ją od razu.
            // Jeśli nie istnieje, wchodzimy do synchronized i tworzymy bazę tylko raz.
            return INSTANCE ?: synchronized(this) {
                // databaseBuilder tworzy bazę o nazwie "wrofit_database".
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WroFitDatabase::class.java,
                    "wrofit_database"
                )
                    // fallbackToDestructiveMigration usuwa starą bazę przy niezgodności wersji.
                    // To jest wygodne w projekcie edukacyjnym, ale niebezpieczne dla produkcji.
                    .fallbackToDestructiveMigration()
                    .build()

                // Zapisujemy utworzoną instancję do singletona.
                INSTANCE = instance

                // Seedowanie uruchamiamy w tle, żeby nie blokować głównego wątku UI.
                CoroutineScope(Dispatchers.IO).launch {
                    // Pobieramy DAO potrzebne do wstępnego wypełnienia tabel.
                    val galleryDao = instance.galleryImageDao()
                    val tutorialVideoDao = instance.tutorialVideoDao()

                    // Jeśli tabela galerii jest pusta, wczytujemy dane z assets.
                    if (galleryDao.count() == 0) {
                        galleryDao.insertAll(loadDefaultGalleryImages(context))
                    }

                    // Jeśli tabela filmów jest pusta, wczytujemy dane z assets.
                    if (tutorialVideoDao.count() == 0) {
                        tutorialVideoDao.insertAll(loadDefaultTutorialVideos(context))
                    }
                }

                // Na końcu zwracamy gotową instancję bazy.
                instance
            }
        }

        // Pomocnicza funkcja czytająca domyślne obrazki galerii z pliku JSON.
        private fun loadDefaultGalleryImages(context: Context): List<GalleryImage> {
            // Otwieramy plik assets/gallery_images.json i czytamy cały tekst.
            val json = context.assets.open("gallery_images.json").bufferedReader().use { it.readText() }
            // Parsujemy tekst jako tablicę JSON.
            val array = JSONArray(json)
            // buildList tworzy listę wynikową obiektów GalleryImage.
            return buildList {
                // Iterujemy po wszystkich elementach tablicy JSON.
                for (index in 0 until array.length()) {
                    // Pobieramy pojedynczy obiekt JSON.
                    val item = array.getJSONObject(index)
                    // Dodajemy do listy nową encję GalleryImage.
                    add(
                        GalleryImage(
                            // title będzie pokazywany w UI.
                            title = item.getString("title"),
                            // drawableName ma odpowiadać nazwie pliku w res/drawable.
                            drawableName = item.getString("drawableName")
                        )
                    )
                }
            }
        }

        // Pomocnicza funkcja czytająca filmy instruktażowe z pliku JSON.
        private fun loadDefaultTutorialVideos(context: Context): List<TutorialVideo> {
            // Otwieramy plik assets/tutorial_videos.json.
            val json = context.assets.open("tutorial_videos.json").bufferedReader().use { it.readText() }
            // Zamieniamy tekst JSON na tablicę obiektów.
            val array = JSONArray(json)
            // Budujemy listę encji TutorialVideo.
            return buildList {
                // Przechodzimy po całej tablicy JSON.
                for (index in 0 until array.length()) {
                    // Pobieramy element na bieżącym indeksie.
                    val item = array.getJSONObject(index)
                    // Tworzymy encję i dodajemy ją do listy.
                    add(
                        TutorialVideo(
                            // id jest kluczem głównym tabeli.
                            id = item.getString("id"),
                            // title będzie wyświetlany w dialogu filmu.
                            title = item.getString("title"),
                            // resourceName ma odpowiadać nazwie pliku w res/raw.
                            resourceName = item.getString("resourceName")
                        )
                    )
                }
            }
        }
    }
}
