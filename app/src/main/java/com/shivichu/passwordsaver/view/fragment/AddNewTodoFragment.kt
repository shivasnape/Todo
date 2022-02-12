package com.shivichu.passwordsaver.view.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.shivichu.passwordsaver.R
import com.shivichu.passwordsaver.database.entity.Todo
import com.shivichu.passwordsaver.databinding.FragmentAddNewTodoBinding
import com.shivichu.passwordsaver.delegate.AppOrientation
import com.shivichu.passwordsaver.view.base.BaseFragment
import com.shivichu.passwordsaver.viewModel.ToDoViewModel
import com.sof.retail.viewModel.common.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import java.text.SimpleDateFormat
import java.util.*

class AddNewTodoFragment : BaseFragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var mBinding : FragmentAddNewTodoBinding
    private val todoViewModel: ToDoViewModel by kodeinViewModel()
    lateinit var cal : Calendar
    var mType = 1


    override fun getAppOrientation(): AppOrientation {
        return AppOrientation.PORTRAIT
    }

    override fun getToolbarTitle(): String {
        return "Add New Todo"
    }

    override fun hideToolbar(): Boolean {
        return false
    }

    override fun showBackIcon(): Boolean {
        return false
    }

    override fun showLogoutIcon(): Boolean {
        return false
    }

    override fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_add_new_todo,
            null,
            false
        )
        return mBinding.root
    }

    override fun getViewCreated(view: View, savedInstanceState: Bundle?) {

        cal = Calendar.getInstance()

        mBinding.btnSaveTodo.setOnClickListener {
            insertTodo()
        }

        mBinding.todoTime.setOnClickListener {
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        mBinding.todoDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireActivity(), { view, year, monthOfYear, dayOfMonth ->
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val targetSDF = SimpleDateFormat("dd-MM-yyyy")
                val selectedDate = ""+dayOfMonth + "/" + (monthOfYear +1) + "/" + year
                val parsed = sdf.parse(selectedDate)
                val formatted = targetSDF.format(parsed)
                mBinding.todoDate.setText(formatted)

            }, year, month, day)

            dpd.show()
        }

        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.radioDaily) {
                mType = 1
            }
            else if(checkedId == R.id.radioWeekly) {
                mType = 2
            }
        }
    }

    private fun insertTodo() {
        val todoTitle = mBinding.evTodoTitle.text.toString()
        val todoDesc = mBinding.evTodoDescription.text.toString()
        val time =  mBinding.todoTime.text.toString()
        val date =  mBinding.todoDate.text.toString()

        if (todoTitle.isNotEmpty() && todoDesc.isNotEmpty()) {
            val todoItem = Todo(0, todoTitle, todoDesc, time,date,mType)
            todoViewModel.insertTodo(todoItem)
           showToast("Successfully Added")
            findNavController().navigate(R.id.action_goto_list_todo)
        } else {
           showToast("Title and Description must be non-empty")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)

        mBinding.todoTime.setText(SimpleDateFormat("HH:mm").format(cal.time))
    }
}