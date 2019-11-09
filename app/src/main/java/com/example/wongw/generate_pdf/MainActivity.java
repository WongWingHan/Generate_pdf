package com.example.wongw.generate_pdf;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 1000;
    EditText mTextEt2;
    Button mSaveBtn;
    Button mbtnDate;
    Button mbtnDate2;
    DatabaseReference reff;
    String CourierName;
    String End;
    String PackageNo;
    String ShipmentNo;
    String Start;
    String Vehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing view(activity_main.xml)
        mTextEt2=findViewById(R.id.textEt2);
        mSaveBtn=findViewById(R.id.saveBtn);
        mbtnDate=findViewById(R.id.btnDate);
        mbtnDate2=findViewById(R.id.btnDate2);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todate = dateFormat.format(currentdate());
        String finalto = todate.toString();
        mbtnDate.setText(finalto);
        DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        String todate2 = dateFormat2.format(currentdate2());
        String finalto2 = todate2.toString();
        mbtnDate2.setText(finalto2);

        //handle button click
        mSaveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            // we need to handle runtime permission for devices with marshmellow and above
                if(Build.VERSION.SDK_INT >Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions,STORAGE_CODE);
                    } else {
                        //permission already granted, call save pdf method
                    savePdf();
                    }
                }
                else{
                    //system OS < Marshmallow, call save pdf method
                    savePdf();
                    }

            }
        });


    }
    private Date currentdate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        return cal.getTime();
    }
    private Date currentdate2() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        return cal.getTime();
    }
    private void savePdf() {
        String mFileName = "ShipmentReport";
        String mFilePath = Environment.getExternalStorageDirectory()+ "/"+mFileName+".pdf";


        try{


            reff= FirebaseDatabase.getInstance().getReference().child("shipmentdetails").child("001").child("01");
            reff.addValueEventListener(new ValueEventListener() {
                //create object of Document class
                Document mDoc = new Document();
                //pdf file name
                String mFileName = "ShipmentReport";
                String mFilePath = Environment.getExternalStorageDirectory()+ "/"+mFileName+".pdf";
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CourierName=dataSnapshot.child("CourierName").getValue().toString();
                    End=dataSnapshot.child("End").getValue().toString();
                    PackageNo=dataSnapshot.child("PackageNo").getValue().toString();
                    ShipmentNo=dataSnapshot.child("ShipmentNo").getValue().toString();
                    Start=dataSnapshot.child("Start").getValue().toString();
                    Vehicle=dataSnapshot.child("Vehicle").getValue().toString();

                    //create instance of PdfWritter class
                    try {
                        PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //open the document for writing
                    mDoc.open();
                    //get text from EditText i.e. mTextEt
                    String mText2 = mTextEt2.getText().toString();
                    String btnDate= mbtnDate.getText().toString();
                    String btnDate2 =mbtnDate2.getText().toString();

                    // add author of the document(optional)
                    mDoc.addAuthor("Wong Wing Han ");
                    Font f = new Font(Font.FontFamily.TIMES_ROMAN,30.0f,Font.BOLDITALIC, BaseColor.BLUE);
                    Font g = new Font(Font.FontFamily.TIMES_ROMAN,20.0f,Font.NORMAL,BaseColor.BLACK);
                    Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
                    float[] columnWidths = {7f,7f,7f,7f,7f,7f,7f};
                    PdfPTable table = new PdfPTable(columnWidths);
                    table.setWidthPercentage(90f);
                    insertCell(table,"Date", Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,"Courier Name", Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,"End", Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,"Package No", Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,"Shipment No", Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,"Start", Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,"Vehicle", Element.ALIGN_LEFT,1,bfBold12);
                    table.setHeaderRows(1);
                    insertCell(table,btnDate, Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,CourierName, Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,End, Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,PackageNo, Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,ShipmentNo, Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,Start, Element.ALIGN_LEFT,1,bfBold12);
                    insertCell(table,Vehicle, Element.ALIGN_LEFT,1,bfBold12);


                    // add paragraph to the document
                    try {
                        mDoc.add(new Paragraph("\t\t\t\tReport in PDF",f));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    try {
                        mDoc.add(new Paragraph("Email to:"+mText2,g));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    try {
                        mDoc.add(new Paragraph("Date From:"+btnDate));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    try {
                        mDoc.add(new Paragraph("Date To:"+btnDate2));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    try {
                        mDoc.add(table);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }


                    //close the document
                    mDoc.close();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //show message that file is saved, it will show file name and file path
            Toast.makeText(this,mFileName+".pdf\nis saved to\n"+mFilePath,Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            // if any thing goes wrong causing exception get and show exception message
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        try {
            File root= Environment.getExternalStorageDirectory();
            String filelocation= Environment.getExternalStorageDirectory()+ "/" + mFileName+".pdf";
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            String mText2 = mTextEt2.getText().toString();
            String message="File to be shared is " + mFileName + ".";
            intent.putExtra(Intent.EXTRA_SUBJECT, "Shipment Report");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse( "file://"+filelocation));
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setData(Uri.parse("mailto:"+mText2));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } catch(Exception e)  {
            System.out.println("is exception raises during sending mail"+e);
        }
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }
    //handle permission result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permission was granted from popup,call save pdf method
                    savePdf();
                }
                else{
                    //permission was denied from popup,show error message
                    Toast.makeText(this,"Permission denied....!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void btnDate_OnClick(View view) {
        DateFragment df = new DateFragment();
        df.show(getSupportFragmentManager(),"Date");
    }
    public void setDate(int year, int month,int day){
        String newDate = day +"/"+(month+1)+"/"+year;
        mbtnDate.setText(newDate);
    }

    public void btnDate2_OnClick(View view) {
        DateFragment2 df2 = new DateFragment2();
        df2.show(getSupportFragmentManager(),"Date");
    }
    public void setDate2(int year2, int month2,int day2){
        String newDate2 = day2 +"/"+(month2+1)+"/"+year2;
        mbtnDate2.setText(newDate2);
    }

    public void btnViewPdf_OnClick(View view) {
    String mFileName = "ShipmentReport";
    String mFilePath = Environment.getExternalStorageDirectory()+ "/"+mFileName+".pdf";
    }
}
