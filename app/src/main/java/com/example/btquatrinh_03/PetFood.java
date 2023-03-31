package com.example.btquatrinh_03;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PetFood extends AppCompatActivity {

    ListView listTitle;
    ArrayList<String> arrayTitle;
    ArrayList<String> arrayLink= new ArrayList<>();
    ArrayList<String> arrayDescription= new ArrayList<>();
    ArrayList<String> arrayDate= new ArrayList<>();
    ArrayList<String> arrayImage= new ArrayList<>();
    ArrayAdapter<String> arrayAdap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_food);

        listTitle = findViewById(R.id.listTitle);
        arrayTitle = new ArrayList<>();
        arrayAdap = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayTitle);

        listTitle.setAdapter(arrayAdap);
        Intent in= getIntent();
        String link= in.getStringExtra("link");
        new ReadRSS().execute(link);
        listTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(PetFood.this, arrayDescription.get(0), Toast.LENGTH_SHORT).show();
                Dialog(position);
            }
        });
        Intent in2= getIntent();
        String link2= in2.getStringExtra("link");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(link2);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(inputStream);

                    Element root = doc.getDocumentElement();
                    NodeList items = root.getElementsByTagName("item");

                    for (int i = 0; i < items.getLength(); i++) {
                        Element item = (Element) items.item(i);
                        Element mediaContent = (Element) item.getElementsByTagName("media:content").item(0);
                        String imageUrl = mediaContent.getAttribute("url");
                        arrayImage.add(imageUrl);
                    }

                    // Do something with the imageUrls array
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }


                try {
                    // Tạo đối tượng URL để lấy dữ liệu từ trang web
                    URL url = new URL(link2);

                    // Tạo đối tượng XmlPullParser để phân tích dữ liệu XML
                    XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = xmlFactoryObject.newPullParser();
                    parser.setInput(url.openConnection().getInputStream(), null);

                    // Lặp lại các thẻ trong tài liệu XML
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tagName = parser.getName();
                        if (eventType == XmlPullParser.START_TAG && tagName.equals("description")) {
                            // Đọc nội dung trong thẻ description
                            String description = parser.nextText();
                            // Xử lý nội dung tại đây
//                            Log.d("PetFoodIndustry", description);
//                            System.out.println("nct: "+description);
                            arrayDescription.add(description);
                        }
                        eventType = parser.next();
                    }
                    arrayDescription.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void Dialog(int i){
        Dialog dia= new Dialog(this);
        dia.setContentView(R.layout.dialog);
        dia.setCanceledOnTouchOutside(false);
        Button btnMore= dia.findViewById(R.id.btnMore);
        Button btnClose= dia.findViewById(R.id.btnClose);

        Intent in= getIntent();
        String name= in.getStringExtra("nameList");
        TextView txtNameTit= dia.findViewById(R.id.txtNameTitle);
        txtNameTit.setText(name);

        TextView txtDes= dia.findViewById(R.id.txtDes);
        txtDes.setText(arrayDescription.get(i));
        TextView txtNameFood= dia.findViewById(R.id.txtNameFood);
        txtNameFood.setText(arrayTitle.get(i));
        TextView txtDate= dia.findViewById(R.id.txtDate);
        txtDate.setText(arrayDate.get(i));
        ImageView imgMain= dia.findViewById(R.id.image);
        Picasso.get().load(arrayImage.get(i)).into(imgMain);

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte= new Intent(Intent.ACTION_VIEW, Uri.parse(arrayLink.get(i)));
                startActivity(inte);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
            }
        });
        dia.show();
    }
    private class ReadRSS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content= new StringBuilder();
            try {
                URL url= new URL(strings[0]);
                InputStreamReader inputSRead= new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferRead= new BufferedReader(inputSRead);

                String line="";
                while ((line=bufferRead.readLine()) != null){
                    content.append(line);
                }
                bufferRead.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //đọc xml
            XMLDOMParser par= new XMLDOMParser();
            Document doc= par.getDocument(s);
            NodeList nodeList= doc.getElementsByTagName("item");
            String title="";
            String link="";
            String date="";
            for (int i=0; i<nodeList.getLength(); i++){

                Element elem= (Element) nodeList.item(i);
                title= par.getValue(elem, "title");
                arrayTitle.add(title);
                link=par.getValue(elem, "link");
                arrayLink.add(link);
                date=par.getValue(elem, "pubDate");
                arrayDate.add(date);


            }
            arrayAdap.notifyDataSetChanged();
        }
    }
}