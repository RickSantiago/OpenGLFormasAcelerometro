package computacaografica.openglincial;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//classe principal da aplicacao android
public class OpenGL extends Activity
{
    //Cria o objeto que representa a superficie de desenho
    GLSurfaceView superficieDesenho = null;
    Renderizador objetoDesenho = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        //metodo chamado durante a criacao do metodo
        super.onCreate(savedInstanceState);

        //Instancia o objeto de desenho
        objetoDesenho = new Renderizador(this);

        //Instancia a superficie de desenho e a liga a tela
        superficieDesenho = new GLSurfaceView(this);

        //Liga o objeto de desenho na superficie de desenho
        superficieDesenho.setRenderer(objetoDesenho);

        //publica a superficie de desenho na tela
        setContentView(superficieDesenho);

//        //registra o ontouch
//        superficieDesenho.setOnTouchListener(objetoDesenho);
    }

}

//class Renderizador implements GLSurfaceView.Renderer,  View.OnTouchListener {
class Renderizador implements GLSurfaceView.Renderer{

    int posicao = 0;
    int altura = 0;
    int largura = 0;

    float posicaoX=0;
    float posicaoY=0;
    float direcao = 1.0f;
    float direcao2 = 1.0f;
    float angulo = 0;
    float angulo2 = 0;

    AGAccelerometer acelerometro = new AGAccelerometer();

    ArrayList <Posicao> posicoes = new ArrayList<>();

    float[] vetorJava =
            {       -100, -100,
                    -100, 100,
                    100, -100,
                    100, 100};

    int identificadorTextura1 = 0;
    int identificadorTextura2 = 0;

    OpenGL contexto = null;

    public Renderizador (OpenGL pContexto)
    {
        contexto = pContexto;
    }

    @Override
    public void onSurfaceCreated(GL10 vrOpengl, EGLConfig eglConfig)
    {
        //metodo chamado no momento da criacao da superficie
        vrOpengl.glClearColor(1, 1, 1, 1); //configura a cor q especifiquei
       //variavel de referencia da classe do acelerometro
        acelerometro.init(contexto);
        acelerometro.onResume();
    }

    //metodo chamado quando a tela sofrer modificacao e na inicializacao do app
    @Override
    public void onSurfaceChanged(GL10 vrOpengl, int width, int height)
    {
        altura = height;
        largura = width;
        posicaoX = largura;
        posicaoY = altura;


        vrOpengl.glClearColor(0, 0, 0, 1.0f); //configura a cor q especifiquei

        vrOpengl.glViewport(0, 0, width, height);// configura a janela de visualização para o mesmo tamanho da tela
        //pode ter varias viewport

        vrOpengl.glMatrixMode(GL10.GL_PROJECTION); //OpenGl aponta p/ matriz de projecao

        vrOpengl.glLoadIdentity();// carrega na matriz de projecao p/ poder configurar a matriz

        vrOpengl.glOrthof(0, width, 0, height, 1.0f, -1.0f); //funcao que configura a projecao ortogonal(configura a area de desenho)

        vrOpengl.glMatrixMode(GL10.GL_MODELVIEW); //armazena transformaçoes geometricas

        vrOpengl.glLoadIdentity();//E carrega matriz identidade p/ modelVIew

        vrOpengl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//habilita opengel desenho


    }


    FloatBuffer retornaVetorC(float[] vetorJava)
    {

        //aloca memoria necessaria para as coordenadas de desenho e encapsula no vetor java na classe FLoatBuffer
        ByteBuffer vetorBytes = ByteBuffer.allocateDirect(vetorJava.length * 4);
        vetorBytes.order(ByteOrder.nativeOrder());

        FloatBuffer vetorC = vetorBytes.asFloatBuffer();
        vetorC.clear();
        vetorC.put(vetorJava);
        vetorC.flip();

        return vetorC;
    }



    @Override
    public void onDrawFrame(GL10 vrOpengl)
    {
        vrOpengl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        vrOpengl.glVertexPointer(2, GL10.GL_FLOAT, 0, retornaVetorC(vetorJava));
        vrOpengl.glLoadIdentity(); // Zera matriz
            //Desenha quadrado branco
            // muda a referencia de posição da matriz
            vrOpengl.glColor4f(1, 1, 1, 0); // Muda cor do desenho
            vrOpengl.glTranslatef(posicaoX /2, posicaoY/2, 0); // Translada o desenho
            // vrOpengl.glRotatef(angulo,0,0,1);
            vrOpengl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); // Desenha o desenho
            // angulo +=8;

        acelerometro.getAccelZ();

        if(acelerometro.getAccelX() < -2)
        {
          posicaoX -= 10;
        }
        else if (acelerometro.getAccelX() > 2)
        {
            posicaoX += 10;
        }
        if (acelerometro.getAccelY() > 2)
        {
            posicaoY += 10;
        }
        else if(acelerometro.getAccelY() < -2)
        {
            posicaoY -= 10;
        }



//        for (Posicao p: posicoes)
//        {
//            vrOpengl.glLoadIdentity(); // Zera matriz
//            //----------------------------------
//
//            //Desenha quadrado branco
//            // muda a referencia de posição da matriz
//            vrOpengl.glColor4f(1, 1, 1, 0); // Muda cor do desenho
//            vrOpengl.glTranslatef(p.getX(), p.getY(),0); // Translada o desenho
//            // vrOpengl.glRotatef(angulo,0,0,1);
//            vrOpengl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); // Desenha o desenho
//            // angulo +=8;
//        }

    }
//
//    public boolean onTouch(View view, MotionEvent motionEvent)
//    {
//
//        posicoes.add(new Posicao( motionEvent.getX(),altura - motionEvent.getY()));
//
//        return true;
//    }

}

class Posicao
{
    float x=0;
    float y=0;

    public Posicao(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
