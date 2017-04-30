package com.github.rrsystems.utilsplus.android;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber.PhoneNumber;

/**
 * Created by Clear Bits on 4/12/2017.
 */
public class UtilsPlus {


    static String DEFAULT_SHARED_PREFS_NAME = "app_preferences";

    static SharedPreferences preference;

    static SharedPreferences.Editor editor;

    public static UtilsPlus instance;

    public static Context mcontext;


    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

    /**
     * initialize utils plus library
     * @param context
     * @param prefs_name
     */
    public static void initialize(Context context, String prefs_name) {

        if (prefs_name == null) {
            throw new IllegalArgumentException("Preference name can not be null");
        }
        if (prefs_name.isEmpty()) {
            throw new IllegalArgumentException("Preference name can not be empty");
        }
        if (context == null) {
            throw new IllegalArgumentException("Preference name can not be null");
        }
        mcontext = context;
        preference = context.getSharedPreferences(prefs_name, context.MODE_PRIVATE);
        editor = preference.edit();


    }

    /**
     * initialize Utils Plus Library
     * @param context
     */
    public static void initialize(Context context) {

        if (context == null) {
            throw new IllegalArgumentException("Preference name can not be null");
        }
        preference = context.getSharedPreferences(DEFAULT_SHARED_PREFS_NAME, context.MODE_PRIVATE);
        editor = preference.edit();
        mcontext = context;

    }


    public static synchronized UtilsPlus getInstance() {
        if (instance == null) {
            return instance = new UtilsPlus();
        }
        return instance;
    }

    public UtilsPlus put(String key, Object obj) {
        // if(obj instanceof Float)

        if (obj instanceof String) {
            editor.putString(key, (String) obj);
            editor.commit();
            return this;

        } else if (obj instanceof Boolean) {

            editor.putBoolean(key, (Boolean) obj);
            editor.commit();
            return this;


        } else if (obj instanceof Integer) {

            editor.putInt(key, (Integer) obj);
            editor.commit();
            return this;


        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
            editor.commit();
            return this;


        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
            editor.commit();
            return this;

        } else {
            throw new ClassCastException(obj.getClass().getName() + " is not allowed type of object.");

        }

    }


    public UtilsPlus insertStringSet(String key, Set<String> values) {
        editor.putStringSet(key, values);
        return this;
    }

    public Set<String> retrievetStringSet(String key, Set<String> defaultValue) {
        return preference.getStringSet(key, defaultValue);
    }


    public boolean getBoolean(String key, boolean defalutValue) {
        return preference.getBoolean(key, defalutValue);
    }

    public String getString(String key, String defalutValue) {
        return preference.getString(key, defalutValue);
    }

    public int getInt(String key, int defalutValue) {
        return preference.getInt(key, defalutValue);
    }

    public float getFloat(String key, float defalutValue) {
        return preference.getFloat(key, defalutValue);
    }

    public long getLong(String key, long defalutValue) {
        return preference.getLong(key, defalutValue);
    }

    /**
     * removes specified preference using its name
     * @param key name of the key
     * @return object
     */
    public UtilsPlus removeKey(String key) {
        editor.remove(key);
        editor.commit();
        return this;
    }

    /**
     * this utlity method clears all preferences
     * @return object
     */
    public UtilsPlus clear() {
        editor.clear();
        editor.commit();
        return this;
    }

