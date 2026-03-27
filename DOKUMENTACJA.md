# Dokumentacja Techniczna Aplikacji WroFit

## 1. Cel projektu

WroFit to aplikacja mobilna na Androida wspierająca użytkownika w obszarach:

- odżywiania,
- snu,
- ćwiczeń,
- profilu użytkownika,
- materiałów instruktażowych w postaci filmu i galerii pozycji.

Projekt został zbudowany tak, aby spełniał wymagania:

- architektura `MVVM`,
- interfejs napisany w `Jetpack Compose`,
- komunikacja z wewnętrzną bazą danych `Room`,
- konfiguracja filmu i galerii nie jest hardcodowana w UI, lecz ładowana do Room z plików konfiguracyjnych `assets`.

## 2. Technologie użyte w projekcie

Projekt wykorzystuje:

- Kotlin,
- Android SDK,
- Jetpack Compose,
- Room,
- LiveData,
- ViewModel,
- KSP,
- standardowe komponenty Android do odtwarzania filmu:
  - `VideoView`,
  - `MediaController`,
  - `DatePickerDialog`,
  - `TimePickerDialog`.

Najważniejsza konfiguracja znajduje się w [app/build.gradle.kts](E:/Apka_mobilna/ApkaWroFit/app/build.gradle.kts).

## 3. Architektura aplikacji

Aplikacja wykorzystuje wzorzec `MVVM`.

### 3.1. Warstwa View

Warstwa widoku jest zbudowana w Jetpack Compose i znajduje się głównie w:

- [MainActivity.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/view/MainActivity.kt)

Ta warstwa:

- renderuje interfejs,
- obserwuje dane z `ViewModel`,
- reaguje na kliknięcia użytkownika,
- przekazuje akcje do `ViewModel`.

### 3.2. Warstwa ViewModel

Widoki są wspierane przez osobne klasy `ViewModel`:

- [HomeViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/HomeViewModel.kt)
- [NavigationViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/NavigationViewModel.kt)
- [FoodViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/FoodViewModel.kt)
- [SleepViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/SleepViewModel.kt)
- [ExerciseViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/ExerciseViewModel.kt)
- [ProfileViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/ProfileViewModel.kt)

### 3.3. Warstwa Repository

Repozytoria pośredniczą między `ViewModel` a `Room`.

- [FoodReposiroty.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/repository/FoodReposiroty.kt)
- [GalleryRepository.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/repository/GalleryRepository.kt)
- [TutorialVideoRepository.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/repository/TutorialVideoRepository.kt)

### 3.4. Warstwa Room

Encje:

- [FoodEntry.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/model/FoodEntry.kt)
- [GalleryImage.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/model/GalleryImage.kt)
- [TutorialVideo.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/model/TutorialVideo.kt)

DAO:

- [FoodDao.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/dao/FoodDao.kt)
- [GalleryImageDao.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/dao/GalleryImageDao.kt)
- [TutorialVideoDao.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/dao/TutorialVideoDao.kt)

Baza:

- [WroFitDatabase.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/database/WroFitDatabase.kt)

## 4. Struktura projektu

Najważniejsze katalogi:

- `app/src/main/java/com/example/wrofit/data`
- `app/src/main/java/com/example/wrofit/repository`
- `app/src/main/java/com/example/wrofit/ui/viewmodel`
- `app/src/main/java/com/example/wrofit/ui/view`
- `app/src/main/assets`
- `app/src/main/res/drawable`
- `app/src/main/res/raw`

## 5. Punkt wejścia do aplikacji

Punkt startowy aplikacji:

- [AndroidManifest.xml](E:/Apka_mobilna/ApkaWroFit/app/src/main/AndroidManifest.xml)

Główna aktywność:

- `com.example.wrofit.ui.view.MainActivity`

## 6. Nawigacja

Za nawigację odpowiada:

- [NavigationViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/NavigationViewModel.kt)
- [MainActivity.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/view/MainActivity.kt)

Zakładki:

- `0` Home
- `1` Food
- `2` Sleep
- `3` Exercise
- `4` Profile

## 7. Opis ekranów

### 7.1. Home

