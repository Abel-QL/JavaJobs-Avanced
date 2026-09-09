package zd.zona_fit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zd.zona_fit.models.Cliente;
import zd.zona_fit.repository.IClienteRepositorio;

import java.util.List;

@Service

public class Servicio implements IClienteServicio {
    //hacemos una inyeccion de dependecia de la interfaz clienterepositorio a esta clase para hacer vainas 
    @Autowired
    private IClienteRepositorio clienteRepositorio;

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }

    @Override
    public Cliente buscarClientePorId(Integer idCliente) {
        return clienteRepositorio.findById(idCliente).orElse(null);
    }

    @Override
    public void saveCliente(Cliente cliente) {
        clienteRepositorio.save(cliente);

    }

    @Override
    public void deleteCliente(Cliente cliente) {
        clienteRepositorio.delete(cliente);
    }
}
