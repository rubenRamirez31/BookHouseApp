package com.example.bibliotecaapp.AdaptadorRV;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;

public class LibrosViewHolder extends RecyclerView.ViewHolder {
    //creamos por los elementos con los que vamos a trabajar y
    //les asignamos un nombre
    TextView lblNombreLibro, lblGeneroLibro;
    Button btnSeleccionarLibro, btnThisSeleccionar;
   EditText txtNombre,txtApePat,txtApeMat,txtCalle,txtNumExt,txtColonia;
   ImageView ImgMostrarImagenLibro;

    public LibrosViewHolder(@NonNull View itemView) {
        super(itemView);
        //llamamos a los elementos anteriores pero ahora los enlazamos
        // con su parte grafica que se declaro en el layout
        ImgMostrarImagenLibro = itemView.findViewById(R.id.imageView9);
        lblNombreLibro =  itemView.findViewById(R.id.lblNombreLibroRV);
        lblGeneroLibro = itemView.findViewById(R.id.lblGeneroLibroRV);
        btnSeleccionarLibro = itemView.findViewById(R.id.btnSeleccionarLibro);
        btnThisSeleccionar = itemView.findViewById(R.id.btnThisSeleccionar);
    }
}
