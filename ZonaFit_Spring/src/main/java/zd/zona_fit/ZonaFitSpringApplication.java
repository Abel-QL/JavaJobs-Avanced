package zd.zona_fit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zd.zona_fit.models.Cliente;
import zd.zona_fit.services.IClienteServicio;

import java.util.List;
import java.util.Scanner;

//@SpringBootApplication
public class ZonaFitSpringApplication implements CommandLineRunner {
  //  @Autowired
    private IClienteServicio clienteServicio;
    //esto es básicamente sustituir el método print por uno más adecuado para la aplicación
    private static final Logger LOGGER = LoggerFactory.getLogger(ZonaFitSpringApplication.class);

    static void main(String[] args) {
        LOGGER.info("Iniciando la aplicacion oooh seee");
        SpringApplication.run(ZonaFitSpringApplication.class, args);
        LOGGER.info("OHH Aplicacion finalizada baby");
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("--- ZONA FIT (GYM) - Aplicacion ---");
        PresentacionFit();
    }


    private static int showMenu(Scanner console) {
        System.out.print("""
                +----------------------------+
                |         ZONA FIT           |
                +----------------------------+
                | 1. Registrar cliente       |
                | 2. Listar clientes         |
                | 3. Actualizar cliente      |
                | 4. Eliminar cliente        |
                | 5. Buscar cliente por ID   |
                | 6. EXIT                    |
                +----------------------------+
                Seleccione una opción:\s""");
        return Integer.parseInt(console.nextLine());
    }

    public void PresentacionFit() {
        boolean exit = false;
        try (Scanner console = new Scanner(System.in)) {
            while (!exit) {
                try {
                    var option = showMenu(console);
                    exit = ExecOptions(console, option);
                } catch (Exception e) {
                    LOGGER.error("Error: {}", e.getMessage());
                }
            }
        }
    }

    private boolean ExecOptions(Scanner console, int option) {
        boolean exit = false;
        switch (option) {
            case 1 -> registrarCliente(console);
            case 2 -> listarClientes();
            case 3 -> actualizarCliente(console);
            case 4 -> eliminarCliente(console);
            case 5 -> buscarCliente(console);
            case 6 -> exit = true;
            default -> System.out.println(" Ingresa un valor valido");

        }
        return exit;
    }

    private void registrarCliente(Scanner console) {
        System.out.print("Introduce el nombre del cliente: ");
        String nombre = console.nextLine();
        System.out.print("Introduce el apellido del cliente: ");
        String apellido = console.nextLine();
        int membresia;

        try {
            membresia = leerEnteroPositivo(console, "Introduce la membresia del cliente: ");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            Cliente cliente = new Cliente(null, nombre, apellido, membresia);
            clienteServicio.saveCliente(cliente);
            System.out.println("Cliente registrado correctamente " + cliente);
        } catch (Exception e) {
            System.out.println("Error al registrar el cliente.  error: " + e.getMessage());
        }
    }

    private void listarClientes() {
        System.out.println("--- Lista de clientes:");
        List<Cliente> clientes = clienteServicio.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            clientes.forEach(System.out::println);
        }
        System.out.println("---");
    }

    private void actualizarCliente(Scanner console) {
        int id;
        try {
            id = leerEnteroPositivo(console, "Introduce el id a modificar: ");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (clienteServicio.buscarClientePorId(id) == null) {
            System.out.println("No existe un cliente con ese id.");
            return;
        }

        System.out.print("Introduce el nombre del cliente: ");
        String nombre = console.nextLine();
        System.out.print("Introduce el apellido del cliente: ");
        String apellido = console.nextLine();
        int membresia;
        try {
            membresia = leerEnteroPositivo(console, "Introduce la membresia del cliente: ");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            Cliente cliente = new Cliente(id, nombre, apellido, membresia);
            clienteServicio.saveCliente(cliente);
            System.out.println("Cliente modificado correctamente " + cliente);
        } catch (Exception e) {
            System.out.println("Error al ACTUALIZAR el cliente.  error: " + e.getMessage());
        }

    }

    private void eliminarCliente(Scanner console) {
        int id;
        try {
            id = leerEnteroPositivo(console, "Introduce el id a eliminar: ");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        Cliente cliente = clienteServicio.buscarClientePorId(id);
        if (cliente == null) {
            System.out.println("No existe un cliente con ese id. Por lo que no se puede eliminar.");
            return;
        }

        try {
            clienteServicio.deleteCliente(cliente);
            System.out.println("Cliente eliminado correctamente.");
            listarClientes();
        } catch (Exception e) {
            System.out.println("Error al eliminar el cliente. error: " + e.getMessage());
        }
    }

    private void buscarCliente(Scanner console) {
        int id;
        try {
            id = leerEnteroPositivo(console, "Introduce el id a buscar: ");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        Cliente cliente = clienteServicio.buscarClientePorId(id);
        if (cliente == null) {
            System.out.println("No existe un cliente con ese id.");
        } else {
            System.out.println("Cliente encontrado correctamente.");
            System.out.println(cliente);
        }
    }

    private static int leerEnteroPositivo(Scanner console, String mensaje) {
        System.out.print(mensaje);
        var valor = Integer.parseInt(console.nextLine());
        if (valor <= 0) {
            throw new IllegalArgumentException("El valor debe ser mayor a 0");
        }
        return valor;
    }
}
