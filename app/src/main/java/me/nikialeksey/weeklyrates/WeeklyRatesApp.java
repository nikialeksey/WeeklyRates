package me.nikialeksey.weeklyrates;

import android.app.Application;

import io.realm.Realm;
import me.nikialeksey.weeklyrates.components.ApplicationComponent;
import me.nikialeksey.weeklyrates.components.DaggerApplicationComponent;
import me.nikialeksey.weeklyrates.modules.ApplicationModule;
import me.nikialeksey.weeklyrates.modules.NetworkModule;
import me.nikialeksey.weeklyrates.modules.PropertiesModule;

public class WeeklyRatesApp extends Application {

    private static ApplicationComponent applicationComponent;

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .propertiesModule(new PropertiesModule())
                .networkModule(new NetworkModule())
                .build();
    }

}
