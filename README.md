# Chon
Get Sean (Chon) to finally pick something to watch while we wait for this pandemic to end society as we know it!

Chon is an Android (possibly iOS in the future) app developed with a single purpose in mind: spend less time scrolling through Netflix and more time indulging in self-destructive lifestyles. This app features several slices that can take up a wheel, and the wheel is spun to allow users to randomly decide on a place to eat, movie to watch, or TV show to blare in the background of playing Animal Crossing: New Horizons.

# Known Bugs
- The wheel does not appear on the canvas. This is intentional as it has not been fully implemented yet.
- The back button goes back to the most previously visited menu, rather than going down the desired hierarchy (Main -> Wheel Selection -> Wheel Editor).
- Putting the app in portrait mode distorts UI elements.
- The wheel canvas is not always 1:1 on some devices, causing the wheel to distort.


# Log

### 5/20/20
<img align="left" src="RMImages/1.PNG">
<p align="right">
For the initial v0.1 release of this app, I implemented the basic functionality of this app with a minimalistic UI. This version includes the ability create, save, load, and delete wheel objects to your Android device. The editor also validates input to make sure that there are no duplicate wheel names or wheel items within a wheel. Loading a wheel object in the main menu allows you to “roll” and get a random item within your wheel while playing a fun song in the background. There are still a lot of features to implement and bugs to fix, but I’ve managed to develop a decent base to work off of.
</p>

### 5/21/20
<img align="left" src="RMImages/2.PNG">
<p align="right">
For this version, I fixed a few bugs related to blank naming schemes for item and wheel name editors. In addition, I fixed a bug that caused a crash when all items are set to static. I also uploaded an OpenGL library and added a rendering context to the main menu. Eventually, this will render a spinning wheel representing the loaded wheel data.
</p>
