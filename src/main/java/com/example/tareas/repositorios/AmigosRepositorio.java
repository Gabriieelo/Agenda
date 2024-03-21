package com.example.tareas.repositorios;

import com.example.tareas.entidades.Amigos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface AmigosRepositorio extends JpaRepository<Amigos, String> {
    
    @Query("SELECT a FROM Amigos a WHERE a.id = :id")
    public List<Amigos> buscarAmigosPorUsuario(@Param("id") String id);
}
