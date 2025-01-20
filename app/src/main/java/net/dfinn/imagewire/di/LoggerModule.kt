package net.dfinn.imagewire.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.dfinn.imagewire.util.LogCatLogger
import net.dfinn.imagewire.util.Logger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerModule {
    @Singleton
    @Binds
    abstract fun bindsLogger(logCatLogger: LogCatLogger): Logger
}