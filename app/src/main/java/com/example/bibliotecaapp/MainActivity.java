package com.example.bibliotecaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bibliotecaapp.AdaptadorRV.Login;
import com.example.bibliotecaapp.AdaptadorRV.Registro;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static int transalateLeft = R.anim.transalate_left_side;
    Button btnIniciarSesion, btnRegistrar;
    EditText txtMainUsuario, txtMainContrasena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMainUsuario = (EditText)findViewById(R.id.txtLoginUsuario);
        txtMainContrasena = (EditText)findViewById(R.id.txtLoginContraseña);

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnIniciarSesion.setEnabled(false);

        txtMainUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().equals("")){
                    btnIniciarSesion.setEnabled(false);
                }
                else{
                    btnIniciarSesion.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            //Se realiza una validacion para que el boton este habilitado
            //o deshabilitado en funcion de si hay texto en el los edittext
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")){
                    btnIniciarSesion.setEnabled(false);
                }
                else{
                    btnIniciarSesion.setEnabled(true);
                }

            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ValidarLogin();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrarActivity.class));
            }
        });

    }

    //creamos el metodo validar login
    private void ValidarLogin() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://morrisr-001-site1.htempurl.com/api/Libros/Login";
        JSONObject DatoJSON = new JSONObject();
        Login lg = new Login();
        //obtenemos los datos de los editText
        lg.setPseudonimo(txtMainUsuario.getText().toString());
        lg.setPassword(txtMainContrasena.getText().toString());

        try {
            //aqui mandamos los datos en formato json
            DatoJSON.put("user", lg.getPseudonimo());
            DatoJSON.put("pass", lg.getPassword());

        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Error al convertir a JSON: " + ex.getMessage()
                    , Toast.LENGTH_SHORT).show();
        }
        final String body = DatoJSON.toString();
        StringRequest request = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //en caso de ser satisfactorio el inciio de sesion
                    //retornamos una respuesta de que se inicio sesion correctamente
                    JSONObject respuesta = new JSONObject(response);
                    if (respuesta.getString("estatus").equalsIgnoreCase("200")){
                        Toast.makeText(MainActivity.this,"Bienvenido: " +txtMainUsuario.getText().toString(),
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainActivity.this,PrincipalActivity.class );
                        startActivity(i);
                    }
                    else {
                        //en el caso contrario enviamos el siguiente mensaje
                        Toast.makeText(MainActivity.this,"Usuario y/o Contraseña incorrectos",
                                Toast.LENGTH_LONG).show();
                    }


                } catch (Exception error) {

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error de servidor: "
                                + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    if (body == null) {
                        return null;
                    } else {
                        return body.getBytes("utf-8");
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Error de cuerpo " +
                                    "de solicitud" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        };
        queue.add(request);
    }
}