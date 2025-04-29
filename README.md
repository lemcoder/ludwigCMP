# Ludwig for Compose Multiplatform

### Getting Started
#### Create VectorSource from ImageVector
```kotlin
VectorSource.fromImageVector(Icons.Outlined.Star)
```
#### Use AnimatedVector component
When vectorSource changes, AnimatedVector will morph animate the change.
```kotlin
AnimatedVector(
    vectorSource = selectedVectorSource
)
