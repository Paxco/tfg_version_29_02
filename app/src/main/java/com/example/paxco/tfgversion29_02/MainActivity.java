package com.example.paxco.tfgversion29_02;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paxco.tfgversion29_02.R;
import com.mcc.ul.AiChanMode;
import com.mcc.ul.AiDevice;
import com.mcc.ul.AiScanOption;
import com.mcc.ul.AiUnit;
import com.mcc.ul.AoDevice;
import com.mcc.ul.AoScanOption;
import com.mcc.ul.AoUnit;
import com.mcc.ul.DaqDevice;
import com.mcc.ul.DaqDeviceDescriptor;
import com.mcc.ul.DaqDeviceManager;
import com.mcc.ul.Status;
import com.mcc.ul.ULException;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.EnumSet;


public class MainActivity extends AppCompatActivity {


    private double[][] curvas;
    private int tamcurva ;

    private int cuenta;
    private int salto;
    private TextView cuentavalor;

    private TextView cuentatexto;



    // DE LA LIBRERIA : GRAFICAS
    private GraphicalView gview;
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer renderer;
    private XYSeries serieA, serieB, serieC;
    private XYSeriesRenderer serieAr, serieBr, serieCr;

    // VARIABLES PARA LA TARJEDA DAQ

    private DaqDeviceManager ddm; // Permite crear y detectar DAQ
    private DaqDevice dd;// Representa la DAQ y metodos I/O
    private AiDevice aid;// contiene  AInScan -> Lectura
    private AoDevice aod;// contiene  AoutScan? AOut? -> Escritura
    ArrayList<DaqDeviceDescriptor> ddi;

    // INTERFAZ DE USUARIO
    private Button marcha;
    private Button parada;
    private Button parada_emergencia;
    private SeekBar barra;




    private void cambia(){
        cuentatexto.setText(String.valueOf(salto));
        serieA.clear();
        serieB.clear();
        serieC.clear();
        for(int i=0;i<tamcurva;i++){
          /*  curvas[0][i]=amplitud*Math.sin(2 * Math.PI * (double) i / tamcurva);
            serieA.add(i*360.0/tamcurva, curvas[0][i]);
            curvas[1][i]=amplitud*Math.cos(2 * Math.PI * (double) i / tamcurva);
            serieB.add(i*360.0/tamcurva, curvas[1][i]);
            curvas[2][i]=amplitud*Math.cos(2 * Math.PI * (double) i / tamcurva);
            serieC.add(i*360.0/tamcurva, curvas[1][i]);*/
        }
        gview.repaint();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* // INICIALIZACION TARJETA DAQ

        ddm = new DaqDeviceManager(this);
        ddi = ddm.getDaqDeviceInventory();
        dd = ddm.createDaqDevice(ddi.get(0));

       */ // CONEXION A DAQ

       /* try{
            dd.connect();
            dd.flashLed(3);
            aid = dd.getAiDev();
            aod = dd.getAoDev();

        }
        catch (ULException e){
            e.printStackTrace();
        }
*/



        // ADQUISICION DE DATOS [Lectura]

     /*   try{
            aid.aInScan(0, 1, AiChanMode.DIFFERENTIAL, Range.BIP2PT5VOLTS,
                    100, 5000, EnumSet.of(AiScanOption.BURSTIO),
                    AiUnit.VOLTS, curvas);
            int aidstatus = aid.getStatus().currentStatus;
            while(aidstatus != Status.IDLE){
                SystemClock.sleep(10);
                aidstatus = aid.getStatus().currentStatus;
            }
            aid.stopBackground();
                }
        catch (ULException e){
            e.printStackTrace();
            }*/



        dataset = new XYMultipleSeriesDataset();
        renderer = new XYMultipleSeriesRenderer();
        serieA = new XYSeries("Velocidad");
        serieB = new XYSeries("Velocidad real");
        serieC = new XYSeries("Consigna de frecuencia");
        dataset.addSeries(serieA);
        dataset.addSeries(serieB);
        dataset.addSeries(serieC);
        serieAr = new XYSeriesRenderer();
        serieBr = new XYSeriesRenderer();
        serieCr = new XYSeriesRenderer();
        renderer.addSeriesRenderer(serieAr);
        renderer.addSeriesRenderer(serieBr);
        renderer.addSeriesRenderer(serieCr);


        LinearLayout grafica = (LinearLayout) findViewById(R.id.grafica);
        gview = ChartFactory.getLineChartView(this, dataset, renderer);
        grafica.addView(gview, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        for(int i = 0; i < tamcurva; i++){
            serieA.add(i*360.0/tamcurva, curvas[0][i]);
            serieB.add(i*360.0/tamcurva, curvas[1][i]);
            serieC.add(i*360.0/tamcurva, curvas[2][i]);
        }

// trazado de las graficas

        renderer.setMargins(new int[]{40, 60, 20, 20});
        renderer.setMarginsColor(Color.rgb(236, 236, 236));
        renderer.setAxesColor(Color.BLACK);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
        renderer.setLabelsTextSize(20);
        renderer.setXTitle("tiempo");
        renderer.setYTitle("velocidad");
        renderer.setAxisTitleTextSize(20);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setYAxisMax(10);
        renderer.setYAxisMin(0);
        renderer.setXAxisMax(100);//test
        renderer.setXAxisMin(0);//test
        renderer.setGridColor(Color.DKGRAY);
        renderer.setShowGrid(true);
        renderer.setLegendTextSize(20);
        serieAr.setColor(Color.BLUE);
        serieBr.setColor(Color.RED);
        serieCr.setColor(Color.GREEN);





        //CONTROLES DEL USUARIO
        //BOTONES

        marcha=(Button) findViewById(R.id.marcha);
        parada=(Button) findViewById(R.id.parada);
        parada_emergencia=(Button) findViewById(R.id.parada_emergencia);

        marcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //pasamos la orden de marcha al VF [Escritura]
                Toast.makeText(getApplicationContext(), "Motor en marcha", Toast.LENGTH_LONG).show();

                /*try{
                    aod.aOutScan(0, 1, Range.BIP2PT5VOLTS,
                            100, 5000, EnumSet.of(AoScanOption.BURSTMODE),
                            AoUnit.VOLTS, curvas);

                    int aidstatus = aid.getStatus().currentStatus;
                    while(aidstatus != Status.IDLE){
                        SystemClock.sleep(10);
                        aidstatus = aid.getStatus().currentStatus;
                    }
                    aid.stopBackground();
                }
                catch (ULException e){
                    e.printStackTrace();
                }*/


            }
        });


        parada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //pasamos la orden de paro al VF
                 Toast.makeText(getApplicationContext(), "Parando motor", Toast.LENGTH_LONG).show();


            }
        });


          parada_emergencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //pasamos la orden de paro de emergencia al VF
                 Toast.makeText(getApplicationContext(), "Activando paro de emergencia", Toast.LENGTH_LONG).show();


            }
        });




        //SEEKBAR
       /* cuenta = 0;
        salto = 5;
        cuentavalor = (TextView)findViewById(R.id.cuentavalor);
        v_seek = (SeekBar)findViewById(R.id.barra);
        cuentavalor.setText(String.valueOf(salto));
        v_seek.setProgress(salto);

        v_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar v_seek, int valor, boolean fromUser) {
                salto = valor;
                cambia();
            }

            @Override
            public void onStartTrackingTouch(SeekBar v_seek) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar v_seek) {
            }
        });*/












    }
}
