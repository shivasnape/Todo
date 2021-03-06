package com.shivichu.passwordsaver.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.adapter.TodoAdapter
import com.google.gson.Gson
import com.shivichu.passwordsaver.R
import com.shivichu.passwordsaver.database.entity.Todo
import com.shivichu.passwordsaver.databinding.FragmentListTodoBinding
import com.shivichu.passwordsaver.delegate.AppOrientation
import com.shivichu.passwordsaver.delegate.OnDeleteClicked
import com.shivichu.passwordsaver.utils.SessionManager
import com.shivichu.passwordsaver.view.activity.BaseHomeActivity
import com.shivichu.passwordsaver.view.base.BaseFragment
import com.shivichu.passwordsaver.viewModel.ToDoViewModel
import com.sof.retail.viewModel.common.kodeinViewModel
import kotlinx.android.synthetic.main.activity_base_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import timber.log.Timber

class HomeTodoListFragment : BaseFragment(), KodeinAware, OnDeleteClicked {

    override val kodein by kodein()
    private lateinit var mBinding: FragmentListTodoBinding
    private val todoViewModel: ToDoViewModel by kodeinViewModel()
    private lateinit var todoAdapter: TodoAdapter
    private var mList : MutableList<Todo> = arrayListOf()
    private lateinit var sessionManager : SessionManager

    override fun getAppOrientation(): AppOrientation {
        return AppOrientation.PORTRAIT
    }

    override fun getToolbarTitle(): String {
        return "Home"
    }

    override fun hideToolbar(): Boolean {
        return false
    }

    override fun showBackIcon(): Boolean {
        return false
    }

    override fun showLogoutIcon(): Boolean {
        return true
    }

    override fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_list_todo,
            null,
            false
        )
        sessionManager = SessionManager(requireContext())
        setupRecyclerAdapter()
        return mBinding.root
    }

    override fun getViewCreated(view: View, savedInstanceState: Bundle?) {

        todoViewModel.getAllTodo().observe(viewLifecycleOwner, { todo ->
            mList = todo as MutableList<Todo>
            //todoAdapter.differ.submitList(todo)
            todoAdapter.updateList(mList)
            updateUI(todo)
        })

        mBinding.btnAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_goto_add_new_task)
        }

        (activity as BaseHomeActivity).imageLogout.setOnClickListener {
            showLogoutDialog()
        }

    }

    private fun showLogoutDialog() {
        val alertDialog = android.app.AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle("Logout")
        alertDialog.setMessage("Are you sure you want to Logout?")
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK", { dialog, which ->
            if (::sessionManager.isInitialized) {
                sessionManager.logoutUser()
            }
            dialog.dismiss()
            showMessage("Logged Out..!!")
            findNavController().popBackStack(R.id.homeTodoListFragment, true)
            findNavController().navigate(R.id.todoLoginFragment)
        })
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "Cancel", { dialog, which ->
            dialog.dismiss()
        })
        alertDialog.show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(todo: List<Todo>) {
        if (todo.isNotEmpty()) {
            mBinding.tvNoNoteAvailable.visibility = View.GONE
            mBinding.recyclerView.visibility = View.VISIBLE
        } else {
            mBinding.tvNoNoteAvailable.visibility = View.VISIBLE
            mBinding.recyclerView.visibility = View.GONE
        }
    }

    private fun setupRecyclerAdapter() {
        val dividerItemDecoration = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        mBinding.recyclerView.apply {
            todoAdapter = TodoAdapter(todoViewModel,mList)
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(dividerItemDecoration)
             todoAdapter.deleteListener = this@HomeTodoListFragment
        }
    }

    private fun deleteCheckedTodo(position: Int, todo: Todo) {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Are you sure want to Delete?")
            setPositiveButton("Yes") { _, _ ->
                deleteItem(position, todo)
            }
            setNegativeButton("No") { _, _ -> }
            create()
            show()
        }
    }

    private fun deleteItem(position: Int, todo: Todo) {
        todoViewModel.deleteTodo(todo).observe(viewLifecycleOwner,{
            if(it) {
                showToast("Deleted Successfully")
                mList.removeAt(position)
                todoAdapter.notifyItemRemoved(position)
                todoAdapter.notifyItemRangeChanged(position, mList.size)
                updateUI(mList)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    override fun onDelete(position: Int, todo: Todo) {
        if(position!=-1) {
            deleteCheckedTodo(position,todo)
        }
    }

    override fun onItemClicked(position: Int, todo: Todo) {
        if(position!=1) {
            val data = bundleOf("currentTodo" to Gson().toJson(todoAdapter.mList[position]))
            Timber.e("Data : " + Gson().toJson(todoAdapter.mList[position]))
            findNavController().navigate(R.id.action_goto_update_task, data)
        }
    }
}

