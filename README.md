# UtilsPlus
Android utility library which contains usefull utility classes with simple reusable methods.

## Adding to your project:
To start using this library, add these lines to the build.gradle of your project:
```
compile 'com.github.rrsystems:utilsplus:1.0.0'
```

### Initialize the libray in your application class
```
  UtilsPlus.initialize(getApplicationContext());
```
if you want to define name of your shared preference, intialize like below
```
    UtilsPlus.initialize(getApplicationContext(), "my_preference_name");
```
### To toast a message, simply insert this line.
First parameter specifies message to be shown and second parameter specifies toast time in seconds
```
      UtilsPlus.getInstance().toast("Hello World!", 5);
```
### How to copy a text to clipboard?
```
UtilsPlus.getInstance().copyToClipboard("text to be copied");
```

### How to check there is an internet connection or not?
```
 if (UtilsPlus.getInstance().checkNetworkState()){
 Log.d("utils plus","internet connection");
 }
 else {
  Log.d("utils plus","no internet connection");
 }
 ```
### How to check whether the string is a valid email id or not?
```
 UtilsPlus.getInstance().checkEmailIsValid("rakshithsj30@gmail.com");
```
### How to check whether the string is a valid phone number or not?
``` 
// second parameter is country code
UtilsPlus.getInstance().validatePhoneNumber("9480523270","91");
```
### How to generate a random string?
```
UtilsPlus.getInstance().getRandomString();
```

### How to display a simple push notification?
```
 /*
     * Utility method which will help you to show notification in the status bar.
     * @param title title of the push notification
     * @param body content to be displayed in the notification
     * @param small_icon small icon which will be showed in the notification. should be of following format:R.drawable.imageName
     * @param large_icon Large icon which will be showed in the notification. should be of following format:R.drawable.imageName
     * @param class_name The  activity which will open on clicking notification. Parameter format: activity_name.class
     * @param autoCancel true or false. if set to true, notification will be disappeared after clicking it otherwise it will remain in the status bar
     */
     

 UtilsPlus.getInstance().displaySimplePushNotification("New Mesage","new message received",R.drawable.photo,R.drawable.photo,MainActivity.class,true);

```
### How to send an email using email clients installed in the device?
```
  UtilsPlus.getInstance().sendEmail("Intent chooser title", "Email title", "Email body", "recipient1@xyz.com", "recipient2@abc.com");
```
### How to encypt and decrypt a string?
encrypting a string:
```
UtilsPlus.getInstance().encryptIt("secret key", "string to be encrypted");

```
decrypting a string:
```
UtilsPlus.getInstance().decryptIt("secret key", "string to be decrypted");
```
### How to get IMEI Code of the device?
To make this code work, you need to specify relevant permissions in android manifest. For mashmallow and above, user has to grant permissions.
```
 UtilsPlus.getInstance().getIMEICode();
```
### How to get device id of the device?
To make this code work, you need to specify relevant permissions in android manifest. For mashmallow and above, user has to grant permissions.
```
UtilsPlus.getInstance().getDeviceID();
```
### How to check whether a service created by your app is running or not?
```
UtilsPlus.getInstance().checkServiceIsRunning("service1.class");
```

### How to convert drawable image into bitmap?
```
UtilsPlus.getInstance().drawableToBitmap(drawable);
```

### How to use shared preference?





