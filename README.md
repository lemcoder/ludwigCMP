# Ludwig for Jetpack Compose

### Getting Started
#### Create VectorSource from ImageVector
```kotlin
VectorSource.fromImageVector(Icons.Outlined.Star)
VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.androidlogo))
```
#### Use AnimatedVector component
When vectorSource changes, AnimatedVector will morph animate the change.
```kotlin
AnimatedVector(
    vectorSource = selectedVectorSource
)
