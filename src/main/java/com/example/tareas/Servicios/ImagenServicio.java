package com.example.tareas.Servicios;

import com.example.tareas.entidades.Imagen;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service

public interface ImagenServicio {
    
    @Autowired(required=true)
    
    @Transactional
    public Imagen guardarImagen(MultipartFile archivo);

    @Transactional
    public Imagen actualizar(MultipartFile archivo, String idImagen);

    @Transactional
    public List<Imagen> listarImagen();

    @Transactional
    public void borrarImagen(String idImagen);

    @Transactional
    public List<Imagen> guardarImagenLista(List<MultipartFile> archivos);

    @Transactional
    public Imagen getOne(String id);
}