    /**
     * this utility method helps to save images in internal memory. Since the image is saved in the internal memory, the image is private
     * @param bitmap image in bitmap form
     * @param name name of the file
     * @param type format of the image. only png and jpg formats are allowed. parameter should be of following format. "png" or "jpg"
     * @param path path of the folder in which image to be saved. sample path format: /folder1/folder2/folder3/
     * @param quality quality of the image
     * @param isPrivate
     * @return path in which image is saved
     */
    public String SaveImage(Bitmap bitmap, String name, String type, String path, int quality, boolean isPrivate) {
        File file_path = null;
        File dir2 = null;

        String a = File.separator;


            if (path == null || path.isEmpty() || path.length() <= 3 || !path.contains("/")) {
                Log.e("Utils Plus", "Folder path seems to be incorrect. Please correct the path");

            }
            if (type == null || type.isEmpty()) {

                Log.e("Utils Plus", "Image type can not be null or empty.");
            }
            if (name == null || name.isEmpty()) {

                Log.e("Utils Plus", "Image name can not be null or empty.");
            }
            if (quality < 0 || quality > 100) {

                Log.e("Utils Plus", "Quality must be greater than 0 and less than 100");
            }

            if (type != null) {
                if (!type.equalsIgnoreCase("PNG") && !type.equalsIgnoreCase("JPG")) {
                    Log.e("Utils Plus", "format not supported except png and jpg");
                }
            }

            if (type.equalsIgnoreCase("PNG")) {
                name = name + ".png";
            } else if (type.equalsIgnoreCase("JPG")) {
                name = name + ".jpg";
            }

            if (!isPrivate) {
                File rootDir = null;
                path = path.replaceAll("//*", "/");
                String[] folders = path.split("/");
                for (int i = 0; i < folders.length; i++) {
                    if (i == 0) {
                        rootDir = mcontext.getDir(folders[i], Context.MODE_APPEND); //Creating an internal dir;
                        if (!rootDir.exists()) {
                            rootDir.mkdirs();
                        }
                    } else {
                        rootDir = new File(rootDir, folders[i]);
                        rootDir.mkdir();

                    }

                }
                return rootDir.getAbsolutePath();

            }
            ContextWrapper cw = new ContextWrapper(mcontext);
            File dir = cw.getFilesDir();
            dir2 = new File(dir, path);
            dir2.mkdirs();

            file_path = new File(dir2, name);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file_path);
                // Use the compress method on the BitMap object to write image to the OutputStream
                if (type.equalsIgnoreCase("PNG")) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
                } else if (type.equalsIgnoreCase("JPG")) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        return dir2.getAbsolutePath();
    }

    /**
     * Checks whether there is an active network connection or not
     * @return true or false
     */
    public boolean checkNetworkState() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * utlity method used to toast the message
     * @param message message to be displayed in the toast
     * @param seconds duration of time that message is to be shown
     */
    public void toast(String message, int seconds) {

        if (message == null) {

            Log.d("Utils Plus", "message can not be null");
            return;

        }

        if (seconds <= 0) {
            Log.d("Utils Plus", "Time in seconds can not be <= 0");
            return;
        }
        long milliseconds;
        milliseconds = seconds * 1000;
        final Toast toast = Toast.makeText(mcontext, message, Toast.LENGTH_SHORT);
        toast.show();

        new CountDownTimer(milliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        }.start();


    }

    /**
     * this utility method is used to encrypt the information
     * @param encryptKey secret key which is used to encrypt the information
     * @param value information to be encrypted
     * @return encrypted information
     */
    public String encryptIt(String encryptKey, String value) {
        try {
            DESKeySpec keySpec = new DESKeySpec(encryptKey.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] clearText = value.getBytes("UTF8");
            // Cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
            return encrypedValue;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     *  this utility method is used to decrypt the encrypted information.
     * @param decryptKey secret key which is required  to decrypt the information
     * @param value  information which is to be decrypted
     * @return decrypted information
     */
    public String decryptIt(String decryptKey, String value) {
        try {
            DESKeySpec keySpec = new DESKeySpec(decryptKey.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
            // cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

            String decrypedValue = new String(decrypedValueBytes);
            return decrypedValue;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * generates random string
     * @return random string
     */
    private String getRandomString() {
        return UUID.randomUUID().toString();

    }

    /**
     * converts image of the form "drawable" to "bitmap"
     * @param drawable drawable resource
     * @return bitmap obtained from drawable
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * validates email
     * @param email email address
     * @return true or false.
     */
    public boolean checkEmailIsValid(String email) {
        if (email != null && !email.isEmpty()) {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
        } else {
            Log.e("Utils Plus", "email can not be null or empty.");
            return false;
        }
    }

    /**
     * validates the phone number with country code
     * @param phone_no phone number
     * @param country_code country code for example: +91,+1,+93 etc
     * @return true or false. whether the given phone no is valid or not.
     */
    public boolean validatePhoneNumber(String phone_no, String country_code) {
        PhoneNumber phoneNumber = null;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(mcontext);
        String finalNumber = null;
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(country_code));
        boolean isValid = false;
        PhoneNumberUtil.PhoneNumberType isMobile = null;
        try {
            phoneNumber = phoneNumberUtil.parse(phone_no, isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            if (isValid) return true;
            if (!isValid) return false;
        } catch (NumberParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     *  returns device id of the device. User should grant "android.permission.READ_PHONE_STATE" permission
     * @return
     */
    public String getDeviceID() {
        return Settings.Secure.getString(mcontext.getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }

    /**
     * returns the IMEI Code of the device. proper permission to be granted inorder to make this method work.
     * @return IMEI code of the device.
     */
    public String getIMEICode() {
        TelephonyManager telephonyManager = (TelephonyManager) mcontext
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * Copies specified text to the clipboard
     * @param text text to be copied to the clipboard.
     */
    public String copyToClipboard(String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mcontext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            toast("Copied to Clipboard",3);
            return  text;
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mcontext.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            toast("Copied to Clipboard",3);
            return text;
        }
    }

    /**
     * this utility method checks whether particular service is running or not
     * @param serviceClass name of the service class. for example: myservice.class
     * @return true or false. whether the given service is running or not
     */
    private boolean checkServiceIsRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mcontext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Utility method which will help you to show notification in the status bar.
     * @param title title of the push notification
     * @param body content to be displayed in the notification
     * @param small_icon small icon which will be showed in the notification. should be of following format:R.drawable.imageName
     * @param large_icon Large icon which will be showed in the notification. should be of following format:R.drawable.imageName
     * @param class_name The  activity which will open on clicking notification. Parameter format: activity_name.class
     * @param autoCancel true or false. if set to true, notification will be disappeared after clicking it otherwise it will remain in the status bar
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void displaySimplePushNotification(String title, String body, int small_icon, int large_icon, Class<?> class_name, boolean autoCancel) {
        NotificationManager notificationManager;
        Notification notification;
        boolean autoCancelValue = autoCancel;
        Intent intent = new Intent(mcontext, class_name);
        PendingIntent pIntent = PendingIntent.getActivity(mcontext, (int) System.currentTimeMillis(), intent, 0);

        notification = new Notification.Builder(mcontext)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(autoCancelValue)
                .setSmallIcon(small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(mcontext.getResources(), large_icon))
                .build();

        notificationManager =
                (NotificationManager) mcontext.getSystemService(mcontext.NOTIFICATION_SERVICE);

        int maximum=9999, minimum=0;
        Random rn = new Random();
        int n = maximum - minimum + 1;
        int i = rn.nextInt() % n;
        notificationManager.notify(minimum + i, notification);

    }

    /**
     * sendEmail method is used to send email from available email clients installed in the app
     * @param chooserTitle title of the client. Can not be null
     * @param subject email subject. Can not be null.
     * @param body body of the email. Can not be null
     * @param recipients list of recipients. For example, "abc@xyz.com, xyz@dfc.com". this parameter can not be null
     */
    public void sendEmail(String chooserTitle,String subject, String body, String... recipients){

        checkNull("chooserTitle not allowed to be null", chooserTitle);
        checkNull("subject not allowed to be null", subject);
        checkNull("body not allowed to be null", body);
        checkNull("recipients not allowed to be null", recipients);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        try {
            Intent chooserIntent= Intent.createChooser(intent, chooserTitle);
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mcontext.startActivity(chooserIntent);
        } catch (android.content.ActivityNotFoundException ex) {
           Log.d("Utils Plus", "Activity Not Found");
        }
    }

    public static <T> T checkNull(String message, T object) {
        if(object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

}