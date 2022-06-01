package com.example.bibliotecaapp.ui.SeleccionarLibro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bibliotecaapp.AdaptadorRV.Libro;
import com.example.bibliotecaapp.AdaptadorRV.LibrosAdapter;
import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.databinding.FragmentSeleccionarBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.util.zip.Inflater;

public class SeleccionarFragment extends Fragment {

    FragmentSeleccionarBinding binding;
    TextView lblNombre, lblAutor, lblGenero, lblAño, lblDisponibilidad;
    String id = "";
    String Urllibi;
    Button btnThisSeleccionar;
    ImageView pbImagenLib;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSeleccionarBinding.inflate(inflater, container, false);

        lblNombre = binding.lblNombreLibro;
        lblAutor = binding.lblAutorLibro;
        lblGenero = binding.lblGeneroLibro;
        lblAño = binding.lblAnoLibro;
        lblDisponibilidad = binding.lblDisponibilidadLibro;
        btnThisSeleccionar = binding.btnThisSeleccionar;

        btnThisSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.drawable.ic_baseline_warning).
                        setTitle("Alerta")
                        .setMessage("¿Desea seleccionar este libro?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActualizarEstatusLibro();
                                Bundle bundle = new Bundle();
                                bundle.putString("nombre",lblNombre.getText().toString());
                                Navigation.findNavController(v).navigate(R.id.nav_datosUsuarios,bundle);

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        pbImagenLib = binding.imageView3;

        if (getArguments() != null) {
            id = getArguments().getString("id");
        }

        SeleccionarLibrosDeApi();
        return binding.getRoot();

    }

    //aqui jalamos el ID del libro indivualmente para desplegarlos en esta pantalla
    private void SeleccionarLibrosDeApi() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://morrisr-001-site1.htempurl.com/api/Libros/ObtenerProductoXId/" + id;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Aqui es donde se llena la parte grafica con los datos
                            JSONObject libroJSON = new JSONObject(response);
                            TextView lblNombreLibro = binding.lblNombreLibro;
                            lblNombreLibro.setText(libroJSON.getString("nombre"));

                            TextView lblAutorLibro = binding.lblAutorLibro;
                            lblAutorLibro.setText(libroJSON.getString("autor"));

                            TextView lblGeneroLibro = binding.lblGeneroLibro;
                            lblGeneroLibro.setText(libroJSON.getString("genero"));

                            TextView lblAñoLibro = binding.lblAnoLibro;
                            lblAñoLibro.setText(libroJSON.getString("año"));

                            //botenemos el url para adjuntarselo al imageview, para que
                            // se muestre
                            Urllibi = libroJSON.getString("url");
                            Picasso.get().load(Urllibi)
                                    .error(R.drawable.ic_baseline_image_search_24)
                                    .into(pbImagenLib);

                            String mens = "";
                            //aqui valida si el estatus del libro es false o true
                            //en la parte grafica se pinta un Disponible o un no disponible
                            boolean dis = libroJSON.getBoolean("disponible");
                            if(dis == true){
                                mens = "Disponible";
                            }
                            else{
                                mens = "No disponible";
                               btnThisSeleccionar.setEnabled(false);
                            }
                            TextView lblDisponibilidad = binding.lblDisponibilidadLibro;

                            lblDisponibilidad.setText(mens);

                        } catch (Exception ex) {
                            Toast.makeText(getContext(),  ex.getMessage()
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
    }

        //aqui actualizamos el estatus del libro
    // a falso para que aparezca como que no esta disponible
    private void ActualizarEstatusLibro() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://morrisr-001-site1.htempurl.com/api/Libros/ActualizarLibro";
        JSONObject libroJSON = new JSONObject();
        Libro l = new Libro();
        //tenemos que enviar todos los datos iguales a excepcion del apartado disponibilidad
        // que se envia como un false
        l.setId(id);
        l.setNombre(lblNombre.getText().toString());
        l.setAutor(lblAutor.getText().toString());
        l.setGenero(lblGenero.getText().toString());
        l.setAño(Integer.parseInt(lblAño.getText().toString()));
        l.setUrl(Urllibi);
        l.setDisponibilidad(false);
        try {
            libroJSON.put("id", l.getId());
            libroJSON.put("nombre", l.getNombre());
            libroJSON.put("autor", l.getAutor());
            libroJSON.put("genero", l.getGenero());
            libroJSON.put("año", l.getAño());
            libroJSON.put("disponible", l.isDisponibilidad());
            libroJSON.put("url",l.getUrl());
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Error al convertir a JSON: " + ex.getMessage()
                    , Toast.LENGTH_SHORT).show();
        }
        final String body = libroJSON.toString();
        StringRequest request = new StringRequest(Request.Method.PUT,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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