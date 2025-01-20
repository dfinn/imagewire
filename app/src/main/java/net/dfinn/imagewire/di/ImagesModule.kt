package net.dfinn.imagewire.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.dfinn.imagewire.data.images.ImageRepository
import net.dfinn.imagewire.data.images.ImageRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImagesModule {
    @Singleton
    @Binds
    abstract fun bindsImagesRepository(imagesRepositoryImpl: ImageRepositoryImpl): ImageRepository
}