Ekran `Home`:

- pokazuje dwie karty poradnikowe,
- otwiera film instruktażowy,
- otwiera galerię pozycji.

Stan:

- [HomeViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/HomeViewModel.kt)

### 7.2. Food

Ekran `Food` zawiera:

- wybór daty,
- trzy rozwijane sekcje,
- 9 pól kcal,
- sumy sekcyjne i sumę całkowitą.

Stan:

- [FoodViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/FoodViewModel.kt)

### 7.3. Sleep

Ekran `Sleep`:

- data,
- godzina zaśnięcia,
- godzina pobudki,
- obliczenie długości snu,
- trudności.

Stan:

- [SleepViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/SleepViewModel.kt)

### 7.4. Exercise

Ekran `Exercise`:

- data treningu,
- lista ćwiczeń,
- zaznaczanie wykonanych ćwiczeń,
- reset.

Stan:

- [ExerciseViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/ExerciseViewModel.kt)

### 7.5. Profile

Ekran `Profile`:

- formularz danych użytkownika,
- dropdowny,
- podstawowa walidacja.

Stan:

- [ProfileViewModel.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/ui/viewmodel/ProfileViewModel.kt)

## 8. Film i galeria bez hardcodowania

Pliki konfiguracyjne:

- [gallery_images.json](E:/Apka_mobilna/ApkaWroFit/app/src/main/assets/gallery_images.json)
- [tutorial_videos.json](E:/Apka_mobilna/ApkaWroFit/app/src/main/assets/tutorial_videos.json)

Te pliki są ładowane do Room przy starcie bazy, jeśli odpowiednie tabele są puste.

## 9. Room

### 9.1. Tabela `food_table`

Encja:

- [FoodEntry.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/model/FoodEntry.kt)

DAO:

- [FoodDao.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/dao/FoodDao.kt)

### 9.2. Tabela `gallery_images`

Encja:

- [GalleryImage.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/model/GalleryImage.kt)

DAO:

- [GalleryImageDao.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/dao/GalleryImageDao.kt)

### 9.3. Tabela `tutorial_videos`

Encja:

- [TutorialVideo.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/model/TutorialVideo.kt)

DAO:

- [TutorialVideoDao.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/dao/TutorialVideoDao.kt)

### 9.4. Klasa bazy

Plik:

- [WroFitDatabase.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/data/database/WroFitDatabase.kt)

Odpowiada za:

- utworzenie i utrzymanie singletona bazy,
- rejestrację encji,
- udostępnienie DAO,
- seed danych z `assets`.

## 10. Repozytoria

- [FoodReposiroty.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/repository/FoodReposiroty.kt)
- [GalleryRepository.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/repository/GalleryRepository.kt)
- [TutorialVideoRepository.kt](E:/Apka_mobilna/ApkaWroFit/app/src/main/java/com/example/wrofit/repository/TutorialVideoRepository.kt)

## 11. Przepływ danych

Schemat:

`Compose UI -> ViewModel -> Repository -> DAO -> Room`

### Dla galerii

1. `HomeScreen` otwiera galerię.
2. `PositionsGalleryDialog` obserwuje `galleryImages`.
3. `HomeViewModel` pobiera dane z `GalleryRepository`.
4. `GalleryRepository` pobiera dane z `GalleryImageDao`.
5. `GalleryImageDao` czyta rekordy z Room.
6. UI renderuje zdjęcia.

### Dla filmu

1. `HomeScreen` otwiera film.
2. `TutorialVideoDialog` obserwuje `tutorialVideo`.
3. `HomeViewModel` pobiera dane z `TutorialVideoRepository`.
4. `TutorialVideoRepository` pobiera rekord filmu z `TutorialVideoDao`.
5. UI zamienia `resourceName` na zasób w `res/raw` i odtwarza plik.


## 12. Podsumowanie

WroFit jest aplikacją Android opartą o `MVVM`, `Jetpack Compose` i `Room`. Dane o galerii i filmie nie są zaszyte w UI, lecz ładowane z plików `assets` do bazy Room, a następnie odczytywane przez `ViewModel` i renderowane przez Compose.
