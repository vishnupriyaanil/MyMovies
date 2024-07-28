package com.circuithouse.mymovies.utils.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

val springAnimation = spring(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow,
    visibilityThreshold = 0.001f,
)

val defaultScaleAnimation = infiniteRepeatable<Float>(
    tween(3000, easing = LinearEasing),
    repeatMode = RepeatMode.Reverse,
)

