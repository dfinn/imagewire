package net.dfinn.imagewire.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.dfinn.imagewire.navigation.Navigator
import net.dfinn.imagewire.navigation.NavigatorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Singleton
    @Binds
    abstract fun bindsNavigator(navigator: NavigatorImpl): Navigator
}