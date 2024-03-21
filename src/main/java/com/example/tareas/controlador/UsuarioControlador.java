package com.example.tareas.controlador;

import com.example.tareas.entidades.Amigos;
import com.example.tareas.servicio.UsuarioServicio;
import com.example.tareas.entidades.Usuario;
import com.example.tareas.excepciones.MiException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio us;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
    }

    
    @GetMapping("/registrar")
    public String registroUsuario() {
        return "registro.html";
    }

    @GetMapping("/amigo")
    public String registroAmigo() {
        return "amigo.html";
    }

    @GetMapping("/editar")
    public String editar(ModelMap modelo) throws MiException {
        List<Amigos> amigos = us.listarUsuarios();
        modelo.addAttribute("amigos", amigos);
        return "editar.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) throws MiException {
        List<Amigos> amigos = us.listarUsuarios();
        modelo.addAttribute("amigos", amigos);
        return "lista";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return "servicios.html";
    }

    @GetMapping("/nosotros")
    public String nosotros() {
        return "nosotros.html";
    }

    @GetMapping("/iniciosesion")
    public String loginUsuario(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo, RedirectAttributes redirectAttributes) throws MiException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario != null) {
            modelo.put("usuario", us.getOne(usuario.getId()));
            redirectAttributes.addAttribute("mensaje", "Bienvenido");

            return "inicio.html";
        }
        if (error != null) {

            modelo.put("error", "usuario o contraseña invalidos");
        }
        return "iniciosesion.html";
    }
    
    @PostMapping("/registrar")
    public String registrarUsuario(@RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2,
            ModelMap modelo,
            RedirectAttributes redirectAttributes) throws MiException {
        try {
            us.crearUsuario(nombre, apellido, email, password, password2);
            redirectAttributes.addAttribute("mensaje", "Registo completado con Exito");
        } catch (MiException ex) {
            System.out.println(ex.getMessage());
            redirectAttributes.addAttribute("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("email", email);
            return "registro.html";
        }
        return "inicio.html";
    }

    @PostMapping("/amigo")
    public String registroAmigo(@RequestParam String nombre,
            @RequestParam String nacionalidad, String profesion,
            String telefono, ModelMap modelo) throws MiException {
        try {
            us.registrarAmigo(nombre, nacionalidad, profesion, telefono);
            modelo.put("Exito", "El amigo fue cargado correctamente");
        } catch (MiException ex) {
            Logger.getLogger(UsuarioControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "amigo.html";
        }
        return "amigo.html";
    }
    
    @GetMapping("/perfil")
    public String perfilUsuario(ModelMap modelo) throws MiException{
        List <Usuario> usuario = us.mostrarPerfil();
        modelo.addAttribute("usuario",usuario);
        return "perfil.html";
    }

    @GetMapping("/eliminar")
    public String mostrarFormularioEliminar(ModelMap modelo) throws MiException {
        List<Amigos> amigos = us.listarUsuarios();
        modelo.addAttribute("amigos", amigos);
        return "eliminar.html";
    }

    @PostMapping("/eliminar")
    public String eliminarAmigo(@RequestParam String id, ModelMap modelo) {
        System.out.println("ID recibido para eliminar: " + id); // Imprimir el ID recibido
        try {
            us.borrarAmigo(id);
            modelo.addAttribute("exito", "Amigo eliminado correctamente");
        } catch (MiException e) {
            modelo.addAttribute("error", e.getMessage());
        }
        return "redirect:/inicio";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        System.out.println(id);
        modelo.put("amigo", us.getOneAmigo(id));
        return "modificar.html";
    }

    @PostMapping("/modificar")
    public String modificarAmigo( @RequestParam  String id,@RequestParam String nombre,@RequestParam String nacionalidad,@RequestParam String profesion,String telefono,
            ModelMap modelo) throws MiException {
        System.out.println(modelo);
        System.out.println(id);
        try {
            us.modificarAmigo(id, nombre, profesion, nacionalidad, telefono);
            System.out.println("controlador");
            modelo.put("Exito", "Fue modificado correctamente");
            return "inicio.html";
        } catch (MiException ex) {
            ex.printStackTrace();
            modelo.put("error", ex.getMessage());
            return "inicio.html";
        }
    }
}
