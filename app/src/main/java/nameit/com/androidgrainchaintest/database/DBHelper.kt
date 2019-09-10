package nameit.com.androidgrainchaintest.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import nameit.com.androidgrainchaintest.utils.Constants

class DBHelper(context: Context?) : SQLiteOpenHelper(context, InfoDB.DATABASENAME, null, InfoDB.VERSION_DATABASE) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SchemaSQL.MtoCat_Routes)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun saveRoute(routeName: String, db: SQLiteDatabase): Boolean {
        val contentValue = ContentValues()
        var tag = 1

        var locationA = Location("")
        var locationB = Location("")

        for (point in Constants.dataListPointGPS) {
            if (tag == 1) {
                contentValue.put("InitLat", point.lat.toString())
                contentValue.put("InitLon", point.lon.toString())
                tag = 2

                locationA!!.latitude = point.lat
                locationA!!.longitude = point.lon
            } else {
                contentValue.put("FinalLat", point.lat.toString())
                contentValue.put("FinalLon", point.lon.toString())
                tag = 1

                locationB!!.latitude = point.lat
                locationB!!.longitude = point.lon
            }
        }

        contentValue!!.put("NameRoute", routeName)
        contentValue!!.put("Distance", locationA!!.distanceTo(locationB).toString())
        contentValue!!.put("Time", ((Constants.FINAL_TIME - Constants.START_TIME) / 60000).toString())

        return (db.insert(InfoDB.NAME_TABLE, null, contentValue) > 0)
    }

    fun showRouteList(db: SQLiteDatabase): Cursor {
            return db.rawQuery("select RouteID, NameRoute from " + InfoDB.NAME_TABLE + " order by RouteID desc", null)
    }

    fun showRouteDetails(routeID: String, db: SQLiteDatabase): Cursor {
        return db.rawQuery("select * from " + InfoDB.NAME_TABLE + " where RouteID = " + routeID, null)
    }
}
