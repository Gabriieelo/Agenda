package com.example.tareas.servicio;

import com.example.tareas.entidades.Usuario;
import com.example.tareas.excepciones.MiException;
import com.example.tareas.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio us;
    @Transactional
    public void crearUsuario(String nombre, String apellido, String email, String password, String password2, Integer dni, Date FechaDeNacimiento, Integer telefono) throws MiException {
        validar(nombre, apellido, email, password, password2, dni, FechaDeNacimiento, telefono);
        if (us.buscarporEmail(email) != null) {
            throw new MiException("Ese email ya se encuentra registrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setDni(dni);
        usuario.setFechaDeNacimiento(FechaDeNacimiento);
        usuario.setTelefono(telefono);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        us.save(usuario);
    }
    @Transactional
    public void actualizar(String id, String nombre, String apellido, String email, String rol, Integer dni, Date FechaDeNacimiento, Integer telefono) {
        try {
            Optional<Usuario> respuesta = us.findById(id);
            System.out.println(respuesta);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setEmail(email);
                usuario.setDni(dni);
                usuario.setFechaDeNacimiento(FechaDeNacimiento);
                usuario.setTelefono(telefono);
                us.save(usuario);
            } else {
                throw new IllegalArgumentException("El usuario con el ID proporcionado no existe");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Error al actualizar usuario: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error general al actualizar usuario: " + ex.getMessage());
        }
    }
        public List<Usuario> listarUsuarios() {
        return us.findAll();
    }

    private void validar(String nombre, String apellido, String email, String password, String password2, Integer dni, Date FechaDeNacimiento, Integer telefono) throws MiException {

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
        if (dni == null || dni.equals("")) {
            throw new MiException("El dni no puede ser nulo");
        }
        if (FechaDeNacimiento == null || FechaDeNacimiento.equals("")) {
            throw new MiException("La fecha de nacimiento no puede ser nulo");
        }

        if (telefono == null) {
            throw new MiException("El telefono no puede ser nulo");
        }
    }
        public void guardarUsuarioCompleto(Usuario usuario) {
        us.save(usuario);
    }
         public void borrarUsuario(String id) throws MiException {
        if (id.isEmpty() || id.equals("")) {
            throw new MiException("El id proporcionado es nulo");
        } else {
            Optional<Usuario> respuesta = us.findById(id);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                us.delete(usuario);
            }
        }
    }

    public Usuario getOne(String id) {
        return us.getOne(id);
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Usuario usuario = us.buscarporEmail(email);
//        if (usuario != null) {
//            List<GrantedAuthority> permisos = new ArrayList();
//            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
//            permisos.add(p);
//            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//            HttpSession session = attr.getRequest().getSession(true);
//            session.setAttribute("usuariosession", usuario);
//            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
//        } else {
//            throw new UsernameNotFoundException("Usuario Invalido");
//        }
//    }

     @Transactional
    public boolean cambiarContrasena(String id, String contrasenaActual, String nuevaContrasena, String confirmarNuevaContrasena) {
        // Verificar que la nueva contraseña y la confirmación coincidan
        if (!nuevaContrasena.equals(confirmarNuevaContrasena)) {
            return false; // La nueva contraseña y la confirmación no coinciden
        }

        // Obtener el usuario desde la base de datos
        Optional<Usuario> optionalUsuario = us.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            // Verificar si la contraseña actual proporcionada coincide con la almacenada
            if (new BCryptPasswordEncoder().matches(contrasenaActual, usuario.getPassword())) {
                // Actualizar la contraseña en la base de datos
                usuario.setPassword(new BCryptPasswordEncoder().encode(nuevaContrasena));
                us.save(usuario);
                return true; // Cambio exitoso
            } else {
                return false; // La contraseña actual no es correcta
            }
        } else {
            return false; // El usuario no se encontró
        }
    }
     @Transactional
    //servicio para actualizar el perfil de usuario, opcion disponible para el usuario, no para el administrador
    public void actualizarPerfil(
            String id,
            String nombre,
            String apellido,
            String email,
            Integer dni,
            Date fechaDeNacimiento,
            Integer telefono)  {
    
    
        Usuario usuario = us.findById(id).orElse(null);
        
        // Actualizando info del perfil
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setDni(dni);
        usuario.setFechaDeNacimiento(fechaDeNacimiento);
        usuario.setTelefono(telefono);
        
        // guardando la info en la base de datos
        us.save(usuario);
        
    }
}
