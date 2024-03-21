package com.example.tareas.servicio;

import com.example.tareas.entidades.Amigos;
import com.example.tareas.entidades.Usuario;
import com.example.tareas.enumeraciones.Rol;
import com.example.tareas.excepciones.MiException;
import com.example.tareas.repositorios.AmigosRepositorio;
import com.example.tareas.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio us;
    @Autowired
    private AmigosRepositorio amigoRepo;
    
    @Transactional
    public void crearUsuario(String nombre, String apellido, String email, String password, String password2) throws MiException {
        if (us.buscarporEmail(email) != null) {
            throw new MiException("Ese email ya se encuentra registrado");
        }
        Usuario usuario = new Usuario();
        validar(nombre, apellido, email, password, password2);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        us.save(usuario);
    }

    private void validar(String nombre, String apellido, String email, String password, String password2) throws MiException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new MiException("El apellido no puede ser nulo");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede ser nulo");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener mas de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
    }

    public List<Amigos> listarUsuarios() {
        List<Amigos> amigos = new ArrayList();
        amigos = amigoRepo.findAll();
        return amigos;
    }
    public List<Usuario>mostrarPerfil(){
        List<Usuario> usuario = new ArrayList();
        usuario = us.findAll();
        return usuario;
    }
    @Transactional
    public void registrarAmigo(String nombre, String nacionalidad, String profesion, String telefono) throws MiException {
        validarAmigo(nombre, nacionalidad, profesion, telefono);
        Amigos amigo = new Amigos();
        amigo.setNombre(nombre);
        amigo.setNacionalidad(nacionalidad);
        amigo.setProfesion(profesion);
        amigo.setTelefono(telefono);
        amigoRepo.save(amigo);
    }
    @Transactional
    public void modificarAmigo(String id, String nombre, String nacionalidad, String profesion, String telefono) throws MiException {
        validarAmigo(nombre, nacionalidad, profesion, telefono);
        System.out.println("Entrando al servicio");
        
        Optional<Amigos> respuesta = amigoRepo.findById(id.trim());
        System.out.println(respuesta);
        if (respuesta.isPresent()) {
            System.out.println("modificando amigo nuevo");
            Amigos amigo = respuesta.get();
            amigo.setNombre(nombre);
            amigo.setNacionalidad(nacionalidad);
            amigo.setProfesion(profesion);
            amigo.setTelefono(telefono);
            amigoRepo.save(amigo);
        } else {
            throw new MiException("No se encontró ningún amigo con el ID proporcionado");
        }
    }
    

    public void validarAmigo(String nombre, String nacionalidad, String profesion, String telefono) throws MiException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo");
        }
        if (nacionalidad.isEmpty() || nacionalidad == null) {
            throw new MiException("La nacionalidad no puede ser nulo");
        }
        if (profesion.isEmpty() || profesion == null) {
            throw new MiException("La profesion no puede ser nula");
        }
        if (telefono == null) {
            throw new MiException("El telefono no puede ser nulo");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = (Usuario) us.buscarAmigosPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuarioSession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }
    
    public void guardarUsuarioCompleto(Usuario usuario) {
        us.save(usuario);
    }

    public Usuario getOne(String id) {
        return us.getOne(id);
    }
    public Amigos getOneAmigo(String id){
        return amigoRepo.getOne(id);
    }
    @Transactional
    public void borrarAmigo(String id) throws MiException {
        System.out.println("Eliminando amigo con ID: " + id);
        if (id.isEmpty() || id == null) {
            throw new MiException("El id proporcionado es nulo");
        } else {
            Optional<Amigos> respuesta = amigoRepo.findById(id);
            if (respuesta.isPresent()) {
                Amigos amigo = respuesta.get();
                amigoRepo.delete(amigo);
                System.out.println("Amigo eliminado correctamente"); // Agrega registros de depuración
            } else {
                throw new MiException("No se encontró ningún amigo con el ID proporcionado");
            }
        }
    }

}
