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
