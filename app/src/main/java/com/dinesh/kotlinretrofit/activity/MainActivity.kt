package com.dinesh.kotlinretrofit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinesh.kotlinretrofit.R
import com.dinesh.kotlinretrofit.adapter.ListAdapter
import com.example.retrofitlibrary.model.Question
import com.example.retrofitlibrary.model.QuestionList
import com.example.retrofitlibrary.rest.APIService
import com.example.retrofitlibrary.rest.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var mApiService: APIService? = null

    private var mAdapter: ListAdapter? = null;
    private var listRecyclerView: RecyclerView? = null;
    private var mQuestions: MutableList<Question> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mApiService = RestClient.client.create(APIService::class.java)

        listRecyclerView!!.layoutManager = LinearLayoutManager(this)

        mAdapter = ListAdapter(this, mQuestions, R.layout.question_item)
        listRecyclerView!!.adapter = mAdapter

        fetchQuetionList()


    }

    private fun fetchQuetionList() {
        val call = mApiService!!.fetchQuestions("android");
        call.enqueue(object : Callback<QuestionList> {

            override fun onResponse(call: Call<QuestionList>, response: Response<QuestionList>) {

                Log.d(TAG, "Total Questions: " + response.body()!!.items!!.size)
                val questions = response.body()
                if (questions != null) {
                    mQuestions.addAll(questions.items!!)
                    mAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<QuestionList>, t: Throwable) {
                Log.e(TAG, "Got error : " + t.localizedMessage)
            }
        })
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

}