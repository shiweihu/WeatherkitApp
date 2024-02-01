package com.unisa.weatherkitapp.compose.view

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdvertiseViewCompose(
    adid:String = "ca-app-pub-3940256099942544/6300978111",
    adsize:AdSize = AdSize.BANNER
) {
    val context = LocalContext.current
    // 创建 AdView
    val adView = remember {
        AdView(context).also {
            it.setAdSize(adsize)
            it.adUnitId = adid
        }
    }

    // 加载广告
    DisposableEffect(context) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        onDispose {
            // 在组件销毁时释放资源
            adView.destroy()
        }
    }
    val density = LocalDensity.current.density
    val adHeight = adsize.getHeightInPixels(context) / density
    // 将 AdView 嵌入到 Compose 中
    AndroidView(
        factory = {adView},
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(adHeight.dp)
    ) { view ->
        // 在这里可以进行额外的配置，例如设置 AdView 的 LayoutParams


        view.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            adsize.getHeightInPixels(context)
        )
    }
}
fun pxToDp(context: Context, pixels: Float): Float {
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, displayMetrics)
}