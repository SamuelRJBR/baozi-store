package br.com.baozi.store.controller;

import br.com.baozi.store.model.Pedido;
import br.com.baozi.store.repository.PedidoRepository;
import br.com.baozi.store.repository.ClienteRepository;
import br.com.baozi.store.repository.ProdutoRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoRepository repository;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public PedidoController(PedidoRepository repository, ClienteRepository clientes,
                            ProdutoRepository produtos) {
        this.repository = repository;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@Valid @RequestBody Pedido dados) {
        if (!clientes.existsById(dados.getClienteId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }
        if (!produtos.existsById(dados.getProdutoId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }

        try {
            Pedido salvo = repository.save(dados);
            var localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(salvo.getId()).toUri();
            return ResponseEntity.created(localizacao).body(salvo);
        } catch (DataIntegrityViolationException erro) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflito ao gravar o registro", erro);
        }
    }

    @GetMapping
    public List<Pedido> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        Pedido registro = buscar(id);
        try {
            repository.delete(registro);
        } catch (DataIntegrityViolationException erro) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O registro está vinculado a um pedido", erro);
        }
    }
}
