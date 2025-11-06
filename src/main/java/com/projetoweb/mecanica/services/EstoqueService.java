package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.EstoqueDto;
import com.projetoweb.mecanica.entities.Estoque;
import com.projetoweb.mecanica.entities.Produto;
import com.projetoweb.mecanica.repositories.EstoqueRepository;
import com.projetoweb.mecanica.repositories.ProdutoRepository;
import com.projetoweb.mecanica.mapper.EstoqueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public List<EstoqueDto> findAll() {
        List<Estoque> estoques = estoqueRepository.findAll();
        return estoques.stream()
                .map(EstoqueMapper::toDto)
                .toList();
    }

    @Transactional
    public EstoqueDto insert(EstoqueDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("EstoqueDto nao pode ser nulo");
        }
        if (dto.getProdutoId() == null) {
            throw new IllegalArgumentException("ID do produto nao pode ser nulo");
        }
        if (dto.getQuantidade() == null || dto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero");
        }

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado com ID: " + dto.getProdutoId()));

        Estoque entity = new Estoque();
        entity.setProduto(produto);
        entity.setQuantidadeEstoque(dto.getQuantidade());

        entity = estoqueRepository.save(entity);
        return EstoqueMapper.toDto(entity);
    }

    @Transactional
    public EstoqueDto update(Long id, EstoqueDto dto) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        if (dto == null) {
            throw new IllegalArgumentException("EstoqueDto nao pode ser nulo");
        }
        if (dto.getProdutoId() == null) {
            throw new IllegalArgumentException("ID do produto nao pode ser nulo");
        }
        if (dto.getQuantidade() == null || dto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero");
        }

        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque nao encontrado com ID: " + id));

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado com ID: " + dto.getProdutoId()));

        entity.setProduto(produto);
        entity.setQuantidadeEstoque(dto.getQuantidade());
        entity = estoqueRepository.save(entity);

        return EstoqueMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque nao encontrado com ID: " + id));
        estoqueRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public EstoqueDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque nao encontrado com ID: " + id));
        return EstoqueMapper.toDto(entity);
    }

    @Transactional
    public EstoqueDto venderProduto(Long id, Integer quantidade) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque nao encontrado com ID: " + id));

        entity.venderProduto(quantidade);

        entity = estoqueRepository.save(entity);

        return EstoqueMapper.toDto(entity);
    }

    @Transactional
    public EstoqueDto adicionarProduto(Long id, Integer quantidade) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque nao encontrado com ID: " + id));

        entity.adicionarEstoque(quantidade);

        entity = estoqueRepository.save(entity);

        return EstoqueMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public boolean temEstoqueSuficiente(Long id, Integer quantidade) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        if (quantidade == null || quantidade < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero");
        }

        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque nao encontrado com ID: " + id));

        return entity.temEstoqueSuficiente(quantidade);
    }
}
