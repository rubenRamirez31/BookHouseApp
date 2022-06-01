package com.example.bibliotecaapp.ui.MostrarLibro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bibliotecaapp.AdaptadorRV.Libro;
import com.example.bibliotecaapp.AdaptadorRV.LibrosAdapter;
import com.example.bibliotecaapp.PrincipalActivity;
import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.databinding.FragmentMostrarLibroBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MostrarLibrosFragment extends Fragment {
    //declaramos el array
    FragmentMostrarLibroBinding binding;
    ArrayList<Libro> libros;
    private SwipeRefreshLayout swip1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        libros = new ArrayList<>();
        binding = FragmentMostrarLibroBinding.inflate(inflater,container,false);
        ObtenerLibrosDeApi();
        swip1 = binding.swipep;
        swip1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(),"ola",Toast.LENGTH_SHORT).show();

                swip1.setRefreshing(false);
            }
        });

        return  binding.getRoot();


    }
    //se crea el metodo para obtener  los libros de la wweb api proporcionando la direccion
    private void ObtenerLibrosDeApi(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://morrisr-001-site1.htempurl.com/api/Libros/ObtenerLibros";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++){
                                //este es el cuerpo de la respuesta que nos retirnara
                                // en JSON
                                JSONObject libroJSON = array.getJSONObject(i);
                                Libro l = new Libro();
                                l.setId(libroJSON.getString("id"));
                                l.setAutor(libroJSON.getString("autor"));
                                l.setAño(libroJSON.getInt("año"));
                                l.setDisponibilidad(libroJSON.getBoolean("disponible"));
                                l.setNombre(libroJSON.getString("nombre"));
                                l.setGenero(libroJSON.getString("genero"));
                                l.setUrl(libroJSON.getString("url"));
                                libros.add(l);
                            }
                            //hacemos referencia a lo creado anteriormente
                            LibrosAdapter adapter = new LibrosAdapter(libros);
                            RecyclerView rv = binding.librosRV;
                            rv.setAdapter(adapter);
                            rv.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getContext(),
                                    "Error al procesar lsita" + ex.getMessage()
                            ,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override//se crea el error en caso de que exista algun problema
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage() , Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}