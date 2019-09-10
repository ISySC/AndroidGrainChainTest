package nameit.com.androidgrainchaintest.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.android.synthetic.main.layout_bottom_sheet_routes.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_routes.view.ic_close
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_header.view.tv_title
import kotlinx.android.synthetic.main.layout_info_alert.view.*
import kotlinx.android.synthetic.main.layout_toast.view.*
import nameit.com.androidgrainchaintest.R
import nameit.com.androidgrainchaintest.database.DBHelper
import nameit.com.androidgrainchaintest.models.PointGPSModel

class Utils {
    companion object {
        fun startActivity(context: Context, target: Class<*>) {
            context.startActivity(Intent(context, target))
        }

        fun userMessage(activity: Activity, message: String, image: Int) {
            val tv_message: TextView
            val imageView: ImageView

            val inflater = activity.layoutInflater
            val view = inflater.inflate(
                R.layout.layout_toast,
                activity.findViewById(R.id.container), false
            )

            tv_message = view.findViewById(R.id.tv_message)
            imageView = view.findViewById(R.id.ic_accion)

            val toast = Toast(activity)

            tv_message.text = message
            imageView.setImageResource(image)

            toast.view = view
            toast.duration = Toast.LENGTH_LONG
            toast.show()
        }

        fun showAlertDialog(context: Context, title: String, layout: Int) {
            val mDialogView = LayoutInflater.from(context).inflate(layout, null)

            mDialogView.tv_title.text = title

            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            mDialogView.ic_close.setOnClickListener {
                mAlertDialog.dismiss()
            }

            mDialogView.btn_guardar.setOnClickListener {
                if ((saveRoute(context, mDialogView.ed_route_name.text.toString()))) {
                    Utils.userMessage(context as Activity, "Ruta guardada correctamente", R.drawable.ic_done)
                    mAlertDialog.dismiss()
                    Constants.dataListPointGPS = ArrayList()
                }
            }
        }

        private fun saveRoute(context: Context, name: String): Boolean {
            return DBHelper(context).saveRoute(name, DBHelper(context).writableDatabase)
        }

        fun bitmapDescriptorFromVector(context: Context, vector: Int): BitmapDescriptor {

            val vectorDrawable = ContextCompat.getDrawable(context, vector)
            vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(
                    vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            val canvas = Canvas(bitmap)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}