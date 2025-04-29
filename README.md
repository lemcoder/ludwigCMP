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


https://github.com/user-attachments/assets/9f186fb0-74ef-47ea-827d-3578f6581fa9

