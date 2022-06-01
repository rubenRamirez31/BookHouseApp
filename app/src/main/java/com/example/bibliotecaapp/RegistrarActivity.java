package com.example.bibliotecaapp;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton$InspectionCompanion;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.bibliotecaapp.AdaptadorRV.DatosSolicitud;
import com.example.bibliotecaapp.AdaptadorRV.Registro;
import com.example.bibliotecaapp.databinding.ActivityRegistrarBinding;

import org.json.JSONObject;

public class RegistrarActivity extends AppCompatActivity {

//declaramos los elementos con los que se va a trabaajr
    EditText txtNombreUsuario,txtuser,txtContrasena;
   Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
//los enlazamos con la parte del layoud
      txtNombreUsuario = (EditText)findViewById(R.id.txtNombreUser);
      txtuser = (EditText)findViewById(R.id.txtPseudonimo);
      txtContrasena = (EditText)findViewById(R.id.txtContrasena);

        btnRegistrar = (Button)findViewById(R.id.btnRegistrarse);

       btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override//en este evento llamamos al metodo de insertar libro para que al precionarse se mande el registro
            public void onClick(View v) {
                InsertarRegistro();
            }
        });
    }

    // aqui enviamos los datos para hacer el registro
    private void InsertarRegistro() {
        RequestQueue queue = Volley.newRequestQueue(RegistrarActivity.this);
        String url = "http://morrisr-001-site1.htempurl.com/api/Libros/Registro";
        JSONObject DatoJSON = new JSONObject();
        Registro r = new Registro();
        r.setNombreUser(txtNombreUsuario.getText().toString());
        r.setPseudonimo(txtuser.getText().toString());
        r.setContrasenap(txtContrasena.getText().toString());


        try {
            DatoJSON.put("nombre", r.getNombreUser());
            DatoJSON.put("user", r.getPseudonimo());
            DatoJSON.put("pass", r.getContrasenap());

        } catch (Exception ex) {
            Toast.makeText(RegistrarActivity.this, "Error al convertir a JSON: " + ex.getMessage()
                    , Toast.LENGTH_SHORT).show();
        }
        final String body = DatoJSON.toString();
        StringRequest request = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //en caso de que algun usuario ya tenga los mismos datos de registro
                    // se mandara este error
                    JSONObject respuesta = new JSONObject(response);
                    if (respuesta.getString("estatus").equalsIgnoreCase("200")){
                        Intent i = new Intent(RegistrarActivity.this,MainActivity.class );
                        startActivity(i);
                    }
                        //de otra forma, se da un mensaje de procesado correctamente
                    Toast.makeText(RegistrarActivity.this, "Procesado Correctamente", Toast.LENGTH_LONG).show();

                } catch (Exception error) {

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrarActivity.this, "Error de servidor: "
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
                    Toast.makeText(RegistrarActivity.this, "Error de cuerpo " +
                                    "de solicitud" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        };
        queue.add(request);
    }


}