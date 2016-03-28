package ar.com.wolox.wolmo.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

public class NavigationUtils {

    public static void openBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void jumpTo(Context a, Class clazz, String reference, long id) {
        Intent userIntent = new Intent(a, clazz);
        userIntent.putExtra(reference, id);
        a.startActivity(userIntent);
    }

    public static void jumpTo(Context a, Class clazz, String reference, int id) {
        Intent userIntent = new Intent(a, clazz);
        userIntent.putExtra(reference, id);
        a.startActivity(userIntent);
    }

    public static void jumpTo(Context a, Class clazz, String reference, String id) {
        Intent userIntent = new Intent(a, clazz);
        userIntent.putExtra(reference, id);
        a.startActivity(userIntent);
    }

    public static void jumpTo(Context context, Class clazz, IntentObject... intentObjects) {
        Intent intent = new Intent(context, clazz);
        for (IntentObject intentObject : intentObjects) {
            intent.putExtra(intentObject.reference, intentObject.object);
        }
        context.startActivity(intent);
    }

    public static void jumpToClearingTask(Context context, Class clazz,
                                          IntentObject... intentObjects) {
        Intent intent = new Intent(context, clazz);
        for (IntentObject intentObject : intentObjects) {
            intent.putExtra(intentObject.reference, intentObject.object);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void jumpToWithAnimation(Activity activity,
                                           Class clazz,
                                           ActivityOptionsCompat transitionActivityOptions,
                                           IntentObject... intentObjects) {
        Intent intent = new Intent(activity, clazz);
        for (IntentObject intentObject : intentObjects) {
            intent.putExtra(intentObject.reference, intentObject.object);
        }
        ActivityCompat.startActivity(activity, intent, transitionActivityOptions.toBundle());
    }

    public static ActivityOptionsCompat buildActivityOptions(
            Activity activity, Pair<View, String>... pairs) {
        return ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, pairs);
    }

    public static class Builder {

        private Activity activity;
        private Class clazz;
        private ArrayList<Pair<View, String>> sharedElements;
        private ArrayList<IntentObject> intentObjects;

        public Builder(Activity activity) {
            this.activity = activity;
            sharedElements = new ArrayList<>();
            intentObjects = new ArrayList<>();
        }

        public Builder setClass(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder addSharedElement(View sharedView, String sharedString) {
            sharedElements.add(new Pair<View, String>(sharedView, sharedString));
            return this;
        }

        public Builder addIntentObjects(IntentObject... intentObjects) {
            for (IntentObject intentObject : intentObjects) addIntentObject(intentObject);
            return this;
        }

        public Builder addIntentObject(IntentObject intentObject) {
            this.intentObjects.add(intentObject);
            return this;
        }

        public Builder addExtra(String reference, Serializable object) {
            addIntentObject(new IntentObject(reference, object));
            return this;
        }

        public void jump() {
            if (sharedElements.isEmpty()) {
                jumpTo(activity, clazz,
                        intentObjects.toArray(new IntentObject[intentObjects.size()]));
            } else {
                jumpToWithAnimation(activity, clazz, buildActivityOptions(activity,
                        sharedElements.toArray(new Pair[sharedElements.size()])),
                        intentObjects.toArray(new IntentObject[intentObjects.size()]));
            }

        }
    }

    public static class IntentObject {
        private String reference;
        private Serializable object;

        public IntentObject(String reference, Serializable object) {
            this.reference = reference;
            this.object = object;
        }
    }
}