package com.bahanh.billingtest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList


class MainActivity : ComponentActivity() {

    private lateinit var billingClient: BillingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        initBillingClient()

        setContent {
            Box(Modifier.safeDrawingPadding()) {
                Button(onClick = {
                    queryProducts()
                }) {
                    Text("Get purchases")
                }
            }
        }

    }

    private fun initBillingClient() {
        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                // To be implemented in a later section.
            }
        billingClient =
            BillingClient.newBuilder(this).enablePendingPurchases()
                .setListener(purchasesUpdatedListener).build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    Log.d("BillingClientStateListener", "🦖 Finished setup")
                }
                else print("🔴 $billingResult")
            }
            override fun onBillingServiceDisconnected() {
                Log.d("BillingClientStateListener", "✂\uFE0F Service Disconnected")
            }

        })
    }

    private fun queryProducts() {
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    ImmutableList.of(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId("tokyonet.premium.subscription")
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()))
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
            billingResult,
            productDetailsList -> Log.d("productDetailsList", productDetailsList.toString())
        }
    }

}





