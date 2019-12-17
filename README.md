# GiphyAppDemo

1.  Present a user interface to allow a user to search for videos by keyword.
2.  Query the Giphy public API for a list of videos matching the user's search term: https://developers.giphy.com/docs/
The Giphy API returns links to image thumbnails and MP4 video files.
3. Display the thumbnails for found videos in a GridView (or similar) control to allow the user to select one video at a time to view.
4. Use the Exoplayer media player to play the MP4 video the user selects:  https://github.com/google/ExoPlayer
5. Add thumbs up/down buttons to the UI to allow the user to upvote/downvote a video.  
6. Store the user's selection in a local ObjectBox database: http://greenrobot.org/announcement/introducing-objectbox-beta/ 
7. Add a counter to the UI to display the total number of times the user clicked to upvote or downvote.

Most importantly, this project should utilize the following design patterns and frameworks:

Model-View-Presenter and Clean:  This must be the core architecture of the app.  
A sample project from Google to model:  https://github.com/googlesamples/android-architecture/tree/todo-mvp-clean

Some information on Clean Architecture:
https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html

<b>Kotlin :
https://kotlinlang.org/docs/tutorials/kotlin-android.html</b>

These core frameworks MUST be used to demonstrate knowledge of them:
Conductor (used for presenting Views):  
https://github.com/bluelinelabs/Conductor

RxJava/RxAndroid:
https://www.raywenderlich.com/141980/rxandroid-tutorial
https://github.com/ReactiveX/RxAndroid

Dagger (dependency injection):
http://square.github.io/dagger/

Butterknife (UI bindings):
http://jakewharton.github.io/butterknife/
