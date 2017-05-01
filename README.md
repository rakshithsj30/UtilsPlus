# UtilsPlus
Android utility library which contains usefull utility classes with simple reusable methods.

## Adding to your project:
To start using this library, add these lines to the build.gradle of your project:
```
compile 'com.github.rrsystems:utilsplus:1.0.0'
```

### initialize the libray in your application class
```
  UtilsPlus.initialize(getApplicationContext());
```
if you want to define name of your shared preference, intialize like below
```
    UtilsPlus.initialize(getApplicationContext(), "my_preference_name");
    ```

