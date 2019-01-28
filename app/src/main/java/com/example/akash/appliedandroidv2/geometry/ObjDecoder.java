package com.example.akash.appliedandroidv2.geometry;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.akash.appliedandroidv2.Triangle;

import com.example.akash.appliedandroidv2.R;

import java.io.*;
import java.util.ArrayList;

public class ObjDecoder {
    private int id;
    private ArrayList<Vector3> vertices;
    private ArrayList<Vector3> normals;
    private ArrayList<ArrayList<Integer>> drawConfig;
    private ArrayList<Triangle> triangles;


    private Context context;
    public ObjDecoder(int fileId, Context context){
        this.id = fileId;
        this.context = context;
        vertices = new ArrayList<Vector3>();
        normals = new ArrayList<Vector3>();
        drawConfig = new ArrayList<ArrayList<Integer>>();
        triangles = new ArrayList<Triangle>();
        InputStream ir = context.getResources().openRawResource(fileId);
        InputStreamReader isr = new InputStreamReader(ir);

        BufferedReader bufferedReader = new BufferedReader(isr);
        readFile(bufferedReader);
       generateTriangles();
    }

    private void readFile(BufferedReader reader){
        String temp = "";
        int vPointer=0;
        int vnpointer = 0;
        int rPointer = 0;
        int cPointer=0;
        try {
            while ((temp = reader.readLine()) != null) {
                String[] verts = temp.split(" ");
                if (verts[0].compareTo("v") == 0) {
                    vertices.add(new Vector3(Float.parseFloat(verts[1]),
                            Float.parseFloat(verts[2]),
                            Float.parseFloat(verts[3])));
                    vPointer++;
                } else if (verts[0].compareTo("vn") == 0) {
                    normals.add(new Vector3(Float.parseFloat(verts[1]),
                            Float.parseFloat(verts[2]),
                            Float.parseFloat(verts[3])));
                    vnpointer++;

                } else if (verts[0].compareTo("f")==0){
                    ArrayList<Integer> l = new ArrayList<Integer>();
                    l.add(Integer.parseInt(verts[1].charAt(0)+""));
                    l.add(Integer.parseInt(verts[2].charAt(0)+""));
                    l.add(Integer.parseInt(verts[3].charAt(0)+""));
                    drawConfig.add(l);
                   /* drawConfig.set(rPointer,[0] = Integer.parseInt(verts[1].charAt(0)+"");
                    drawConfig[rPointer][1] = Integer.parseInt(verts[2].charAt(0)+"");
                    drawConfig[rPointer][2] = Integer.parseInt(verts[3].charAt(0)+"");*/
                    rPointer++;
                }else{
                       continue;
               }
            }
        }catch (IOException e){
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    private void generateTriangles(){
        for(int i=0;i<drawConfig.size();i++){
            /*float x1 = vertices.get(drawConfig[i][0]-1).getVx()/3;
            float y1 = vertices[drawConfig[i][0]-1].getVy()/3;
            float z1 = vertices[drawConfig[i][0]-1].getVz()/3;

            float x2 = vertices[drawConfig[i][1]-1].getVx()/3;
            float y2 = vertices[drawConfig[i][1]-1].getVy()/3;
            float z2 = vertices[drawConfig[i][1]-1].getVz()/3;

            float x3 = vertices[drawConfig[i][2]-1].getVx()/3;
            float y3 = vertices[drawConfig[i][2]-1].getVy()/3;
            float z3 = vertices[drawConfig[i][2]-1].getVz()/3;*/
            float x1 = vertices.get(drawConfig.get(i).get(0)-1).getVx();
            float y1 = vertices.get(drawConfig.get(i).get(0)-1).getVy();
            float z1 = vertices.get(drawConfig.get(i).get(0)-1).getVz();

            float x2 = vertices.get(drawConfig.get(i).get(1)-1).getVx();
            float y2 = vertices.get(drawConfig.get(i).get(1)-1).getVy();
            float z2 = vertices.get(drawConfig.get(i).get(1)-1).getVz();

            float x3 = vertices.get(drawConfig.get(i).get(2)-1).getVx();
            float y3 = vertices.get(drawConfig.get(i).get(2)-1).getVy();
            float z3 = vertices.get(drawConfig.get(i).get(2)-1).getVz();

            float[] v = {x1,y1,z1,
                        x2,y2,z2,
                        x3,y3,z3};
            float[] c = {(float)Math.random(),(float)Math.random(),(float)Math.random(),1.0f};
            triangles.add(new Triangle(v,c));
        }
    }

    public void drawTriangles(float[] mMVPmatrix){
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);

        float[] scratcht = new float[16];
        float[] tempMoveMat = new float[16];
        Matrix.setIdentityM(tempMoveMat, 0);
        //Matrix.translateM(tempMoveMat, 0, 0.0f, 0.0f, 0f);
        //Matrix.rotateM(tempMoveMat, 0, angle, 1f, 0f, 0f);

        //float angle2 = 0.090f * ((int) time);
        Matrix.rotateM(tempMoveMat, 0, angle, 1f, 0f, 0f);
        //Matrix.rotateM(tempMoveMat, 0, angle, 0f, 1f, 1f);

        Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mMVPmatrix, 0);
        for(int i=0;i<triangles.size();i++){
            triangles.get(i).draw(scratcht);
       }

    }

    public ArrayList<Vector3> getVertices(){return this.vertices;}
}
