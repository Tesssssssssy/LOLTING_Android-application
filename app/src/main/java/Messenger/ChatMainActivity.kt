package Messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sogating_app.R
import com.example.sogating_app.auth.UserDataModel
import com.example.sogating_app.utils.FirebaseRef
import com.example.sogating_app.databinding.ActivityChatMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatMainActivity : AppCompatActivity() {

    // 채팅 메인화면 바이딩 설정
    lateinit var binding : ActivityChatMainBinding

    // 파이어베이스 설정
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference

    // 유저 어뎁터 및 리스트 설정
    lateinit var adapter : UserAdapter
    private lateinit var userList : ArrayList<UserDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 초기화
        mAuth = Firebase.auth

        // DB 초기화
        mDbRef = Firebase.database.reference

        // 리스트 초기화
        userList = ArrayList()

        // 어뎁터 초기화
        adapter = UserAdapter(this, userList)

        // RecyclerView 초기화
        binding.userRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerview.adapter = adapter


        // 사용자 정보 가져오기
        FirebaseRef.userInfoRef.addValueEventListener(object :ValueEventListener{

            // onDataChange 함수는 데이터가 변경되면 실행.
            override fun onDataChange(snapshot: DataSnapshot) {
                // 자기 자신에 대한 정보는 노출하지않고 친구 추가된 항목만 가져와야함.
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(UserDataModel::class.java) // 유저 정보

                    if (mAuth.currentUser?.uid != currentUser?.uid){
                        //mAuth 객체를 통해서 현재 로그인한 나의 정보를 알 수 있다.
                        // 나의 uid와 등록된 사용자 uid 정보가 다를 때만 userList에 추가한다.
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged() // notifyDataSetChanged함수를 통해서 데이터가 실제 화면에 적용.
            }

            // onCancelled 함수는 오류가 발생하면 실행.
            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
}