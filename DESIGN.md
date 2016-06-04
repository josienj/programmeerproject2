# Design Document

### Sketches

### External sources
For this app, the Facebook SDK(Software Development Kit) is needed. The Facebook SDK for Android enables people to sign into your app with Facebook Login.Furthermore, the NS (Nederlandse Spoorwegen) API is needed to handle the main activity of the app, namely the possibility to 'check-in' at a train and see what Facebookfriends are in the same train.
The NS API works with basic authorization instead of a key before you can get access to the API. They give access to five different services of the NS. For this app, only two services are needed: the service that provide all train stations in the Netherlands and the service that provide all departure times and final destinations of every train station.
Unfortunately, the NS don't provide any stop-overs within the API, so only the train station where you depart from is known and the final destination of the train. However, the trip number of the trip is included, so based on the trip number comparisons with friends in the same train can be made even when they don't board at the same train station.

### Diagram
![diagram programmeerproject](https://cloud.githubusercontent.com/assets/18394953/15799812/a6d8850c-2a69-11e6-9107-4ad77e6da581.png)<br>
