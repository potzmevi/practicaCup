package com.example.practicacup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.io.StringReader;
import java.security.interfaces.DSAPrivateKey;
import java.util.ArrayList;
import java.util.List;

import Forma.ErrorLexico;
import Forma.Formas;
import Forma.Operador;
import codigo.LexerCup;
import codigo.Parser;

public class MainActivity extends AppCompatActivity {
    ArrayList<Formas> formasMain = new ArrayList<Formas>();
    ArrayList<Forma.Error> errores = new ArrayList<Forma.Error>();
    ArrayList<ErrorLexico> erroresLexicos = new ArrayList<ErrorLexico>();
    ArrayList<Operador> operadores = new ArrayList<Operador>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendForma(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        Bundle bundle = new Bundle();
        EditText editText = (EditText) findViewById(R.id.editText);

        String message = editText.getText().toString();
        LexerCup lexer = new  LexerCup(new StringReader(message));
        Parser s = new Parser(lexer);
        erroresLexicos=lexer.getErrorsLexList();
        formasMain=s.getListaFormas();
        errores=s.getErrorsList();
        operadores=s.getListaOperadores();
        TextView error =(TextView) findViewById(R.id.errortxt);
        try {
            error.setText("");
            s.parse();
            bundle.putSerializable("formas",(Serializable)formasMain);
            bundle.putSerializable("operadores",(Serializable)operadores);
            intent.putExtra("Bundle_Array",bundle);
            if (formasMain.size()!=0) {
                startActivity(intent);
            }
        } catch (Exception ex) {
            System.out.println("Error irrecuperable " + ex);
            for(int i=0; i< errores.size();i++){
                error.setText(error.getText()+ "Error Sintactico con el Token: "+ errores.get(i).getLexema()+" este no pertenece a la estructura- linea: "+ errores.get(i).getLinea()+"  columna: "+ errores.get(i).getColumna()+". Corrige e intenta de nuevo. \n");
            }
        }
    }

    public void sendErrores(View view){
        if(errores.size()>0 || erroresLexicos.size()>0){
            Intent intent = new Intent(this, Errores.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("errores",(Serializable)errores);
            bundle.putSerializable("erroresLexicos",(Serializable)erroresLexicos);
            intent.putExtra("ArrayErrores",bundle);
            startActivity(intent);
        }else{
            TextView error =(TextView) findViewById(R.id.errortxt);
            error.setText("Debe tener errores para generar el reporte");
        }
    }

}