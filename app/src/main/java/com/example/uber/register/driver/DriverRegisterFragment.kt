package com.example.uber.register.driver

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.Navigation
import com.example.uber.App
import com.example.uber.R
import com.example.uber.api.RetrofitBuilder
import com.example.uber.databinding.FragmentDriverRegisterBinding
import com.example.uber.model.CreatUserResponse
import com.example.uber.model.SignInrResponse
import com.example.uber.model.User
import com.example.uber.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverRegisterFragment : Fragment() {

    private val binding: FragmentDriverRegisterBinding by lazy {
        FragmentDriverRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.toCustomerApp.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.from_driver_to_customer)
        }
        binding.signUpBtnDriver.setOnClickListener {
            signUp()
        }

        binding.signInBtnDriver.setOnClickListener {
            signIn()
        }
        return binding.root
    }

    private fun signIn() {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_driver, null)

        val email: EditText = dialogLayout.findViewById(R.id.email)
        val user_Name: EditText = dialogLayout.findViewById(R.id.user_Name)
        val car: EditText = dialogLayout.findViewById(R.id.car)
        val phone: EditText = dialogLayout.findViewById(R.id.phone)
        val password: EditText = dialogLayout.findViewById(R.id.password)

        dialogLayout.findViewById<EditText>(R.id.car).visibility = View.GONE
        dialogLayout.findViewById<EditText>(R.id.email).visibility = View.GONE
        dialogLayout.findViewById<EditText>(R.id.phone).visibility = View.GONE

        with(builder) {
            setTitle("SIGN IN")
            setMessage("Please use email to Register")
            setPositiveButton("Sign In") { dialog, which ->

                val username = user_Name.text.toString().trim()
                val passwordd = password.text.toString().trim()
                val customer = "driver"

                val api = RetrofitBuilder.api.signIn(username,passwordd)
                api.enqueue(object : Callback<SignInrResponse> {
                    override fun onResponse(
                        call: Call<SignInrResponse>,
                        response: Response<SignInrResponse>
                    ) {
                        if (response.isSuccessful) {
                            App.pref =  activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
                            val editor = App.pref?.edit()
                            editor?.putString("userName", response.body()?.username)
                            editor?.putString("phone", response.body()?.phone)
                            editor?.putString("userType", "driver")
                            editor?.apply()
                            toast("${username} может перейти")
                            startActivity(Intent(requireContext(), OrderList::class.java))

                        }
                    }

                    override fun onFailure(call: Call<SignInrResponse>, t: Throwable) {
                        toast("что то пошло не так")
                        Log.d("fail", t.message.toString())
                    }

                })

            }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .setView(dialogLayout).create().show()
        }
    }

    private fun signUp() {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_driver, null)

        val email: EditText = dialogLayout.findViewById(R.id.email)
        val user_Name: EditText = dialogLayout.findViewById(R.id.user_Name)
        val car: EditText = dialogLayout.findViewById(R.id.car)
        val phone: EditText = dialogLayout.findViewById(R.id.phone)
        val password: EditText = dialogLayout.findViewById(R.id.password)

        with(builder) {
            setTitle("SIGN UP")
            setMessage("Please use email to Register")
            setPositiveButton("Register") { dialog, which ->

                val user = User()
                user.email = email.text.toString().trim()
                user.car = car.text.toString().trim()
                user.username = user_Name.text.toString().trim()
                user.userType = "driver"
                user.password = password.text.toString().trim()
                user.phone = phone.text.toString().trim()

                val api = com.example.uber.api.RetrofitBuilder.api.creatNewUser(user)
                api.enqueue(object : Callback<CreatUserResponse> {
                    override fun onResponse(
                        call: Call<CreatUserResponse>,
                        response: Response<CreatUserResponse>
                    ) {
                        if (response.isSuccessful) {
                            toast("${user.username} is created")
                            App.pref =  activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
                            val editor = App.pref?.edit()
                            editor?.putString("userName", user.username)
                            editor?.putString("phone", user.phone)
                            editor?.putString("userType", "driver")
                            editor?.apply()
                            startActivity(Intent(requireContext(), OrderList::class.java))
                        }
                    }

                    override fun onFailure(call: Call<CreatUserResponse>, t: Throwable) {
                        toast("что то пошло не так")
                    }

                })

            }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .setView(dialogLayout).create().show()
        }
    }

}