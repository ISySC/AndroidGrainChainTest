package nameit.com.androidgrainchaintest.fragments

import android.database.Cursor
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_bottom_sheet_routes.view.*
import nameit.com.androidgrainchaintest.R
import nameit.com.androidgrainchaintest.models.RouteModel
import nameit.com.androidgrainchaintest.adapters.RouteNameAdapter
import nameit.com.androidgrainchaintest.database.DBHelper


class BottomSheetRouteListFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_bottom_sheet_routes, container, false)

        setListeners(view)
        setValues(view)

        return view
    }

    private fun setValues(view: View) {
        val recyclerViewRouteList: RecyclerView

        recyclerViewRouteList = view.findViewById(R.id.rl_container)
        recyclerViewRouteList.setLayoutManager(LinearLayoutManager(context?.let { it }))

        val dataList = ArrayList<RouteModel>()

        val cursor: Cursor = DBHelper(context?.let { it }).showRouteList(DBHelper(context?.let { it }).writableDatabase)

        while (cursor.moveToNext()) {
            dataList.add(RouteModel(cursor.getString(0), cursor.getString(1)))
        }

        val rvAdapter = RouteNameAdapter(dataList)
        recyclerViewRouteList.adapter = rvAdapter
    }

    private fun setListeners(view: View) {
        view.ic_close.setOnClickListener { dismiss() }
    }
}