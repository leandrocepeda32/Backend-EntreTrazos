package com.integrador.ProyectoIntegradorV1.controladores;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.integrador.ProyectoIntegradorV1.entidades.Autor;
import com.integrador.ProyectoIntegradorV1.entidades.Categoria;
import com.integrador.ProyectoIntegradorV1.entidades.Libro;
import com.integrador.ProyectoIntegradorV1.servicios.IAutorServicio;
import com.integrador.ProyectoIntegradorV1.servicios.ICategoriaServicio;
import com.integrador.ProyectoIntegradorV1.servicios.ILibroServicio;

@Controller
@RequestMapping(path ="/libro")
public class LibroController {
	
	@Autowired
	@Qualifier("LibroServicio")
	private ILibroServicio libroServicio;
	
	@Autowired
	@Qualifier("AutorServicio")
	private IAutorServicio autorServicio;
	
	@Autowired
	@Qualifier("CategoriaServicio")
	private ICategoriaServicio categoriaServicio;

	
	//PAGINA DE BIENVENIDA
	@GetMapping(value ="")
	public String bienvenido() {
		return "bienvenido";
	}
	
	@GetMapping(value="/agregar")
	public String agregarLibro(Model model) {
		model.addAttribute("libro", new Libro());
		
		return "libro/agregar-libro";
	}
	
	@PostMapping(value = "/agregar")
	public String guardarProducto(@ModelAttribute @Valid Libro libro, RedirectAttributes redir, BindingResult bindingResult) throws Exception {
		if(bindingResult.hasErrors()) {
			return "libro/agregar-libro";
		}
		
		//CODIGO PARA QUE NO ME REPITA AUTORES NI CATEGORIAS, CUANDO CREO LIBROS CON EL MISMO AUTOR/CATEGORIAS (TAMBIEN SE HACE CUANDO EDITO UN LIBRO)
		try {
			
//			Autor autor = autorServicio.findByNombre(libro.getAutor().getNombre());
//			if(autor != null) {
//				libro.setAutor(autor);
//			}
//			
//			Categoria categoria = categoriaServicio.findByNombre(libro.getCategoria().getNombre());
//			if(categoria != null) {
//				libro.setCategoria(categoria);	 
//			}
			
			libroServicio.save(libro); //ME GUARDA EL LIBRO JUNTO CON EL AUTOR Y LA CATEGORIA
			redir.addFlashAttribute("mensaje", "El producto se agrego correctamente");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "redirect:/libro/agregar";
	}
	
	@GetMapping(value="/lista")
	public String mostrarLibros(Model model) {
		
		try {
			model.addAttribute("libros", libroServicio.findAll());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "libro/mostrar-libros";
		
	}
	
	@GetMapping(value="/editar/{id}")
	public String editarLibro(@PathVariable int id, Model model) {
		try {
			model.addAttribute("libro", libroServicio.findById(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "libro/editar-libro";
		
	}
	
	@PostMapping(value="/editar/{id}")
	public String actualizarLibro(@ModelAttribute @Valid Libro libro,@PathVariable int id, BindingResult bindingResult, RedirectAttributes redir) {
		if(bindingResult.hasErrors()) {
			return "libro/editar-libro";
		}
	
		try {
			libroServicio.update(id,libro);
			redir.addFlashAttribute("mensaje", "El libro se editó correctamente");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/libro/lista";	
	}
	
	@PostMapping(value="/eliminar")
	public String eliminarLibro(@ModelAttribute Libro libro, RedirectAttributes redir) {
		
		try {
			libroServicio.delete(libro.getId());
			redir.addFlashAttribute("mensaje", "Eliminado correctamente");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:/libro/lista";
	}
	

}