# Design Document

### Sketches
![sketches programmerproject](https://cloud.githubusercontent.com/assets/18394953/15800449/4157e3a6-2a7a-11e6-8762-942068a1fc63.jpg)
<br>
1: Beginscreen (Facebook Login Activity), whereby Facebook is implemented in the login<br>
2: When the user is succesfully logged in, the user can say from which station he is leaving and get the departure trains back, when clicking on one departure train, screen 3 will start.<br>
3: In this screen the user can confirm that he is really in this train by clicking on 'INCHECKEN'<br>
4: After the button is hit, the user can see in which trains his friends are sitting, when the user see there is a friend in the same train, a social interaction can be made by the button bottom left.<br>
### External sources
For this app, the Facebook SDK(Software Development Kit) is needed. The Facebook SDK for Android enables people to sign into your app with Facebook Login.Furthermore, the NS (Nederlandse Spoorwegen) API is needed to handle the main activity of the app, namely the possibility to 'check-in' at a train and see what Facebookfriends are in the same train.
The NS API works with basic authorization instead of a key before you can get access to the API. They give access to five different services of the NS. For this app, only two services are needed: the service that provide all train stations in the Netherlands and the service that provide all departure times and final destinations of every train station.
Unfortunately, the NS don't provide any stop-overs within the API, so only the train station where you depart from is known and the final destination of the train. However, the trip number of the trip is included, so based on the trip number comparisons with friends in the same train can be made even when they don't board at the same train station.

### Diagram
![diagram programmeerproject](https://cloud.githubusercontent.com/assets/18394953/15799812/a6d8850c-2a69-11e6-9107-4ad77e6da581.png)<br>
####Database:
When the time allows it, it is nice to implement a database in which the history of the check-ins of the user are stored. This will be a SQLite Database, with only local storage. Then the user can see the history of his check-ins even when there is no internet-access.
####(Helpers)-classes:
Facebook SDK:<br>
The Facebook SDK (Software Development Kit) allows the app to login with Facebook, the SDK is a helpers-class of the Activitiy FacebookLogin. The Facebook SDK has a lot of autoimplemented stuff to make the login relatively easy.<br><br>
AsyncTask:<br>
The AsyncTask handles retrieving data, it goes through 4 methods: onPreExecute, doinBackground, onProgressUpdate and onPostExecute, whereby for retrieving the right data, it goes through every method and ends with onPostExecute, whereby the data should be parsed right and get the real data you want.<br><br>
HttpRequestHelper:<br>
This Helperclass handles the Http-request. It reads the real request with own input like in this case the departure train station. It also reads the code that returns from the http-request.<br><br>
CheckInAdapter:<br>
To correctly show the output in the assigned ListView, a CheckInAdapter is needed. <br><br>
####Activities:
Facebook login: <br>
Very first screen of the app, the user has to login with their Facebookaccount. After once logged-in, the account will be stored.<br><br>
MainActivity:<br>
The MainActivity is the search for the trains leaving at your departure train station. Here the request to the NS APIs are necessary and therefore the AsyncTask and HttpRequestHelper came in handy. It is also needed to have an Adapter here, although it is not in the diagram. The parsed data must be displayed in the ListView in MainActivity.<br><br>
Check-in:<br>
When you have clicked on an item (a leaving train), you can check-in at this Activity. So, the item clicked on at MainActivity must be displayed here.<br><br>
Friends:<br>
When you have checked-in at a leaving train, you can here see in which trains your friends are. All of this is based on the Facebookaccounts and the friends of a facebookaccount.<br><br>
Settings:<br>
The settings don't need to have support by external sources. It are settings about the app itself, which are not really important for now.<br><br>
#### Navigation bar: <br>
The navigation bar is displayed at MainActivity, Settings and Friends. At every Activity you can go to the other Activities. This is nice for the user because it is easy to use and useful.
