package vidura.chathuranga.jobspot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VacancyAdapter(
    private val vacancyList: ArrayList<VacancyModel>,
) :
    RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_card, parent, false)
        return VacancyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val vacancy = vacancyList[position]
        holder.jobPosition.text = vacancy.jobPosition
        "Company PVT LTD, ${vacancy.jobLocation}".also { holder.jobCompany.text = it }
        holder.jobDescription.text = vacancy.jobDescription
        if (vacancy.isClosed) {
            holder.isClosed.text = "Closed"
        } else {
            holder.isClosed.text = "Active"
        }
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }

    class VacancyViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val jobPosition = itemView.findViewById(R.id.vacancy_title) as TextView
        val jobCompany = itemView.findViewById(R.id.vacancy_company) as TextView
        val jobDescription = itemView.findViewById(R.id.vacancy_description) as TextView
        val isClosed = itemView.findViewById(R.id.vacancy_isClosed) as TextView

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

}