package nameit.com.androidgrainchaintest.utils

import nameit.com.androidgrainchaintest.models.PointGPSModel

class Constants {
    companion object {
        val REQUEST_PERMISSION_LOCATION = 1000
        var START_TIME: Long = 0
        var FINAL_TIME: Long = 0
        var dataListPointGPS = ArrayList<PointGPSModel>()
        var ROUTE_ID_SELECT = ""
    }
}