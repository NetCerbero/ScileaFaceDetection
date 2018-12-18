package com.universidad.bluenet.opencvagain;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;

public class RegistroPython extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2,MyTaskInformer{
    String key;
    public static String TAG = "Python";
    JavaCameraView javaCameraView;
    int tActual;
    int tRetardo;
    Mat mRgba;
    boolean sw;
    Dialog myDialog;
    CascadeClassifier cascadeClassifier;
    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    cargarCascade();
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_python);
        sw = false;
        tRetardo = 0;
        javaCameraView = findViewById(R.id.registroPython);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.i(TAG,"Open successfully");

            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }else{
            Log.i(TAG,"not opened");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this,baseLoaderCallback);
        }
    }
    @Override
    public void onTaskDone(String output) {
        //Log.i(TAG, output);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(width,height, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rotate(mRgba,270);
        }
        Mat grayscaleImage = new Mat();
        Imgproc.cvtColor(mRgba, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        MatOfRect faces = new MatOfRect();
        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            //cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2, new Size(mRgba.width(), mRgba.height()), new Size());
            cascadeClassifier.detectMultiScale(grayscaleImage,faces,1.3,5);
            Log.i(TAG,"usando el cascade");
        }else{
            Log.i(TAG,"No se cargo cascade");
        }
        // If there are any faces found, draw a rectangle around it
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i <facesArray.length; i++) {
            Mat areaFace = mRgba.submat(facesArray[i].y,facesArray[i].y + facesArray[i].height,facesArray[i].x, facesArray[i].x + facesArray[i].width);
            Bitmap bitmap = Bitmap.createBitmap(areaFace.cols(), areaFace.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(areaFace, bitmap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            String analizar = "AnalizarFoto+";
            byte[] array = new byte[analizar.length() + byteArrayOutputStream.size()];
            System.arraycopy(analizar.getBytes(),0,array,0,analizar.length());
            System.arraycopy(byteArrayOutputStream.toByteArray(),0,array,analizar.length(),byteArrayOutputStream.size());
            //byte[] array = byteArrayOutputStream.toByteArray();
            Log.i(TAG,"length face " + array.length);
            Log.i(TAG,"Se encontro rostros");
            //if(sw){
                //sw = false;
                Coneccion coneccion = new Coneccion(this);
                coneccion.execute(array);
            //}
            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
        }
        return mRgba;
    }

    private void cargarCascade(){
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();


            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {

        }
    }

    public void rotate(Mat image, double angle) {
        //Calculate size of new matrix
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int w = getWindowManager().getDefaultDisplay().getWidth();
        int h = getWindowManager().getDefaultDisplay().getHeight();
        int newWidth = (int) (image.width() * cos + image.height() * sin);
        int newHeight = (int) (image.width() * sin + image.height() * cos);

        // rotating image
        Point center = new Point(newWidth / 2, newHeight / 2);
        Mat rotMatrix = Imgproc.getRotationMatrix2D(center, angle, 1.0); //1.0 means 100 % scale

        //Size size = new Size(newWidth, newHeight);
        Imgproc.warpAffine(image, image, rotMatrix, image.size());
    }

    public class Coneccion extends AsyncTask<byte[],Void,Void> {


        String IP = "192.168.43.15";
        int PORT = 10369;
        String TAG = "ACTIVITYSOCKET";
        String reg;
        private long startTime = 0l;
        private Socket connectionSocket;
        private WeakReference<MyTaskInformer> mCallBack;

        public Coneccion(MyTaskInformer callback) {
            this.mCallBack = new WeakReference<>(callback);
        }

        @Override
        protected Void doInBackground(byte[]... bytes) {
            try{
                connectionSocket = new Socket(IP,PORT);
                OutputStream out = connectionSocket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);

                InputStream in = connectionSocket.getInputStream();
                DataInputStream din = new DataInputStream(in);
                dos.write(bytes[0],0,bytes[0].length);
                connectionSocket.shutdownOutput();

                Log.i(TAG,"datos enviados");
                byte[] buffer = new byte[100];
                din.read(buffer);
                reg = new String(buffer,"utf-8").trim();
                Log.i("Respuesta",reg);
                Log.i(TAG,"datos recibidos");
                din.close();
                in.close();

                Log.i(TAG,"Cerrando conexion");
                connectionSocket.close();
            } catch (UnknownHostException e) {
                //e.printStackTrace();
                Log.i(TAG,e.toString());
            } catch (IOException e) {
                //e.printStackTrace();
                Log.i(TAG,e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyTaskInformer callBack = mCallBack.get();
            if(callBack != null) {
                Log.i("RSP","enviando respuesta a main");
                callBack.onTaskDone(this.reg);
            }
        }
    }
}
