package com.example.bibliotecaapp.ui.PantallaFinal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.databinding.FragmentPantallaFinalBinding;
import com.example.bibliotecaapp.databinding.FragmentSeleccionarBinding;

public class PantallaFinalFragment extends Fragment {
    FragmentPantallaFinalBinding binding;
    Button btnAceptar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPantallaFinalBinding.inflate(inflater, container, false);

        btnAceptar = binding.btnAceptar;

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_mostrarLibros);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

