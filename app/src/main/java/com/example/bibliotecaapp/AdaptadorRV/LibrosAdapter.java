package com.example.bibliotecaapp.AdaptadorRV;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bibliotecaapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LibrosAdapter extends RecyclerView.Adapter<LibrosViewHolder> {
    //declaramos la lista de libros
    List<Libro> libros;

    public LibrosAdapter(List<Libro> libros) {
        this.libros = libros;
    }

    @NonNull
    @Override
    public LibrosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //creamos el contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View librosRow = inflater.inflate(R.layout.libro_row, parent, false);
        //hacemos referencia al fragment
        LibrosViewHolder librosViewHolder = new LibrosViewHolder(librosRow);
        return librosViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull LibrosViewHolder holder, int position) {

        //agregamos todos los componentes visuales que estan ya en el libro_rows
        Libro l = libros.get(position);
        String urll = l.getUrl();
        ImageView pbImagenLib = holder.ImgMostrarImagenLibro;
        Picasso.get().load(urll)
                .error(R.drawable.ic_baseline_image_search_24)
                .into(pbImagenLib);

        TextView lblNombreLibro = holder.lblNombreLibro;
        lblNombreLibro.setText(l.getNombre());
        TextView lblGeneroLibro = holder.lblGeneroLibro;
        lblGeneroLibro.setText(l.getGenero());
        Button btnSeleccionar = holder.btnSeleccionarLibro;
        //con el boton seleccionamos individualmente cada libro mediante
        //la obtencion del id para desplegarlos en la siguiente pantalla
        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id",l.getId());
                Navigation.findNavController(view).navigate(R.id.nav_seleccionar,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }
}
