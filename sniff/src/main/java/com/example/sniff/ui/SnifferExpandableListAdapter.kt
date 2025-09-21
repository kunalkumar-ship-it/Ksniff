package com.example.sniff.ui

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.sniff.R
import org.json.JSONArray
import org.json.JSONObject

class SnifferExpandableListAdapter(
    private val context: Context,
    private val requestList: List<String>,
    private val responseMap: HashMap<String, String>
) : BaseExpandableListAdapter() {

    override fun getGroupCount() = requestList.size
    override fun getChildrenCount(groupPosition: Int) = 1 // one response per request
    override fun getGroup(groupPosition: Int) = requestList[groupPosition]
    override fun getChild(groupPosition: Int, childPosition: Int) =
        responseMap[requestList[groupPosition]]
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()
    override fun hasStableIds() = false
    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?
    ): View {
        val title = getGroup(groupPosition) as String
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_group, parent, false)
        val requestTitle = view.findViewById<TextView>(R.id.requestTitle)
        val arrowIcon = view.findViewById<ImageView>(R.id.arrowIcon)

        requestTitle.text = title
        requestTitle.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        requestTitle.textSize = 18f
        arrowIcon.rotation = if (isExpanded) 180f else 0f
        return view
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View {
        val response = getChild(groupPosition, childPosition) as String
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item, parent, false)
        val responseText = view.findViewById<TextView>(R.id.responseDescription)
        responseText.text = formatJson(response)
        responseText.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        responseText.textSize = 13f
        responseText.typeface = Typeface.MONOSPACE
        return view
    }
    fun formatJson(json: String?): String {
        return try {
            val jsonObject = JSONObject(json ?: "")
            jsonObject.toString(4)
        } catch (e: Exception) {
            try {
                val jsonArray = JSONArray(json ?: "")
                jsonArray.toString(4)
            } catch (e: Exception) {
                json ?: ""
            }
        }
    }


}
