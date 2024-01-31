package com.example.tareas.controlador;

import com.example.tareas.Servicios.ImagenServicio;
import com.example.tareas.entidades.Imagen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    @Autowired
    private ImagenServicio imagenServicio;
    @GetMapping("/vista/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id){
        //Usuario usuario = usuarioServicio.getOne(id);
        Imagen archivo =imagenServicio.getOne(id);
        byte[] imagen = archivo.getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
}
