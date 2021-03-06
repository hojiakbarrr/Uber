package com.example.uber.register.customer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
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
import com.example.uber.databinding.FragmentCustomerSignInBinding
import com.example.uber.model.CreatUserResponse
import com.example.uber.model.SignInrResponse
import com.example.uber.model.User
import com.example.uber.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerSignInFragment : Fragment() {
    private val binding: FragmentCustomerSignInBinding by lazy {
        FragmentCustomerSignInBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.signUpBtnCustomer.setOnClickListener {
            signUP()
        }
        binding.toCustomerApp.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.from_customer_to_driver)
        }
        binding.signInBtnCustomer.setOnClickListener {
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
                            App.pref =  activity?.getSharedPreferences("pref", MODE_PRIVATE)
                            val editor = App.pref?.edit()
                            editor?.putString("userName", response.body()?.username)
                            editor?.putString("phone", response.body()?.phone)
                            editor?.putString("userType", "customer")
                            editor?.apply()
                            toast("$username ?????????? ??????????????")
                            startActivity(Intent(requireContext(), CustomerMapsActivity::class.java))

                        }
                    }

                    override fun onFailure(call: Call<SignInrResponse>, t: Throwable) {
                        toast("?????? ???? ?????????? ???? ??????")
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

    @SuppressLint("InflateParams")
    private fun signUP() {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_driver, null)

        val email: EditText = dialogLayout.findViewById(R.id.email)
        val user_Name: EditText = dialogLayout.findViewById(R.id.user_Name)
        val car: EditText = dialogLayout.findViewById(R.id.car)
        val phone: EditText = dialogLayout.findViewById(R.id.phone)
        val password: EditText = dialogLayout.findViewById(R.id.password)

        dialogLayout.findViewById<EditText>(R.id.car).visibility = View.GONE

        with(builder) {
            setTitle("SIGN UP")
            setMessage("Please use email to Register")
            setPositiveButton("Register") { dialog, which ->

                val user = User()
                user.email = email.text.toString().trim()
//                user.car = car.text.toString().trim()
                user.username = user_Name.text.toString().trim()
                user.userType = "customer"
                user.password = password.text.toString().trim()
                user.phone = phone.text.toString().trim()

                val api = RetrofitBuilder.api.creatNewUser(user)
                api.enqueue(object : Callback<CreatUserResponse> {
                    override fun onResponse(
                        call: Call<CreatUserResponse>,
                        response: Response<CreatUserResponse>
                    ) {
                        if (response.isSuccessful) {
                            toast("${user.username} is created")

                            App.pref = activity?.getSharedPreferences("pref", MODE_PRIVATE)
                            val editor = App.pref?.edit()
                            editor?.putString("userName", user.username)
                            editor?.putString("phone", user.phone)
                            editor?.putString("userType", "customer")
                            editor?.apply()

                            startActivity(Intent(requireContext(), CustomerMapsActivity::class.java))

                        }
                    }

                    override fun onFailure(call: Call<CreatUserResponse>, t: Throwable) {
                        toast("?????? ???? ?????????? ???? ??????")
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