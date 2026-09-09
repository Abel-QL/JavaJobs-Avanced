package zd.zona_fit.services;

import zd.zona_fit.models.Cliente;

import java.util.List;

public interface IClienteServicio {
     List<Cliente> listarClientes();
     Cliente buscarClientePorId(Integer idCliente);
     void saveCliente(Cliente cliente);
     void deleteCliente(Cliente cliente);
}
