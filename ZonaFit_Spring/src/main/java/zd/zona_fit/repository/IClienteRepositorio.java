package zd.zona_fit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zd.zona_fit.models.Cliente;

public interface IClienteRepositorio extends JpaRepository<Cliente, Integer> {
}
