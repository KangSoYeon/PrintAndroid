package com.example.printertesthtml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //1. image 인쇄
                doPhotoPrint();

                //2. html 인쇄
                //doWebViewPrint();
                //createWebPrintJob(mWebView);
            }
        });
    }

    private void doWebViewPrint() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(MainActivity.this);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("ll", "page finished loading " + url);
                createWebPrintJob(view);
                mWebView = null;
            }
        });

        //사용자 입력정보 가져오기
        EditText nameE = (EditText) findViewById(R.id.name);
        EditText q1E = (EditText)findViewById(R.id.q1);
        EditText q2E = (EditText)findViewById(R.id.q2);

        String name = nameE.getText().toString();
        String q1 = q1E.getText().toString();
        String q2 = q2E.getText().toString();


        // Generate an HTML document on the fly:
//        String htmlDocument = "<html><body><h1>" +
//                name +
//                "</h1>" + "<h3>" +
//                q1 + "</h3><h3>" +
//                q2 + "</h3>" +
//                "</body></html>";


        String htmlDocument = "<html>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "  <h1>출입허가증(문진)</h1>\n" +
                "  <h3>Permission to Access(Examination)</h3>\n" +
                "  <br/><br/>\n" +
                "  <h5>※본 허가증으로 입원병동 출입은 불가합니다.</h5>\n" +
                "  <br/>\n" +
                "  <p>○ 출입허가증은 받으신 당일에만 효력이 있습니다.</p>\n" +
                "  <p>○ 출입허가증이 없으면 진료가 어려울 수 있으니 모든 \n" +
                "    진료/검사가 끝나 댁으로 귀가하실 때까지 이 확인증을 소지하여 주십시오.</p>\n" +
                "  <p>○ 병원 방문 시에는 마스크를 착용하여 주시고 손씻기, 기침 시\n" +
                "    옷소매로 가리기 등 코로나바이러스감염증(COVID)-19 예방 수칙을 준수해 주세요.\n" +
                "  </p>\n" +
                "  <p>○ 쾌유를 기원하며 협조해 주셔서 감사합니다.</p>\n" +
                "  <br/><br/><br/><br/>\n" +
                "  <h3>서울대학교 병원</h3>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>";


        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;
    }

    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        //getActivity() --> MainActivity.this 로 바꾸면 됨
        //PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
        PrintManager printManager = (PrintManager) MainActivity.this.getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        //Do print
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());

    }

    private void doPhotoPrint() {

        AssetManager am = getResources().getAssets();
        InputStream is = null;
        Bitmap bm = null;

        try{
            is = am.open("permission.png");
            bm = BitmapFactory.decodeStream(is);

        }catch(Exception e){
            e.printStackTrace();
        }

        PrintHelper photoPrinter = new PrintHelper(MainActivity.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.droids);
        photoPrinter.printBitmap("droids.jpg - test print", bm);
    }

}
