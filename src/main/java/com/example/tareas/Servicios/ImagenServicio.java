package com.example.tareas.Servicios;

import com.example.tareas.entidades.Imagen;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public interface ImagenServicio {

    @Autowired(required = true)

    public Imagen guardarImagen(MultipartFile archivo);

    public Imagen actualizar(MultipartFile archivo, String idImagen);

    public List<Imagen> listarImagen();

    public void borrarImagen(String idImagen);

    public List<Imagen> guardarImagenLista(List<MultipartFile> archivos);

    public Imagen getOne(String id);
}
