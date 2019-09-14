package me.nikialeksey.weeklyrates;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

public class DebugApp extends WeeklyRatesApp {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize (
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                                        RealmInspectorModulesProvider.builder(this).build()
                        )
                        .build()
        );;
    }
}
