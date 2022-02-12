package com.shivichu.passwordsaver.view.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.shivichu.passwordsaver.R
import com.shivichu.passwordsaver.databinding.FragmentLoginTodoBinding
import com.shivichu.passwordsaver.delegate.AppOrientation
import com.shivichu.passwordsaver.utils.CustomProgressBar
import com.shivichu.passwordsaver.utils.SessionManager
import com.shivichu.passwordsaver.view.base.BaseFragment
import com.shivichu.passwordsaver.viewModel.ToDoViewModel
import com.sof.retail.viewModel.common.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class TodoLoginFragment : BaseFragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var mBinding : FragmentLoginTodoBinding
    private val todoViewModel: ToDoViewModel by kodeinViewModel()
    val progressBar = CustomProgressBar()
    private lateinit var sessionManager: SessionManager

    override fun getAppOrientation(): AppOrientation {
        return AppOrientation.PORTRAIT
    }

    override fun getToolbarTitle(): String {
        return "Home"
    }

    override fun hideToolbar(): Boolean {
        return true
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
            R.layout.fragment_login_todo,
            null,
            false
        )
        sessionManager = SessionManager(requireContext())
        return mBinding.root
    }

    override fun getViewCreated(view: View, savedInstanceState: Bundle?) {

        if(::sessionManager.isInitialized) {
            if(sessionManager.isLoggedIn) {
                findNavController().navigate(R.id.action_goto_list_todo)
            }
        }

        mBinding.evTodoEmail.setText("eve.holt@reqres.in")
        mBinding.evTodoPassword.setText("cityslicka")

        mBinding.btnLogin.setOnClickListener {

            if(mBinding.evTodoEmail.text.isNullOrEmpty()) {
                showMessage("Please enter email address")
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(mBinding.evTodoEmail.text.toString()).matches()) {
                showMessage("Invalid email address")
                return@setOnClickListener
            }

            if(mBinding.evTodoPassword.text.isNullOrEmpty()) {
                showMessage("Please enter password")
                return@setOnClickListener
            }
            progressBar.show(requireContext(), "Loading.....")
            authenticateUser(mBinding.evTodoEmail.text.toString(), mBinding.evTodoPassword.text.toString())

        }

    }

    private fun authenticateUser(email: String, password: String) {
        todoViewModel.authenticateUser(email,password).observe(viewLifecycleOwner,{
            Log.e("Response : ", Gson().toJson(it))
            progressBar.dialog.dismiss()
            if(it!=null) {
                showMessage("Login Success")
                sessionManager.setLogin(true)
                sessionManager.setToken(it.token?:"null")
                findNavController().navigate(R.id.action_goto_list_todo)
            }
            else {
                showMessage("Login Failed")
            }
        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

}