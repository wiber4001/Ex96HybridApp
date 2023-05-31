package com.wny2023.ex96hybridapp

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.wny2023.ex96hybridapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //웹뷰 설정들
        binding.wv.settings.javaScriptEnabled=true //JS 사용 허용
        binding.wv.settings.allowFileAccess=true // ajax 기술은 서버사이드(http://)에서만 동작함
        // 로컬에서도 동작하도록 허용하는 명령
        binding.wv.webViewClient = WebViewClient() //웹뷰안에서 보이게하려면
        binding.wv.webChromeClient = WebChromeClient() // 웹뷰안에서 크롬이 열리게 하려면

        //2) 웹뷰에서 사용할 메소드들을 가지고 있는 중계인객체를 생성 및 웹뷰에 설정
        binding.wv.addJavascriptInterface(WebViewConnector(),"Droid")//name 파라미터에 줄 이름
        // 이 이름(여기서는 "Droid")이름이 웹문서의 window 객체(BOM트리의 최상위 객체)의 멤버변수로 자동 추가됨

        //웹뷰가 보여줄 웹문서(.html)로드하기
        binding.wv.loadUrl("file:///android_asset/index.html")
        //1) native에서 web UI를 제어
        binding.btn.setOnClickListener {
            //HTML을 직접제어는 못함. JS의 특정 함수를 호출하여 대신 제어하기
            var msg:String=binding.et.text.toString()
            binding.wv.loadUrl("javascript:setMessage('$msg')")
            binding.et.setText("")
        }

    }//onCreate()...

    //2)웹뷰의 JS에서 호출할 수 있는 메소드를 모아놓은 클래스 설계
    inner class WebViewConnector{ //임의의 클래스 이름
        // 자바스크립트에서 호출할 수 있는 메소드 @annotation 필수
        @JavascriptInterface
        fun showMessage(msg:String){
            binding.tv.text="웹뷰로부터 받은 메세지 : $msg"
        }
        //디바이스의 고유기능인 갤러리 사진선택 앱을 열어주는 기능 메소드
        @JavascriptInterface
        fun openPhotoApp(){
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivity(intent) //원래는 startActivityForResult
            //선택한 사진을 웹서버에 전송하고 웹문서에서 업로드된 사진 파일을 보여주는 방식으로 코딩

        }

    }
}