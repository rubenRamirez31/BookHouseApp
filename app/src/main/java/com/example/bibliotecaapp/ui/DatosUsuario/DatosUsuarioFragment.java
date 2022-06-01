package com.example.bibliotecaapp.ui.DatosUsuario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.bibliotecaapp.AdaptadorRV.Libro;
import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.databinding.FragmentDatosUsuarioBinding;
import com.example.bibliotecaapp.databinding.FragmentPantallaFinalBinding;
import com.example.bibliotecaapp.databinding.FragmentSeleccionarBinding;

import org.json.JSONObject;

public class DatosUsuarioFragment extends Fragment {
    //creamos el binding de este fragmento
    FragmentDatosUsuarioBinding binding;
    // en el caso de los fragments tenemos que crear el tipo de componente
    // con los que vamos a trabajar
    EditText txtNombre, txtApePat, txtApeMat, txtCalle, txtNumExt, txtColonia;
    Button btnSolicitar;
    String nombrelibro = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //los en enlazamos con la parte que se declaro en el layout
        binding = FragmentDatosUsuarioBinding.inflate(inflater, container, false);
        txtNombre = binding.txtNombreSolicitante;
        txtApePat = binding.txtApeMat;
        txtApeMat = binding.txtApeMat;

        txtCalle = binding.txtCalle;
        txtNumExt = binding.txtNumExt;
        txtColonia = binding.txtColonia;

        btnSolicitar = binding.btnSolicitar;

        //una vez enviado el cuerpo de la solicitud y registrado correctamente
        // nos movemos a la panalla final
        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertarDatosSolicitud();
                 Navigation.findNavController(v).navigate(R.id.nav_pantallaFinal);

            }
        });

        if (getArguments() != null) {
            nombrelibro = getArguments().getString("nombre");
        }

        return binding.getRoot();
    }

    //este es el metodo para poder enviar la solicitud con un cuerpo
    private void InsertarDatosSolicitud() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://morrisr-001-site1.htempurl.com/api/Libros/InsertarSolicitud";
        JSONObject DatoJSON = new JSONObject();
        DatosSolicitud ds = new DatosSolicitud();
        //le mandamos los datos que se ingresan a la clase encapsulada
        ds.setNombreLibro(nombrelibro);
        ds.setNombreSolicitante(txtNombre.getText().toString() + " " + txtApePat.getText().toString() + " " + txtApeMat.getText().toString());
        ds.setDomicilio(txtCalle.getText().toString() + " " + txtNumExt.getText().toString() + "," + txtColonia.getText().toString());
        try {
            //y enviamos los datos en formato json
            DatoJSON.put("nombre", ds.getNombreSolicitante());
            DatoJSON.put("libro", ds.getNombreLibro());
            DatoJSON.put("direccion", ds.getDomicilio());

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Error al convertir a JSON: " + ex.getMessage()
                    , Toast.LENGTH_SHORT).show();
        }
        final String body = DatoJSON.toString();
        StringRequest request = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //esta es la parte de la respuestamque regresa un mensaje para saber que se ha
                    //correctamente la informacion
                    JSONObject respuesta = new JSONObject(response);
                    Toast.makeText(getContext(), "Procesado Correctamente", Toast.LENGTH_LONG).show();

                } catch (Exception error) {

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error de servidor: "
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
                    Toast.makeText(getContext(), "Error de cuerpo " +
                                    "de solicitud" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        };
        queue.add(request);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}