package com.example.tareas.repositorios;

import com.example.tareas.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository <Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarporEmail(@Param("email") String email);
    
    @Query("SELECT u.amigos FROM Usuario u WHERE u.email = :email")
    public List<Usuario> buscarAmigosPorEmail(@Param("email") String email);

}
    

