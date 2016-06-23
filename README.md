# Programmeerproject
## Josien Jansen

###PROPOSAL
Many people, especially a lot of students, use the train on a daily basis, or at least every weekend to go from their student room to their parents’ home. Many students have to travel the same route, but don’t see or speak to each other on a daily basis. Regularly, friends see each other at their final destination and wanted that they know earlier in what train their friends were. So, here is the solution: an app, where you can log in with Facebook, and ‘check-in’ at the trains you will take. When your friends do that too, you can easily see that you are in the same train and find each other there. A boring train journey can then suddenly change in a nice journey with your friends!
###FEATURES
####Minimum Viable Product:
-	Login with Facebook
-	See all trains with their departure time from your train station
-	Choose the train you will take
-	Show in which trains your friends are
-	Possibility to create social interaction<br>

####Additional features:

-	Make a ‘friend-list’ yourself based on your Facebook connections (possibly you don’t want to speak every friend on Facebook in the train)
-	Push messages when some friends are at the same train
-	Connect it with other social media canals (e.g. Whatsapp)
-	Show a history of your check-ins
-	Possibility to check-out from a train<br>

### TECHNICAL FEATURES
<br>
For this app, external features are necessary to create the app. First, the login will be based on the Plugin of Facebook, because everyone has to login with their Facebook-account. Furthermore, the API of the NS (Nederlandse Spoorwegen) will be used. The NS provide us with five different services, whereby this app will use two services: a list with all train stations in the Netherlands and actual departure times. For the Edittext whereby the user must type the train station he is leaving from, the service of all train stations will be used. Type suggestions of the stations will be implemented with this service. When searching for a train station, the service of departure times will come in handy. This service will search for a particular station what trains will be leaving at what time and with their final destination. That will be showed in a ListView, whereby you can click on an item and continue to the real ‘check-in’-page. After that, Facebook and the check-in have to be combined and show in which trains other friends are sitting. 
There will probably also a SQLite Database in this app, to store at which trains the user was sitting and with what friends. To not show a fully empty app when there is no Internet,  it is nice to have a database that show us some old actions of the user.<br>

### DIFFICULTIES
<br>
The implementation of the Plugin of Facebook can be a little bit problematic, because it works with complex stuff and it has to work confidentially.  Furthermore, the API of the NS works also not so easy. Instead of a key, NS works with basic authorization. That means that there is a lot more code necessary to get this API working.The combination of the Facebook accounts and the check-in will also be somewhat complex to do. The addition of doing some social interaction with the Facebook friends can be difficult too, because it needs more external features or there  has to be a database that can store social interactions. 

###ACKNOWLEDGEMENT
for FacebookSDK usage:
https://github.com/facebook/facebook-android-sdk/tree/master/samples/MessengerSendSample (FriendsActivity, method onMessengerButtonClicked)
https://www.numetriclabz.com/get-facebook-friends-list-in-android-list-view/ (FbLoginActivity, method getLoginDetails)


