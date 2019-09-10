package nameit.com.androidgrainchaintest.adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nameit.com.androidgrainchaintest.R
import nameit.com.androidgrainchaintest.models.RouteModel
import nameit.com.androidgrainchaintest.utils.Utils
import nameit.com.androidgrainchaintest.activities.RouteDetailsActivity
import nameit.com.androidgrainchaintest.utils.Constants


class RouteNameAdapter(val contactList: ArrayList<RouteModel>) :
    RecyclerView.Adapter<RouteNameAdapter.ViewHolder>() {

    lateinit var contextGroup: ViewGroup

    override fun onCreateViewHolder(contextGroup: ViewGroup, p1: Int): ViewHolder {
        val view =
            LayoutInflater.from(contextGroup?.context).inflate(R.layout.item_route, contextGroup, false)
        this.contextGroup = contextGroup

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(view: ViewHolder, position: Int) {
        view.tv_route_name.text = contactList[position].nameRoute

        view.list_route_container.setOnClickListener {
            Constants.ROUTE_ID_SELECT = contactList[position].routeID
            Utils.startActivity(contextGroup?.context, RouteDetailsActivity::class.java)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_route_name = itemView.findViewById<TextView>(R.id.tv_route_name)
        val list_route_container = itemView.findViewById<CardView>(R.id.list_route_container)
    }
}